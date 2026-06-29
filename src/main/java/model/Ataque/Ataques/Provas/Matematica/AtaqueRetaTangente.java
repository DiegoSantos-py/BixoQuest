package model.Ataque.Ataques.Provas.Matematica;

import model.Batalha.EntidadeBatalha;
import model.Ataque.Ataque;
import model.Player.PlayerProva;
import model.Projetil.Projetil;
import model.Projetil.Comportamentos.ProjetilQueSegue;
import model.Projetil.Comportamentos.ComportamentoFactory;

public class AtaqueRetaTangente extends Ataque {

    private float timer = 0;
    private int projeteisSpawnados = 0;
    private boolean pontoSpawnado = false;
    private Projetil pontoHoming = null;

    private float attackDuration = 10f;

    public AtaqueRetaTangente(PlayerProva target, EntidadeBatalha owner, float dificuldade) {
        super(target, owner, dificuldade, 60);
    }

    @Override
    protected void logicaAtaque(float dt) {

        if (!pontoSpawnado) {
            float startX = owner.getCentro().getX();
            float startY = owner.getCentro().getY();

            // Spawna o projétil "ponto" que vai seguir o player
            pontoHoming = spawnProjetil(
                    startX, startY,
                    32f, 32f,
                    100f, // Velocidade inicial
                    0f, 0f,
                    1, 1f, // Dano
                    attackDuration, 
                    "/assets/batalha/projeteis/ponto.png"); // Placeholder para o projétil ponto

            if (pontoHoming != null) {
                pontoHoming.addComportamento(ComportamentoFactory.getAI("PREDITIVO"));
            }
            pontoSpawnado = true;
        }

        timer += dt;

        float interval = 0.35f / (dificuldade / 10f); // Quanto maior a dificuldade, menor o intervalo
        int maxProjeteis = (int) (attackDuration / interval);

        if (pontoHoming != null && !pontoHoming.isAtivo()) {
            pontoHoming = null;
        }

        if (pontoHoming != null && timer >= interval && projeteisSpawnados < maxProjeteis) {
            float posX = pontoHoming.getCentro().getX();
            float posY = pontoHoming.getCentro().getY();

            // Reta tangente ao movimento: o ângulo do vetor velocidade atual do ponto + 90 graus
            float anguloTangente = (float) Math.atan2(pontoHoming.getVelocidade().getY(), pontoHoming.getVelocidade().getX()) + (float) (Math.PI / 2);

            Projetil previa = spawnProjetil(
                    posX, posY,
                    3, 600, 
                    0f, 0f, 
                    anguloTangente,
                    0, 0f, // Nenhum dano
                    0.5f,
                    "/assets/batalha/projeteis/arranhaoPrevia.png");

            if (previa != null) {
                // Ao desaparecer a prévia, spawna o arranhão real
                previa.addComportamentoDespawn(
                        ComportamentoFactory.getDespawn("SPAWN_ARRANHAO")
                );
                
                projeteisSpawnados++;
                timer = 0;
            }
        }

        // Encerra o ataque quando o tempo acabar e todos os arranhões (e o ponto) tiverem sumido
        if (tempoDecorrido >= attackDuration && getProjeteis().isEmpty()) {
            encerrarAtaque();
        } else if (pontoHoming == null && getProjeteis().isEmpty()) {
            // Caso o ponto seja destruído cedo e as prévias/arranhões também tenham sumido
            encerrarAtaque();
        }
    }

    @Override
    public void reiniciarAtaque() {
        super.reiniciarAtaque();
        this.timer = 0;
        this.projeteisSpawnados = 0;
        this.pontoSpawnado = false;
        this.pontoHoming = null;
    }

    @Override
    public String toString() {
        return "Ataque Reta Tangente";
    }
}

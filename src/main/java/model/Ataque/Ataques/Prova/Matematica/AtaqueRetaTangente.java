package model.Ataque.Ataques.Prova.Matematica;

import model.Batalha.EntidadeBatalha;
import model.Ataque.Ataque;
import model.Player.PlayerProva;
import model.Projetil.Projetil;
import model.Projetil.Comportamentos.ProjetilQuePreve;
import model.Projetil.Comportamentos.ProjetilSpawnAoMorrer;

public class AtaqueRetaTangente extends Ataque {

    private float timer = 0;
    private int projeteisLancados = 0;
    private boolean pontoGerado = false;
    private Projetil pontoTeleguiado = null;

    private final ProjetilQuePreve preditivo = new ProjetilQuePreve(125f, 6.0f, 0.8f);
    private final ProjetilSpawnAoMorrer spawnArranhao = new ProjetilSpawnAoMorrer("arranhao.png", 6, 650, 1, 0.75f, 0.2f, 1f);

    private float duracaoAtaque = 10f;

    public AtaqueRetaTangente(PlayerProva target, EntidadeBatalha owner, float dificuldade) {
        super(target, owner, dificuldade, 60);
    }

    @Override
    protected void logicaAtaque(float dt) {

        if (!pontoGerado) {
            float inicioX = owner.getCentro().getX();
            float inicioY = owner.getCentro().getY();

            // Spawna o projétil "ponto" que vai seguir o player
            pontoTeleguiado = spawnProjetil(
                    inicioX, inicioY,
                    32f, 32f,
                    100f, // Velocidade inicial
                    0f, 0f,
                    1, 1f, // Dano
                    duracaoAtaque, 
                    "ponto.png"); // Placeholder para o projétil ponto

            if (pontoTeleguiado != null) {
                pontoTeleguiado.addComportamento(preditivo);
            }
            pontoGerado = true;
        }

        timer += dt;

        float intervalo = 0.35f / (dificuldade / 30f); // Quanto maior a dificuldade, menor o intervalo
        int maxProjeteis = (int) (duracaoAtaque / intervalo);

        if (pontoTeleguiado != null && !pontoTeleguiado.isAtivo()) {
            pontoTeleguiado = null;
        }

        if (pontoTeleguiado != null && timer >= intervalo && projeteisLancados < maxProjeteis) {
            float posX = pontoTeleguiado.getCentro().getX();
            float posY = pontoTeleguiado.getCentro().getY();

            // Reta tangente ao movimento: o ângulo do vetor velocidade atual do ponto + 90 graus
            float anguloTangente = (float) Math.atan2(pontoTeleguiado.getVelocidade().getY(), pontoTeleguiado.getVelocidade().getX()) + (float) (Math.PI / 2);

            Projetil previa = spawnProjetil(
                    posX, posY,
                    3, 600, 
                    0f, 0f, 
                    anguloTangente,
                    0, 0f, // Nenhum dano
                    0.5f,
                    "arranhaoPrevia.png");

            if (previa != null) {
                previa.addComportamentoDespawn(spawnArranhao);
                
                projeteisLancados++;
                timer = 0;
            }
        }

        // Encerra o ataque quando o tempo acabar e todos os arranhões (e o ponto) tiverem sumido
        if (tempoDecorrido >= duracaoAtaque && getProjeteis().isEmpty()) {
            encerrarAtaque();
        } else if (pontoTeleguiado == null && getProjeteis().isEmpty()) {
            // Caso o ponto seja destruído cedo e as prévias/arranhões também tenham sumido
            encerrarAtaque();
        }
    }

    @Override
    public void reiniciarAtaque() {
        super.reiniciarAtaque();
        this.timer = 0;
        this.projeteisLancados = 0;
        this.pontoGerado = false;
        this.pontoTeleguiado = null;
    }


}

package model.Ataque.Ataques.Prova.Matematica;

import model.Batalha.EntidadeBatalha;
import model.Ataque.Ataque;
import model.Player.PlayerProva;
import model.Projetil.Projetil;
import model.Projetil.Comportamentos.ProjetilSpawnAoMorrer;
import java.util.Random;

public class AtaqueFuncaoAfim extends Ataque {

    private float timer = 0;
    private int projeteisGerados = 0;
    private boolean eixosGerados = false;
    private Random random;

    private final ProjetilSpawnAoMorrer spawnFuncao = new ProjetilSpawnAoMorrer("arranhao.png", 6, 1200, 1, 0.75f, 0.2f,1f);

    public AtaqueFuncaoAfim(PlayerProva target, EntidadeBatalha owner, float dificuldade) {
        super(target, owner, dificuldade, 60);
        this.random = new Random();
    }

    @Override
    protected void logicaAtaque(float dt) {
        timer += dt;
        float centroCaixaX = (minX + maxX) / 2;
        float centroCaixaY = (minY + maxY) / 2;
        float duracaoAtaque = 10f;
        float intervalo = 0.15f / (dificuldade / 10f); // Ex: Dificuldade 10 spawna a cada 1 segundo
        int maxProjeteis = (int) (duracaoAtaque / intervalo);
        if (!eixosGerados) {
            Projetil eixos = spawnProjetil(centroCaixaX, centroCaixaY, 200, 200, 0, 0, 0, 0,
                    0, 12.5f, "eixos.png");
            if (eixos != null) {
                eixos.setPersistente(true);
            }
            eixosGerados = true;
        }
        if (timer >= intervalo && projeteisGerados < maxProjeteis) {

            // Spawna aleatoriamente na area de ataque do inimigo
            float posX = centroCaixaX - 200 + (random.nextFloat() * 400);
            float posY = centroCaixaY - 200 + (random.nextFloat() * 400);

            // Ângulo aleatório para o arranhão
            float anguloAleatorio = random.nextFloat() * (float) (Math.PI * 2);

            // Spawna a PRÉVIA
            Projetil previa = spawnProjetil(
                    posX, posY,
                    3, 600,
                    0f, 0f,
                    anguloAleatorio,
                    0, 0f, // Nenhum dano
                    1.25f, // Permanece como prévia por 1.25 segundos
                    "arranhaoPrevia.png");

            if (previa != null) {
                previa.addComportamentoDespawn(spawnFuncao);

                projeteisGerados++;
                timer = 0;
            }
        }

        if (tempoDecorrido >= duracaoAtaque && getProjeteis().isEmpty()) {
            encerrarAtaque();
        }
    }



    @Override
    public void reiniciarAtaque() {
        super.reiniciarAtaque();
        this.timer = 0;
        this.projeteisGerados = 0;
        this.eixosGerados = false;
    }
}

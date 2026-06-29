package model.Ataque.Ataques.Provas.Matematica;

import model.Batalha.EntidadeBatalha;
import model.Ataque.Ataque;
import model.Player.PlayerProva;
import model.Projetil.Projetil;

public class AtaqueIntegral extends Ataque {

    private float timer = 0;
    private int projeteisSpawnados = 0;
    private float anguloSpawn = 0f;
    private boolean integralSpawnado = false;
    private boolean integralAtivado = false;
    private Projetil integralCentro = null;
    private Projetil integralHitbox = null;

    private int direcao = 1;
    private float gracePeriod = 1.5f;

    // camelCase por padrão do projeto
    private float velAngIntegral = .623f; // rad/s — rotação do símbolo
    private float velAngSpawn = velAngIntegral * 2f; // rad/s — rotação dos bursts
    private int projeteisPorBurst = 4 + (int) (dificuldade / 6f);
    private float velocidadeSaida = 200f; // px/s
    private float attackDuration = 15f;
    String spriteDir;

    float boxCentroX = 960f;
    float boxCentroY = 800f;

    public AtaqueIntegral(PlayerProva target, EntidadeBatalha owner, float dificuldade) {
        super(target, owner, dificuldade, 80);
    }

    @Override
    protected void logicaAtaque(float dt) {

        if (direcao == 1) {
            spriteDir = "/assets/batalha/projeteis/integral.png";
        } else {
            spriteDir = "/assets/batalha/projeteis/integralInvertida.png";
        }

        // spawna o símbolo uma única vez no primeiro frame do ataque
        if (!integralSpawnado) {
            integralCentro = spawnProjetil(
                    boxCentroX, boxCentroY,
                    99, 222,
                    0f, 0f,
                    0f,
                    0,
                    0f,
                    attackDuration + 2f,
                    spriteDir);

            if (integralCentro != null) {
                // começa parado e sem dano — ativa após gracePeriod
                integralCentro.setVelocidadeAngular(0f);
                integralCentro.setPersistente(true);
            }

            float offsetRotacao = (float) Math.toRadians(10) * direcao;
            integralHitbox = spawnProjetil(
                    boxCentroX, boxCentroY,
                    99, 444,
                    0f, 0f,
                    offsetRotacao, 0,
                    0f,
                    attackDuration,
                    ""); // sem sprite

            if (integralHitbox != null) {
                // começa sem rotação e sem dano
                integralHitbox.setVelocidadeAngular(0f);
                integralHitbox.setDanoShield(0);
                integralHitbox.setDanoNota(0f);
                integralHitbox.setPersistente(true);
            }
            integralSpawnado = true;
        }

        timer += dt;

        // ativa rotação e dano após o grace period
        if (!integralAtivado && tempoDecorrido >= gracePeriod) {
            if (integralCentro != null)
                integralCentro.setVelocidadeAngular(velAngIntegral);
            if (integralHitbox != null) {
                integralHitbox.setVelocidadeAngular(velAngIntegral);
                integralHitbox.setDanoShield(1);
                integralHitbox.setDanoNota(1.8f); // ativa o dano de nota
            }
            integralAtivado = true;
        }

        // acumula o ângulo do burst independente do símbolo
        anguloSpawn += velAngSpawn * dt;

        float interval = 1.15f / (dificuldade / 10f);
        int maxBursts = (int) (attackDuration / interval);

        if (timer >= interval && projeteisSpawnados < maxBursts * projeteisPorBurst) {

            for (int i = 0; i < projeteisPorBurst; i++) {
                float angulo = anguloSpawn + (i * ((float) (2 * Math.PI) / projeteisPorBurst));

                spawnProjetil(
                        boxCentroX, boxCentroY,
                        20f, 20f,
                        velocidadeSaida,
                        angulo,
                        angulo,
                        1,
                        1.5f,
                        4f,
                        "/assets/batalha/projeteis/c.png"); // PLACEHOLDER
            }

            projeteisSpawnados += projeteisPorBurst;
            timer = 0;
        }

        // mínimo necessário para encerrar o ataque
        int minBursts = 10 + (int) (dificuldade / 5f);

        if (projeteisSpawnados >= minBursts * projeteisPorBurst) {
            if (timer >= 2f) {
                this.encerrarAtaque();
            }
        }
    }

    @Override
    public void reiniciarAtaque() {
        super.reiniciarAtaque();
        this.timer = 0;
        this.projeteisSpawnados = 0;
        this.anguloSpawn = 0f;
        this.direcao *= -1;
        this.velAngIntegral *= -1;
        this.velAngSpawn *= -1;
        this.integralSpawnado = false;
        this.integralAtivado = false;
        this.integralCentro = null;
        this.integralHitbox = null;
    }

    @Override
    public String toString() {
        return "Ataque Integral";
    }
}
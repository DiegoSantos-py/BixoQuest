package model.Ataque.Ataques.Provas.Matematica;

import model.Ataque.Ataque;
import model.Batalha.EntidadeBatalha;
import model.Player.PlayerProva;
import model.Projetil.Projetil;

public class AtaqueIntegral2 extends Ataque {

    private float timer = 0;
    private int projeteisSpawnados = 0;
    private float anguloSpawn = 0f;
    private boolean integralSpawnado = false;
    private boolean integralAtivado  = false;
    private Projetil integralCentro = null;
    private Projetil integralHitbox = null;
    private Projetil integralCentro2 = null;
    private Projetil integralHitbox2 = null;

    private int direcao = 1;
    private float gracePeriod = 1.5f;

    // camelCase por padrão do projeto
    private float velAngIntegral  = .623f;                // rad/s — rotação do símbolo
    private float velAngSpawn     = velAngIntegral * 2f; // rad/s — rotação dos bursts
    private int   projeteisPorBurst = 4 + (int)(dificuldade/6f);
    private float velocidadeSaida   = 100f * (1+ (dificuldade/10f));              // px/s
    private float attackDuration    = 12.5f;
    String spriteDir;

    float boxCentroX = 960f;
    float boxCentroY = 800f;

    public AtaqueIntegral2(PlayerProva target, EntidadeBatalha owner, float dificuldade) {
        super(target, owner, dificuldade, 150);
    }

    @Override
    protected void logicaAtaque(float dt) {

        if(direcao==1){
            spriteDir = "/assets/batalha/projeteis/integral.png";
        }
        else{
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

            integralCentro2 = spawnProjetil(
                    boxCentroX, boxCentroY,
                    99, 222,
                    0f, 0f, 
                    (float) Math.PI / 2f,
                    0,
                    0f,
                    attackDuration + 2f,
                    spriteDir);

            if (integralCentro != null && integralCentro2 != null) {
                // começa parado e sem dano — ativa após gracePeriod
                integralCentro.setVelocidadeAngular(0f);
                integralCentro2.setVelocidadeAngular(0f);
                integralCentro.setPersistente(true);
                integralCentro2.setPersistente(true);
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

            integralHitbox2 = spawnProjetil(
                    boxCentroX, boxCentroY,
                    99, 444,
                    0f, 0f,
                    offsetRotacao + (float) Math.PI / 2f, 0,
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
            if (integralHitbox2 != null) {
                // começa sem rotação e sem dano
                integralHitbox2.setVelocidadeAngular(0f);
                integralHitbox2.setDanoShield(0);
                integralHitbox2.setDanoNota(0f);
                integralHitbox2.setPersistente(true);
            }
            integralSpawnado = true;
        }

        timer += dt;

        // ativa rotação e dano após o grace period
        if (!integralAtivado && tempoDecorrido >= gracePeriod) {
            if (integralCentro != null) integralCentro.setVelocidadeAngular(velAngIntegral);
            if (integralCentro2 != null) integralCentro2.setVelocidadeAngular(velAngIntegral);
            if (integralHitbox != null) {
                integralHitbox.setVelocidadeAngular(velAngIntegral);
                integralHitbox.setDanoShield(1);
                integralHitbox.setDanoNota(1.25f);   // ativa o dano de nota
            }
            if (integralHitbox2 != null) {
                integralHitbox2.setVelocidadeAngular(velAngIntegral);
                integralHitbox2.setDanoShield(1);
                integralHitbox2.setDanoNota(1.25f);   // ativa o dano de nota
            }
            integralAtivado = true;
        }

        // acumula o ângulo do burst independente do símbolo
        anguloSpawn += velAngSpawn * dt;

        float interval = 2f / (dificuldade / 10f);
        int maxBursts = (int) (attackDuration / interval);

        if (timer >= interval && projeteisSpawnados < maxBursts * projeteisPorBurst) {

            for (int i = 0; i < projeteisPorBurst; i++) {
                float step = (float) (2 * Math.PI) / projeteisPorBurst;
                float angulo = anguloSpawn + (i * step);
                float anguloIntermediario = angulo + (step / 2f);

                spawnProjetil(
                        boxCentroX, boxCentroY,
                        20f, 20f,
                        velocidadeSaida,
                        angulo,
                        angulo,
                        1,
                        1f,
                        4f,
                        "/assets/batalha/projeteis/c.png"); // PLACEHOLDER
                
                spawnProjetil(
                        boxCentroX, boxCentroY,
                        20f, 20f,
                        velocidadeSaida * 0.7f,
                        anguloIntermediario,
                        anguloIntermediario,
                        1,
                        1f,
                        4f,
                        "/assets/batalha/projeteis/mais.png"); // PLACEHOLDER
            }

            projeteisSpawnados += projeteisPorBurst;
            timer = 0;
        }

        if (projeteisSpawnados >= maxBursts * projeteisPorBurst) {
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
        this.integralAtivado  = false;
        this.integralCentro = null;
        this.integralCentro2 = null;
        this.integralHitbox = null;
        this.integralHitbox2 = null;
    }

    @Override
    public String toString() {
        return "Ataque Integral 2";
    }
}
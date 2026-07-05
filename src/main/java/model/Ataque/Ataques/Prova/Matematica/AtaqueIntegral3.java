package model.Ataque.Ataques.Prova.Matematica;

import model.Ataque.Ataque;
import model.Batalha.EntidadeBatalha;
import model.Player.PlayerProva;
import model.Projetil.Projetil;

public class AtaqueIntegral3 extends Ataque {

    private float timer = 0;
    private int projeteisLancados = 0;
    private float anguloSpawn = 0f;
    private boolean integralGerada = false;
    private boolean integralAtivada  = false;
    private Projetil centroIntegral = null;
    private Projetil centroIntegral2 = null;
    private Projetil centroIntegral3 = null;
    private Projetil hitboxIntegral = null;
    private Projetil hitboxIntegral2 = null;
    private Projetil hitboxIntegral3 = null;

    private int direcao = 1;
    private float periodoGraca = 1.5f;

    // camelCase por padrão do projeto
    private float velAngIntegral  = .623f;                // rad/s — rotação do símbolo
    private float velAngCriacao     = velAngIntegral * 2f; // rad/s — rotação dos bursts
    private int   projeteisPorOnda = 4 + (int)(dificuldade/6f);
    private float velocidadeSaida   = 110f * (1+ (dificuldade/10f));              // px/s
    private float duracaoAtaque    = 12.5f;
    String direcaoSprite;

    float centroCaixaX = 960f;
    float centroCaixaY = 800f;

    public AtaqueIntegral3(PlayerProva target, EntidadeBatalha owner, float dificuldade) {
        super(target, owner, dificuldade, 150);
    }

    @Override
    protected void logicaAtaque(float dt) {

        if(direcao==1){
            direcaoSprite = "integral.png";
        }
        else{
            direcaoSprite = "integralInvertida.png";
        }

        // spawna o símbolo uma única vez no primeiro frame do ataque
        if (!integralGerada) {
            centroIntegral = spawnProjetil(
                    centroCaixaX, centroCaixaY,
                    99, 222,
                    0f, 0f,
                    0f,
                    0,
                    0f,
                    duracaoAtaque + 2f,
                    direcaoSprite);

            centroIntegral2 = spawnProjetil(
                    centroCaixaX, centroCaixaY,
                    99, 222,
                    0f, 0f, 
                    (float) Math.PI / 3f,
                    0,
                    0f,
                    duracaoAtaque + 2f,
                    direcaoSprite);

            centroIntegral3 = spawnProjetil(
                    centroCaixaX, centroCaixaY,
                    99, 222,
                    0f, 0f, 
                    (float) Math.PI * 2f / 3f,
                    0,
                    0f,
                    duracaoAtaque + 2f,
                    direcaoSprite);

            if (centroIntegral != null && centroIntegral2 != null && centroIntegral3 != null) {
                // começa parado e sem dano — ativa após gracePeriod
                centroIntegral.setVelocidadeAngular(0f);
                centroIntegral2.setVelocidadeAngular(0f);
                centroIntegral3.setVelocidadeAngular(0f);
                centroIntegral.setPersistente(true);
                centroIntegral2.setPersistente(true);
                centroIntegral3.setPersistente(true);
            }

            float offsetRotacao = (float) Math.toRadians(10) * direcao;

            
            hitboxIntegral = spawnProjetil(
                    centroCaixaX, centroCaixaY,
                    33, 480,
                    0f, 0f,
                    offsetRotacao, 0,
                    0f,
                    duracaoAtaque,
                    ""); // sem sprite

            hitboxIntegral2 = spawnProjetil(
                    centroCaixaX, centroCaixaY,
                    33, 480,
                    0f, 0f,
                    offsetRotacao + (float) Math.PI / 3f, 0,
                    0f,
                    duracaoAtaque,
                    ""); // sem sprite

            hitboxIntegral3 = spawnProjetil(
                    centroCaixaX, centroCaixaY,
                    33, 480,
                    0f, 0f,
                    offsetRotacao + (float) Math.PI * 2f / 3f, 0,
                    0f,
                    duracaoAtaque,
                    ""); // sem sprite

            if (hitboxIntegral != null) {
                // começa sem rotação e sem dano
                hitboxIntegral.setVelocidadeAngular(0f);
                hitboxIntegral.setDanoShield(0);
                hitboxIntegral.setDanoNota(0f);
                hitboxIntegral.setPersistente(true);
            }
            if (hitboxIntegral2 != null) {
                // começa sem rotação e sem dano
                hitboxIntegral2.setVelocidadeAngular(0f);
                hitboxIntegral2.setDanoShield(0);
                hitboxIntegral2.setDanoNota(0f);
                hitboxIntegral2.setPersistente(true);
            }
            if (hitboxIntegral3 != null) {
                // começa sem rotação e sem dano
                hitboxIntegral3.setVelocidadeAngular(0f);
                hitboxIntegral3.setDanoShield(0);
                hitboxIntegral3.setDanoNota(0f);
                hitboxIntegral3.setPersistente(true);
            }
            integralGerada = true;
        }

        timer += dt;

        // ativa rotação e dano após o grace period
        if (!integralAtivada && tempoDecorrido >= periodoGraca) {
            if (centroIntegral != null) centroIntegral.setVelocidadeAngular(velAngIntegral);
            if (centroIntegral2 != null) centroIntegral2.setVelocidadeAngular(velAngIntegral);
            if (centroIntegral3 != null) centroIntegral3.setVelocidadeAngular(velAngIntegral);
            if (hitboxIntegral != null) {
                hitboxIntegral.setVelocidadeAngular(velAngIntegral);
                hitboxIntegral.setDanoShield(2);
                hitboxIntegral.setDanoNota(1.75f);   // ativa o dano de nota
            }
            if (hitboxIntegral2 != null) {
                hitboxIntegral2.setVelocidadeAngular(velAngIntegral);
                hitboxIntegral2.setDanoShield(2);
                hitboxIntegral2.setDanoNota(1.75f);   // ativa o dano de nota
            }
            if (hitboxIntegral3 != null) {
                hitboxIntegral3.setVelocidadeAngular(velAngIntegral);
                hitboxIntegral3.setDanoShield(2);
                hitboxIntegral3.setDanoNota(1.75f);   // ativa o dano de nota
            }
            integralAtivada = true;
        }

        // acumula o ângulo do burst independente do símbolo
        anguloSpawn += velAngCriacao * dt;

        float intervalo = (2.4f) / (dificuldade / 10f);
        int maxOndas = (int) (duracaoAtaque / intervalo);

        if (timer >= intervalo && projeteisLancados < maxOndas * projeteisPorOnda) {

            for (int i = 0; i < projeteisPorOnda; i++) {
                float passo = (float) (2 * Math.PI) / projeteisPorOnda;
                float angulo = anguloSpawn + (i * passo);
                float anguloIntermediario = angulo + (passo / 2f);

                spawnProjetil(
                        centroCaixaX, centroCaixaY,
                        20f, 20f,
                        velocidadeSaida,
                        angulo,
                        angulo,
                        1,
                        1f,
                        4f,
                        "c.png"); 
                
                spawnProjetil(
                        centroCaixaX, centroCaixaY,
                        20f, 20f,
                        velocidadeSaida * 0.7f,
                        anguloIntermediario,
                        anguloIntermediario,
                        1,
                        1f,
                        4f,
                        "mais.png"); 
                    }

            projeteisLancados += projeteisPorOnda;
            timer = 0;
        }

        if (projeteisLancados >= maxOndas * projeteisPorOnda) {
            if (timer >= 2f) {
                this.encerrarAtaque();
            }
        }
    }

    @Override
    public void reiniciarAtaque() {
        super.reiniciarAtaque();
        this.timer = 0;
        this.projeteisLancados = 0;
        this.anguloSpawn = 0f;
        this.direcao *= -1;
        this.velAngIntegral *= -1;
        this.velAngCriacao *= -1;
        this.integralGerada = false;
        this.integralAtivada  = false;
        this.centroIntegral = null;
        this.centroIntegral2 = null;
        this.centroIntegral3 = null;
        this.hitboxIntegral = null;
        this.hitboxIntegral2 = null;
        this.hitboxIntegral3 = null;
    }


}

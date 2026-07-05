package model.Ataque.Ataques.Prova.Matematica;

import model.Batalha.EntidadeBatalha;
import model.Ataque.Ataque;
import model.Player.PlayerProva;
import model.Projetil.Projetil;

public class AtaqueIntegral extends Ataque {

    private float timer = 0;
    private int projeteisLancados = 0;
    private float anguloSpawn = 0f;
    private boolean integralGerada = false;
    private boolean integralAtivada = false;
    private Projetil centroIntegral = null;
    private Projetil hitboxIntegral = null;

    private int direcao = 1;
    private float periodoGraca = 1.5f;

    // camelCase por padrão do projeto
    private float velAngIntegral = .623f; // rad/s — rotação do símbolo
    private float velAngCriacao = velAngIntegral * 2f; // rad/s — rotação dos bursts
    private int projeteisPorOnda = 4 + (int) (dificuldade / 6f);
    private float velocidadeSaida = 200f; // px/s
    private float duracaoAtaque = 15f;
    String direcaoSprite;

    float centroCaixaX = 960f;
    float centroCaixaY = 800f;

    public AtaqueIntegral(PlayerProva target, EntidadeBatalha owner, float dificuldade) {
        super(target, owner, dificuldade, 80);
    }

    @Override
    protected void logicaAtaque(float dt) {

        if (direcao == 1) {
            direcaoSprite = "integral.png";
        } else {
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

            if (centroIntegral != null) {
                // começa parado e sem dano — ativa após gracePeriod
                centroIntegral.setVelocidadeAngular(0f);
                centroIntegral.setPersistente(true);
            }

            float offsetRotacao = (float) Math.toRadians(10) * direcao;
            hitboxIntegral = spawnProjetil(
                    centroCaixaX, centroCaixaY,
                    99, 480,
                    0f, 0f,
                    offsetRotacao, 0,
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
            integralGerada = true;
        }

        timer += dt;

        // ativa rotação e dano após o grace period
        if (!integralAtivada && tempoDecorrido >= periodoGraca) {
            if (centroIntegral != null)
                centroIntegral.setVelocidadeAngular(velAngIntegral);
            if (hitboxIntegral != null) {
                hitboxIntegral.setVelocidadeAngular(velAngIntegral);
                hitboxIntegral.setDanoShield(1);
                hitboxIntegral.setDanoNota(1.8f); // ativa o dano de nota
            }
            integralAtivada = true;
        }

        //angulo q vai spawnar os projeteis
        anguloSpawn += velAngCriacao * dt;

        float intervalo = 1.15f / (dificuldade / 10f);
        int maxOndas = (int) (duracaoAtaque / intervalo);


        //atira os priojetis a cada intervalo tempo maxOndas vezezes
        if (timer >= intervalo && projeteisLancados < maxOndas * projeteisPorOnda) {

            for (int i = 0; i < projeteisPorOnda; i++) {
                float angulo = anguloSpawn + (i * ((float) (2 * Math.PI) / projeteisPorOnda));

                spawnProjetil(
                        centroCaixaX, centroCaixaY,
                        20f, 20f,
                        velocidadeSaida,
                        angulo,
                        angulo,
                        1,
                        1.5f,
                        4f,
                        "c.png");
            }

            projeteisLancados += projeteisPorOnda;
            timer = 0;
        }

        // mínimo necessário para encerrar o ataque
        int minOndas = 10 + (int) (dificuldade / 5f);

        if (projeteisLancados >= minOndas * projeteisPorOnda) {
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
        //inverte a direcao da integral
        this.direcao *= -1;
        this.velAngIntegral *= -1;
        this.velAngCriacao *= -1;
        this.integralGerada = false;
        this.integralAtivada = false;
        this.centroIntegral = null;
        this.hitboxIntegral = null;
    }


}
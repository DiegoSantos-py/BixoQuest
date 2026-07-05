package model.Ataque.Ataques.Prova.Matematica;

import model.Ataque.Ataque;
import model.Batalha.EntidadeBatalha;
import model.Player.PlayerProva;

public class AtaqueEstatistica extends Ataque {

    private float timer = 0;
    private boolean ataqueIniciado = false;
    private float duracaoAtaque = 12f;
    private float velocidade = 250f;

    public AtaqueEstatistica(PlayerProva target, EntidadeBatalha owner, float dificuldade) {
        super(target, owner, dificuldade, 150);
        // Wider arena
        this.setMinX(660f);
        this.setMaxX(1260f);
    }

    @Override
    protected void logicaAtaque(float dt) {
        if (!ataqueIniciado) {
            this.target.setSoulMode(PlayerProva.SoulMode.BLUE);
            this.ataqueIniciado = true;
            this.velocidade = 180f * (1 + (dificuldade / 15f));
        }

        timer += dt;

        float intervalo = 1.5f / (dificuldade / 20f);
        if (timer >= intervalo) {
            float larguraBase = 30f;
            float tamanhoVao = 50f; // Espaço físico entre a bara de baixo e de cima
            float alturaMaximaChaoVisual = 160f; // Pulo máximo é ~200px
            float alturaMinimaChaoVisual = 30f; 

            // Calcula as alturas visuais na tela
            float alturaVisualChao = alturaMinimaChaoVisual + (float) Math.random() * (alturaMaximaChaoVisual - alturaMinimaChaoVisual);
            float alturaVisualTeto = 400f - tamanhoVao - alturaVisualChao;

            float parametroAlturaChao = alturaVisualChao / 2f;
            float parametroAlturaTeto = alturaVisualTeto / 2f;
            float margem = 20f;
            // barra do chao
            // Visual
            spawnProjetil(
                    this.maxX + 50f, 
                    1000f - parametroAlturaChao, 
                    larguraBase, parametroAlturaChao, 
                    velocidade, 
                    (float) Math.PI, 
                    0f, 
                    0, 0f, 
                    duracaoAtaque, 
                    "quadrado.png"
            );
            // Hitbox
            spawnProjetil(
                    this.maxX + 50f, 
                    1000f - parametroAlturaChao + (margem / 2f), 
                    (larguraBase * 2f) - margem, (parametroAlturaChao * 2f) - margem,
                    velocidade, 
                    (float) Math.PI, 
                    0f, 
                    1, 1.25f, 
                    duracaoAtaque, 
                    ""
            );

            // teto
            // Visual
            spawnProjetil(
                    this.maxX + 50f, 
                    600f + parametroAlturaTeto, 
                    larguraBase, parametroAlturaTeto, 
                    velocidade, 
                    (float) Math.PI, 
                    0f, 
                    0, 0f, 
                    duracaoAtaque, 
                    "quadrado.png"
            );
            // Hitbox
            spawnProjetil(
                    this.maxX + 50f, 
                    600f + parametroAlturaTeto - (margem / 2f), 
                    (larguraBase * 2f) - margem, (parametroAlturaTeto * 2f) - margem,
                    velocidade, 
                    (float) Math.PI, 
                    0f, 
                    1, 1.25f, 
                    duracaoAtaque, 
                    ""
            );

            timer = 0;
        }

        if (this.tempoDecorrido >= duracaoAtaque) {
            encerrarAtaque();
        }
    }

    @Override
    public void encerrarAtaque() {
        super.encerrarAtaque();
        if (this.target != null) {
            this.target.setSoulMode(PlayerProva.SoulMode.RED);
        }
    }

    @Override
    public void reiniciarAtaque() {
        super.reiniciarAtaque();
        this.timer = 0;
        this.ataqueIniciado = false;
        if (this.target != null) {
            this.target.setSoulMode(PlayerProva.SoulMode.RED);
        }
    }


}

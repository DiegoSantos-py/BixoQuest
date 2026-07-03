package model.Ataque.Ataques.Prova.Matematica;

import model.Ataque.Ataque;
import model.Batalha.EntidadeBatalha;
import model.Player.PlayerProva;

public class AtaqueEstatistica extends Ataque {

    private float cronometro = 0;
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

        cronometro += dt;

        float intervalo = 1.5f / (dificuldade / 10f);
        if (cronometro >= intervalo) {
            float larguraBase = 30f;
            float tamanhoVao = 50f; // Espaço físico entre o chão e o teto
            float alturaMaximaChaoVisual = 180f; // Pulo máximo é ~200px
            float alturaMinimaChaoVisual = 30f; 

            // Calcula as alturas visuais na tela
            float alturaVisualChao = alturaMinimaChaoVisual + (float) Math.random() * (alturaMaximaChaoVisual - alturaMinimaChaoVisual);
            float alturaVisualTeto = 400f - tamanhoVao - alturaVisualChao;

            // O projétil usa metade da altura visual como parâmetro (pois o renderer multiplica por 2)
            float parametroAlturaChao = alturaVisualChao / 2f;
            float parametroAlturaTeto = alturaVisualTeto / 2f;
            float margem = 20f;
            // --- BARRA DO CHÃO ---
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
            // Hitbox (com margem de segurança configurável)
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

            // --- BARRA DO TETO ---
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

            cronometro = 0;
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
        this.cronometro = 0;
        this.ataqueIniciado = false;
        if (this.target != null) {
            this.target.setSoulMode(PlayerProva.SoulMode.RED);
        }
    }

    @Override
    public String toString() {
        return "Ataque Estatistica";
    }
}

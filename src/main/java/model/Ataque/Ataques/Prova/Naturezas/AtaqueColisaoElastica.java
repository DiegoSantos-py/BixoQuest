package model.Ataque.Ataques.Prova.Naturezas;

import model.Ataque.Ataque;
import model.Batalha.EntidadeBatalha;
import model.Player.PlayerProva;
import model.Projetil.Comportamentos.ProjetilQuicaNasBordas;
import model.Projetil.Projetil;

import java.util.Random;

public class AtaqueColisaoElastica extends Ataque {

    private float timer = 0;
    private int fase = 0; // 0 = spawn, 1 = espera (1.5s), 2 = caos
    private Random rand = new Random();
    
    private float tempoFimAtaque = 0;

    public AtaqueColisaoElastica(PlayerProva target, EntidadeBatalha owner, float dificuldade) {
        super(target, owner, dificuldade, 10);
    }

    @Override
    protected void logicaAtaque(float dt) {
        timer += dt;

        if (fase == 0) {
            float centroX = (this.getMinX() + this.getMaxX()) / 2f;
            float centroY = (this.getMinY() + this.getMaxY()) / 2f;

            ProjetilQuicaNasBordas quica = new ProjetilQuicaNasBordas(getMinX(), getMaxX(), getMinY(), getMaxY(), 15f);

            for (int i = 0; i < 5; i++) {

                float tempoVoo = 4f + rand.nextFloat() * 6f;
                float duracaoTotal = 1.5f + tempoVoo;
                
                if (duracaoTotal > tempoFimAtaque) {
                    tempoFimAtaque = duracaoTotal;
                }

                Projetil p = spawnProjetil(
                        centroX, centroY,
                        15f, 15f,
                        0f,     // sem velocidade
                        0f, 0f, 
                        0, 0f,  // inofensivo no início
                        duracaoTotal,
                        "circulo.png"
                );
                
                if (p != null) {
                    p.setPersistente(true);
                    p.addComportamento(quica);
                }
            }
            //INICIA a diversao
            fase = 1;
        } 
        else if (fase == 1) {
            // Fica parado no centro por 1.5s
            if (timer >= 1.5f) {
                // Dispersa os projeteis
                for (Projetil p : getProjeteis()) {
                    if (!p.isAtivo()) continue;
                    
                    // Velocidade baseada na dificuldade + variação aleatória (-50 a +50)
                    float velocidade = 300f + (dificuldade * 10f) + (rand.nextFloat() * 100f - 50f);
                    float angulo = rand.nextFloat() * (float) Math.PI * 2f;
                    
                    p.getVelocidade().set(
                            (float) (velocidade * Math.cos(angulo)), 
                            (float) (velocidade * Math.sin(angulo))
                    );
                    
                    // Ficam perigosos
                    p.setDanoShield(1);
                    p.setDanoNota(1f);
                }
            }
        }
        if (timer >= tempoFimAtaque) {
            this.encerrarAtaque();
        }
    }

    @Override
    public void reiniciarAtaque() {
        super.reiniciarAtaque();
        this.timer = 0;
        this.fase = 0;
        this.tempoFimAtaque = 0;
    }


}

package model.Ataque.Ataques.Prova.Naturezas;

import model.Ataque.Ataque;
import model.Batalha.EntidadeBatalha;
import model.Player.PlayerProva;
import model.Projetil.Projetil;

import java.util.Random;

public class AtaqueColisaoElastica extends Ataque {

    private float timer = 0;
    private int fase = 0; // 0 = spawn, 1 = espera (1.5s), 2 = caos
    private Random rand = new Random();
    
    private float tempoFimAtaque = 0;
    private final float RAIO_ELETRON = 15f;

    public AtaqueColisaoElastica(PlayerProva target, EntidadeBatalha owner, float dificuldade) {
        super(target, owner, dificuldade, 10);
    }

    @Override
    protected void logicaAtaque(float dt) {
        timer += dt;

        if (fase == 0) {
            float centroX = (this.getMinX() + this.getMaxX()) / 2f;
            float centroY = (this.getMinY() + this.getMaxY()) / 2f;

            for (int i = 0; i < 5; i++) {
                // Randomiza o tempo que cada elétron vai viver
                // Como ele fica 1.5s parado antes de agir, a duração total é 1.5s + (4 a 10s voando)
                float tempoVoo = 4f + rand.nextFloat() * 6f;
                float duracaoTotal = 1.5f + tempoVoo;
                
                if (duracaoTotal > tempoFimAtaque) {
                    tempoFimAtaque = duracaoTotal;
                }

                Projetil p = spawnProjetil(
                        centroX, centroY,
                        RAIO_ELETRON * 2, RAIO_ELETRON * 2, // tamanho do elétron
                        0f,     // sem velocidade
                        0f, 0f, 
                        0, 0f,  // inofensivo no início
                        duracaoTotal, // o projetil gerencia seu próprio despawn
                        "eletron.png"
                );
                
                if (p != null) {
                    p.setPersistente(true);
                }
            }
            fase = 1;
        } 
        else if (fase == 1) {
            // Fica parado no centro por 1.5s
            if (timer >= 1.5f) {
                // Dispersa os elétrons
                for (Projetil p : getProjeteis()) {
                    if (!p.isAtivo()) continue;
                    
                    // Velocidade baseada na dificuldade + variação aleatória (-50 a +50)
                    float vel = 300f + (dificuldade * 10f) + (rand.nextFloat() * 100f - 50f);
                    float angulo = rand.nextFloat() * (float) Math.PI * 2f;
                    
                    p.getVelocidade().set(
                            (float) (vel * Math.cos(angulo)), 
                            (float) (vel * Math.sin(angulo))
                    );
                    
                    // Ficam perigosos
                    p.setDanoShield(1);
                    p.setDanoNota(1f);
                }
                fase = 2;
            }
        } 
        else if (fase == 2) {
            // Checa colisão com as paredes para o "Bounce"
            for (Projetil p : getProjeteis()) {
                if (!p.isAtivo()) continue;
                
                float px = p.getX();
                float py = p.getY();
                
                // Quica na esquerda / direita
                if (px - RAIO_ELETRON <= this.getMinX()) {
                    p.setX(this.getMinX() + RAIO_ELETRON + 1);
                    p.getVelocidade().setX(Math.abs(p.getVelocidade().getX()));
                } else if (px + RAIO_ELETRON >= this.getMaxX()) {
                    p.setX(this.getMaxX() - RAIO_ELETRON - 1);
                    p.getVelocidade().setX(-Math.abs(p.getVelocidade().getX()));
                }
                
                // Quica no topo / baixo
                if (py - RAIO_ELETRON <= this.getMinY()) {
                    p.setY(this.getMinY() + RAIO_ELETRON + 1);
                    p.getVelocidade().setY(Math.abs(p.getVelocidade().getY()));
                } else if (py + RAIO_ELETRON >= this.getMaxY()) {
                    p.setY(this.getMaxY() - RAIO_ELETRON - 1);
                    p.getVelocidade().setY(-Math.abs(p.getVelocidade().getY()));
                }
            }
            
            // Encerra quando o timer ultrapassar a maior duração gerada
            if (timer >= tempoFimAtaque) {
                this.encerrarAtaque();
            }
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

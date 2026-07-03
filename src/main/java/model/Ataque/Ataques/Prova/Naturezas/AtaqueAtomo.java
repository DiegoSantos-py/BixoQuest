package model.Ataque.Ataques.Prova.Naturezas;

import model.Ataque.Ataque;
import model.Batalha.EntidadeBatalha;
import model.Player.PlayerProva;
import model.Projetil.Projetil;
import model.util.Vector2D;

public class AtaqueAtomo extends Ataque {

    private float timer = 0;
    private int fase = 0; // 0 = spawnar atomo, 1 = desacelerar, 2 = burst
    private Projetil atomo;
    private float burstTimer = 0;
    private float burstTime = 0;
    private float burstTimeMax = 7.5f;
    private float anguloBurst = 0;

    public AtaqueAtomo(PlayerProva target, EntidadeBatalha owner, float dificuldade) {
        super(target, owner, dificuldade, 150);
    }

    @Override
    protected void logicaAtaque(float dt) {
        if (fase == 0) {

            float startX = owner.getX();
            float startY = owner.getY() -50f;
            
            atomo = spawnProjetil(
                    startX, startY, 
                    45, 45, // tamanho do átomo
                    900f, // velocidade inicial bem alta
                    (float) Math.PI / 2f, // atirando para cima
                    (float) -Math.PI / 2f, 
                    1, 0f, 
                    8.5f, // duração
                    "atomo.png" // placeholder do átomo
            );
            
            if (atomo != null) {
                atomo.setPersistente(true);
                // Inicia sem dar dano para não ser injusto logo no spawn
                atomo.setDanoShield(0);
                atomo.setDanoNota(0f);
            }
            fase = 1;
        } 
        else if (fase == 1) {
            timer += dt;
            
            if (atomo != null) {
                float factor = 0.976f;
                Vector2D vel = atomo.getVelocidade();
                vel.set(vel.getX() * factor, vel.getY() * factor);
            }
            
            // Após 1.5s o átomo "para" e se torna instável
            if (timer >= 1.5f) {
                if(atomo != null){
                    atomo.setVelocidade(new Vector2D(0f, 0f));
                }
                fase = 2;
                timer = 0;
                if (atomo != null) {
                    // O átomo se torna perigoso
                    atomo.setDanoShield(1); 
                    atomo.setDanoNota(1f);
                }
            }
        } 
        else if (fase == 2) {
            if (atomo == null || !atomo.isAtivo()) {
                this.encerrarAtaque();
                return;
            }
            
            burstTimer += dt;
            burstTime += dt;
            float burstInterval = 0.25f - (dificuldade / 100f);

            if(burstTime < burstTimeMax) {
                if (burstTimer >= burstInterval) {
                    float atomX = atomo.getX();
                    float atomY = atomo.getY();


                    for (int i = 0; i < 4; i++) {
                        float angulo = anguloBurst + (i * (float) (Math.PI / 2f));

                        spawnProjetil(
                                atomX, atomY,
                                15, 15, // tamanho do elétron
                                225f + (dificuldade * 10), // velocidade do elétron
                                angulo,
                                angulo,
                                1, 0.5f,
                                4f,
                                "eletron.png" // placeholder do elétron
                        );
                    }

                    // Faz a espiral girar com o tempo
                    anguloBurst += 0.367f;
                    burstTimer = 0;
                }
            }
            // Encerra após atirar todos os bursts
            if (burstTimer >= burstTimeMax) {
                timer += dt;
                // Espera um pouco para os últimos elétrons saírem da tela
                if (timer > 1.5f) {
                    this.encerrarAtaque();
                }
            }
        }
    }

    @Override
    public void reiniciarAtaque() {
        super.reiniciarAtaque();
        this.timer = 0;
        this.fase = 0;
        this.burstTimer = 0;
        this.burstTime = 0;
        this.anguloBurst = 0;
        if (this.atomo != null) {
            this.atomo.desativar();
            this.atomo = null;
        }
    }

    @Override
    public String toString() {
        return "Ataque Átomo Instável";
    }
}

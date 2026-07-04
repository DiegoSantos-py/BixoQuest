package model.Ataque.Ataques.Prova.Naturezas;

import model.Ataque.Ataque;
import model.Batalha.EntidadeBatalha;
import model.Player.PlayerProva;
import model.Projetil.Projetil;
import model.util.MathUtils;
import model.util.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class AtaqueAtomosGravidade extends Ataque {

    private float timer = 0;
    private int fase = 0; 
    private List<Projetil> atomos = new ArrayList<>();
    private float burstTimer = 0;
    private float burstTime = 0;
    private float burstTimeMax = 7.5f;
    private float anguloBurst = 0;

    private float timerCarro = 0;
    private final float SPAWN_DELAY_CARRO = 1.0f;
    private boolean started = false;

    public AtaqueAtomosGravidade(PlayerProva target, EntidadeBatalha owner, float dificuldade) {
        super(target, owner, dificuldade, 150);
        this.minX = 760f;
        this.maxX = 1160f;
        this.minY = 600f;
        this.maxY = 1000f; 
    }

    @Override
    protected void logicaAtaque(float dt) {
        if (!started) {
            if (target != null) {
                target.setSoulMode(PlayerProva.SoulMode.BLUE);
            }
            started = true;
        }

        // Car spawn logic
        timerCarro += dt;
        if (timerCarro >= SPAWN_DELAY_CARRO) {
            timerCarro = 0;
            spawnCarro();
        }

        if (fase == 0) {
            float startX = owner.getX();
            float startY = owner.getY() - 50f;
            
            float[] angles = {(float) Math.PI / 4f, (float) (3 * Math.PI / 4f)};
            for (float angle : angles) {
                Projetil p = spawnProjetil(
                        startX, startY, 
                        45, 45, 
                        900f, 
                        angle, 
                        angle - (float) Math.PI / 2f,
                        1, 0f, 
                        8.5f, 
                        "atomo.png" 
                );
                if (p != null) {
                    p.setPersistente(true);
                    p.setDanoShield(0);
                    p.setDanoNota(0f);
                    atomos.add(p);
                }
            }
            fase = 1;
        } 
        else if (fase == 1) {
            timer += dt;
            
            for (Projetil atomo : atomos) {
                if (atomo != null) {
                    float factor = 0.976f;
                    Vector2D vel = atomo.getVelocidade();
                    vel.set(vel.getX() * factor, vel.getY() * factor);
                }
            }
            
            if (timer >= 1.5f) {
                for (Projetil atomo : atomos) {
                    if (atomo != null) {
                        atomo.setVelocidade(new Vector2D(0f, 0f));
                        atomo.setDanoShield(1); 
                        atomo.setDanoNota(1f);
                    }
                }
                fase = 2;
                timer = 0;
            }
        } 
        else if (fase == 2) {
            boolean anyActive = false;
            for (Projetil atomo : atomos) {
                if (atomo != null && atomo.isAtivo()) {
                    anyActive = true;
                    break;
                }
            }
            if (!anyActive) {
                this.encerrarAtaque();
                return;
            }
            
            burstTimer += dt;
            burstTime += dt;
            float burstInterval = 0.25f - (dificuldade / 100f);

            if(burstTime < burstTimeMax) {
                if (burstTimer >= burstInterval) {
                    for (Projetil atomo : atomos) {
                        if (atomo == null || !atomo.isAtivo()) continue;
                        float atomX = atomo.getX();
                        float atomY = atomo.getY();

                        for (int i = 0; i < 4; i++) {
                            float angulo = anguloBurst + (i * (float) (Math.PI / 2f));
                            spawnProjetil(
                                    atomX, atomY,
                                    15, 15, 
                                    225f + (dificuldade * 10), 
                                    angulo,
                                    angulo,
                                    1, 0.5f,
                                    4f,
                                    "eletron.png" 
                            );
                        }
                    }
                    anguloBurst += 0.367f;
                    burstTimer = 0;
                }
            }
            
            if (burstTime >= burstTimeMax) {
                timer += dt;
                if (timer > 1.5f) {
                    this.encerrarAtaque();
                }
            }
        }
    }

    private void spawnCarro() {
        boolean vemDaEsquerda = MathUtils.randomFloatInRange(0, 1) > 0.5f;
        float startY = maxY - 20; // Apenas no chão
        float startX = vemDaEsquerda ? minX - 100 : maxX + 100;
        float angulo = vemDaEsquerda ? 0 : (float) Math.PI;
        String sprite = vemDaEsquerda ? "carro.png" : "carroEsq.png";

        spawnProjetil(
            startX, startY,
            80, 40,
            250f, angulo, 0f,
            1, 0.5f,
            5f, sprite
        );
    }

    @Override
    public void reiniciarAtaque() {
        super.reiniciarAtaque();
        this.timer = 0;
        this.fase = 0;
        this.burstTimer = 0;
        this.burstTime = 0;
        this.anguloBurst = 0;
        this.timerCarro = 0;
        this.started = false;
        for (Projetil atomo : atomos) {
            if (atomo != null) atomo.desativar();
        }
        this.atomos.clear();
    }


}

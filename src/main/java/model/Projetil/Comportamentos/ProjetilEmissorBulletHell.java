package model.Projetil.Comportamentos;

import model.Projetil.ComportamentoProjetil;
import model.Projetil.Projetil;

public class ProjetilEmissorBulletHell implements ComportamentoProjetil {

    private String spriteDir;
    private float burstInterval;
    private int numProjBurst;
    private float initialAngle;
    private float angleShift;
    private float projectileSpeed;
    private float projectileSize;
    private float projectileDuration;
    private float delayAntesDeAtirar;

    public ProjetilEmissorBulletHell(String spriteDir, float burstInterval, int numProjBurst, 
            float angleShift, float projectileSpeed, float projectileSize, 
            float projectileDuration, float initialAngle, float delayAntesDeAtirar) {
        this.spriteDir = spriteDir;
        this.burstInterval = burstInterval;
        this.numProjBurst = numProjBurst;
        this.angleShift = angleShift;
        this.projectileSpeed = projectileSpeed;
        this.projectileSize = projectileSize;
        this.projectileDuration = projectileDuration;
        this.initialAngle = initialAngle;
        this.delayAntesDeAtirar = delayAntesDeAtirar;
    }

    @Override
    public void executar(Projetil projetil, float dt) {
        if (!projetil.isAtivo()) return;

        float tempoAtivo = projetil.getTempoDeVida() - delayAntesDeAtirar;
        if (tempoAtivo < 0) return;

        float previousTempoAtivo = tempoAtivo - dt;
        int currentBurstIndex = (int) (tempoAtivo / burstInterval);
        int previousBurstIndex = previousTempoAtivo < 0 ? -1 : (int) (previousTempoAtivo / burstInterval);

        if (currentBurstIndex > previousBurstIndex) {
            float angleIncrement = (float) (Math.PI * 2) / numProjBurst;
            float currentAngle = initialAngle + (currentBurstIndex * angleShift);

            for (int i = 0; i < numProjBurst; i++) {
                float angle = currentAngle + (i * angleIncrement);
                projetil.getFactory().spawn(
                        projetil.getX(), projetil.getY(),
                        projectileSize, projectileSize,
                        projectileSpeed,
                        angle, angle,
                        1, 0.5f, projectileDuration, spriteDir
                );
            }
        }
    }
}

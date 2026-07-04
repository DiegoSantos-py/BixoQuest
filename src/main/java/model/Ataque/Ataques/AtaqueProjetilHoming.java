
package model.Ataque.Ataques;

import model.Batalha.EntidadeBatalha;
import model.Ataque.Ataque;
import model.Player.PlayerProva;
import model.Projetil.Comportamentos.ProjetilQueSegue;
import model.Projetil.Projetil;

public class AtaqueProjetilHoming extends Ataque {

    private float timer = 0;
    private int projeteisSpawnados = 0;

    private final ProjetilQueSegue homing = new ProjetilQueSegue(125f, 4.5f);

    public AtaqueProjetilHoming(PlayerProva target, EntidadeBatalha owner, float dificuldade) {

        super(target, owner, dificuldade, 60);
    }

    @Override
    protected void logicaAtaque(float dt) {

        timer += dt;
        float velocidade = 10f * (dificuldade / 2 + 0.5f); // 1 pra ind 10 2 pra ind 30

        int limiteProjeteis = 5 + (int) (dificuldade / 10);
        if (timer >= (10 / dificuldade) && !this.isFinalizado() && projeteisSpawnados < limiteProjeteis) {

            int framesNoFuturo = (int) (3 + (dificuldade / 10) * 2);
            float anguloParaPlayer = (float) Math.atan2(
                    (target.getY() + target.getVelocidade().getY() * dt * framesNoFuturo) - owner.getY(),
                    (target.getX() + target.getVelocidade().getX() * dt * framesNoFuturo) - owner.getX());
            Projetil p = spawnProjetil(owner.getX(), owner.getY(), 40, 40, velocidade, anguloParaPlayer,
                    anguloParaPlayer, 1, 0f, 7.5f, "mordida.png");

            if (p != null) {
                p.addComportamento(homing);
            }

            projeteisSpawnados++;
            timer = 0;
        }

        // lanca 5 + (tiros adicionais baseado na dificuldade) e
        if (projeteisSpawnados >= limiteProjeteis) {
            if (timer >= 1f) {
                this.encerrarAtaque();
            }
        }
    }

    @Override
    public void reiniciarAtaque() {
        super.reiniciarAtaque();
        this.timer = 0;
        this.projeteisSpawnados = 0;
    }


}

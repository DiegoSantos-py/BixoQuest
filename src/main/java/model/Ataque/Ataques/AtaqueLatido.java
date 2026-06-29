package model.Ataque.Ataques;

import model.Batalha.EntidadeBatalha;
import model.Ataque.Ataque;
import model.Player.PlayerProva;

public class AtaqueLatido extends Ataque {

    private float timer = 0;
    private int projeteisSpawnados = 0;

    public AtaqueLatido(PlayerProva target, EntidadeBatalha owner, float dificuldade) {

        super(target, owner, dificuldade, 200);
    }

    @Override
    protected void logicaAtaque(float dt) {

        timer += dt;
        float velocidade = 50f * (dificuldade / 2 + 0.5f); // 1 pra ind 10 2 pra ind 30

        int limiteProjeteis = 5 + (int) (dificuldade / 10);

        if (timer >= (10 / dificuldade) && !this.isFinalizado() && projeteisSpawnados < limiteProjeteis) {
            
            int framesNoFuturo = (int) (3 + (dificuldade/10) * 2);
            float anguloParaPlayer = (float) Math.atan2(
                (target.getHitbox().getCentro().getY() + target.getVelocidade().getY() * dt * framesNoFuturo) - owner.getHitbox().getCentro().getY(),
                (target.getHitbox().getCentro().getX() + target.getVelocidade().getX() * dt * framesNoFuturo) - owner.getHitbox().getCentro().getX()
            );

            spawnProjetil(owner.getHitbox().getCentro().getX(),
                    owner.getHitbox().getCentro().getY(),
                    40,
                    40,
                    velocidade,
                    anguloParaPlayer,
                    anguloParaPlayer,
                    1,
                    0f,
                    7.5f,
                    "/assets/batalha/projeteis/latido.png");

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

    @Override
    public String toString() {
        return "Ataque Latido";
    }
}

package model.Projetil.Comportamentos;

import model.Projetil.ComportamentoProjetil;
import model.Projetil.Projetil;
import model.util.Vector2D;

public class ProjetilAceleracao implements ComportamentoProjetil {

    private float aceleracao;

    public ProjetilAceleracao(float aceleracao) {
        this.aceleracao = aceleracao;
    }

    @Override
    public void executar(Projetil projetil, float dt) {
        Vector2D vel = projetil.getVelocidade();
        float speed = (float) Math.sqrt(vel.getX() * vel.getX() + vel.getY() * vel.getY());

        if (speed > 0.2f) { //aplica se n parou
            float newSpeed = speed + (aceleracao * dt);

            if (newSpeed <= 0.2f) {
                //para dps de certo ponto
                projetil.setVelocidade(new Vector2D(0f, 0f));
            } else {
                float ratio = newSpeed / speed;
                projetil.setVelocidade(new Vector2D(vel.getX() * ratio, vel.getY() * ratio));
            }
        }
    }
}

package model.Projetil.Comportamentos;

import model.Projetil.ComportamentoProjetil;
import model.Projetil.Projetil;
import model.util.Vector2D;

public class ProjetilSenoidal implements ComportamentoProjetil {

    private float amplitude; // the max distance from center
    private float frequencia; // how fast it oscillates
    private boolean moveHorizontal;

    public ProjetilSenoidal(float amplitude, float frequencia, boolean moveHorizontal) {
        this.amplitude = amplitude;
        this.frequencia = frequencia;
        this.moveHorizontal = moveHorizontal;
    }


    @Override
    public void executar(Projetil projetil, float dt) {
        float tempo = projetil.getTempoDeVida();
        Vector2D vel = projetil.getVelocidade();

        // A derivada de posicao(t) = A * sin(f * t) é vel(t) = A * f * cos(f * t)
        // Isso integra perfeitamente de volta para uma onda senoidal sem precisar
        // rastrear a posicao original!
        float velPerpendicular = (float) (amplitude * frequencia * Math.cos(frequencia * tempo)) * 0.8f;

        if (moveHorizontal) {
            // Se a velocidade principal for em X, a onda sobe e desce em Y
            vel.setY(velPerpendicular);
        } else {
            // Se a velocidade principal for em Y, a onda vai pra esquerda e direita em X
            vel.setX(velPerpendicular);
        }

        // Atualiza o ângulo do projétil para apontar na direção exata da sua curva senoidal!
        projetil.getHitbox().setAnguloRad((float) Math.atan2(vel.getY(), vel.getX()));
    }
}

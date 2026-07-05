package model.Projetil.Comportamentos;

import model.Projetil.ComportamentoProjetil;
import model.Projetil.Projetil;
import java.util.Random;

public class ProjetilRandomizaVelocidadeAposTempo implements ComportamentoProjetil {
    
    private float tempoAtraso;
    private float velMin;
    private float velMax;
    private Random rand = new Random();

    public ProjetilRandomizaVelocidadeAposTempo(float tempoAtraso, float velMin, float velMax) {
        this.tempoAtraso = tempoAtraso;
        this.velMin = velMin;
        this.velMax = velMax;
    }

    @Override
    public void executar(Projetil projetil, float dt) {
        // Se a velocidade for exatamente 0 em X e Y, ele ainda não randomizou.
        if (projetil.getTempoDeVida() >= tempoAtraso && projetil.getVelocidade().getX() == 0 && projetil.getVelocidade().getY() == 0) {
            float vel = velMin + rand.nextFloat() * (velMax - velMin);
            float angulo = rand.nextFloat() * (float) Math.PI * 2f;
            projetil.getVelocidade().set(
                    (float) (vel * Math.cos(angulo)), 
                    (float) (vel * Math.sin(angulo))
            );
        }
    }
}

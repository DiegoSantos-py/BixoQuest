package model.Projetil.Comportamentos;

import model.Projetil.ComportamentoProjetil;
import model.Projetil.Projetil;
import model.util.MathUtils;

public class ProjetilGravidade implements ComportamentoProjetil {

    private final float forcaGravidade;

    public ProjetilGravidade(float forcaGravidade) {
        this.forcaGravidade = forcaGravidade;
    }

    @Override
    public void executar(Projetil projetil, float dt) {
        float vy = projetil.getVelocidade().getY();
        
        // Aplica a aceleração no eixo Y (gravidade)
        projetil.getVelocidade().setY(vy - forcaGravidade * dt);

    }
}

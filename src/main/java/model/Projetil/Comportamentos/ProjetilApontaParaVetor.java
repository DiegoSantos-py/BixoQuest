package model.Projetil.Comportamentos;

import model.Projetil.ComportamentoProjetil;
import model.Projetil.Projetil;

public class ProjetilApontaParaVetor implements ComportamentoProjetil {

    @Override
    public void executar(Projetil projetil, float dt) {
        float vx = projetil.getVelocidade().getX();
        float vy = projetil.getVelocidade().getY();
        
        // Só atualiza se ele estiver se movendo, para não bugar a conta caso a velocidade seja 0
        if (vx != 0 || vy != 0) {
            projetil.getHitbox().setAnguloRad((float) Math.atan2(vy, vx));
        }
    }
}

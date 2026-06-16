package model.Projetil.Comportamentos;

import model.Projetil.*;
import model.util.*;

public class ProjetilExplosivo implements ComportamentoProjetil, ComportamentoAoDespawnar {




    @Override
    public void aoDespawnar(Projetil projetil, ProjetilFactory factory) {
        if (projetil.getFactory() == null) return;
        //se ele n tiver factory(n sei vai que ne, nada acontece)

        float cx = projetil.getHitbox().getCentro().getX();
        float cy = projetil.getHitbox().getCentro().getY();
        //pega as posicoes x e y do projetil
        for(int i = 0; i < 8; i++) {
            float angle = (float)(i * (Math.PI * 2) / 8) + projetil.getHitbox().getAnguloRotacao();
            System.out.println(angle);
            projetil.getFactory().spawn(cx, cy, 15, 15, 150f, angle, angle, 1, 0.1f, 10f,
                    "/assets/batalha/projeteis/osso.png");
        }
    }

    @Override
    public void executar(Projetil projetil, float dt) {

    }
}
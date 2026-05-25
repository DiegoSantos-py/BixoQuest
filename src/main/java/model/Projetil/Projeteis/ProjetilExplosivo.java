package model.Projetil.Projeteis;

import model.util.*;
import model.Projetil.Projetil;
import model.Projetil.ProjetilID;

public class ProjetilExplosivo extends Projetil {

    public ProjetilExplosivo(Hitbox hitbox,Vector2D velocidade, int danoShield, float danoNota, float duracaoMaxima) { 
        super(hitbox, velocidade, danoShield, danoNota, duracaoMaxima); 
    }

    @Override 
    public void executarAI(float dt) {
        //n tem nada pra acontecer aq

    }


    @Override
    public void aoColidirComPlayer() {

    }

    @Override
    public void aoDespawnar() {
        if (this.factory == null) return;
        //se ele n tiver factory(n sei vai que ne, nada acontece)

        float cx = this.hitbox.getCentro().getX();
        float cy = this.hitbox.getCentro().getY();
        //pega as posicoes x e y do projetil
        for(int i = 0; i < 8; i++) {
            float angle = (float)(i * (Math.PI * 2) / 8) + this.hitbox.getAnguloRotacao();
            this.factory.spawn(cx, cy, 15, 15, 70f, angle, angle, ProjetilID.BASICO, 1, 0.1f, 10f);
        }
        //spawna 8 projeteis ao que meio q saem da psoicao do projetil original com uma diferença de 360/8 + a rotacao original do projetil graus
    }
}
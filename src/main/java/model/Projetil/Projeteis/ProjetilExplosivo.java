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
         //dps eu faço isso
        //n tem nada pra acontecer aq

    }


    @Override
    public void aoColidirComPlayer() {

    }

    @Override
    public void aoDespawnar() {
        if (this.factory == null) return;
        
        float cx = this.hitbox.getCentro().getX();
        float cy = this.hitbox.getCentro().getY();

        for(int i = 0; i < 8; i++) {
            float angle = (float)(i * (Math.PI * 2) / 8) + this.hitbox.getAnguloRotacao();
            this.factory.spawn(cx, cy, 15, 15, 70f, angle, angle, ProjetilID.BASICO, 1, 0.1f, 10f);
        }
    }
}
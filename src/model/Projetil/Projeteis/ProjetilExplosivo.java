package model.Projetil.Projeteis;

import model.util.*;
import model.Projetil.Projetil;

public class ProjetilExplosivo extends Projetil {

    public ProjetilExplosivo(Hitbox hitbox,Vector2D velocidade, int danoShield, float danoNota, int duracaoMaxima) { 
        super(hitbox, velocidade, danoShield, danoNota, duracaoMaxima); 
    }

    @Override 
    public void executarAI(float dt) { 
         //dps eu faço isso
    }


    @Override
    public void aoColidirComPlayer() {
         this.ativo = false; 
    }

    @Override
    public void aoDespawnar() {
        for(int i = 0; i < 8; i++) {
            float angle = (float)(i * Math.PI / 4); // 8 projeteis em 360 graus
            Vector2D siz = new Vector2D(5, 5);
            Vector2D dir = new Vector2D((float)Math.cos(angle), (float)Math.sin(angle));
            Vector2D vel = dir.mult(200); // velocidade do projetil filho
            Hitbox hb = new Hitbox(this.hitbox.getCentro(), siz, angle); // hitbox menor
            ProjetilBasico filho = new ProjetilBasico(hb, vel, this.danoShield / 2, this.danoNota / 2, 60);
            //TODO : adicionar o filho na lista de projetil ativos do jogo
        }
    }
}
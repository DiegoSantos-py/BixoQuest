package model.Projetil.Projeteis;

import model.util.*;
import model.Projetil.Projetil;

public class ProjetilBasico extends Projetil {

    public ProjetilBasico(Hitbox hitbox,Vector2D velocidade, int danoShield, float danoNota, int duracaoMaxima) { 
        super(hitbox, velocidade, danoShield, danoNota, duracaoMaxima); 
    }

    @Override 
    public void executarAI(float dt) { 

    }

    @Override
    public void aoColidirComPlayer() {
         this.ativo = false; // Comportamento: sumir
    }
}
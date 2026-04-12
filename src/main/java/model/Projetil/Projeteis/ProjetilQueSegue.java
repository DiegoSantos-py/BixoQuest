package model.Projetil.Projeteis;

import model.util.*;
import model.Projetil.Projetil;
import model.Player.PlayerProva;

public class ProjetilQueSegue extends Projetil {


    static PlayerProva TARGET = PlayerProva.getInstancia();

    public ProjetilQueSegue(Hitbox hitbox,Vector2D velocidade, int danoShield, float danoNota, int duracaoMaxima) { 
        super(hitbox, velocidade, danoShield, danoNota, duracaoMaxima); 
    }

    @Override 
    public void executarAI(float dt) { 
        Vector2D direcao = TARGET.getCentro().subtrair(this.getCentro()).normalize();
        this.velocidade = direcao.mult(this.velocidade.magnitude());

    }

    @Override
    public void aoColidirComPlayer() {
         this.ativo = false; // Comportamento: sumir
    }
}
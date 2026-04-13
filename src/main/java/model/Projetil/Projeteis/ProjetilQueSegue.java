package model.Projetil.Projeteis;

import model.util.*;
import model.Projetil.Projetil;

public class ProjetilQueSegue extends Projetil {

    public ProjetilQueSegue(Hitbox hitbox,Vector2D velocidade, int danoShield, float danoNota, float duracaoMaxima) { 
        super(hitbox, velocidade, danoShield, danoNota, duracaoMaxima); 
    }

    @Override 
    public void executarAI(float dt) { 
        if (this.target != null) {
            Vector2D direcao = this.target.getCentro().subtrair(this.getCentro()).normalize();
            this.velocidade = direcao.mult(this.velocidade.magnitude());
        }
    }

    @Override
    public void aoColidirComPlayer() {
        super.aoColidirComPlayer(); // aplica o dano no player (ReceberDano)
        desativar();               // se destrói após o impacto
    }
}
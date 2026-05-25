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
            //calcula a direção para o alvo
            Vector2D direcaoDesejada = this.target.getCentro().subtrair(this.getCentro()).normalize();

            // define a velocidade máxima que ele pode atingir
            float velocidadeMaxima = 105f;
            Vector2D velocidadeDesejada = direcaoDesejada.mult(velocidadeMaxima);

            // define a força com que ele vai seguir o target (quanto maior, mais rápido ele vira/acelera)
            float forcaInercia = 5.0f;

            // calcula a aceleracao (VelocidadeDesejada - VelocidadeAtual) * força
            Vector2D aceleracao = velocidadeDesejada.subtrair(this.velocidade).mult(forcaInercia * dt);

            // aplica a aceleração na velocidade atual
            this.velocidade = this.velocidade.add(aceleracao);
        }
        //boom temos inercia
    }

    @Override
    public void aoColidirComPlayer() {
        super.aoColidirComPlayer(); // aplica o dano no player (ReceberDano)
        desativar();               // se destrói após o impacto
    }
}
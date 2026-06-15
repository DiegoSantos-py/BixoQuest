package model.Projetil.Comportamentos;

import model.Player.PlayerProva;
import model.Projetil.*;
import model.util.*;

public class ProjetilQueSegue implements ComportamentoProjetil, ComportamentoAoColidirComPlayer {



    @Override
    public void executar(Projetil projetil, float dt) {
        if (projetil.getTarget() != null) {
            //calcula a direção para o alvo
            Vector2D direcaoDesejada = projetil.getTarget().getCentro().subtrair(projetil.getCentro()).normalize();

            // define a velocidade máxima que ele pode atingir
            float velocidadeMaxima = 105f;
            Vector2D velocidadeDesejada = direcaoDesejada.mult(velocidadeMaxima);

            // define a força com que ele vai seguir o target (quanto maior, mais rápido ele vira/acelera)
            float forcaInercia = 5.0f;

            // calcula a aceleracao (VelocidadeDesejada - VelocidadeAtual) * força
            Vector2D aceleracao = velocidadeDesejada.subtrair(projetil.getVelocidade()).mult(forcaInercia * dt);

            // aplica a aceleração na getVelocidade() atual
            projetil.setVelocidade(projetil.getVelocidade().add(aceleracao));;
        }
        //boom temos inercia

    }

    @Override
    public void aoColidirComPlayer(Projetil projetil, PlayerProva target) {
        projetil.aoColidirComPlayer(); // aplica o dano no player (ReceberDano)
        projetil.desativar();
    }
}
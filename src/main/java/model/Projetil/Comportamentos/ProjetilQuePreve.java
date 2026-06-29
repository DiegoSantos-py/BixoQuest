package model.Projetil.Comportamentos;

import model.Player.PlayerProva;
import model.Projetil.*;
import model.util.*;

public class ProjetilQuePreve implements ComportamentoProjetil, ComportamentoAoColidirComPlayer {

    @Override
    public void executar(Projetil projetil, float dt) {
        if (projetil.getTarget() != null) {
            
            // Calcula a distância atual até o alvo
            float distancia = projetil.getTarget().getCentro().subtrair(projetil.getCentro()).magnitude();
            
            // Velocidade máxima do projétil
            float velocidadeMaxima = 125f;
            
            // Estima o tempo para interceptar (distância / velocidade)
            float tempoInterceptacao = distancia / velocidadeMaxima; 
            
            // Limita o tempo de previsão para não mirar longe demais se estiver muito distante
            tempoInterceptacao = Math.min(tempoInterceptacao, 1.2f);

            // Calcula a posição futura: PosAtual + (Velocidade * Tempo)
            Vector2D posicaoFutura = projetil.getTarget().getCentro().add(
                projetil.getTarget().getVelocidade().mult(tempoInterceptacao)
            );

            // Calcula a direção desejada apontando para a posição FUTURA
            Vector2D direcaoDesejada = posicaoFutura.subtrair(projetil.getCentro());
            
            if (direcaoDesejada.getX() != 0 || direcaoDesejada.getY() != 0) {
                direcaoDesejada = direcaoDesejada.normalize();
            }

            Vector2D velocidadeDesejada = direcaoDesejada.mult(velocidadeMaxima);

            // Força de inércia dita o quão rápido o projétil consegue fazer a curva
            float forcaInercia = 6.0f;

            // Calcula a aceleração
            Vector2D aceleracao = velocidadeDesejada.subtrair(projetil.getVelocidade()).mult(forcaInercia * dt);

            // Aplica a aceleração
            projetil.setVelocidade(projetil.getVelocidade().add(aceleracao));
            
            // Rotaciona (+ PI/2 pois o sprite aponta para cima)
            float anguloAtual = (float) Math.atan2(projetil.getVelocidade().getY(), projetil.getVelocidade().getX()) + (float) (Math.PI / 2);
            projetil.getHitbox().setAnguloRad(anguloAtual);
        }
    }

    @Override
    public void aoColidirComPlayer(Projetil projetil, PlayerProva target) {
        projetil.aoColidirComPlayer();
        projetil.desativar();
    }
}

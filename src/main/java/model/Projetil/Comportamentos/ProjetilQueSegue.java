package model.Projetil.Comportamentos;

import model.Batalha.EntidadeBatalha;
import model.Player.PlayerProva;
import model.Projetil.*;
import model.util.*;

public class ProjetilQueSegue implements ComportamentoProjetil, ComportamentoAoColidirComPlayer {

    private final float velocidadeMaxima;
    private final float forcaInercia;
    private final EntidadeBatalha alvoCustomizado;

    public ProjetilQueSegue(float velocidadeMaxima, float forcaInercia) {
        this(velocidadeMaxima, forcaInercia, null);
    }

    public ProjetilQueSegue(float velocidadeMaxima, float forcaInercia, EntidadeBatalha alvoCustomizado) {
        this.velocidadeMaxima = velocidadeMaxima;
        this.forcaInercia = forcaInercia;
        this.alvoCustomizado = alvoCustomizado;
    }

    @Override
    public void executar(Projetil projetil, float dt) {
        EntidadeBatalha alvo = (alvoCustomizado != null) ? alvoCustomizado : projetil.getTarget();
        if (alvo != null) {
            // calcula a direcão para o alvo
            Vector2D direcaoDesejada = alvo.getCentro().subtrair(projetil.getCentro());
            
            // Normalize safety check
            if (direcaoDesejada.getX() != 0 || direcaoDesejada.getY() != 0) {
                direcaoDesejada = direcaoDesejada.normalize();
            }

            // define a velocidade máxima que ele pode atingir
            Vector2D velocidadeDesejada = direcaoDesejada.mult(velocidadeMaxima);

            // define a força com que ele vai seguir o target
            // calcula a aceleracao (VelocidadeDesejada - VelocidadeAtual) * força
            Vector2D aceleracao = velocidadeDesejada.subtrair(projetil.getVelocidade()).mult(forcaInercia * dt);

            // aplica a aceleração na getVelocidade() atual
            projetil.setVelocidade(projetil.getVelocidade().add(aceleracao));

            // rotaciona visualmente e fisicamente a hitbox para apontar na direção do
            // movimento
            // (+90 graus / PI/2 radianos para compensar que os sprites apontam para cima
            // por padrão)
            float anguloAtual = (float) Math.atan2(projetil.getVelocidade().getY(), projetil.getVelocidade().getX())
                    + (float) (Math.PI / 2);
            projetil.getHitbox().setAnguloRad(anguloAtual);
        }
        // boom temos inercia

    }

    @Override
    public void aoColidirComPlayer(Projetil projetil, PlayerProva target) {
        projetil.aoColidirComPlayer(); // aplica o dano no player (ReceberDano)
        projetil.desativar();
    }
}
package model.Ataque.Ataques.Prova.Matematica;

import model.Ataque.Ataque;
import model.Batalha.EntidadeBatalha;
import model.Player.PlayerProva;
import model.util.MathUtils;
import model.util.Vector2D;

import java.util.Vector;

public class AtaqueVetores extends Ataque {

    private float timer = 0;
    private int projeteisLancados = 0;

    // Configurações da Dispersão (Spread)
    private int quantidadeProjeteisDispersao = 5;
    private float anguloDispersaoTotal = (float) Math.toRadians(50);
    private float posicaoXOriginal = 0;
    private float posicaoYOriginal = 0;
    private boolean origemSalva = false;

    public AtaqueVetores(PlayerProva target, EntidadeBatalha owner, float dificuldade) {

        super(target, owner, dificuldade, 200);
    }

    @Override
    protected void logicaAtaque(float dt) {

        timer += dt;
        float velocidade = 42.5f * (dificuldade / 2 + 0.5f);

        int limiteProjeteis = 9 + (int) (dificuldade / 10);

        if (timer >= (10 /(dificuldade/2)) && !this.isFinalizado() && projeteisLancados < limiteProjeteis) {
            //salva a posicao original da prova pra mandar ela pra la dps q o ataqueAcaba
            if (!origemSalva && owner != null) {
                posicaoXOriginal = owner.getX();
                posicaoYOriginal = owner.getY();
                origemSalva = true;
            }

            float distanciaMinima = 200f;
            //intervalo de disntacica X no qual a prova pode se teleportar
            float distanciaMaxima = 450f;

            float distanciaDir = MathUtils.randomFloatInRange(distanciaMinima, distanciaMaxima);
            float distanciaEsq = MathUtils.randomFloatInRange(distanciaMinima, distanciaMaxima);
            float posXDir = this.getMaxX() + distanciaDir;
            float posXEsq = this.getMinX() - distanciaEsq;
            float posX = MathUtils.randomBoolean() ? posXDir : posXEsq;

            float posY = MathUtils.randomFloatInRange(this.getMinY() * 0.8f, this.getMaxY() * 1.1f);

            owner.setX(posX);
            owner.setY(posY);
            int framesNoFuturo = (int) (3 + (dificuldade/10) * 2); // a prova preve onde vc estará e atira nessa direcao
            float anguloParaPlayer = (float) Math.atan2(
                    (target.getHitbox().getCentro().getY() + target.getVelocidade().getY() * dt * framesNoFuturo) - owner.getHitbox().getCentro().getY(),
                    (target.getHitbox().getCentro().getX() + target.getVelocidade().getX() * dt * framesNoFuturo) - owner.getHitbox().getCentro().getX()
            );

            float anguloInicial = anguloParaPlayer - (anguloDispersaoTotal / 2f);//pega o angulo inicial pra atirar em voce
            float passo = quantidadeProjeteisDispersao > 1 ? anguloDispersaoTotal / (quantidadeProjeteisDispersao - 1) : 0f; //calcula o angulo entre cada projetil


            for (int i = 0; i < quantidadeProjeteisDispersao; i++) {
                float anguloAtual = quantidadeProjeteisDispersao == 1 ? anguloParaPlayer : anguloInicial + (i * passo);

                spawnProjetil(owner.getHitbox().getCentro().getX(),
                        owner.getHitbox().getCentro().getY(),
                        32,
                        15,
                        velocidade,
                        anguloAtual,
                        anguloAtual,
                        1,
                        1f,
                        4.5f,
                        "vetor.png"); // Changed to vetor.png
            }

            projeteisLancados++;
            timer = 0;
        }

        // lanca 5 + (tiros adicionais baseado na dificuldade) e
        if (projeteisLancados >= limiteProjeteis) {
            if (timer >= 1f) {
                this.encerrarAtaque();
            }
        }
    }

    @Override
    public void reiniciarAtaque() {
        super.reiniciarAtaque();
        this.timer = 0;
        this.projeteisLancados = 0;
        this.owner.setX(posicaoXOriginal);
        this.owner.setY(posicaoYOriginal);
    }


}

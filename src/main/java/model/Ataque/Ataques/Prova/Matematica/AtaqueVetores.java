package model.Ataque.Ataques.Prova.Matematica;

import model.Ataque.Ataque;
import model.Batalha.EntidadeBatalha;
import model.Player.PlayerProva;
import model.util.MathUtils;
import model.util.Vector2D;

import java.util.Vector;

public class AtaqueVetores extends Ataque {

    private float timer = 0;
    private int projeteisSpawnados = 0;

    // Configurações do Spread
    private int quantidadeProjeteisSpread = 5;
    private float anguloSpreadTotal = (float) Math.toRadians(50);
    private float originalPosX = 0;
    private float originalPosY = 0;
    private boolean originSaved = false;

    public AtaqueVetores(PlayerProva target, EntidadeBatalha owner, float dificuldade) {

        super(target, owner, dificuldade, 200);
    }

    @Override
    protected void logicaAtaque(float dt) {

        timer += dt;
        float velocidade = 42.5f * (dificuldade / 2 + 0.5f);

        int limiteProjeteis = 9 + (int) (dificuldade / 10);

        if (timer >= (10 / dificuldade) && !this.isFinalizado() && projeteisSpawnados < limiteProjeteis) {
            
            if (!originSaved && owner != null) {
                originalPosX = owner.getX();
                originalPosY = owner.getY();
                originSaved = true;
            }

            float maxDistance = ((this.getMaxX() - this.getMinX()) + 200f) / 3f;

            float distanceDir = MathUtils.randomFloatInRange(0, maxDistance);
            float distanceEsq = MathUtils.randomFloatInRange(0, maxDistance);
            float posXDir = this.getMaxX() + distanceDir;
            float posXEsq = this.getMinX() - distanceEsq;
            float posX = MathUtils.randomBoolean() ? posXDir : posXEsq;

            float posY = MathUtils.randomFloatInRange(this.getMinY()* 0.9f, this.getMaxX() * 1.1f);

            owner.setX(posX);
            owner.setY(posY);
            int framesNoFuturo = (int) (3 + (dificuldade/10) * 2);
            float anguloParaPlayer = (float) Math.atan2(
                    (target.getHitbox().getCentro().getY() + target.getVelocidade().getY() * dt * framesNoFuturo) - owner.getHitbox().getCentro().getY(),
                    (target.getHitbox().getCentro().getX() + target.getVelocidade().getX() * dt * framesNoFuturo) - owner.getHitbox().getCentro().getX()
            );

            float anguloInicial = anguloParaPlayer - (anguloSpreadTotal / 2f);
            float step = quantidadeProjeteisSpread > 1 ? anguloSpreadTotal / (quantidadeProjeteisSpread - 1) : 0f;

            for (int i = 0; i < quantidadeProjeteisSpread; i++) {
                float anguloAtual = quantidadeProjeteisSpread == 1 ? anguloParaPlayer : anguloInicial + (i * step);

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

            projeteisSpawnados++;
            timer = 0;
        }

        // lanca 5 + (tiros adicionais baseado na dificuldade) e
        if (projeteisSpawnados >= limiteProjeteis) {
            if (timer >= 1f) {
                this.encerrarAtaque();
            }
        }
    }

    @Override
    public void reiniciarAtaque() {
        super.reiniciarAtaque();
        this.timer = 0;
        this.projeteisSpawnados = 0;
        this.owner.setX(originalPosX);
        this.owner.setY(originalPosY);
    }

    @Override
    public String toString() {
        return "Ataque Vetores";
    }
}

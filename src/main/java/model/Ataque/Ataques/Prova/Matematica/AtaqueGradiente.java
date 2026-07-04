package model.Ataque.Ataques.Prova.Matematica;

import model.Ataque.Ataque;
import model.Batalha.EntidadeBatalha;
import model.Player.PlayerProva;

public class AtaqueGradiente extends Ataque {

    private float timer = 0;
    private int projeteisSpawnados = 0;
    private float anguloRotacao = 0;

    public AtaqueGradiente(PlayerProva target, EntidadeBatalha owner, float dificuldade) {
        super(target, owner, dificuldade, 200);
    }

    @Override
    protected void logicaAtaque(float dt) {
        timer += dt;
        anguloRotacao += dt * 1.7f;

        float velocidade = 25 * (dificuldade / 2 + 0.5f);
        int limiteWaves = 13 + (int) (dificuldade / 10);

        if (timer >= (9 / dificuldade) && !this.isFinalizado() && projeteisSpawnados < limiteWaves) {
            
            float centerX = (this.getMinX() + this.getMaxX()) / 2f;
            float centerY = (this.getMinY() + this.getMaxY()) / 2f;
            
            float boxWidth = this.getMaxX() - this.getMinX();
            float boxHeight = this.getMaxY() - this.getMinY();
            float radius = (float) Math.hypot(boxWidth, boxHeight) / 2f + 40f;

            for (int i = 0; i < 10; i++) {
                float angleOff = anguloRotacao + (i * (float) Math.PI / 4f);
                
                float spawnX = centerX + (float) Math.cos(angleOff) * radius;
                float spawnY = centerY + (float) Math.sin(angleOff) * radius;

                float anguloParaCentro = (float) Math.atan2(
                        centerY - spawnY,
                        centerX - spawnX
                );

                spawnProjetil(spawnX,
                        spawnY,
                        32,
                        15,
                        velocidade,
                        anguloParaCentro,
                        anguloParaCentro,
                        1,
                        1f,
                        3f,
                        "vetor.png");
            }

            projeteisSpawnados++;
            timer = 0;
        }

        if (projeteisSpawnados >= limiteWaves) {
            if (timer >= 2f) {
                this.encerrarAtaque();
            }
        }
    }

    @Override
    public void reiniciarAtaque() {
        super.reiniciarAtaque();
        this.timer = 0;
        this.projeteisSpawnados = 0;
        this.anguloRotacao = 0;
    }


}

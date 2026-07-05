package model.Ataque.Ataques.Prova.Matematica;

import model.Ataque.Ataque;
import model.Batalha.EntidadeBatalha;
import model.Player.PlayerProva;

public class AtaqueGradiente extends Ataque {

    private float timer = 0;
    private int ondasLancadas = 0;
    private float anguloRotacao = 0;

    public AtaqueGradiente(PlayerProva target, EntidadeBatalha owner, float dificuldade) {
        super(target, owner, dificuldade, 200);
    }

    @Override
    protected void logicaAtaque(float dt) {
        timer += dt;
        anguloRotacao += dt * 3.67f;

        float velocidade = 27.5f * (dificuldade / 2 + 0.5f);
        int limiteOndas = 13 + (int) (dificuldade / 10);
        float centroX = (this.getMinX() + this.getMaxX()) / 2f;
        float centroY = (this.getMinY() + this.getMaxY()) / 2f;

        float larguraCaixa = this.getMaxX() - this.getMinX();
        float alturaCaixa = this.getMaxY() - this.getMinY();
        float raio = (float) Math.hypot(larguraCaixa, alturaCaixa) / 2f + 40f; //pega um raio q engloba toda area do ataque
        if (timer >= (9 / dificuldade) && !this.isFinalizado() && ondasLancadas < limiteOndas) {
            //spawna os 10 vetores em direcao ao centro do ataquqe
            for (int i = 0; i < 10; i++) {
                float anguloDesvio = anguloRotacao + (i * (float) Math.PI / 4f); //spawna cada tiro a esse angulo entre cada
                float origemX = centroX + (float) Math.cos(anguloDesvio) * raio;
                float origemY = centroY + (float) Math.sin(anguloDesvio) * raio;

                float anguloParaCentro = (float) Math.atan2(
                        centroY - origemY,
                        centroX - origemX
                );

                spawnProjetil(origemX,
                        origemY,
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

            ondasLancadas++;
            timer = 0;
        }

        if (ondasLancadas >= limiteOndas) {
            if (timer >= 2f) {
                this.encerrarAtaque();
            }
        }
    }

    @Override
    public void reiniciarAtaque() {
        super.reiniciarAtaque();
        this.timer = 0;
        this.ondasLancadas = 0;
        this.anguloRotacao = 0;
    }


}

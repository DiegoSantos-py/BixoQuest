package model.Projetil.Comportamentos;

import model.Projetil.ComportamentoAoDespawnar;
import model.Projetil.Projetil;
import model.Projetil.ProjetilFactory;

public class ProjetilExplodeAoSerAtingido implements ComportamentoAoDespawnar {

    private int numProjeteis;
    private String sprite;
    private float sizeX;
    private float sizeY;
    private float velocidade;
    private int danoShield;
    private float danoNota;
    private float duracaoMaximaExplosao;

    public ProjetilExplodeAoSerAtingido(int numProjeteis, String sprite, float sizeX, float sizeY, float velocidade, int danoShield, float danoNota, float duracaoMaximaExplosao) {
        this.numProjeteis = numProjeteis;
        this.sprite = sprite;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.velocidade = velocidade;
        this.danoShield = danoShield;
        this.danoNota = danoNota;
        this.duracaoMaximaExplosao = duracaoMaximaExplosao;
    }

    @Override
    public void aoDespawnar(Projetil projetil, ProjetilFactory factory) {
        // Explode apenas se for destruído ANTES de atingir sua duracao maxima
        // (Ou seja, ao ser atingido pelo tiro do player ou outra interação que chame desativar())
        if (projetil.getTempoDeVida() < projetil.getDuracaoMaxima()) {
            for (int j = 0; j < numProjeteis; j++) {
                float angle = (float) (j * (Math.PI * 2f / numProjeteis));
                factory.spawn(
                    projetil.getX(), projetil.getY(),
                    sizeX, sizeY,
                    velocidade, angle, angle,
                    danoShield, danoNota, duracaoMaximaExplosao,
                    sprite
                );
            }
        }
    }
}

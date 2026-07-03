package model.Projetil.Comportamentos;

import model.Projetil.*;
import model.util.*;

public class ProjetilExplosivo implements ComportamentoProjetil, ComportamentoAoDespawnar {

    private final String sprite;
    private final int numProjeteis;
    private final float tamanhoX;
    private final float tamanhoY;
    private final float velocidade;
    private final int danoShield;
    private final float danoNota;
    private final float duracao;



    public ProjetilExplosivo(String sprite, int numProjeteis,
            float tamanhoX, float tamanhoY, float velocidade,
            int danoShield, float danoNota, float duracao) {
        this.sprite = sprite;
        this.numProjeteis = numProjeteis;
        this.tamanhoX = tamanhoX;
        this.tamanhoY = tamanhoY;
        this.velocidade = velocidade;
        this.danoShield = danoShield;
        this.danoNota = danoNota;
        this.duracao = duracao;
    }

    @Override
    public void aoDespawnar(Projetil projetil, ProjetilFactory factory) {
        if (projetil.getFactory() == null) return;

        float cx = projetil.getHitbox().getCentro().getX();
        float cy = projetil.getHitbox().getCentro().getY();

        for (int i = 0; i < numProjeteis; i++) {
            float angle = (float)(i * (Math.PI * 2) / numProjeteis) + projetil.getHitbox().getAnguloRotacao();
            projetil.getFactory().spawn(cx, cy, tamanhoX, tamanhoY, velocidade, angle, angle,
                    danoShield, danoNota, duracao, sprite);
        }
    }

    @Override
    public void executar(Projetil projetil, float dt) {
    }
}
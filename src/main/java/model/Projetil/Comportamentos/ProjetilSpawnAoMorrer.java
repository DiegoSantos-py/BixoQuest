package model.Projetil.Comportamentos;

import model.Projetil.ComportamentoAoDespawnar;
import model.Projetil.Projetil;
import model.Projetil.ProjetilFactory;

public class ProjetilSpawnAoMorrer implements ComportamentoAoDespawnar {

    private String spriteDir;
    private float tamanhoX;
    private float tamanhoY;
    private int danoShield;
    private float danoNota;
    private float duracao;
    private float spriteMult;

    public ProjetilSpawnAoMorrer(String spriteDir, float tamanhoX, float tamanhoY, int danoShield, float danoNota, float duracao, float spriteMult) {
        this.spriteDir = spriteDir;
        this.tamanhoX = tamanhoX;
        this.tamanhoY = tamanhoY;
        this.danoShield = danoShield;
        this.danoNota = danoNota;
        this.duracao = duracao;
        this.spriteMult = spriteMult;
    }

    @Override
    public void aoDespawnar(Projetil projetil, ProjetilFactory factory) {
        if (factory == null) return;
        
        float cx = projetil.getHitbox().getCentro().getX();
        float cy = projetil.getHitbox().getCentro().getY();
        float angulo = projetil.getHitbox().getAnguloRotacao();
        
        // Spawn o projétil real exatamente na mesma posição e rotação, parado
        Projetil p = factory.spawn(cx, cy, tamanhoX, tamanhoY, 0f, angulo, angulo, danoShield, danoNota, duracao, spriteDir);
        p.setMultiplicadorSprite(spriteMult);

    }
}

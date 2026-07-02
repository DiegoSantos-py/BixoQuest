package model.Projetil.Comportamentos;

import model.Projetil.ComportamentoAoDespawnar;
import model.Projetil.Projetil;
import model.Projetil.ProjetilFactory;
import model.util.MathUtils;

public class ProjetilExplosaoNuclear implements ComportamentoAoDespawnar {

    private String spriteDir;
    private float tamanhoX;
    private float tamanhoY;
    private int danoShield;
    private float danoNota;
    private float duracao;
    private int numProjeteis;

    public ProjetilExplosaoNuclear(String spriteDir, float tamanhoX, float tamanhoY, int danoShield, float danoNota, float duracao, int numProjeteis) {
        this.spriteDir = spriteDir;
        this.tamanhoX = tamanhoX;
        this.tamanhoY = tamanhoY;
        this.danoShield = danoShield;
        this.danoNota = danoNota;
        this.duracao = duracao;
        this.numProjeteis = numProjeteis;
    }

    @Override
    public void aoDespawnar(Projetil projetil, ProjetilFactory factory) {
        if (factory == null) return;
        
        float cx = projetil.getHitbox().getCentro().getX();
        float cy = projetil.getHitbox().getCentro().getY();
        
        float step = (float) (Math.PI * 2) / numProjeteis;

        for (int i = 0; i < numProjeteis; i++) {
            float angulo = i * step;
            float velocidadeAleatoria = 70f + MathUtils.randomFloatInRange(0, 300f);
            factory.spawn(
                cx, cy, 
                tamanhoX, tamanhoY, 
                velocidadeAleatoria, 
                angulo, 
                angulo, 
                danoShield, danoNota, duracao, spriteDir
            );
        }
    }
}

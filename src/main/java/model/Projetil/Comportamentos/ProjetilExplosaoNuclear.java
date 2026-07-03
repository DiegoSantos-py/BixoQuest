package model.Projetil.Comportamentos;

import model.Projetil.ComportamentoAoDespawnar;
import model.Projetil.ComportamentoProjetil;
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
    private final ComportamentoProjetil comportamentoGravidade;
    private final ComportamentoProjetil comportamentoAponta;

    public ProjetilExplosaoNuclear(String spriteDir, float tamanhoX, float tamanhoY,
            int danoShield, float danoNota, float duracao, int numProjeteis,
            ComportamentoProjetil comportamentoGravidade, ComportamentoProjetil comportamentoAponta) {
        this.spriteDir = spriteDir;
        this.tamanhoX = tamanhoX;
        this.tamanhoY = tamanhoY;
        this.danoShield = danoShield;
        this.danoNota = danoNota;
        this.duracao = duracao;
        this.numProjeteis = numProjeteis;
        this.comportamentoGravidade = comportamentoGravidade;
        this.comportamentoAponta = comportamentoAponta;
    }

    @Override
    public void aoDespawnar(Projetil projetil, ProjetilFactory factory) {
        if (factory == null) return;
        
        float cx = projetil.getHitbox().getCentro().getX();
        float cy = projetil.getHitbox().getCentro().getY();
        
        // Queremos atirar apenas para cima (de 180 graus até 360/0 graus)
        // No JavaFX, Y cresce para baixo. Então 180 (PI) é esquerda, 270 (1.5 PI) é cima, 360 (2 PI) é direita.
        float startAngle = (float) Math.PI;
        float endAngle = (float) (Math.PI * 2);
        float step = (endAngle - startAngle) / (numProjeteis - 1);

        for (int i = 0; i < numProjeteis; i++) {
            float angulo = startAngle + (i * step);
            float velocidadeAleatoria = 100f + MathUtils.randomFloatInRange(0, 200f);
            
            Projetil fogo = factory.spawn(
                cx, cy, 
                tamanhoX, tamanhoY, 
                velocidadeAleatoria, 
                angulo, 
                angulo, 
                danoShield, danoNota, duracao, spriteDir
            );
            
            if (fogo != null) {
                fogo.addComportamento(comportamentoGravidade);
                fogo.addComportamento(comportamentoAponta);
            }
        }
    }
}

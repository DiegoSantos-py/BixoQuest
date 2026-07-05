package model.Projetil.Comportamentos;

import model.Projetil.ComportamentoProjetil;
import model.Projetil.Projetil;

public class ProjetilAtivaDanoAposTempo implements ComportamentoProjetil {
    
    private float tempoAtraso;
    private int danoShield;
    private float danoNota;

    public ProjetilAtivaDanoAposTempo(float tempoAtraso, int danoShield, float danoNota) {
        this.tempoAtraso = tempoAtraso;
        this.danoShield = danoShield;
        this.danoNota = danoNota;
    }

    @Override
    public void executar(Projetil projetil, float dt) {
        if (projetil.getTempoDeVida() >= tempoAtraso) {
            projetil.setDanoShield(danoShield);
            projetil.setDanoNota(danoNota);
        }
    }
}

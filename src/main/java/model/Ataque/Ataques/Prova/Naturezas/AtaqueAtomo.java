package model.Ataque.Ataques.Prova.Naturezas;

import model.Ataque.Ataque;
import model.Batalha.EntidadeBatalha;
import model.Player.PlayerProva;
import model.Projetil.Projetil;
import model.Projetil.Comportamentos.ProjetilEmissorBulletHell;
import model.Projetil.Comportamentos.ProjetilAceleracao;
import model.Projetil.Comportamentos.ProjetilAtivaDanoAposTempo;

public class AtaqueAtomo extends Ataque {

    private boolean iniciado = false;
    private Projetil atomo;
    private float tempoMaximoOnda = 7.5f;

    private ProjetilAceleracao compAceleracao = new ProjetilAceleracao(-600f);
    private ProjetilAtivaDanoAposTempo compDano = new ProjetilAtivaDanoAposTempo(1.5f, 1, 1f);
    private ProjetilEmissorBulletHell compEmissor;

    public AtaqueAtomo(PlayerProva target, EntidadeBatalha owner, float dificuldade) {
        super(target, owner, dificuldade, 150);
        this.compEmissor = new ProjetilEmissorBulletHell(
            "eletron.png",
            0.25f - (dificuldade / 100f),
            4,
            0.367f,
            225f + (dificuldade * 10),
            15f,
            4f,
            0f,
            1.5f
        );
    }

    @Override
    protected void logicaAtaque(float dt) {
        if (!iniciado) {
            float inicioX = owner.getX();
            float inicioY = owner.getY() - 50f;
            
            atomo = spawnProjetil(
                    inicioX, inicioY, 
                    45, 45,
                    900f,
                    (float) Math.PI / 2f,
                    (float) -Math.PI / 2f, 
                    1, 0f, 
                    tempoMaximoOnda + 1.5f,
                    "atomo.png"
            );
            
            if (atomo != null) {
                atomo.setPersistente(true);
                atomo.setDanoShield(0);
                atomo.setDanoNota(0f);
                
                atomo.addComportamento(compAceleracao);
                atomo.addComportamento(compDano);
                atomo.addComportamento(compEmissor);
            }
            iniciado = true;
        } 
        
        if (tempoDecorrido >= tempoMaximoOnda + 3.0f) {
            encerrarAtaque();
        }
    }

    @Override
    public void reiniciarAtaque() {
        super.reiniciarAtaque();
        this.iniciado = false;
        if (this.atomo != null) {
            this.atomo.desativar();
            this.atomo = null;
        }
    }
}

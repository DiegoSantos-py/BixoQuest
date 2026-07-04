package model.Ataque.Ataques.Prova.Naturezas;

import model.Ataque.Ataque;
import model.Batalha.EntidadeBatalha;
import model.Player.PlayerProva;
import model.Projetil.Projetil;
import model.Projetil.Comportamentos.ProjetilEmissorBulletHell;
import model.Projetil.Comportamentos.ProjetilAceleracao;
import model.Projetil.Comportamentos.ProjetilAtivaDanoAposTempo;

public class AtaqueAtomo extends Ataque {

    private boolean started = false;
    private Projetil atomo;
    private float burstTimeMax = 7.5f;

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
        if (!started) {
            float startX = owner.getX();
            float startY = owner.getY() - 50f;
            
            atomo = spawnProjetil(
                    startX, startY, 
                    45, 45,
                    900f,
                    (float) Math.PI / 2f,
                    (float) -Math.PI / 2f, 
                    1, 0f, 
                    burstTimeMax + 1.5f,
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
            started = true;
        } 
        
        if (tempoDecorrido >= burstTimeMax + 3.0f) {
            encerrarAtaque();
        }
    }

    @Override
    public void reiniciarAtaque() {
        super.reiniciarAtaque();
        this.started = false;
        if (this.atomo != null) {
            this.atomo.desativar();
            this.atomo = null;
        }
    }
}

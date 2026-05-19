package model.Ataque;

import java.util.List;

import model.EntidadeBatalha;
import model.Player.PlayerProva;
import model.Projetil.Projetil;
import model.Projetil.ProjetilFactory;
import model.Projetil.ProjetilID;

public abstract class Ataque {
    protected float tempoDecorrido = 0;
    protected boolean finalizado = false;
    protected float dificuldade;
    protected ProjetilFactory factory;
    protected EntidadeBatalha owner;
    protected PlayerProva target;



    public Ataque(PlayerProva target, EntidadeBatalha owner,float dificuldade, int maxBasico, int maxHoming, int maxExplosivo) {
        this.target = target;
        this.owner = owner;
        this.dificuldade = dificuldade;
        this.factory = new ProjetilFactory(target, owner, maxBasico, maxHoming, maxExplosivo);
    }

    public final void atualizar(float dt) {
        if (finalizado) return;
        
        tempoDecorrido += dt;
        logicaAtaque(dt);
        factory.atualizar(dt);  
    }

    protected void spawnProjetil(float posX, float posY, float tamanhoX, float tamanhoY, float velocidade, 
                                 float anguloSpawn, float anguloHitbox, ProjetilID id, 
                                 int danoShield, float danoNota, float duracaoMaxima) {
        
        factory.spawn(posX, posY, tamanhoX, tamanhoY, velocidade, anguloSpawn, anguloHitbox, id, 
                      danoShield, danoNota, duracaoMaxima);
    }

    protected abstract void logicaAtaque(float dt);

    public void encerrarAtaque() {
        this.finalizado = true;
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    public void setTarget(PlayerProva target) {
        this.target = target;
        this.factory.setTarget(target);
    }

    public void reiniciarAtaque() {
        this.finalizado = false;
        this.tempoDecorrido = 0;
        //restarta o ataque e desliga todos os projeteis da pool de projeteis(pra eles n ficarem no ar parados)
        for (Projetil p : factory.getAtivos()) {
            p.desativar();
        }
    }

    public List<Projetil> getProjeteis() { 
        return factory.getAtivos(); 
    }
}

package model.Projetil;

import model.util.Hitbox;
import model.util.Vector2D;
import model.EntidadeBatalha;
import model.Player.*;
public abstract class Projetil extends EntidadeBatalha {

    protected int danoShield; // protected para os filhos acessarem se precisarem
    protected float danoNota;
    protected int tempoDeVida = 0; // contador de frames vivos
    protected int duracaoMaxima;// em frames
    protected boolean ativo = true; // isso aq é pra fazer a pre-alocacao do projetil, pra n atiçar o garbage collector


    public Projetil(Hitbox hitbox, Vector2D velocidade, int danoShield, float danoNota, int duracaoMaxima) {
        super(hitbox, velocidade);
        this.danoShield = danoShield;
        this.danoNota = danoNota;
        this.duracaoMaxima =  duracaoMaxima;
    }

    public abstract void executarAI(float deltaTime);

    public void aoColidirComPlayer() {
        PlayerProva.getInstancia().ReceberDano(this.danoShield, this.danoNota);
    }

    public void aoDespawnar() {
        
    }

    public final void atualizar(float deltaTime) {
        if (!isAtivo()) return;
        
        if (tempoDeVida >= duracaoMaxima) {
            this.ativo = false; 
            aoDespawnar();  
            return;
        }


        this.tempoDeVida += deltaTime;
        executarAI(deltaTime);          
        this.atualizarPosicao(deltaTime); 
        if(this.hitbox.checarColisao(PlayerProva.getInstancia().getHitbox())) {
            aoColidirComPlayer();
        }
    }

    public int getDanoShield() { return danoShield; }
    public float getDanoNota() { return danoNota; }
}
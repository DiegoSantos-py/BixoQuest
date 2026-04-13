package model.Projetil;

import model.util.Hitbox;
import model.util.Vector2D;
import model.EntidadeBatalha;
import model.Player.*;
public abstract class Projetil extends EntidadeBatalha {

    protected int danoShield; // protected para os filhos acessarem se precisarem
    protected float danoNota;
    protected float tempoDeVida = 0; //contador de tempo vivo em ms
    protected float duracaoMaxima; //duracao maxima em ms
    // ATENÇÃO: NÃO re-declarar campo 'ativo' aqui! Ele pertence ao pai EntidadeBatalha.
    // Re-declarar causaria field shadowing: isAtivo() leria o campo do pai (true) ignorando este.
    
    protected EntidadeBatalha owner;
    protected PlayerProva target;
    protected ProjetilFactory factory;

    public Projetil(Hitbox hitbox, Vector2D velocidade, int danoShield, float danoNota, float duracaoMaxima) {
        super(hitbox, velocidade);
        this.danoShield = danoShield;
        this.danoNota = danoNota;
        this.duracaoMaxima = duracaoMaxima;
        desativar(); // garante que projéteis pré-alocados começam inativos na Object Pool
    }

    public void setFactory(ProjetilFactory factory) {
        this.factory = factory;
    }

    public void setOwner(EntidadeBatalha owner) {
        this.owner = owner;
    }
    
    public void setTarget(PlayerProva target) {
        this.target = target;
    }

    public void reviver(float posX, float posY, float sizeX, float sizeY, float velX, float velY, float anguloHitbox, int danoShield, float danoNota, float duracaoMaxima) {
        this.hitbox.setCentro(posX, posY);
        this.hitbox.setTamanho(sizeX, sizeY);
        this.hitbox.setAnguloRad(anguloHitbox);
        this.velocidade.set(velX, velY);
        
        this.danoShield = danoShield;
        this.danoNota = danoNota;
        this.duracaoMaxima = duracaoMaxima;
        this.tempoDeVida = 0;
        this.ativo = true;
    }

    public abstract void executarAI(float deltaTime);

    public void aoColidirComPlayer() {
        if (this.target != null) {
            this.target.ReceberDano(this.danoShield, this.danoNota);
        }
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
        if(this.target != null && this.hitbox.checarColisao(this.target.getHitbox())) {
            aoColidirComPlayer();
        }
    }

    public int getDanoShield() { return danoShield; }
    public float getDanoNota() { return danoNota; }
}
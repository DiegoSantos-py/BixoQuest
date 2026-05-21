package model.Batalha;

import model.util.Hitbox;
import model.util.Vector2D;

public abstract class EntidadeBatalha {
    
    // Atributos protected para que as classes filhas (PlayerProva, Projetil) tenham acesso
    protected Hitbox hitbox;
    protected Vector2D velocidade;
    protected float velocidadeAngular; // Rotação em radianos por segundo
    protected boolean ativo;

    public EntidadeBatalha(Hitbox hitbox, Vector2D velocidade) {
        this.hitbox = hitbox;
        this.velocidade = velocidade;
        this.velocidadeAngular = 0.0f; 
        this.ativo = true;
    }

    // o main de fisica que vai ser rodado a cada frame do jogo
    public void atualizarPosicao(float deltaTime) {
        if (!ativo) {
            return;
        }

        Vector2D deslocamento = new Vector2D(
            velocidade.getX() * deltaTime, 
            velocidade.getY() * deltaTime
        );

        this.hitbox.atualizarPos(deslocamento);

        if (velocidadeAngular != 0.0f) {
            this.hitbox.rotacionar(velocidadeAngular * deltaTime);
        }
    }


    public Hitbox getHitbox() {
        return hitbox;
    }
    public float getX(){
        return this.hitbox.getCentro().getX();
    }
    public float getY(){
        return this.hitbox.getCentro().getY();
    }
    public Vector2D getCentro() {
        return hitbox.getCentro();
    }

    public Vector2D getVelocidade() {
        return velocidade;
    }

    public void setVelocidade(Vector2D velocidade) {
        this.velocidade = velocidade;
    }

    public float getVelocidadeAngular() {
        return velocidadeAngular;
    }

    public void setVelocidadeAngular(float velocidadeAngular) {
        this.velocidadeAngular = velocidadeAngular;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void ativar(){
        this.ativo = true;
        this.hitbox.ativar();
    }
    public void desativar() {
        this.ativo = false;
        this.hitbox.desativar();
    }
}
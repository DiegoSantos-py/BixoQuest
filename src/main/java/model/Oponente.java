package model;

import model.Ataque.Ataque;
import model.Player.PlayerProva;
import model.util.Hitbox;
import model.util.Vector2D;

public abstract class Oponente extends EntidadeBatalha {
    protected String nome;
    protected float hpAtual;
    protected float hpMaximo;

    public Oponente(Hitbox hitbox, Vector2D velocidade, String nome, float hpMaximo) {
        super(hitbox, velocidade);
        this.nome = nome;
        this.hpMaximo = hpMaximo;
        this.hpAtual = hpMaximo;
    }

    public abstract Ataque criarAtaque(PlayerProva alvo, EntidadeBatalha owner);

    public void receberDano(float valor) {
        this.hpAtual -= valor;
        if (this.hpAtual < 0) {
            this.hpAtual = 0;
        }
    }

    public boolean isDerrotado() {
        return this.hpAtual <= 0;
    }

    public String getNome() {
        return nome;
    }

    public float getHpAtual() {
        return hpAtual;
    }

    public float getHpMaximo() {
        return hpMaximo;
    }
}

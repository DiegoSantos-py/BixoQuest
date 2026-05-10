package model;

import java.util.ArrayList;
import java.util.List;

import model.Ataque.Ataque;
import model.Disciplina.AreaConhecimento;
import model.Player.PlayerProva;
import model.util.Hitbox;
import model.util.Vector2D;

public class Oponente extends EntidadeBatalha {
    protected String nome;
    protected boolean isDerrotado = false;
    protected AreaConhecimento areaConhecimento;
    protected float hpMaximo;
    protected float hpAtual;
   
    protected List<Ataque> ataquesDisponiveis = new ArrayList<>();
    

    public Oponente(Hitbox hitbox, Vector2D velocidade, String nome, float hpMaximo, AreaConhecimento areaConhecimento) {
        super(hitbox, velocidade);
        this.nome = nome;
        this.areaConhecimento = areaConhecimento;
        this.hpMaximo = hpMaximo;
        this.hpAtual = hpMaximo;
    }


    public void receberDano(float valor) {
        this.hpAtual -= valor;
        if (this.hpAtual < 0) {
            this.hpAtual = 0;
            this.isDerrotado = true;
        }
    }

    public String getNome() {
        return nome;
    }

    public float getHpAtual() {
        return hpAtual;
    }

    public AreaConhecimento getAreaConhecimento() {
        return areaConhecimento;
    }
    public float getHpMaximo() {
        return hpMaximo;
    }

    public List<Ataque> getAtaquesDisponiveis() {
        return ataquesDisponiveis;
    }

    public void adicionarAtaque(Ataque ataque) {
        this.ataquesDisponiveis.add(ataque);
    }
}

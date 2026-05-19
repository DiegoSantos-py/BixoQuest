package model.Evento.Prova;

import model.Ataque.Ataque;
import model.Disciplina.AreaConhecimento;
import model.Disciplina.Disciplina;

public class Questao {
    private String nome;
    private String descricao;
    private AreaConhecimento areaConhecimento;
    private float dificuldade; //escala com nivel da prova
    private Ataque ataque;
    private float hp;

    public Questao(String nome, float hp, String descricao, AreaConhecimento areaConhecimento, float dificuldade, Ataque ataque) {
        this.nome = nome;
        this.hp = hp;
        this.descricao = descricao;
        this.areaConhecimento = areaConhecimento;
        this.dificuldade = dificuldade;
        this.ataque = ataque;

    }

    public float getHp(){
        return hp;
    }

    public void setHp(float hp){
        this.hp = hp;
    }
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public AreaConhecimento getAreaConhecimento() {
        return areaConhecimento;
    }

    public void setAreaConhecimento(AreaConhecimento areaConhecimento) {
        this.areaConhecimento = areaConhecimento;
    }

    public float getDificuldade() {
        return dificuldade;
    }

    public void setDificuldade(float dificuldade) {
        this.dificuldade = dificuldade;
    }

    public Ataque getAtaque() {
        return ataque;
    }

    public void setAtaque(Ataque ataque) {
        this.ataque = ataque;
    }
}



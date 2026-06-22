package model.Evento.Prova.Questao;

import model.Ataque.Ataque;
import model.Disciplina.AreaConhecimento;

public class Questao {
    private String nome;
    private String descricao;
    private String textoCaixa;
    private AreaConhecimento areaConhecimento;
    private float dificuldade; // escala com nivel da prova
    private Ataque ataque;
    private float hp;

    public Questao(String nome, float hp, AreaConhecimento areaConhecimento, float dificuldade, Ataque ataque,
            String descricao, String textoCaixa) {
        this.nome = nome;
        this.hp = hp;
        this.areaConhecimento = areaConhecimento;
        this.dificuldade = dificuldade;
        this.ataque = ataque;
        this.descricao = descricao;
        this.textoCaixa = textoCaixa;

    }

    public float getHp() {
        return hp;
    }

    public void setHp(float hp) {
        this.hp = hp;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public AreaConhecimento getAreaConhecimento() {
        return areaConhecimento;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getTextoCaixa() {
        return textoCaixa;
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

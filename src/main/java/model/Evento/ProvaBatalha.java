package model.Evento;

import model.Batalhavel;

public class ProvaBatalha extends Evento implements Batalhavel {
    
    private int nivelDisciplina;
    private int quantidadeQuestoes;

    public ProvaBatalha(String nome, String descricao, int nivelDisciplina, int quantidadeQuestoes) {
        super();
        this.setNome(nome);
        this.setDescricao(descricao);
        this.nivelDisciplina = nivelDisciplina;
        this.quantidadeQuestoes = quantidadeQuestoes;
    }

    @Override
    public String getNomeBatalha() {
        return getNome();
    }

    public int getNivelDisciplina() {
        return nivelDisciplina;
    }

    public void setNivelDisciplina(int nivelDisciplina) {
        this.nivelDisciplina = nivelDisciplina;
    }

    public int getQuantidadeQuestoes() {
        return quantidadeQuestoes;
    }

    public void setQuantidadeQuestoes(int quantidadeQuestoes) {
        this.quantidadeQuestoes = quantidadeQuestoes;
    }
}

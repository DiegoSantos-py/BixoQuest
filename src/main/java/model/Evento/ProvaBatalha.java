package model.Evento;

import model.Oponente;

public class ProvaBatalha extends Evento implements Batalhavel {
    
    private int nivelDisciplina;
    private int quantidadeQuestoes;
    private Oponente oponente;

    public ProvaBatalha(String nome, String descricao, int nivelDisciplina, int quantidadeQuestoes) {
        super();
        this.setNome(nome);
        this.setDescricao(descricao);
        this.nivelDisciplina = nivelDisciplina;
        this.quantidadeQuestoes = quantidadeQuestoes;
    }

    //TODO: @OVERRIDE AQUI N FUNCIONA
    //@Override
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
    //TODO: IMPLEMENTAR GET OPONENTE DIREITO
    public Oponente getOponente(){
        return this.oponente;
    }
}

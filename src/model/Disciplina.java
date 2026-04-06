package model;

public class Disciplina {
    private String nome;
    private int vezesFeita;
    private int conhecimento;
    private ProvaBatalha prova;

    public int getConhecimento() {
        return conhecimento;
    }

    public void setConhecimento(int conhecimento) {
        this.conhecimento = conhecimento;
    }

    public int getVezesFeita() {
        return vezesFeita;
    }

    public void setVezesFeita(int vezesFeita) {
        this.vezesFeita = vezesFeita;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ProvaBatalha getProva() {
        return prova;
    }

    public void setProva(ProvaBatalha prova) {
        this.prova = prova;
    }
}

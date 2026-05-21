package model.Evento.Prova;

import model.Personagem;

public class ResultadoProva {
    private Personagem personagem;
    private String provaNome;
    private float notaFinal;
    private int turnosUsados;
    private boolean todosOsAcertosPerfeitos;
    private boolean levouAlgumDano;
    private boolean perdeuNota;

    public ResultadoProva() {
    }

    public ResultadoProva(Personagem personagem, String provaNome, float notaFinal,
                          int turnosUsados, boolean todosOsAcertosPerfeitos,
                          boolean levouAlgumDano, boolean perdeuNota) {
        this.personagem = personagem;
        this.provaNome = provaNome;
        this.notaFinal = notaFinal;
        this.turnosUsados = turnosUsados;
        this.todosOsAcertosPerfeitos = todosOsAcertosPerfeitos;
        this.levouAlgumDano = levouAlgumDano;
        this.perdeuNota = perdeuNota;
    }

    public Personagem getPersonagem() { return personagem; }
    public String getProvaNome() { return provaNome; }
    public float getNotaFinal() { return notaFinal; }
    public int getTurnosUsados() { return turnosUsados; }
    public boolean isTodosOsAcertosPerfeitos () { return todosOsAcertosPerfeitos; }
    public boolean isLevouAlgumDano() { return levouAlgumDano; }
    public boolean isPerdeuNota() { return perdeuNota; }
}

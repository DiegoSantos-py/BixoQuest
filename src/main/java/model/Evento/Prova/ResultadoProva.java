package model.Evento.Prova;

import model.Personagem;

public class ResultadoProva {
    private Personagem personagem;
    private ProvaBatalha prova;
    private float notaFinal;
    private int turnosUsados;
    private boolean todosOsAcertosPerfeitos;
    private boolean levouAlgumDano;
    private boolean perdeuNota;

    public ResultadoProva(Personagem personagem, ProvaBatalha prova, float notaFinal, 
                          int turnosUsados, boolean todosOsAcertosPerfeitos,
                          boolean levouAlgumDano, boolean perdeuNota) {
        this.personagem = personagem;
        this.prova = prova;
        this.notaFinal = notaFinal;
        this.turnosUsados = turnosUsados;
        this.todosOsAcertosPerfeitos = todosOsAcertosPerfeitos;
        this.levouAlgumDano = levouAlgumDano;
        this.perdeuNota = perdeuNota;
    }

    public Personagem getPersonagem() { return personagem; }
    public ProvaBatalha getProva() { return prova; }
    public float getNotaFinal() { return notaFinal; }
    public int getTurnosUsados() { return turnosUsados; }
    public boolean isTodosOsAcertosPerfeitos () { return todosOsAcertosPerfeitos; }
    public boolean isLevouAlgumDano() { return levouAlgumDano; }
    public boolean isPerdeuNota() { return perdeuNota; }
}

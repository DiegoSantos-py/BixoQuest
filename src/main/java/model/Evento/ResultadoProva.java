package model.Evento;

import model.Personagem;

public class ResultadoProva {
    private Personagem personagem;
    private ProvaBatalha prova;
    private float notaFinal;
    private int turnosUsados;
    private int acertosPerfeitosConsecutivos;
    private int hitsRecebidos;
    private boolean levouAlgumDano;
    private boolean perdeuNota;

    public ResultadoProva(Personagem personagem, ProvaBatalha prova, float notaFinal, 
                          int turnosUsados, int acertosPerfeitosConsecutivos, 
                          int hitsRecebidos, boolean levouAlgumDano, boolean perdeuNota) {
        this.personagem = personagem;
        this.prova = prova;
        this.notaFinal = notaFinal;
        this.turnosUsados = turnosUsados;
        this.acertosPerfeitosConsecutivos = acertosPerfeitosConsecutivos;
        this.hitsRecebidos = hitsRecebidos;
        this.levouAlgumDano = levouAlgumDano;
        this.perdeuNota = perdeuNota;
    }

    public Personagem getPersonagem() { return personagem; }
    public ProvaBatalha getProva() { return prova; }
    public float getNotaFinal() { return notaFinal; }
    public int getTurnosUsados() { return turnosUsados; }
    public int getAcertosPerfeitosConsecutivos() { return acertosPerfeitosConsecutivos; }
    public int getHitsRecebidos() { return hitsRecebidos; }
    public boolean isLevouAlgumDano() { return levouAlgumDano; }
    public boolean isPerdeuNota() { return perdeuNota; }
}

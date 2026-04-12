package model.Evento;

import model.Disciplina.AreaConhecimento;
import model.Local.ZonaInterativa;

import java.util.Map;

public class Evento {
    private boolean status;
    private String nome;
    private String descricao;

    private double efeitoEnergia;
    private Map<AreaConhecimento, Double> efeitoConhecimento;
    private double efeitoMotivacao;
    private double efeitoSaude;
    private double efeitoDinheiro;
    private int efeitoTempo;

    private int tempoRequisito;
    private Evento eventoRequisito;
    private double energiaMinima;
    private double custaDinheiro;

    private boolean repetivel;
    private ZonaInterativa zona;

    public Evento() {}

    public Evento(String nome, String evento){
        this.nome = nome;
        this.descricao = descricao;
    }
    public Evento(double efeitoEnergia, Map<AreaConhecimento, Double> efeitoConhecimento, double efeitoMotivacao,
                  double efeitoSaude, double efeitoDinheiro, int efeitoTempo,
                  int tempoRequisito, Evento eventoRequisito, double energiaMinima){
        this.efeitoEnergia = efeitoEnergia;
        this.efeitoConhecimento = efeitoConhecimento;
        this.efeitoMotivacao = efeitoMotivacao;
        this.efeitoSaude = efeitoSaude;
        this.efeitoDinheiro = efeitoDinheiro;
        this.efeitoTempo = efeitoTempo;
        this.tempoRequisito = tempoRequisito;
        this.eventoRequisito = eventoRequisito;
        this.energiaMinima = energiaMinima;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean isTrue){
        this.status = isTrue;
    }

    public double getEfeitoEnergia() {
        return efeitoEnergia;
    }

    public Map<AreaConhecimento, Double> getEfeitosConhecimento() {
        return efeitoConhecimento;
    }

    public double getEfeitoMotivacao() {
        return efeitoMotivacao;
    }

    public double getEfeitoSaude() {
        return efeitoSaude;
    }

    public double getEfeitoDinheiro() {
        return efeitoDinheiro;
    }

    public int getEfeitoTempo() {
        return efeitoTempo;
    }

    public Evento getEventoRequisito() {
        return eventoRequisito;
    }

    public double getEnergiaMinima() {
        return energiaMinima;
    }

    public double getCustaDinheiro() {
        return custaDinheiro;
    }

    public void setCustaDinheiro(double custaDinheiro) {
        this.custaDinheiro = custaDinheiro;
    }

    public boolean isRepetivel() {
        return repetivel;
    }

    public void setRepetivel(boolean repetivel) {
        this.repetivel = repetivel;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setZona(ZonaInterativa zona) {
        this.zona = zona;
    }

    public ZonaInterativa getZona() {
        return zona;
    }

    @Override
    public String toString(){
        return this.nome + this.descricao;
    }
}


package model;

public abstract class Evento {
    private boolean status;
    private String nome;
    private String descricao;

    private double efeitoEnergia;
    private double efeitoConhecimento;
    private double efeitoMotivacao;
    private double efeitoSaude;
    private double efeitoDinheiro;
    private int efeitoTempo;

    private int tempoRequisito;
    private Evento eventoRequisito;
    private double energiaMinima;
    private double custaDinheiro;

    private boolean repetivel;
    private int cooldown;

    public Evento(String nome, String evento){
        this.nome = nome;
        this.descricao = descricao;
    }
    public Evento(double efeitoEnergia, double efeitoConhecimento, double efeitoMotivacao,
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

    public double getEfeitoConhecimento() {
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

    public int getTempoRequisito() {
        return tempoRequisito;
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

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public abstract void tentarExecutar(Personagem personagem);

    public abstract void executar(Personagem personagem);

    @Override
    public String toString(){
        return this.nome + this.descricao;
    }
}


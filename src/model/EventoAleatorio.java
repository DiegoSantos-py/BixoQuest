package model;

public class EventoAleatorio extends Evento{
    private double chanceAtivacao;

    public EventoAleatorio(String nome, String descricao){
        super(nome, descricao);
    }

    public EventoAleatorio(double efeitoEnergia, double efeitoConhecimento,
                           double efeitoMotivacao, double efeitoSaude,
                           double efeitoDinheiro, int efeitoTempo,
                           int tempoRequisito, Evento eventoRequisito,
                           double energiaMinima) {
        super(efeitoEnergia, efeitoConhecimento, efeitoMotivacao, efeitoSaude, efeitoDinheiro, efeitoTempo, tempoRequisito, eventoRequisito, energiaMinima);
        this.chanceAtivacao = Math.random();
    }

    public EventoAleatorio(double efeitoEnergia, double efeitoConhecimento,
                           double efeitoMotivacao, double efeitoSaude,
                           double efeitoDinheiro, int efeitoTempo,
                           int tempoRequisito, Evento eventoRequisito,
                           double energiaMinima, double chanceAtivacao) {
        super(efeitoEnergia, efeitoConhecimento, efeitoMotivacao, efeitoSaude, efeitoDinheiro, efeitoTempo, tempoRequisito, eventoRequisito, energiaMinima);
        this.chanceAtivacao = chanceAtivacao;
    }

    public double getChanceAtivacao() {
        return chanceAtivacao;
    }

    public void setChanceAtivacao(double chanceAtivacao) {
        this.chanceAtivacao = chanceAtivacao;
    }

    @Override
    public void tentarExecutar(Personagem personagem) {
        if (Math.random() < chanceAtivacao) {
            executar(personagem);
        }
    }

    @Override
    public void executar(Personagem personagem){}
}

package model.Evento;

import model.Disciplina.AreaConhecimento;

import java.util.Map;

public class EventoAleatorio extends Evento {
    private double chanceAtivacao;

    public EventoAleatorio(String nome, String descricao){
        super(nome, descricao);
    }

    public EventoAleatorio(double efeitoEnergia, Map<AreaConhecimento, Double> efeitoConhecimento,
                           double efeitoMotivacao, double efeitoSaude,
                           double efeitoDinheiro, int efeitoTempo,
                           int tempoRequisito, Evento eventoRequisito,
                           double energiaMinima) {
        super(efeitoEnergia, efeitoConhecimento, efeitoMotivacao, efeitoSaude, efeitoDinheiro, efeitoTempo, tempoRequisito, eventoRequisito, energiaMinima);
        this.chanceAtivacao = Math.random();
    }

    public EventoAleatorio(double efeitoEnergia, Map<AreaConhecimento, Double> efeitoConhecimento,
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

}

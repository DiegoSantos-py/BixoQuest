package model.Evento;

import model.Disciplina.AreaConhecimento;

import java.util.Map;

public class EventoObrigatorio extends Evento {

    public EventoObrigatorio(String nome, String descricao){
        super(nome, descricao);
    }

    public EventoObrigatorio(double efeitoEnergia, Map<AreaConhecimento, Double> efeitoConhecimento,
                             double efeitoMotivacao, double efeitoSaude,
                             double efeitoDinheiro, int efeitoTempo,
                             int tempoRequisito, Evento eventoRequisito, double energiaMinima) {
        super(efeitoEnergia, efeitoConhecimento, efeitoMotivacao, efeitoSaude, efeitoDinheiro, efeitoTempo, tempoRequisito, eventoRequisito, energiaMinima);
    }

}

package model.Evento;

import model.Personagem;

public class EventoObrigatorio extends Evento {

    public EventoObrigatorio(String nome, String descricao){
        super(nome, descricao);
    }

    public EventoObrigatorio(double efeitoEnergia, double efeitoConhecimento,
                             double efeitoMotivacao, double efeitoSaude,
                             double efeitoDinheiro, int efeitoTempo,
                             int tempoRequisito, Evento eventoRequisito, double energiaMinima) {
        super(efeitoEnergia, efeitoConhecimento, efeitoMotivacao, efeitoSaude, efeitoDinheiro, efeitoTempo, tempoRequisito, eventoRequisito, energiaMinima);
    }

    @Override
    public void tentarExecutar(Personagem personagem) {
        executar(personagem);
    }

    @Override
    public void executar(Personagem personagem){}
}

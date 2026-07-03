package model.Evento;

import model.Disciplina.AreaConhecimento;

import java.util.Map;
import java.util.Random;

public class EventoAleatorio extends Evento {

    // Variáveis para implementar aleatoriedade
    private double chanceAtivacao; // ex: 0.3 = 30%
    private static final Random random = new Random();

    public EventoAleatorio() {}

    public EventoAleatorio(String nome, String descricao, double chanceAtivacao) {
        super(nome, descricao);
        this.chanceAtivacao = chanceAtivacao;
    }

    public EventoAleatorio(double efeitoEnergia,
                           Map<AreaConhecimento, Double> efeitoConhecimento,
                           double efeitoMotivacao,
                           double efeitoSaude,
                           double efeitoDinheiro,
                           int efeitoTempo,
                           int tempoRequisito,
                           Evento eventoRequisito,
                           double chanceAtivacao) {

        super(efeitoEnergia, efeitoConhecimento, efeitoMotivacao,
                efeitoSaude, efeitoDinheiro, efeitoTempo,
                tempoRequisito, eventoRequisito);

        this.chanceAtivacao = chanceAtivacao;
    }

    public double getChanceAtivacao() {
        return chanceAtivacao;
    }

    public void setChanceAtivacao(double chanceAtivacao) {
        this.chanceAtivacao = chanceAtivacao;
    }

    public boolean deveAtivar() {
        return random.nextDouble() <= chanceAtivacao;
    }


}
package model.Evento;

import model.Disciplina.AreaConhecimento;

import java.util.Map;
import java.util.Random;

public class EventoAleatorio extends Evento {

    private double chanceAtivacao; // ex: 0.3 = 30%
    private static final Random random = new Random();

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
                           double energiaMinima,
                           double chanceAtivacao) {

        super(efeitoEnergia, efeitoConhecimento, efeitoMotivacao,
                efeitoSaude, efeitoDinheiro, efeitoTempo,
                tempoRequisito, eventoRequisito, energiaMinima);

        this.chanceAtivacao = chanceAtivacao;
    }

    public double getChanceAtivacao() {
        return chanceAtivacao;
    }

    public void setChanceAtivacao(double chanceAtivacao) {
        this.chanceAtivacao = chanceAtivacao;
    }

    // método chave
    public boolean deveAtivar() {
        return random.nextDouble() <= chanceAtivacao;
    }
}
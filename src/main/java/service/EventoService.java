package service;

import model.Disciplina.AreaConhecimento;
import model.Evento.Evento;
import model.Local.ZonaInterativa;
import model.Personagem;
import model.Tempo.Dia;

import java.util.Map;

public class EventoService {
    public Evento criarEvento(String nome,
                              String descricao,
                              double efeitoEnergia,
                              Map<AreaConhecimento, Double> efeitoConhecimento,
                              double efeitoMotivacao,
                              double efeitoSaude,
                              double efeitoDinheiro,
                              int efeitoTempo,
                              int tempoRequisito,
                              Evento eventoRequisito,
                              double energiaMinima,
                              double custoDinheiro,
                              boolean repetivel,
                              ZonaInterativa zona) {

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome inválido");
        }

        Evento evento = new Evento(
                efeitoEnergia,
                efeitoConhecimento,
                efeitoMotivacao,
                efeitoSaude,
                efeitoDinheiro,
                efeitoTempo,
                tempoRequisito,
                eventoRequisito,
                energiaMinima
        );

        // atributos que não estão no construtor
        evento.setCustaDinheiro(custoDinheiro);
        evento.setRepetivel(repetivel);
        evento.setZona(zona);
        evento.setStatus(false);

        return evento;
    }

    public boolean podeExecutar(Evento evento, Personagem personagem, Dia diaAtual, DiaService diaService) {

        // já foi executado e não é repetível
        if (evento.isStatus() && !evento.isRepetivel()) {
            return false;
        }

        // energia mínima
        if (personagem.getEnergia() < evento.getEnergiaMinima()) {
            return false;
        }

        // dinheiro suficiente
        if (personagem.getDinheiro() < evento.getCustaDinheiro()) {
            return false;
        }

        // evento pré-requisito
        if (evento.getEventoRequisito() != null &&
                !evento.getEventoRequisito().isStatus()) {
            return false;
        }

        // tempo disponível no dia (convertendo para minutos)
        long tempoRestanteMin = diaService.getTempoRestante(diaAtual) / 60;

        if (tempoRestanteMin < evento.getEfeitoTempo()) {
            return false;
        }

        return true;
    }

    public void executarEvento(Evento evento,
                               Personagem personagem,
                               Dia diaAtual,
                               DiaService diaService) {

        ZonaInterativa zona = evento.getZona();
        String nomeZona = zona.getNome();

        Evento eventoDoDia = null;

        // prioridade: eventos obrigatórios
        if (diaAtual.getEventosObrigatorios().containsKey(nomeZona)) {
            eventoDoDia = diaAtual.getEventosObrigatorios().get(nomeZona);
        }
        // senão, eventos aleatórios
        else if (diaAtual.getEventosAleatorios().containsKey(nomeZona)) {
            eventoDoDia = diaAtual.getEventosAleatorios().get(nomeZona);
        }

        //  não existe evento nessa zona
        if (eventoDoDia == null) {
            throw new IllegalStateException("Nenhum evento disponível para esta zona");
        }

        //  evento não corresponde ao da zona
        if (!eventoDoDia.equals(evento)) {
            throw new IllegalStateException("Evento não corresponde à zona atual");
        }

        // valida regras do evento
        if (!podeExecutar(evento, personagem, diaAtual, diaService)) {
            throw new IllegalStateException("Evento não pode ser executado");
        }

        aplicarEfeitos(evento, personagem, diaAtual, diaService);

        evento.setStatus(true);
    }

    private void aplicarEfeitos(Evento evento,
                                Personagem personagem,
                                Dia diaAtual,
                                DiaService diaService) {

        personagem.setEnergia(
                Math.max(0, personagem.getEnergia() + evento.getEfeitoEnergia())
        );

        if (evento.getEfeitosConhecimento() != null) {
            for (var entry : evento.getEfeitosConhecimento().entrySet()) {
                personagem.adicionarConhecimento(
                        entry.getKey(),
                        entry.getValue()
                );
            }
        }

        personagem.setMotivacao(
                Math.max(0, personagem.getMotivacao() + evento.getEfeitoMotivacao())
        );

        personagem.setSaude(
                Math.max(0, personagem.getSaude() + evento.getEfeitoSaude())
        );

        personagem.setDinheiro(
                Math.max(0, personagem.getDinheiro() + evento.getEfeitoDinheiro())
        );

        // consumo de tempo via service
        diaService.consumirTempoEvento(diaAtual, evento.getEfeitoTempo());
    }
}
package service;

import model.Evento.Evento;
import model.Personagem;
import model.Tempo.Dia;

public class EventoService {

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
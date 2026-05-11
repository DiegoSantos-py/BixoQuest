package service;

import model.Disciplina.AreaConhecimento;
import model.Evento.Evento;
import model.Local.ZonaInterativa;
import model.Personagem;
import model.Tempo.Dia;

import java.util.Map;

public class EventoService {

    // Cria um novo evento
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

        // Cria o evento com os atributos principais
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

        evento.setCustaDinheiro(custoDinheiro);
        evento.setRepetivel(repetivel);
        evento.setZona(zona);
        evento.setStatus(false); // ainda não foi executado

        return evento;
    }


    public boolean podeExecutar(Evento evento, Personagem personagem, Dia diaAtual, DiaService diaService) {

        if (evento.isStatus() && !evento.isRepetivel()) {
            return false;
        }

        if (personagem.getEnergia() < evento.getEnergiaMinima()) {
            return false;
        }

        if (personagem.getDinheiro() < evento.getCustaDinheiro()) {
            return false;
        }

        if (evento.getEventoRequisito() != null &&
                !evento.getEventoRequisito().isStatus()) {
            return false;
        }

        // Verifica se há tempo suficiente no dia
        long tempoRestanteMin = diaService.getTempoRestante(diaAtual) / 60;

        if (tempoRestanteMin < evento.getEfeitoTempo()) {
            return false;
        }

        return true;
    }

    // Executa o evento, aplicando seus efeitos
    public void executarEvento(Evento evento,
                               Personagem personagem,
                               Dia diaAtual,
                               DiaService diaService) {

        ZonaInterativa zona = evento.getZona();
        String nomeZona = zona.getNome();

        Evento eventoDoDia = null;

        if (diaAtual.getEventosObrigatorios().containsKey(nomeZona)) {
            eventoDoDia = diaAtual.getEventosObrigatorios().get(nomeZona);
        }
        else if (diaAtual.getEventosAleatorios().containsKey(nomeZona)) {
            eventoDoDia = diaAtual.getEventosAleatorios().get(nomeZona);
        }

        // Se não houver evento naquela zona, lança erro
        if (eventoDoDia == null) {
            throw new IllegalStateException("Nenhum evento disponível para esta zona");
        }

        if (!eventoDoDia.equals(evento)) {
            throw new IllegalStateException("Evento não corresponde à zona atual");
        }

        // Verifica se pode executar o evento
        if (!podeExecutar(evento, personagem, diaAtual, diaService)) {
            throw new IllegalStateException("Evento não pode ser executado");
        }

        // Aplica os efeitos do evento
        aplicarEfeitos(evento, personagem, diaAtual, diaService);

        // Marca o evento como executado
        evento.setStatus(true);
    }

    private void aplicarEfeitos(Evento evento,
                                Personagem personagem,
                                Dia diaAtual,
                                DiaService diaService) {

        // Atualiza energia
        personagem.setEnergia(
                Math.max(0, personagem.getEnergia() + evento.getEfeitoEnergia())
        );

        // Atualiza conhecimento em cada área, se houver
        if (evento.getEfeitosConhecimento() != null) {
            for (var entry : evento.getEfeitosConhecimento().entrySet()) {
                personagem.atualizarConhecimento(
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

        diaService.consumirTempoEvento(diaAtual, evento.getEfeitoTempo());
    }
}
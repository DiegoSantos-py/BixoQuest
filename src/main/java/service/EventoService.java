package service;

import exception.Evento.EventoInvalidoException;
import exception.Evento.EventoNaoEncontradoException;
import exception.PersistenciaException;
import model.Disciplina.AreaConhecimento;
import model.Evento.Evento;
import model.Local.ZonaInterativa;
import model.Personagem;
import model.Tempo.Dia;
import repository.EventoRepository;

import java.util.Map;

public class EventoService {

    private final EventoRepository eventoRepo;

    public EventoService(EventoRepository eventoRepo) {
        this.eventoRepo = eventoRepo;
    }

    // Inicialização
    /**@throws PersistenciaException se ocorrer falha ao carregar o arquivo*/
    public void carregar() throws PersistenciaException {
        eventoRepo.carregar();
    }

    /**@throws PersistenciaException se ocorrer falha ao salvar o arquivo*/
    public void salvar() throws PersistenciaException {
        eventoRepo.salvar();
    }

    // Escrita
    /**@throws EventoInvalidoException se nome, descrição ou zona forem inválidos*/
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
                              ZonaInterativa zona) throws PersistenciaException {
        if (nome == null || nome.isBlank()) {
            throw new EventoInvalidoException("nome", "não pode ser nulo ou vazio");
        }
        if (zona == null) {
            throw new EventoInvalidoException("zona", "não pode ser nula");
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

        evento.setNome(nome);
        evento.setDescricao(descricao);
        evento.setCustaDinheiro(custoDinheiro);
        evento.setRepetivel(repetivel);
        evento.setZona(zona);
        evento.setStatus(false);

        eventoRepo.adicionarEvento(evento);
        eventoRepo.salvar();

        return evento;
    }

    // Leitura
    /**@throws EventoNaoEncontradoException se não existir evento com o nome informado*/
    public Evento buscarPorNome(String nome) {
        return eventoRepo.buscarPorNome(nome);
    }

    public Map<String, Evento> carregarEventos() {
        return eventoRepo.carregarEventos();
    }

    // Lógica de negócio

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
        if (evento.getEventoRequisito() != null && !evento.getEventoRequisito().isStatus()) {
            return false;
        }

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
        aplicarEfeitos(evento, personagem, diaAtual, diaService);
        evento.setStatus(true);
    }

    // Helpers privados
    private void aplicarEfeitos(Evento evento,
                                Personagem personagem,
                                Dia diaAtual,
                                DiaService diaService) {
        personagem.setEnergia(
                Math.max(0, personagem.getEnergia() + evento.getEfeitoEnergia()));

        if (evento.getEfeitosConhecimento() != null) {
            for (var entry : evento.getEfeitosConhecimento().entrySet()) {
                personagem.atualizarConhecimento(entry.getKey(), entry.getValue());
            }
        }

        personagem.setMotivacao(
                Math.max(0, personagem.getMotivacao() + evento.getEfeitoMotivacao()));

        personagem.setSaude(
                Math.max(0, personagem.getSaude() + evento.getEfeitoSaude()));

        personagem.setDinheiro(
                Math.max(0, personagem.getDinheiro() + evento.getEfeitoDinheiro()));

        diaService.consumirTempoEvento(diaAtual, evento.getEfeitoTempo());
    }
}
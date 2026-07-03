package service;

import exception.Evento.EventoInvalidoException;
import exception.Evento.EventoNaoEncontradoException;
import exception.PersistenciaException;
import model.Disciplina.AreaConhecimento;
import model.Evento.Evento;
import model.Local.ZonaInterativa;
import model.Personagem;
import model.Tempo.Dia;
import model.Tempo.Semestre;
import repository.EventoRepository;

import java.util.Map;

public class EventoService {

    private final EventoRepository eventoRepo;

    public EventoService(EventoRepository eventoRepo) {
        this.eventoRepo = eventoRepo;
    }

    // Inicialização
    /**lança PersistenciaException se ocorrer falha ao carregar o arquivo*/
    public void carregar() throws PersistenciaException {
        eventoRepo.carregar();
    }

    /**lança PersistenciaException se ocorrer falha ao salvar o arquivo*/
    public void salvar() throws PersistenciaException {
        eventoRepo.salvar();
    }

    // Escrita
    /**lança EventoInvalidoException se nome, descrição ou zona forem inválidos*/
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
                eventoRequisito
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
    /**lança EventoNaoEncontradoException se não existir evento com o nome informado*/
    public Evento buscarPorNome(String nome) {
        return eventoRepo.buscarPorNome(nome);
    }

    public Map<String, Evento> carregarEventos() {
        return eventoRepo.carregarEventos();
    }

    // Lógica de negócio

    public boolean podeExecutar(Evento evento, Personagem personagem, Semestre semestre,
                                Dia diaAtual, DiaService diaService) {
        if (evento.isStatus() && !evento.isRepetivel()) {
            return false;
        }
        if (!atendeRequisitosAtributo(evento, personagem)) {
            return false;
        }
        if (personagem.getDinheiro() < evento.getCustaDinheiro()) {
            return false;
        }
        if (evento.getEventoRequisito() != null && !evento.getEventoRequisito().isStatus()) {
            return false;
        }
        if (!atendeRequisitoDisciplina(evento, semestre)) {
            return false;
        }

        long tempoRestanteMin = diaService.getTempoRestante(diaAtual) / 60;
        return tempoRestanteMin >= evento.getEfeitoTempo();
    }

    private boolean atendeRequisitosAtributo(Evento evento, Personagem personagem) {
        Map<String, Double> requisitos = evento.getRequisitosAtributo();
        if (requisitos == null || requisitos.isEmpty()) return true;

        for (Map.Entry<String, Double> req : requisitos.entrySet()) {
            if (valorAtributoAtual(personagem, req.getKey()) < req.getValue()) {
                return false;
            }
        }
        return true;
    }

    private double valorAtributoAtual(Personagem personagem, String nomeAtributo) {
        return switch (nomeAtributo.toUpperCase()) {
            case "ENERGIA" -> personagem.getEnergia();
            case "MOTIVACAO" -> personagem.getMotivacao();
            case "SAUDE" -> personagem.getSaude();
            case "DINHEIRO" -> personagem.getDinheiro();
            default -> throw new IllegalArgumentException("Atributo desconhecido: " + nomeAtributo);
        };
    }

    private boolean atendeRequisitoDisciplina(Evento evento, Semestre semestre) {
        String nomeReq = evento.getDisciplinaRequisitoNome();
        if (nomeReq == null || nomeReq.isBlank()) return true;

        if (semestre == null) return false;

        return semestre.getDisciplinas().stream().anyMatch(d ->
                d.getNome().equals(nomeReq) &&
                        (evento.getDisciplinaRequisitoCodigo() <= 0
                                || d.getCodigo() == evento.getDisciplinaRequisitoCodigo())
        );
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
        // impede que a energia do personagem fique negativa
        personagem.setEnergia(
                Math.max(0, personagem.getEnergia() + evento.getEfeitoEnergia()));

        // recupera efeitos do evento no conhecimento do personagem
        if (evento.getEfeitosConhecimento() != null) {
            for (var entry : evento.getEfeitosConhecimento().entrySet()) {
                personagem.atualizarConhecimento(entry.getKey(), entry.getValue());
            }
        }

        //aplica efeitos do evento
        personagem.setMotivacao(
                Math.max(0, personagem.getMotivacao() + evento.getEfeitoMotivacao()));

        personagem.setSaude(
                Math.max(0, personagem.getSaude() + evento.getEfeitoSaude()));

        personagem.setDinheiro(
                Math.max(0, personagem.getDinheiro() + evento.getEfeitoDinheiro()));

        diaService.consumirTempoEvento(diaAtual, evento.getEfeitoTempo());
    }
}
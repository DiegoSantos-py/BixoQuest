package controller;

import exception.Evento.EventoDuplicadoException;
import exception.Evento.EventoInvalidoException;
import exception.Evento.EventoNaoEncontradoException;
import exception.PersistenciaException;
import model.Disciplina.AreaConhecimento;
import model.Evento.Evento;
import model.Local.ZonaInterativa;
import service.EventoService;

import java.util.Map;
import java.util.Optional;

public class EventoController extends BaseController {

    private final EventoService service;

    public EventoController(EventoService service) {
        this.service = service;
    }

    // Inicialização
    /** Exibe erro se ocorrer falha ao carregar o arquivo */
    public void carregar() {
        try {
            service.carregar();
        } catch (PersistenciaException e) {
            tratarErroPersistencia(e);
        }
    }

    // Escrita
    /** Retorna Optional vazio se evento for inválido, duplicado ou falhar ao salvar */
    public Optional<Evento> criarEvento(String nome,
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
                                        ZonaInterativa zona) {
        try {
            Evento evento = service.criarEvento(nome, descricao, efeitoEnergia, efeitoConhecimento,
                    efeitoMotivacao, efeitoSaude, efeitoDinheiro, efeitoTempo, tempoRequisito,
                    eventoRequisito, custoDinheiro, repetivel, zona);
            exibirSucesso("Evento criado com sucesso.");
            return Optional.of(evento);
        } catch (EventoInvalidoException e) {
            tratarInvalido(e);
            return Optional.empty();
        } catch (EventoDuplicadoException e) {
            tratarDuplicado(e);
            return Optional.empty();
        } catch (PersistenciaException e) {
            tratarErroPersistencia(e);
            return Optional.empty();
        }
    }

    // Leitura
    /** Retorna Optional vazio se evento não for encontrado */
    public Optional<Evento> buscarPorNome(String nome) {
        try {
            return Optional.of(service.buscarPorNome(nome));
        } catch (EventoNaoEncontradoException e) {
            tratarNaoEncontrado(e);
            return Optional.empty();
        }
    }

    public Map<String, Evento> carregarEventos() {
        return service.carregarEventos();
    }

    // Exibição
    @Override
    protected void exibirErro(String mensagem) {
        System.err.println("[ERRO] " + mensagem);
    }

    @Override
    protected void exibirSucesso(String mensagem) {
        System.out.println("[OK] " + mensagem);
    }

    // Tratamento local de exceções
    private void tratarInvalido(EventoInvalidoException e) {
        exibirErro("Dado inválido no campo '" + e.getCampoCausador() + "': " + e.getMessage());
    }

    private void tratarDuplicado(EventoDuplicadoException e) {
        exibirErro("Já cadastrado: " + e.getMessage());
    }

    private void tratarNaoEncontrado(EventoNaoEncontradoException e) {
        exibirErro("Não encontrado: " + e.getMessage());
    }
}

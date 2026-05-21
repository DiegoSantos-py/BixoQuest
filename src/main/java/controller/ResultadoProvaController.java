package controller;

import exception.PersistenciaException;
import exception.Evento.Prova.ResultadoProvaDuplicadoException;
import exception.Evento.Prova.ResultadoProvaInvalidoException;
import model.Evento.Prova.ResultadoProva;
import repository.ResultadoProvaRepository;

import java.util.List;
import java.util.Map;

public class ResultadoProvaController extends BaseController {

    private final ResultadoProvaRepository repository;

    public ResultadoProvaController(ResultadoProvaRepository repository) {
        this.repository = repository;
    }

    // Inicialização
    /**@throws PersistenciaException se ocorrer falha ao carregar o arquivo*/
    public void carregar() {
        try {
            repository.carregar();
        } catch (PersistenciaException e) {
            tratarErroPersistencia(e);
        }
    }

    /**@throws PersistenciaException se ocorrer falha ao salvar o arquivo*/
    public void salvar() {
        try {
            repository.salvar();
        } catch (PersistenciaException e) {
            tratarErroPersistencia(e);
        }
    }

    // Escrita
    /**
     * @throws ResultadoProvaInvalidoException se resultado for inválido
     * @throws ResultadoProvaDuplicadoException se a prova já existir
     */
    public void adicionarResultadoProva(int jogadorId, int semestreNumero, ResultadoProva resultadoProva) {
        try {
            repository.adicionarResultadoProva(jogadorId, semestreNumero, resultadoProva);
            exibirSucesso("Resultado de prova adicionado com sucesso.");
        } catch (ResultadoProvaInvalidoException e) {
            exibirErro("Dado inválido no campo '" + e.getCampoCausador() + "': " + e.getMessage());
        } catch (ResultadoProvaDuplicadoException e) {
            exibirErro("Já cadastrado: " + e.getMessage());
        }
    }

    // Leitura
    public Map<Integer, List<ResultadoProva>> getResultadosProvaPorJogador(int jogadorId) {
        return repository.getResultadosProvaPorJogador(jogadorId);
    }

    public List<ResultadoProva> getResultadoPorJogadorESemestre(int jogadorId, int semestreNumero) {
        return repository.getResultadoPorJogadorESemestre(jogadorId, semestreNumero);
    }

    public Map<Integer, Map<Integer, List<ResultadoProva>>> carregarResultadoProvas() {
        return repository.carregarResultadoProvas();
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
}

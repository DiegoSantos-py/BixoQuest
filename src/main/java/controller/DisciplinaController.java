package controller;

import exception.Disciplina.DisciplinaDuplicadaException;
import exception.Disciplina.DisciplinaInvalidaException;
import exception.Disciplina.DisciplinaNaoEncontradaException;
import exception.PersistenciaException;
import model.Disciplina.AreaConhecimento;
import model.Disciplina.Disciplina;
import service.DisciplinaService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DisciplinaController extends BaseController {

    private final DisciplinaService service;

    public DisciplinaController(DisciplinaService service) {
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
    /** Exibe erro se nome, quantidade ou área forem inválidos, duplicados ou falhar ao salvar */
    public void criarDisciplinasPorNivel(String nome, int quantidadeNiveis, AreaConhecimento area) {
        try {
            service.criarDisciplinasPorNivel(nome, quantidadeNiveis, area);
            exibirSucesso("Disciplinas criadas com sucesso.");
        } catch (DisciplinaInvalidaException e) {
            tratarInvalido(e);
        } catch (DisciplinaDuplicadaException e) {
            tratarDuplicado(e);
        } catch (PersistenciaException e) {
            tratarErroPersistencia(e);
        }
    }

    // Leitura
    /** Retorna lista vazia se não houver disciplinas com o nome */
    public List<Disciplina> buscarPorNome(String nome) {
        try {
            return service.buscarPorNome(nome);
        } catch (DisciplinaNaoEncontradaException e) {
            tratarNaoEncontrado(e);
            return Collections.emptyList();
        }
    }

    public List<Disciplina> buscarPorArea(AreaConhecimento area) {
        return service.buscarPorArea(area);
    }

    /** Retorna Optional vazio se não encontrar a combinação nome + código */
    public Optional<Disciplina> buscar(String nome, float codigo) {
        try {
            return Optional.of(service.buscar(nome, codigo));
        } catch (DisciplinaNaoEncontradaException e) {
            tratarNaoEncontrado(e);
            return Optional.empty();
        }
    }

    public boolean existe(Disciplina d) {
        return service.existe(d);
    }

    /** Retorna Optional vazio se não existir disciplina com código + 1 */
    public Optional<Disciplina> proximaDisciplina(String nome, float codigoAtual) {
        try {
            return Optional.of(service.proximaDisciplina(nome, codigoAtual));
        } catch (DisciplinaNaoEncontradaException e) {
            tratarNaoEncontrado(e);
            return Optional.empty();
        }
    }

    public List<Disciplina> buscarDisciplinasIniciais() {
        return service.buscarDisciplinasIniciais();
    }

    public Map<String, List<Disciplina>> carregarDisciplinas() {
        return service.carregarDisciplinas();
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
    protected void tratarInvalido(DisciplinaInvalidaException e) {
        exibirErro("Dado inválido no campo '" + e.getCampoCausador() + "': " + e.getMessage());
    }

    protected void tratarDuplicado(DisciplinaDuplicadaException e) {
        exibirErro("Já cadastrado: " + e.getMessage());
    }

    protected void tratarNaoEncontrado(DisciplinaNaoEncontradaException e) {
        exibirErro("Não encontrado: " + e.getMessage());
    }
}

package controller;

import exception.Disciplina.DisciplinaDuplicadaException;
import exception.Disciplina.DisciplinaInvalidaException;
import exception.Disciplina.DisciplinaNaoEncontradaException;
import exception.RepositoryException;

public abstract class BaseController {

    // Persistência
    protected void tratarErroPersistencia(RepositoryException e) {
        e.getCause().printStackTrace();
        exibirErro("Erro de persistência: " + e.getMessage());
    }

    // Validação
    protected void tratarInvalido(DisciplinaInvalidaException e) {
        exibirErro("Dado inválido no campo '" + e.getCampoCausador() + "': " + e.getMessage());
    }

    // Busca
    protected void tratarNaoEncontrado(DisciplinaNaoEncontradaException e) {
        exibirErro("Não encontrado: " + e.getMessage());
    }

    // Duplicidade
    protected void tratarDuplicado(DisciplinaDuplicadaException e) {
        exibirErro("Já cadastrado: " + e.getMessage());
    }

    // Exibição — implementado por cada controller concreto
    protected abstract void exibirErro(String mensagem);

    protected abstract void exibirSucesso(String mensagem);
}
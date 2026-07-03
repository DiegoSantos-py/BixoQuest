package controller;

import exception.PersistenciaException;
import exception.Semestre.SemestreInvalidoException;
import exception.Semestre.SemestreNaoEncontradoException;
import model.Disciplina.Disciplina;
import model.Personagem;
import model.Tempo.Semestre;
import service.SemestreService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SemestreController extends BaseController {

    private final SemestreService service;

    public SemestreController(SemestreService service) {
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

    // Leitura
    /** Retorna lista vazia se não houver semestres para o jogador */
    public List<Semestre> getSemestresPorJogador(int jogadorId) {
        try {
            return service.getSemestresPorJogador(jogadorId);
        } catch (SemestreNaoEncontradoException e) {
            tratarNaoEncontrado(e);
            return Collections.emptyList();
        }
    }


    /** Exibe erro se semestre ou disciplina forem inválidos */
    public void definirResultadoDisciplina(Semestre semestre,
                                           Disciplina disciplina,
                                           boolean aprovado) {
        try {
            service.definirResultadoDisciplina(semestre, disciplina, aprovado);
        } catch (SemestreInvalidoException e) {
            tratarInvalido(e);
        }
    }

    public boolean terminouSemestre(Semestre semestre) {
        return service.terminouSemestre(semestre);
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
    private void tratarInvalido(SemestreInvalidoException e) {
        exibirErro("Dado inválido no campo '" + e.getCampoCausador() + "': " + e.getMessage());
    }

    private void tratarNaoEncontrado(SemestreNaoEncontradoException e) {
        exibirErro("Não encontrado: " + e.getMessage());
    }
}

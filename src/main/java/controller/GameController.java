package controller;

import exception.PersistenciaException;
import model.Disciplina.Disciplina;
import model.Evento.Evento;
import model.Evento.Prova.ProvaIDs;
import model.Evento.ResultadoZona;
import model.Personagem;
import model.Tempo.Semestre;
import service.GameService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GameController extends BaseController {

    private final GameService service;

    public GameController(GameService service) {
        this.service = service;
    }

    // Inicialização
    /** Exibe erro se ocorrer falha ao carregar arquivos ou inicializar o jogo */
    public void iniciarJogo(int personagem) {
        try {
            service.iniciarJogo(personagem);
            exibirSucesso("Jogo iniciado com sucesso.");
        } catch (PersistenciaException e) {
            tratarErroPersistencia(e);
        }
    }

    // Lógica de negócio
    /** Exibe erro se ocorrer falha ao salvar após encerramento de dia ou semestre */
    public void atualizar() {
        try {
            service.atualizar();
        } catch (PersistenciaException e) {
            tratarErroPersistencia(e);
        }
    }

    /** Retorna false se o jogador não concluiu o jogo ou não tiver semestres */
    public boolean encerrarJogo() {
        return service.encerrarJogo();
    }

    // Escolha de disciplinas / início de semestre

    /**
     * Indica se o jogo está aguardando o jogador escolher as disciplinas
     * do próximo semestre. A View deve checar isso após iniciarJogo() e
     * após cada atualizar(), já que um semestre pode terminar no meio do jogo.
     */
    public boolean precisaEscolherDisciplinas() {
        return service.precisaEscolherDisciplinas();
    }

    /**
     * Disciplinas que o jogador pode escolher no próximo semestre.
     * Retorna lista vazia se não houver escolha pendente.
     */
    public List<Disciplina> obterDisciplinasDisponiveis() {
        if (!service.precisaEscolherDisciplinas()) {
            return Collections.emptyList();
        }
        return service.obterDisciplinasDisponiveis();
    }

    /**
     * Confirma a escolha do jogador e inicia o semestre com as disciplinas selecionadas.
     * Exibe erro se a escolha for inválida (excede o limite, disciplina indisponível,
     * TCC sem elegibilidade) ou se ocorrer falha ao persistir.
     */
    public boolean confirmarEscolhaDisciplinas(List<Disciplina> disciplinasEscolhidas) {
        try {
            service.confirmarEscolhaDisciplinas(disciplinasEscolhidas);
            exibirSucesso("Semestre iniciado com sucesso.");
            return true;
        } catch (PersistenciaException e) {
            tratarErroPersistencia(e);
            return false;
        } catch (RuntimeException e) {
            // captura SemestreInvalidoException, DisciplinaInvalidaException, etc.
            exibirErro(e.getMessage());
            return false;
        }
    }

    public ResultadoZona processarZona(String nomeZona) {
        return service.processarZona(nomeZona);
    }

    public void confirmarResultadoProva(ProvaIDs provaId, boolean aprovado) {
        try {
            service.confirmarResultadoProva(provaId, aprovado);
        } catch (PersistenciaException e) {
            tratarErroPersistencia(e);
        }
    }

    public void consumirTempoProva(int minutos) {
        service.consumirTempoProva(minutos);
    }

    public void debugForcarFimDeSemestre() {
        try {
            service.debugForcarFimDeSemestre();
        } catch (PersistenciaException e) {
            tratarErroPersistencia(e);
        }
    }

    public boolean houveTransicaoDeDia() {
        return service.houveTransicaoDeDia();
    }

    public void iniciarProximoDia() {
        service.iniciarProximoDia();
    }

    public void pausarDia() {
        service.pausarDia();
    }

    public void retomarDia() {
        service.retomarDia();
    }

    // Leitura
    public Semestre getSemestre() {
        return service.getSemestre();
    }

    public int getDiaAtual() {
        return service.getDiaAtual();
    }

    public Personagem getPersonagem() {
        return service.getPersonagem();
    }

    public long getTempoRestanteSegundos() {
        return service.getTempoRestanteSegundos();
    }

    public void forcarFimDeDia() {
        service.forcarFimDeDia();
    }

    public boolean houveJogoConcluido() {
        return service.houveJogoConcluido();
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
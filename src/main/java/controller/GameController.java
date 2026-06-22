package controller;

import exception.PersistenciaException;
import model.Personagem;
import model.Tempo.Dia;
import model.Tempo.Semestre;
import service.GameService;

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
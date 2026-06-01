package view.controleTelas;

import controller.PersonagemController;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GerenciadorTelas {

    private final Stage stage;
    private final CriadorCenas criadorCenas;

    public GerenciadorTelas(
            Stage stage,
            PersonagemController personagemController
    ) {
        this.stage = stage;
        this.criadorCenas = new CriadorCenas(this, personagemController);
    }

    //TODO: CRIAR TODAS AS TELAS E GUARDAR EM CACHE PARA EVITAR RECRIAÇÃO TODA VEZ
    public void mostrarInicio() {
        trocarCena(criadorCenas.criarInicio());
    }

    public void mostrarMenuInicial() {
        trocarCena(criadorCenas.criarMenuInicial());
    }

    public void mostrarMenuPersonagens() {
        trocarCena(criadorCenas.criarMenuPersonagens());
    }

    public void mostrarCriacaoPersonagem(int slotId) {
        trocarCena(criadorCenas.criarCriacaoPersonagem(slotId));
    }

    public void mostrarAnimacaoInicio() {
        trocarCena(criadorCenas.criarAnimacaoInicio());
    }

    public void mostrarTelaJogo() {
        // stage.setScene(criadorCenas.criarTelaJogo());
    }

    public void mostrarConfiguracoes() {
        // stage.setScene(criadorCenas.criarConfiguracoes());
    }

    private void trocarCena(Scene scene) {
        stage.setScene(scene);
        stage.setFullScreen(true);
    }
}
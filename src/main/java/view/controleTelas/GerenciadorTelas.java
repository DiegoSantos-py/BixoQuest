package view.controleTelas;

import controller.PersonagemController;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.controleTelas.CriadorCenas;


public class GerenciadorTelas {

    private final Stage stage;
    private final CriadorCenas criadorCenas;
    private Scene scene;

    public GerenciadorTelas(
            Stage stage,
            PersonagemController personagemController
    ) {
        this.stage = stage;
        this.criadorCenas = new CriadorCenas(this, personagemController);
    }

    private void trocarRoot(Parent root) {
        if (scene == null) {
            scene = new Scene(root, 1920, 1080);
            stage.setScene(scene);
        } else {
            scene.setRoot(root);
        }

        stage.setFullScreen(true);
    }

    public void mostrarInicio() {
        trocarRoot(criadorCenas.criarInicio());
    }

    public void mostrarMenuInicial() {
        trocarRoot(criadorCenas.criarMenuInicial());
    }

    public void mostrarMenuPersonagens() {
        trocarRoot(criadorCenas.criarMenuPersonagens());
    }

    public void mostrarCriacaoPersonagem(int slotId) {
        trocarRoot(criadorCenas.criarCriacaoPersonagem(slotId));
    }

    public void mostrarAnimacaoInicio() {
        trocarRoot(criadorCenas.criarAnimacaoInicio());
    }

    public void mostrarTelaJogo() {
        // trocarRoot(criadorCenas.criarTelaJogo());
    }

    public void mostrarConfiguracoes(){

    }
}
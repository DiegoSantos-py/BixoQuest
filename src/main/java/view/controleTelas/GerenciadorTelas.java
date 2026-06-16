package view.controleTelas;

import controller.MapaController;
import controller.NpcController;
import controller.PersonagemController;
import controller.BatalhaController;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GerenciadorTelas {

    private final Stage stage;
    private final CriadorCenas criadorCenas;
    private Scene scene;

    public GerenciadorTelas(
            Stage stage,
            PersonagemController personagemController,
            MapaController mapaController,
            NpcController npcController,
            BatalhaController batalhaController
    ) {
        this.stage = stage;
        this.criadorCenas = new CriadorCenas(
                this,
                personagemController,
                mapaController,
                npcController,
                batalhaController
        );
    }

    private void trocarRoot(Parent root) {
        if (scene == null) {
            scene = new Scene(root, 1920, 1080);
            stage.setScene(scene);
        } else {
            scene.setRoot(root);
            root.requestFocus();
        }

        stage.setFullScreen(true);
    }

    public void mostrarInicio() {
        trocarRoot(criadorCenas.criarInicio());
    }

    public void mostrarMenuInicial() {
        trocarRoot(criadorCenas.criarMenuInicial());
    }

    public void mostrarSeletorBatalha() {
        trocarRoot(criadorCenas.criarSeletorBatalha());
    }

    public void mostrarMenuMorte(String texto) {
        trocarRoot(criadorCenas.criarCenaMenuMorte(texto));
    }

    public void mostrarTelaBatalha() {
        trocarRoot(criadorCenas.criarTelaBatalha());
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
        trocarRoot(criadorCenas.criarCenaJogo());
    }

    public void mostrarTelaJogoEntrada(){trocarRoot(criadorCenas.criarCenaEntradaModulo());}

    public void mostrarTelaCantina(){trocarRoot(criadorCenas.criarCenaCantina());}

    public void mostrarInicioDia() {
        trocarRoot(criadorCenas.criarInicioDia());
    }

    public void mostrarConfiguracoes() {

    }
}
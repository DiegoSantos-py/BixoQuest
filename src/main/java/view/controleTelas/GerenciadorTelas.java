package view.controleTelas;

import controller.GameController;
import controller.MapaController;
import controller.NpcController;
import controller.PersonagemController;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GerenciadorTelas {

    private final Stage stage;
    private final ControllerTelas controllerTelas;
    private Scene scene;

    public GerenciadorTelas(
            Stage stage,
            PersonagemController personagemController,
            MapaController mapaController,
            NpcController npcController,
            GameController gameController
    ) {
        this.stage = stage;
        this.controllerTelas = new ControllerTelas(
                this,
                personagemController,
                mapaController,
                npcController,
                gameController
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
        trocarRoot(controllerTelas.criarInicio());
    }

    public void mostrarMenuInicial() {
        trocarRoot(controllerTelas.criarMenuInicial());
    }

    public void mostrarMenuPersonagens() {
        trocarRoot(controllerTelas.criarMenuPersonagens());
    }

    public void mostrarCriacaoPersonagem(int slotId) {
        trocarRoot(controllerTelas.criarCriacaoPersonagem(slotId));
    }

    public void mostrarAnimacaoInicio(int sessaoAtual) {
        trocarRoot(controllerTelas.criarAnimacaoInicio(sessaoAtual));
    }

    public void mostrarTelaPonto() {
        trocarRoot(controllerTelas.criarCenaPonto());
    }

    public void mostrarTelaJogoEntrada(){trocarRoot(controllerTelas.criarCenaEntradaModulo());}

    public void mostrarTelaCantina(){trocarRoot(controllerTelas.criarCenaCantina());}

    public void mostrarCenaPorNome(String nome) {
        if (nome == null) return;

        if (nome.startsWith("Ponto de ônibus"))     mostrarTelaPonto();
        else if (nome.startsWith("Entrada módulo")) mostrarTelaJogoEntrada();
        else if (nome.startsWith("Cantina módulo")) mostrarTelaCantina();
        //else if (nome.startsWith("Sala módulo"))      mostrarTelaSala();
        //else if (nome.equals("Laboratório"))          mostrarTelaLaboratorio();
        //else if (nome.equals("Colegiado"))            mostrarTelaColegiado();
        else System.err.println("Cena não encontrada para local: " + nome);
    }

    public void mostrarConfiguracoes() {

    }
}
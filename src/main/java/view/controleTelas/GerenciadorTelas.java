package view.controleTelas;

import controller.BatalhaController;
import controller.GameController;
import controller.MapaController;
import controller.PersonagemController;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import model.Evento.Prova.ProvaIDs;
import model.Npc.Animal;

public class GerenciadorTelas {

    private final Stage stage;
    private final ControllerTelas controllerTelas;
    private Scene scene;
    private final GameController gameController;

    public GerenciadorTelas(
            Stage stage,
            PersonagemController personagemController,
            MapaController mapaController,
            GameController gameController,
            BatalhaController batalhaController
    ) {
        this.stage = stage;
        this.gameController = gameController;
        this.controllerTelas = new ControllerTelas(
                this,
                personagemController,
                mapaController,
                gameController,
                batalhaController
        );
    }

    private void trocarRoot(Parent root) {
        if (scene == null) {
            scene = new Scene(root, 1920, 1080);
            stage.setScene(scene);
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            stage.setFullScreenExitHint("");
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

    public void mostrarSeletorBatalha() {trocarRoot(controllerTelas.criarSeletorBatalha());}

    public void mostrarMenuMorte(String texto) {trocarRoot(controllerTelas.criarCenaMenuMorte(texto));}

    public void mostrarTelaBatalha() {trocarRoot(controllerTelas.criarTelaBatalha());}

    public void mostrarTelaBatalha(ProvaIDs provaId, String nomeLocalRetorno) {
        trocarRoot(controllerTelas.criarTelaBatalha(provaId, nomeLocalRetorno));
    }

    public void mostrarTelaBatalhaAnimal(Animal animal, String nomeLocalRetorno) {
        trocarRoot(controllerTelas.criarTelaBatalhaAnimal(animal, nomeLocalRetorno));
    }

    public void mostrarCriacaoPersonagem(int slotId) {
        trocarRoot(controllerTelas.criarCriacaoPersonagem(slotId));
    }

    public void mostrarAnimacaoInicio(int sessaoAtual) {
        trocarRoot(controllerTelas.criarAnimacaoInicio(sessaoAtual));
    }

    public void mostrarAnimacaoFim() {trocarRoot(controllerTelas.criarAnimacaoFim());}

    public void mostrarCenaPorNome(String nome) {
        if (nome == null) return;

        if (nome.startsWith("Ponto de ônibus")) {
            trocarRoot(controllerTelas.criarCena(nome, id -> {
                switch (id) {
                    case "Banco de ônibus" -> gameController.forcarFimDeDia();
                    default -> mostrarCenaPorNome(id);
                }
            }));
        } else {
            trocarRoot(controllerTelas.criarCena(nome,
                    id -> mostrarCenaPorNome(id)));
        }
    }

    public void mostrarConfiguracoes() {

    }

}
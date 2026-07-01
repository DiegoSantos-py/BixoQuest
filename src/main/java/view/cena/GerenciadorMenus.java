package view.cena;

import controller.PersonagemController;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import view.menu.MenuAtributosPersonagem;
import view.menu.MenuPause;

import java.util.function.Consumer;

public class GerenciadorMenus {
    private final StackPane raizCena;
    private final MenuPause menuPause;
    private final MenuAtributosPersonagem menuAtributos;
    private final Consumer<Boolean> onPausarJogo;

    public GerenciadorMenus(StackPane raizCena,
                            PersonagemController personagemController,
                            int personagemId,
                            Runnable onSairParaMenuPrincipal,
                            Consumer<Boolean> onPausarJogo) {
        this.raizCena = raizCena;
        this.onPausarJogo = onPausarJogo;

        this.menuPause = new MenuPause(
                this::fecharPause,
                onSairParaMenuPrincipal
        );

        this.menuAtributos = new MenuAtributosPersonagem(
                personagemController,
                personagemId,
                this::fecharAtributos
        );

        raizCena.getChildren().addAll(menuPause, menuAtributos);
    }

    public boolean pauseAberto() {
        return menuPause.isVisible();
    }

    public void abrirPause() {
        menuPause.abrir();
        onPausarJogo.accept(true);
    }

    public void fecharPause() {
        menuPause.fechar();

        onPausarJogo.accept(false);

        Platform.runLater(() -> {
            raizCena.requestFocus();
        });
    }
    public boolean atributosAberto() {
        return menuAtributos.isVisible();
    }
    public void abrirAtributos() {
        menuAtributos.abrir();
        onPausarJogo.accept(true);
    }

    public void fecharAtributos() {
        menuAtributos.fechar();
        onPausarJogo.accept(false);
    }
}
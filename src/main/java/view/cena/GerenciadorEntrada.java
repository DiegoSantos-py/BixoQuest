package view.cena;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

import java.util.HashSet;
import java.util.Set;

public class GerenciadorEntrada {

    private final Set<KeyCode> teclasPressionadas = new HashSet<>();
    private final Runnable onPressionarTAB;
    private final Runnable onPressionarESC;

    public GerenciadorEntrada(Runnable onPressionarTAB, Runnable onPressionarESC) {
        this.onPressionarTAB = onPressionarTAB;
        this.onPressionarESC = onPressionarESC;
    }

    public void configurar(Pane pane) {
        pane.setFocusTraversable(true);

        pane.setOnKeyPressed(e -> {
            System.out.println("Pressionou: " + e.getCode());
            teclasPressionadas.add(e.getCode());
            if (e.getCode() == KeyCode.TAB) {
                if (onPressionarTAB != null) onPressionarTAB.run();
            }
            if (e.getCode() == KeyCode.ESCAPE) {
                if (onPressionarESC != null) onPressionarESC.run();
            }
        });

        pane.setOnKeyReleased(e -> teclasPressionadas.remove(e.getCode()));
        pane.setOnMouseClicked(e -> pane.requestFocus());
        pane.sceneProperty().addListener((obs, old, scene) -> {
            if (scene != null) pane.requestFocus();
        });
    }

    public Set<KeyCode> getTeclasPressionadas() {
        return teclasPressionadas;
    }
}
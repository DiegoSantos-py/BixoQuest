package view.cena;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GerenciadorEntrada {

    private final Set<KeyCode> teclasPressionadas = new HashSet<>();
    private final Runnable onPressionarTAB;
    private final Runnable onPressionarESC;
    private final Runnable onPressionarE;
    private boolean movimentoTravado = false;

    public GerenciadorEntrada(Runnable onPressionarTAB, Runnable onPressionarESC, Runnable onPressionarE) {
        this.onPressionarTAB = onPressionarTAB;
        this.onPressionarESC = onPressionarESC;
        this.onPressionarE = onPressionarE;
    }

    public void configurar(Pane pane) {
        pane.setFocusTraversable(true);

        pane.setOnKeyPressed(e -> {
            teclasPressionadas.add(e.getCode());
            if (e.getCode() == KeyCode.TAB) {
                if (onPressionarTAB != null) onPressionarTAB.run();
            }
            if (e.getCode() == KeyCode.ESCAPE) {
                if (onPressionarESC != null) onPressionarESC.run();
            }
            if (e.getCode() == KeyCode.E) {
                if (onPressionarE != null) onPressionarE.run();
            }
        });

        pane.setOnKeyReleased(e -> teclasPressionadas.remove(e.getCode()));
        pane.setOnMouseClicked(e -> pane.requestFocus());
        pane.sceneProperty().addListener((obs, old, scene) -> {
            if (scene != null) pane.requestFocus();
        });
    }

    public void limparTeclas() {
        teclasPressionadas.clear();
    }

    public void travarMovimento(boolean travado) {
        this.movimentoTravado = travado;
        if (travado) limparTeclas(); // evita "andar preso" ao travar
    }

    public Set<KeyCode> getTeclasPressionadas() {
        if (movimentoTravado) return Collections.emptySet();
        return teclasPressionadas;
    }
}
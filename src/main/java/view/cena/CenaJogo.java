package view.cena;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.function.Consumer;

/**
 * Produto final do ConstrutorCenaJogo.
 * Responsável por montar o Pane JavaFX e detectar colisões entre
 * o player e as zonas interativas.
 */
public class CenaJogo {

    // ── Dados visuais ─────────────────────────────────────────────────────────

    private final ImageView background;
    private final List<ImageView> elements;
    private final List<ImageView> npcs;
    private final List<ZoneEntry> zones;
    private final ImageView playerView;

    private AnimationTimer collisionTimer;

    // ── Registro interno de zona interativa ───────────────────────────────────

    public record ZoneEntry(String id, ImageView view, Consumer<String> onEnter) {}

    // ── Construtor — chamado apenas pelo ConstrutorCenaJogo ───────────────────

    public CenaJogo(ImageView background,
                    List<ImageView> elements,
                    List<ImageView> npcs,
                    List<ZoneEntry> zones,
                    ImageView playerView) {
        this.background = background;
        this.elements   = elements;
        this.npcs       = npcs;
        this.zones      = zones;
        this.playerView = playerView;
    }

    // ── Montagem do Pane JavaFX ───────────────────────────────────────────────

    public Pane buildPane() {
        Pane pane = new Pane();

        // 1. Fundo
        if (background != null)
            pane.getChildren().add(background);

        // 2. Elementos estáticos
        pane.getChildren().addAll(elements);

        // 3. Zonas interativas
        zones.forEach(z -> pane.getChildren().add(z.view()));

        // 4. NPCs
        pane.getChildren().addAll(npcs);

        // 5. Player (sempre por cima)
        if (playerView != null)
            pane.getChildren().add(playerView);

        startCollisionDetection();

        return pane;
    }

    // ── Detecção de colisão contínua ─────────────────────────────────────────

    private void startCollisionDetection() {
        if (playerView == null) return;

        collisionTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                zones.forEach(zone -> {
                    if (playerView.getBoundsInParent()
                            .intersects(zone.view().getBoundsInParent())) {
                        zone.onEnter().accept(zone.id());
                    }
                });
            }
        };
        collisionTimer.start();
    }

    public void stopCollisionDetection() {
        if (collisionTimer != null)
            collisionTimer.stop();
    }
}
package view.cena;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import view.movimento.SistemaMovimento;
import view.util.Borda;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Produto final do ConstrutorCenaJogo.
 * Responsável por montar o Pane JavaFX, movimentar o jogador,
 * detectar colisões com hitboxes de elementos/NPCs e zonas interativas.
 *
 * TODO: migrar lógica de movimento e posição para o PersonagemController
 */
public class CenaJogo {

    // ── Dados visuais ─────────────────────────────────────────────────────────

    private final ImageView background;
    private final List<ImageView> elements;
    private final List<Rectangle> elementHitboxes;
    private final List<ImageView> npcs;
    private final List<Rectangle> npcHitboxes;
    private final List<String> npcNomes;
    private final Consumer<String> onNpcAtingido;
    private final List<ZoneEntry> zones;
    private final ImageView playerView;
    private final Rectangle playerHitbox;
    private final double playerHitboxOffsetX;
    private final double playerHitboxOffsetY;
    private final Consumer<Borda> onBordaAtingida;

    // ── Estado de movimento ───────────────────────────────────────────────────

    private final Set<KeyCode> teclasPressionadas = new HashSet<>();
    private AnimationTimer gameLoop;

    // ── Registro interno de zona interativa ───────────────────────────────────

    public record ZoneEntry(String id, ImageView view, Consumer<String> onEnter) {}

    // ── Construtor — chamado apenas pelo ConstrutorCenaJogo ───────────────────

    public CenaJogo(ImageView background,
                    List<ImageView> elements,
                    List<Rectangle> elementHitboxes,
                    List<ImageView> npcs,
                    List<Rectangle> npcHitboxes,
                    List<ZoneEntry> zones,
                    ImageView playerView,
                    Rectangle playerHitbox,
                    double playerHitboxOffsetX,
                    double playerHitboxOffsetY,
                    Consumer<Borda> onBordaAtingida,
                    List<String> npcNomes,
                    Consumer<String> onNpcAtingido) {
        this.background          = background;
        this.elements            = elements;
        this.elementHitboxes     = elementHitboxes;
        this.npcs                = npcs;
        this.npcHitboxes         = npcHitboxes;
        this.zones               = zones;
        this.playerView          = playerView;
        this.playerHitbox        = playerHitbox;
        this.playerHitboxOffsetX = playerHitboxOffsetX;
        this.playerHitboxOffsetY = playerHitboxOffsetY;
        this.onBordaAtingida     = onBordaAtingida;
        this.npcNomes = npcNomes;
        this.onNpcAtingido = onNpcAtingido;
    }

    // ── Montagem do Pane JavaFX ───────────────────────────────────────────────

    public Pane buildPane() {
        Pane pane = new Pane();

        if (background != null)
            pane.getChildren().add(background);

        pane.getChildren().addAll(elements);
        pane.getChildren().addAll(elementHitboxes);

        zones.forEach(z -> pane.getChildren().add(z.view()));

        pane.getChildren().addAll(npcs);
        pane.getChildren().addAll(npcHitboxes);

        if (playerView != null) {
            pane.getChildren().add(playerView);
            pane.getChildren().add(playerHitbox);
        }

        configurarControles(pane);
        iniciarGameLoop();



        return pane;
    }

    // ── Controles de teclado ──────────────────────────────────────────────────

    private void configurarControles(Pane pane) {
        pane.setFocusTraversable(true);

        pane.setOnKeyPressed(e -> {
            System.out.println("tecla: " + e.getCode());
            teclasPressionadas.add(e.getCode());
        });
        pane.setOnKeyReleased(e -> teclasPressionadas.remove(e.getCode()));

        pane.setOnMouseClicked(e -> pane.requestFocus());

        pane.sceneProperty().addListener((obs, old, scene) -> {
            if (scene != null) pane.requestFocus();
        });

    }

    private SistemaMovimento sistemaMovimento;

    private void iniciarGameLoop() {
        if (playerView == null) return;

        sistemaMovimento = new SistemaMovimento(
                playerView,
                playerHitbox,
                playerHitboxOffsetX,
                playerHitboxOffsetY,
                elementHitboxes,
                npcHitboxes,
                onBordaAtingida
        );

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                sistemaMovimento.atualizar(teclasPressionadas);
                verificarColisaoZonas();
            }
        };
        gameLoop.start();
    }

    // ── Colisão com zonas interativas ─────────────────────────────────────────

    private void verificarColisaoZonas() {
        zones.forEach(zone -> {
            if (playerHitbox.getBoundsInParent()
                    .intersects(zone.view().getBoundsInParent())) {
                zone.onEnter().accept(zone.id());
            }
        });
    }

    private void verificarColisaoNpcs() {
        for (int i = 0; i < npcHitboxes.size(); i++) {
            if (playerHitbox.getBoundsInParent()
                    .intersects(npcHitboxes.get(i).getBoundsInParent())) {
                if (onNpcAtingido != null)
                    onNpcAtingido.accept(npcNomes.get(i));
            }
        }
    }

    // ── Parar o loop ──────────────────────────────────────────────────────────

    public void parar() {
        if (gameLoop != null)
            gameLoop.stop();
    }
}
package view.cena;

import javafx.animation.AnimationTimer;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
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
 */
public class CenaJogo {

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
    private final String spriteBase;
    private final Runnable onPressionarTAB;
    private final Runnable onPressionarESC;

    private AnimationTimer gameLoop;
    private SistemaMovimento sistemaMovimento;
    private SistemaColisao sistemaColisao;
    private GerenciadorEntrada gerenciadorEntrada;

    public record ZoneEntry(String id, ImageView view, Rectangle hitbox, Consumer<String> onEnter) {}

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
                    Consumer<String> onNpcAtingido,
                    String spriteBase,
                    Runnable onPressionarTAB,
                    Runnable onPressionarESC) {
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
        this.npcNomes            = npcNomes;
        this.onNpcAtingido       = onNpcAtingido;
        this.spriteBase          = spriteBase;
        this.onPressionarTAB     = onPressionarTAB;
        this.onPressionarESC     = onPressionarESC;
    }

    public Pane buildPane() {
        Pane pane = new Pane();

        if (background != null)
            pane.getChildren().add(background);

        pane.getChildren().addAll(elements);
        pane.getChildren().addAll(elementHitboxes);

        zones.forEach(z -> {
            if (z.view() != null) pane.getChildren().add(z.view());
            pane.getChildren().add(z.hitbox());
        });

        pane.getChildren().addAll(npcs);
        pane.getChildren().addAll(npcHitboxes);

        if (playerView != null) {
            pane.getChildren().add(playerView);
            pane.getChildren().add(playerHitbox);
        }

        gerenciadorEntrada = new GerenciadorEntrada(onPressionarTAB, onPressionarESC);
        gerenciadorEntrada.configurar(pane);

        iniciarGameLoop();

        return pane;
    }

    private void iniciarGameLoop() {
        if (playerView == null) return;

        sistemaMovimento = new SistemaMovimento(
                playerView, playerHitbox,
                playerHitboxOffsetX, playerHitboxOffsetY,
                elementHitboxes, npcHitboxes, onBordaAtingida, spriteBase
        );

        sistemaColisao = new SistemaColisao(
                playerHitbox, zones, npcHitboxes, npcNomes, onNpcAtingido
        );

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                sistemaMovimento.atualizar(gerenciadorEntrada.getTeclasPressionadas());
                sistemaColisao.verificarZonas();
                sistemaColisao.verificarNpcs();
            }
        };
        gameLoop.start();
    }

    public void parar() {
        if (gameLoop != null)
            gameLoop.stop();
    }
}
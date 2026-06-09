package view.cena;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

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

    // ── Constantes ────────────────────────────────────────────────────────────

    private static final double VELOCIDADE   = 4.0;
    private static final double LARGURA_TELA = 1920;
    private static final double ALTURA_TELA  = 1080;

    // Caminhos dos GIFs por direção
    private static final String GIF_SOUTH = "/Jogador/Jogador1/Animação/walk_south.gif";
    private static final String GIF_NORTH = "/Jogador/Jogador1/Animação/walk_north.gif";
    private static final String GIF_EAST  = "/Jogador/Jogador1/Animação/walk_east.gif";
    private static final String GIF_WEST  = "/Jogador/Jogador1/Animação/walk_west.gif";

    // Identificadores de borda
    public static final String BORDA_NORTE = "norte";
    public static final String BORDA_SUL   = "sul";
    public static final String BORDA_LESTE = "leste";
    public static final String BORDA_OESTE = "oeste";

    // ── Dados visuais ─────────────────────────────────────────────────────────

    private final ImageView background;
    private final List<ImageView> elements;
    private final List<Rectangle> elementHitboxes;
    private final List<ImageView> npcs;
    private final List<Rectangle> npcHitboxes;
    private final List<ZoneEntry> zones;
    private final ImageView playerView;
    private final Rectangle playerHitbox;
    private final double playerHitboxOffsetX;
    private final double playerHitboxOffsetY;
    private final Consumer<String> onBordaAtingida;

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
                    Consumer<String> onBordaAtingida) {
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

    // ── Game loop principal ───────────────────────────────────────────────────

    private void iniciarGameLoop() {
        if (playerView == null) return;

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                moverJogador();
                verificarColisaoZonas();
            }
        };
        gameLoop.start();
    }

    // ── Movimento do jogador ──────────────────────────────────────────────────

    private void moverJogador() {
        double x = playerView.getLayoutX();
        double y = playerView.getLayoutY();
        double novoX = x;
        double novoY = y;

        boolean moveu = false;

        if (teclasPressionadas.contains(KeyCode.W) || teclasPressionadas.contains(KeyCode.UP)) {
            novoY -= VELOCIDADE;
            trocarGif(GIF_NORTH);
            moveu = true;
        }
        if (teclasPressionadas.contains(KeyCode.S) || teclasPressionadas.contains(KeyCode.DOWN)) {
            novoY += VELOCIDADE;
            trocarGif(GIF_SOUTH);
            moveu = true;
        }
        if (teclasPressionadas.contains(KeyCode.A) || teclasPressionadas.contains(KeyCode.LEFT)) {
            novoX -= VELOCIDADE;
            trocarGif(GIF_WEST);
            moveu = true;
        }
        if (teclasPressionadas.contains(KeyCode.D) || teclasPressionadas.contains(KeyCode.RIGHT)) {
            novoX += VELOCIDADE;
            trocarGif(GIF_EAST);
            moveu = true;
        }

        if (!moveu) return;

        // Verifica bordas antes de aplicar movimento
        double larguraPlayer = playerView.getFitWidth();
        double alturaPlayer  = (playerView.getImage().getHeight()
                / playerView.getImage().getWidth()) * larguraPlayer;

        if (novoX <= 0) {
            dispararBorda(BORDA_OESTE);
            return;
        }
        if (novoX >= LARGURA_TELA - larguraPlayer) {
            dispararBorda(BORDA_LESTE);
            return;
        }
        if (novoY <= 0) {
            dispararBorda(BORDA_NORTE);
            return;
        }
        if (novoY >= ALTURA_TELA - alturaPlayer) {
            dispararBorda(BORDA_SUL);
            return;
        }

        // Aplica nova posição ao sprite e à hitbox
        playerView.setLayoutX(novoX);
        playerView.setLayoutY(novoY);
        playerHitbox.setX(novoX + playerHitboxOffsetX);
        playerHitbox.setY(novoY + playerHitboxOffsetY);

        if (colidindoComHitbox()) {
            playerView.setLayoutX(x);
            playerView.setLayoutY(y);
            playerHitbox.setX(x + playerHitboxOffsetX);
            playerHitbox.setY(y + playerHitboxOffsetY);
        }
    }

    // ── Disparo de evento de borda ────────────────────────────────────────────

    private void dispararBorda(String borda) {
        if (onBordaAtingida != null)
            onBordaAtingida.accept(borda);
    }

    // ── Colisão entre hitboxes ────────────────────────────────────────────────

    private boolean colidindoComHitbox() {
        var playerBounds = playerHitbox.getBoundsInParent();

        boolean colideElemento = elementHitboxes.stream()
                .anyMatch(h -> playerBounds.intersects(h.getBoundsInParent()));

        boolean colideNPC = npcHitboxes.stream()
                .anyMatch(h -> playerBounds.intersects(h.getBoundsInParent()));

        return colideElemento || colideNPC;
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

    // ── Troca de GIF conforme direção ─────────────────────────────────────────

    private String gifAtual = GIF_SOUTH;

    private void trocarGif(String caminho) {
        if (gifAtual.equals(caminho)) return;

        var stream = getClass().getResourceAsStream(caminho);
        if (stream == null) return;

        gifAtual = caminho;
        playerView.setImage(new Image(stream));
    }

    // ── Parar o loop ──────────────────────────────────────────────────────────

    public void parar() {
        if (gameLoop != null)
            gameLoop.stop();
    }
}
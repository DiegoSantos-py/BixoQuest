package view.cena;

import controller.PersonagemController;
import javafx.animation.AnimationTimer;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import view.util.Borda;

import java.util.List;
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


    private AnimationTimer gameLoop;
    private SistemaMovimento sistemaMovimento;
    private SistemaColisao sistemaColisao;
    private GerenciadorEntrada gerenciadorEntrada;

    private StackPane raizCena;
    private GerenciadorMenus gerenciadorMenus;

    private final PersonagemController personagemController;
    private final int personagemId;
    private final Runnable onSairParaMenuPrincipal;

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
                    PersonagemController personagemController,
                    int personagemId,
                    Runnable onSairParaMenuPrincipal) {
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
        this.personagemController = personagemController;
        this.personagemId         = personagemId;
        this.onSairParaMenuPrincipal = onSairParaMenuPrincipal;
    }

    public Pane buildPane() {
        Pane conteudoJogo = new Pane();

        if (background != null)
            conteudoJogo.getChildren().add(background);

        conteudoJogo.getChildren().addAll(elements);
        conteudoJogo.getChildren().addAll(elementHitboxes);

        zones.forEach(z -> {
            if (z.view() != null) conteudoJogo.getChildren().add(z.view());
            conteudoJogo.getChildren().add(z.hitbox());
        });

        conteudoJogo.getChildren().addAll(npcs);
        conteudoJogo.getChildren().addAll(npcHitboxes);

        if (playerView != null) {
            conteudoJogo.getChildren().add(playerView);
            conteudoJogo.getChildren().add(playerHitbox);
        }

        raizCena = new StackPane(conteudoJogo);

        gerenciadorEntrada = new GerenciadorEntrada(
                this::abrirMenuAtributos,
                this::alternarMenuPause);
        gerenciadorEntrada.configurar(raizCena); // foco/teclado no StackPane

        inicializarMenus();
        iniciarGameLoop();

        return raizCena; // retorna o StackPane
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

    private void inicializarMenus() {
        gerenciadorMenus = new GerenciadorMenus(
                raizCena,
                personagemController,
                personagemId,
                onSairParaMenuPrincipal,
                pausado -> {
                    if (pausado) {
                        if (gameLoop != null) gameLoop.stop();
                    } else {
                        if (gameLoop != null) gameLoop.start();
                    }
                }
        );
    }

    private void alternarMenuPause() {
        if (gerenciadorMenus.atributosAberto()) {
            gerenciadorMenus.fecharAtributos();
            return;
        }
        if (gerenciadorMenus.pauseAberto()) {
            gerenciadorMenus.fecharPause();
        } else {
            gerenciadorMenus.abrirPause();
        }
    }

    // no listener de ESC ou botão de menu:
    private void abrirMenuPause() {
        gerenciadorMenus.abrirPause();
    }

    private void abrirMenuAtributos(){
        gerenciadorMenus.abrirAtributos();
    }

    public void parar() {
        if (gameLoop != null)
            gameLoop.stop();
    }
}
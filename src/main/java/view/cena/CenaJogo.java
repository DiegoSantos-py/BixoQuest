package view.cena;

import controller.GameController;
import controller.PersonagemController;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.Disciplina.Disciplina;
import view.util.Borda;
import view.util.FonteUtil;

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
    private Label labelTempoRestante;
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
    private final GameController gameController;
    private final int personagemId;
    private final Runnable onSairParaMenuPrincipal;
    private final Runnable onFinalizar;

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
                    Runnable onSairParaMenuPrincipal,
                    GameController gameController,
                    Runnable onFinalizar) {

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
        this.gameController = gameController;
        this.onFinalizar = onFinalizar;
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

        labelTempoRestante = new Label();
        labelTempoRestante.setFont(FonteUtil.pixel(32));
        labelTempoRestante.setTextFill(Color.WHITE);
        StackPane.setAlignment(labelTempoRestante, Pos.TOP_RIGHT);
        StackPane.setMargin(labelTempoRestante, new Insets(20));
        raizCena.getChildren().add(labelTempoRestante);

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

                atualizarLabelTempo();

                gameController.atualizar();

                if (gameController.precisaEscolherDisciplinas()) {
                    gameLoop.stop(); // para o loop; será retomado ao confirmar a escolha
                    abrirMenuEscolhaDisciplina();
                }
                else if (gameController.houveTransicaoDeDia()) {
                    gameLoop.stop();
                    onFinalizar.run();
                }
            }
        };
        gameLoop.start();
    }

    private void atualizarLabelTempo() {
        long segundosRestantes = gameController.getTempoRestanteSegundos();
        long minutos = segundosRestantes / 60;
        long segundos = segundosRestantes % 60;
        labelTempoRestante.setText(String.format("%02d:%02d", minutos, segundos));
    }

    private void abrirMenuEscolhaDisciplina() {
        List<Disciplina> opcoes = gameController.obterDisciplinasDisponiveis();
        gerenciadorMenus.abrirEscolhaDisciplina(opcoes);
    }

    private void confirmarEscolhaDisciplina(List<Disciplina> selecionadas) {
        boolean sucesso = gameController.confirmarEscolhaDisciplinas(selecionadas);
        if (sucesso) {
            gerenciadorMenus.fecharEscolhaDisciplina();
            gameLoop.start(); // retoma o loop só se a escolha foi válida
        }
        // se falhou, o menu permanece aberto — a própria MenuEscolhaDisciplina
        // deve exibir o erro (via GameController.exibirErro, já tratado no Controller)
    }

    private void inicializarMenus() {
        gerenciadorMenus = new GerenciadorMenus(
                raizCena,
                personagemController,
                gameController,
                personagemId,
                onSairParaMenuPrincipal,
                this::confirmarEscolhaDisciplina,
                pausado -> {
                    if (pausado) {
                        if (gameLoop != null) gameLoop.stop();
                        gameController.pausarDia();
                    } else {
                        if (gameLoop != null) gameLoop.start();
                        gameController.retomarDia();
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

    private void abrirMenuAtributos(){
        gerenciadorMenus.abrirAtributos();
    }

    public void parar() {
        if (gameLoop != null)
            gameLoop.stop();
    }
}
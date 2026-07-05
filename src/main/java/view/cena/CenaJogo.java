package view.cena;

import controller.GameController;
import controller.PersonagemController;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.Disciplina.Disciplina;
import model.Npc.Animal;
import model.Npc.Npc;
import view.util.Borda;
import view.util.FonteUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.util.Duration;
import model.Evento.Evento;

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
    private final List<Npc> npcObjetos;
    private final List<ZoneEntry> zones;
    private final ImageView playerView;
    private final Rectangle playerHitbox;
    private Label labelTempoRestante;
    private final double playerHitboxOffsetX;
    private final double playerHitboxOffsetY;
    private final Consumer<Borda> onBordaAtingida;
    private final String spriteBase;

    private StackPane containerFeedback;
    private Label toastAtual;

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
    private final Consumer<Npc> onDialogoFinalizado;

    public record ZoneEntry(String id, ImageView view, Rectangle hitbox, Consumer<String> onEnter) {}

    public CenaJogo(ImageView background,
                    List<ImageView> elements,
                    List<Rectangle> elementHitboxes,
                    List<ImageView> npcs,
                    List<Rectangle> npcHitboxes,
                    List<Npc> npcObjetos,
                    List<ZoneEntry> zones,
                    ImageView playerView,
                    Rectangle playerHitbox,
                    double playerHitboxOffsetX,
                    double playerHitboxOffsetY,
                    Consumer<Borda> onBordaAtingida,
                    String spriteBase,
                    PersonagemController personagemController,
                    int personagemId,
                    Runnable onSairParaMenuPrincipal,
                    GameController gameController,
                    Runnable onFinalizar,
                    Consumer<Npc> onDialogoFinalizado) {

        this.background          = background;
        this.elements            = elements;
        this.elementHitboxes     = elementHitboxes;
        this.npcs                = npcs;
        this.npcHitboxes         = npcHitboxes;
        this.npcObjetos          = npcObjetos;
        this.zones               = zones;
        this.playerView          = playerView;
        this.playerHitbox        = playerHitbox;
        this.playerHitboxOffsetX = playerHitboxOffsetX;
        this.playerHitboxOffsetY = playerHitboxOffsetY;
        this.onBordaAtingida     = onBordaAtingida;
        this.spriteBase          = spriteBase;
        this.personagemController = personagemController;
        this.personagemId         = personagemId;
        this.onSairParaMenuPrincipal = onSairParaMenuPrincipal;
        this.gameController = gameController;
        this.onFinalizar = onFinalizar;
        this.onDialogoFinalizado = onDialogoFinalizado;
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

        containerFeedback = new StackPane();
        containerFeedback.setMouseTransparent(true);
        StackPane.setAlignment(containerFeedback, Pos.TOP_CENTER);
        StackPane.setMargin(containerFeedback, new Insets(80, 0, 0, 0));
        raizCena.getChildren().add(containerFeedback);

        gerenciadorEntrada = new GerenciadorEntrada(
                this::abrirMenuAtributos,
                this::alternarMenuPause,
                this::tentarInteragirComNpc );
        gerenciadorEntrada.configurar(raizCena); // foco/teclado no StackPane

        inicializarMenus();
        iniciarGameLoop();

        raizCena.sceneProperty().addListener((obs, old, newScene) -> {
            if (newScene != null) {
                newScene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
                    if (e.getCode() == KeyCode.F9) {
                        gameController.debugForcarFimDeSemestre();
                    }
                });
            }
        });

        return raizCena; // retorna o StackPane
    }

    private void iniciarGameLoop() {
        if (playerView == null) return;

        sistemaMovimento = new SistemaMovimento(
                playerView, playerHitbox,
                playerHitboxOffsetX, playerHitboxOffsetY,
                elementHitboxes, List.of(), onBordaAtingida, spriteBase // npcHitboxes removido, lista vazia no lugar
        );

        List<SistemaColisao.NpcEntry> npcEntries = new ArrayList<>();
        for (int i = 0; i < npcObjetos.size(); i++) {
            npcEntries.add(new SistemaColisao.NpcEntry(npcObjetos.get(i), npcHitboxes.get(i)));
        }

        sistemaColisao = new SistemaColisao(playerHitbox, zones, npcEntries);

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                sistemaMovimento.atualizar(gerenciadorEntrada.getTeclasPressionadas());
                sistemaColisao.verificarZonas();
                sistemaColisao.atualizarProximidadeNpcs();

                Npc npcParaInteragir = sistemaColisao.verificarInteracaoNpc(gerenciadorEntrada.getTeclasPressionadas());

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
                spriteBase, // novo parâmetro
                onSairParaMenuPrincipal,
                this::confirmarEscolhaDisciplina,
                this::aoFinalizarDialogo,
                pausado -> {
                    if (pausado) {
                        if (gameLoop != null) gameLoop.stop();
                        gameController.pausarDia();
                        gerenciadorEntrada.limparTeclas(); // evita tecla presa ao pausar
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

    public void abrirFeedbackEvento(Evento evento) {
        gerenciadorMenus.abrirFeedbackEvento(evento);
    }

    public void exibirFeedbackMotivo(String motivo) {
        exibirToast(motivo, Color.web("#e74c3c"));
    }

    private void exibirToast(String texto, Color corFundo) {
        if (toastAtual != null) {
            containerFeedback.getChildren().remove(toastAtual);
        }

        Label label = new Label(texto);
        label.setFont(FonteUtil.pixel(20));
        label.setTextFill(Color.WHITE);
        label.setPadding(new Insets(12, 24, 12, 24));
        label.setBackground(new Background(new BackgroundFill(corFundo, new CornerRadii(8), Insets.EMPTY)));

        containerFeedback.getChildren().add(label);
        toastAtual = label;

        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), label);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        PauseTransition espera = new PauseTransition(Duration.seconds(2.5));

        FadeTransition fadeOut = new FadeTransition(Duration.millis(400), label);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> {
            containerFeedback.getChildren().remove(label);
            if (toastAtual == label) toastAtual = null;
        });

        new SequentialTransition(fadeIn, espera, fadeOut).play();
    }

    private void abrirDialogoNpc(Npc npc) {
        gerenciadorEntrada.travarMovimento(true); // novo método, ver abaixo
        gerenciadorMenus.abrirDialogo(npc);
    }

    private void tentarInteragirComNpc() {
        if (gerenciadorMenus.dialogoAberto()) return; // evita reabrir durante diálogo
        Npc npc = sistemaColisao.getNpcProximo();
        if (npc != null) {
            gerenciadorEntrada.travarMovimento(true);
            gerenciadorMenus.abrirDialogo(npc);
        }
    }

    private void aoFinalizarDialogo(Npc npc) {
        gerenciadorEntrada.travarMovimento(false);
        onDialogoFinalizado.accept(npc); // Consumer<Npc> vindo de ControllerTelas
    }
}
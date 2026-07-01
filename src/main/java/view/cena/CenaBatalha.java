package view.cena;

import controller.BatalhaController;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import view.batalha.*;

/**
 * Orquestrador da cena de batalha.
 * Responsabilidades: montar layout, game loop, transições de estado.
 * Toda lógica de UI específica é delegada aos componentes do pacote view.batalha.
 */
public class CenaBatalha extends StackPane {

    private final BatalhaController batalhaController;
    private final Runnable aoVoltar;

    // Componentes
    private final HUDBatalha hud;
    private final BattleRenderer renderer = new BattleRenderer();
    private final MinigameBatalha minigame;
    private final MenuPrincipalBatalha menuPrincipal;
    private final MenuAcoesBatalha menuAcoes;
    private final TelaFinalBatalha telaFinal;

    // Container dinâmico: único nó que troca de conteúdo a cada estado
    private final StackPane dynamicBox;

    // Game loop
    private AnimationTimer gameLoop;
    private long ultimoTempo = 0;

    // Estado da UI
    private enum EstadoUI {
        MENU_PRINCIPAL, MENU_ACOES, MINIGAME_ATAQUE, INIMIGO_ATACANDO, FINALIZADA, FEEDBACK_ACAO
    }
    private EstadoUI estadoUI = null;

    // Rastreamento de mudanças
    private model.Batalha.Turno turnoAnterior = null;
    private model.Batalha.Oponente oponenteAnterior = null;
    private float baseOpX = -1;
    private float baseOpY = -1;

    // Cache dos máximos (para quando o oponente some após derrota)
    private float maxHpOponente;
    private int maxTurnosOponente;

    // Controle de fluxo de ação
    private boolean bloquearTransicaoTurno = false;
    private String textoFeedback = "";

    // Arena persistente para renderização do ataque inimigo
    private Pane arenaPane;

    public CenaBatalha(BatalhaController batalhaController, Runnable aoVoltar) {
        this.batalhaController = batalhaController;
        this.aoVoltar = aoVoltar;

        hud           = new HUDBatalha(batalhaController);
        minigame      = new MinigameBatalha(batalhaController);
        menuPrincipal = new MenuPrincipalBatalha(batalhaController);
        menuAcoes     = new MenuAcoesBatalha(batalhaController);
        telaFinal     = new TelaFinalBatalha(batalhaController);

        dynamicBox = new StackPane();
        dynamicBox.setMaxWidth(1000);
        dynamicBox.setPrefHeight(250);
        dynamicBox.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        montarLayout();
        configurarControles();
        atualizarDadosIniciais();
        iniciarLoop();
        batalhaController.iniciarAudio();

        maxHpOponente     = batalhaController.getEstadoAtual().getOponenteAtual().getHpMaximo();
        maxTurnosOponente = batalhaController.getEstadoAtual().getOponenteAtual().getMaxTurnos();
    }

    // ─── LAYOUT ────────────────────────────────────────────────────────────────

    private void montarLayout() {
        this.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        this.setPadding(new Insets(40));

        VBox layoutPrincipal = hud.getLayoutSection();
        layoutPrincipal.getChildren().add(dynamicBox);

        StackPane.setAlignment(hud.getHudShields(),    javafx.geometry.Pos.BOTTOM_RIGHT);
        StackPane.setAlignment(hud.getDebugText(),     javafx.geometry.Pos.BOTTOM_LEFT);
        StackPane.setMargin(hud.getDebugText(), new Insets(20));
        StackPane.setAlignment(hud.getSpriteInimigo(), javafx.geometry.Pos.TOP_CENTER);

        this.getChildren().addAll(layoutPrincipal, hud.getHudShields(), hud.getDebugText(), hud.getSpriteInimigo());
    }

    // ─── CONTROLES ─────────────────────────────────────────────────────────────

    private void configurarControles() {
        this.sceneProperty().addListener((obs, old, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(e -> {
                    if (e.getCode() == javafx.scene.input.KeyCode.F2) {
                        boolean novo = !renderer.isRenderHitbox();
                        renderer.setRenderHitbox(novo);
                        hud.getDebugText().setText("HITBOX DEBUG: " + (novo ? "ON" : "OFF"));
                        hud.getDebugText().setVisible(true);
                        return;
                    }
                    batalhaController.onTeclaPressionada(e.getCode());
                });
                newScene.setOnKeyReleased(e -> batalhaController.onTeclaLiberada(e.getCode()));
            }
        });
    }

    // ─── GAME LOOP ─────────────────────────────────────────────────────────────

    private void iniciarLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long agora) {
                if (ultimoTempo == 0) { ultimoTempo = agora; return; }
                float dt = (agora - ultimoTempo) / 1_000_000_000f;
                ultimoTempo = agora;
                if (dt > 0.05f) dt = 0.05f;

                try {
                    if (!bloquearTransicaoTurno) batalhaController.atualizar(dt);
                    atualizarUI(dt);
                } catch (Exception ex) {
                    System.out.println("CRASH NO GAME LOOP:");
                    ex.printStackTrace();
                }
            }
        };
        gameLoop.start();
    }

    // ─── UPDATE FRAME ──────────────────────────────────────────────────────────

    private void atualizarUI(float dt) {
        if (batalhaController.getEstadoAtual() == null) return;

        if (batalhaController.getEstadoAtual().isFinalizado()) {
            hud.atualizarTextosHUD(maxHpOponente, maxTurnosOponente);
            if (estadoUI != EstadoUI.FINALIZADA) mudarEstadoUI(EstadoUI.FINALIZADA);
            return;
        }

        model.Batalha.Turno turnoAtual         = batalhaController.getEstadoAtual().getTurnoAtual();
        model.Batalha.Oponente oponenteAtual   = batalhaController.getEstadoAtual().getOponenteAtual();

        if (oponenteAtual != oponenteAnterior) {
            oponenteAnterior = oponenteAtual;
            atualizarDadosIniciais();
            if (turnoAtual == model.Batalha.Turno.TURNO_PLAYER) mudarEstadoUI(EstadoUI.MENU_PRINCIPAL);
        }

        if (turnoAtual != turnoAnterior && !bloquearTransicaoTurno) {
            turnoAnterior = turnoAtual;
            if (turnoAtual == model.Batalha.Turno.TURNO_PLAYER) mudarEstadoUI(EstadoUI.MENU_PRINCIPAL);
            else mudarEstadoUI(EstadoUI.INIMIGO_ATACANDO);
        }

        if (estadoUI == EstadoUI.MINIGAME_ATAQUE) minigame.atualizar(dt);

        // Sincroniza posição do sprite do oponente com a posição física
        if (oponenteAtual != null && baseOpX != -1) {
            hud.getSpriteInimigo().setTranslateX(oponenteAtual.getX() - baseOpX);
            hud.getSpriteInimigo().setTranslateY(oponenteAtual.getY() - baseOpY);
        }

        hud.atualizarHUDShields();
        hud.atualizarTextosHUD(maxHpOponente, maxTurnosOponente);

        if (estadoUI == EstadoUI.INIMIGO_ATACANDO && arenaPane != null) {
            renderer.renderFrame(arenaPane, batalhaController.getEstadoAtual());
        }
    }

    private void atualizarDadosIniciais() {
        float[] base = hud.atualizarDadosIniciais();
        if (base != null) { baseOpX = base[0]; baseOpY = base[1]; }

        if (batalhaController.getEstadoAtual() != null
                && batalhaController.getEstadoAtual().getOponenteAtual() != null) {
            maxHpOponente     = batalhaController.getEstadoAtual().getOponenteAtual().getHpMaximo();
            maxTurnosOponente = batalhaController.getEstadoAtual().getOponenteAtual().getMaxTurnos();
        }
    }

    // ─── STATE MACHINE ─────────────────────────────────────────────────────────

    private void mudarEstadoUI(EstadoUI novoEstado) {
        this.estadoUI = novoEstado;
        dynamicBox.getChildren().clear();
        dynamicBox.setBorder(Border.EMPTY);
        dynamicBox.setPadding(Insets.EMPTY);

        switch (novoEstado) {
            case MENU_PRINCIPAL:
                dynamicBox.getChildren().add(
                        menuPrincipal.build(
                                () -> mudarEstadoUI(EstadoUI.MINIGAME_ATAQUE),
                                () -> mudarEstadoUI(EstadoUI.MENU_ACOES)));
                break;

            case MENU_ACOES:
                dynamicBox.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID,
                        CornerRadii.EMPTY, new BorderWidths(6))));
                dynamicBox.setPadding(new Insets(20));
                dynamicBox.getChildren().add(menuAcoes.build(feedbackText -> {
                    this.textoFeedback = feedbackText;
                    bloquearTransicaoTurno = true;
                    mudarEstadoUI(EstadoUI.FEEDBACK_ACAO);
                    PauseTransition p = new PauseTransition(Duration.seconds(2.0));
                    p.setOnFinished(ev -> bloquearTransicaoTurno = false);
                    p.play();
                }));
                break;

            case MINIGAME_ATAQUE:
                dynamicBox.getChildren().add(minigame.build());
                break;

            case INIMIGO_ATACANDO:
                dynamicBox.setBorder(Border.EMPTY);
                dynamicBox.setPadding(Insets.EMPTY);
                model.Ataque.Ataque atk = batalhaController.getEstadoAtual().getAtaqueAtual();
                float boxH = atk.getMaxY() - atk.getMinY();
                dynamicBox.setPrefHeight(boxH + 20);
                arenaPane = renderer.criarArena(atk);
                dynamicBox.getChildren().add(arenaPane);
                break;

            case FINALIZADA:
                batalhaController.pararAudio();
                Runnable sair = () -> {
                    parar();
                    batalhaController.finalizarBatalha();
                    if (aoVoltar != null) aoVoltar.run();
                };
                if (batalhaController.isBatalhaAnimal()) {
                    dynamicBox.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID,
                            CornerRadii.EMPTY, new BorderWidths(6))));
                    dynamicBox.setPadding(new Insets(20));
                    dynamicBox.getChildren().add(telaFinal.buildAnimal(sair));
                } else {
                    dynamicBox.setBorder(new Border(new BorderStroke(Color.web("#8A2BE2"),
                            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(4))));
                    dynamicBox.setPadding(new Insets(20));
                    dynamicBox.getChildren().add(telaFinal.buildProva(sair));
                }
                break;

            case FEEDBACK_ACAO:
                dynamicBox.getChildren().add(menuAcoes.buildFeedback(textoFeedback));
                break;
        }
    }

    // ─── CICLO DE VIDA ─────────────────────────────────────────────────────────

    public void parar() {
        if (gameLoop != null) gameLoop.stop();
        batalhaController.pararAudio();
    }
}

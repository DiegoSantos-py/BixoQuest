package view.cena;

import controller.BatalhaController;
import service.AudioService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.Player.AcaoBatalha;
import view.util.FonteUtil;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import javafx.animation.AnimationTimer;

public class CenaBatalha extends StackPane {

    private final BatalhaController batalhaController;

    // Elementos da UI
    private Text textNomeInimigo;
    private Text textDescricaoInimigo;
    private Text textTurno;
    private ImageView spriteInimigo;
    
    private StackPane barraProgressoBackground;
    private Rectangle barraProgressoFill;
    private Text textProgresso;

    private StackPane dynamicBox;
    private HBox hudShields;
    private Text textDialogo;
    private Pane arenaPane; // arena persistente do ataque inimigo

    private AnimationTimer gameLoop;
    private long ultimoTempo = 0;
    
    private enum UIState {
        MENU_PRINCIPAL,
        MENU_ACOES,
        MINIGAME_ATAQUE,
        INIMIGO_ATACANDO,
        FINALIZADA
    }
    private UIState estadoUI = null;
    private model.Batalha.Turno turnoAnterior = null;
    private int escudosAnteriores = -1;
    
    // Minigame variables
    private Rectangle minigameCursor;
    private double minigameCursorX = -350;
    private double minigameCursorDir = 1;

    private final AudioService audioService = new AudioService();

    public CenaBatalha(BatalhaController batalhaController) {
        this.batalhaController = batalhaController;
        montarTela();
        atualizarDadosIniciais();
        iniciarLoop();
        configurarControles();
        
        // Inicia a música de fundo
        audioService.playBGM("/assets/audio/musicaAnimal1.mp3");
    }

    private void configurarControles() {
        this.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(e -> {
                    if (batalhaController.getEstadoAtual() == null) return;
                    model.Player.PlayerProva p = batalhaController.getEstadoAtual().getPlayerProva();
                    if (p == null) return;
                    switch (e.getCode()) {
                        case W: p.setMovendoCima(true); break;
                        case S: p.setMovendoBaixo(true); break;
                        case A: p.setMovendoEsquerda(true); break;
                        case D: p.setMovendoDireita(true); break;
                        default: break;
                    }
                });
                newScene.setOnKeyReleased(e -> {
                    if (batalhaController.getEstadoAtual() == null) return;
                    model.Player.PlayerProva p = batalhaController.getEstadoAtual().getPlayerProva();
                    if (p == null) return;
                    switch (e.getCode()) {
                        case W: p.setMovendoCima(false); break;
                        case S: p.setMovendoBaixo(false); break;
                        case A: p.setMovendoEsquerda(false); break;
                        case D: p.setMovendoDireita(false); break;
                        default: break;
                    }
                });
            }
        });
    }

    private void iniciarLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long agora) {
                if (ultimoTempo == 0) {
                    ultimoTempo = agora;
                    return;
                }
                float dt = (agora - ultimoTempo) / 1_000_000_000f;
                ultimoTempo = agora;
                
                try {
                    batalhaController.atualizar(dt);
                    atualizarUI(dt);
                } catch (Exception ex) {
                    System.out.println("CRASH NO GAME LOOP:");
                    ex.printStackTrace();
                }
            }
        };
        gameLoop.start();
    }

    private void atualizarUI(float dt) {
        if (batalhaController.getEstadoAtual() == null) return;
        
        if (batalhaController.getEstadoAtual().isFinalizado()) {
            if (estadoUI != UIState.FINALIZADA) {
                mudarEstadoUI(UIState.FINALIZADA);
            }
            return;
        }

        model.Batalha.Turno turnoAtual = batalhaController.getEstadoAtual().getTurnoAtual();
        
        if (turnoAtual != turnoAnterior) {
            turnoAnterior = turnoAtual;
            if (turnoAtual == model.Batalha.Turno.TURNO_PLAYER) {
                mudarEstadoUI(UIState.MENU_PRINCIPAL);
            } else {
                mudarEstadoUI(UIState.INIMIGO_ATACANDO);
            }
        }
        
        if (estadoUI == UIState.MINIGAME_ATAQUE) {
            atualizarMinigame(dt);
        }
        
        atualizarHUDShields();
        atualizarTextosHUD();
        atualizarAtaqueInimigo();
    }

    private void atualizarTextosHUD() {
        if (batalhaController.getEstadoAtual() != null) {
            int turnos = batalhaController.getEstadoAtual().getPlayerProva().getTurnosUsados();
            textTurno.setText("TURNOS:\n" + turnos + "/15");
            
            if (batalhaController.getEstadoAtual().getOponenteAtual() != null) {
                float hpAtual = batalhaController.getEstadoAtual().getOponenteAtual().getHpAtual();
                float hpMax = batalhaController.getEstadoAtual().getOponenteAtual().getHpMaximo();
                if (hpMax > 0) {
                    int porcentagem = (int) ((1f - (hpAtual / hpMax)) * 100);
                    if (porcentagem < 0) porcentagem = 0;
                    if (porcentagem > 100) porcentagem = 100;
                    textProgresso.setText(porcentagem + "% DOMADO");
                    barraProgressoFill.setWidth((porcentagem / 100f) * 800f);
                }
            }
        }
    }

    // Flyweight Cache de Imagens para evitar instanciar Image repetidamente a cada frame
    private final Map<String, Image> imageCache = new HashMap<>();

    private ImageView criarSprite(model.Batalha.EntidadeBatalha entidade, float largura, float altura, float localX, float localY) {
        try {
            String sDir = entidade.getSpriteUrl();
            if (sDir == null || sDir.isEmpty()) return null;

            Image img = imageCache.get(sDir);
            if (img == null) {
                img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(sDir)));
                if (img.isError()) return null;
                imageCache.put(sDir, img);
            }

            ImageView iv = new ImageView(img);
            iv.setFitWidth(largura);
            iv.setFitHeight(altura);
            iv.setLayoutX(localX);
            iv.setLayoutY(localY);

            double graus = Math.toDegrees(entidade.getHitbox().getAnguloRotacao());
            
            // Compensar a orientação nativa da imagem do projétil (+90 graus para alinhar sprite que aponta pra cima)
            if (entidade instanceof model.Projetil.Projetil) {
                graus += 90;
            }

            if (graus != 0) {
                iv.setRotate(graus);
            }

            iv.setScaleX(2);
            iv.setScaleY(2);

            return iv;
        } catch (Exception e) {
            return null;
        }
    }

    private void atualizarAtaqueInimigo() {
        if (estadoUI != UIState.INIMIGO_ATACANDO || arenaPane == null) return;

        model.Batalha.EstadoBatalha estado = batalhaController.getEstadoAtual();
        if (estado == null) {
            System.out.println("DEBUG: estado == null");
            return;
        }
        if (estado.getAtaqueAtual() == null) {
            System.out.println("DEBUG: ataqueAtual == null");
            return;
        }

        model.Ataque.Ataque atk = estado.getAtaqueAtual();
        float minX = atk.getMinX();
        float minY = atk.getMinY();

        // Mantém fundo + bordas (primeiros 5 filhos), remove o resto
        while (arenaPane.getChildren().size() > 5) {
            arenaPane.getChildren().remove(5);
        }

        // Player
        model.Player.PlayerProva p = estado.getPlayerProva();
        float pW = p.getHitbox().getTamanho().getX(); // Hitbox real (ex: 5x5)
        float pH = p.getHitbox().getTamanho().getY();
        
        // Tamanho visual do player (maior que a hitbox para ficar visível)
        float visualW = 15f;
        float visualH = 15f;
        float spriteLX = p.getX() - minX - visualW / 2;
        float spriteLY = p.getY() - minY - visualH / 2;
        
        // Desenha o sprite do player com o tamanho visual
        ImageView pSprite = criarSprite(p, visualW, visualH, spriteLX, spriteLY);
        arenaPane.getChildren().add(pSprite != null ? pSprite : buildRect(spriteLX, spriteLY, visualW, visualH, Color.RED));
        
        // Desenha a hitbox do player exatamente onde ela fica fisicamente
        float hitboxLX = p.getX() - minX - pW / 2;
        float hitboxLY = p.getY() - minY - pH / 2;
        Rectangle pHb = buildRect(hitboxLX, hitboxLY, pW, pH, Color.TRANSPARENT);
        pHb.setStroke(Color.BLUE); pHb.setStrokeWidth(2);
        arenaPane.getChildren().add(pHb);
        
        //System.out.println("DEBUG Player Coords: physical(" + p.getX() + ", " + p.getY() + ") -> local(" + pLX + ", " + pLY + ")");

        // Projéteis
        for (model.Projetil.Projetil proj : estado.getAtaqueAtual().getProjeteis()) {
            float prW = proj.getHitbox().getTamanho().getX();
            float prH = proj.getHitbox().getTamanho().getY();
            float prLX = proj.getX() - minX - prW / 2;
            float prLY = proj.getY() - minY - prH / 2;

            ImageView prSprite = criarSprite(proj, prW, prH, prLX, prLY);
            arenaPane.getChildren().add(prSprite != null ? prSprite : buildRect(prLX, prLY, prW, prH, Color.WHITE));
            Rectangle prHb = buildRect(prLX, prLY, prW, prH, Color.TRANSPARENT);
            prHb.setStroke(Color.RED); prHb.setStrokeWidth(2);
            arenaPane.getChildren().add(prHb);
        }
    }

    private Rectangle buildRect(float x, float y, float w, float h, Color fill) {
        Rectangle r = new Rectangle(x, y, w, h);
        r.setFill(fill);
        return r;
    }

    private void mudarEstadoUI(UIState novoEstado) {
        this.estadoUI = novoEstado;
        dynamicBox.getChildren().clear();
        dynamicBox.setBorder(Border.EMPTY); // Reseta a borda
        dynamicBox.setPadding(Insets.EMPTY);

        switch (novoEstado) {
            case MENU_PRINCIPAL:
                carregarMenuPrincipal();
                break;
            case MENU_ACOES:
                dynamicBox.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(6))));
                dynamicBox.setPadding(new Insets(20));
                carregarMenuAcoes();
                break;
            case MINIGAME_ATAQUE:
                iniciarMinigame();
                break;
            case INIMIGO_ATACANDO:
                // Monta a arena diretamente no dynamicBox
                dynamicBox.setBorder(Border.EMPTY);
                dynamicBox.setPadding(Insets.EMPTY);
                dynamicBox.setPrefHeight(420);

                model.Ataque.Ataque atkUI = batalhaController.getEstadoAtual().getAtaqueAtual();
                float minXUI = atkUI.getMinX();
                float minYUI = atkUI.getMinY();
                float maxXUI = atkUI.getMaxX();
                float maxYUI = atkUI.getMaxY();

                float boxW = maxXUI - minXUI;
                float boxH = maxYUI - minYUI;
                
                dynamicBox.setPrefHeight(boxH + 20);
                float border = 6f;

                arenaPane = new Pane();
                arenaPane.setPrefSize(boxW, boxH);
                arenaPane.setMinSize(boxW, boxH);
                arenaPane.setMaxSize(boxW, boxH);

                Rectangle fundo = new Rectangle(0, 0, boxW, boxH);
                fundo.setFill(Color.BLACK);
                arenaPane.getChildren().add(fundo);

                for (Rectangle b : new Rectangle[]{
                        new Rectangle(0, 0, boxW, border),
                        new Rectangle(0, boxH - border, boxW, border),
                        new Rectangle(0, 0, border, boxH),
                        new Rectangle(boxW - border, 0, border, boxH)}) {
                    b.setFill(Color.WHITE);
                    arenaPane.getChildren().add(b);
                }

                dynamicBox.getChildren().add(arenaPane);
                break;
            case FINALIZADA:
                audioService.stopBGM();
                
                dynamicBox.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(6))));
                dynamicBox.setPadding(new Insets(20));
                
                boolean isVitoria = batalhaController.getEstadoAtual().isVitoria();
                
                VBox finalBox = new VBox(20);
                finalBox.setAlignment(Pos.CENTER);
                
                Text titleText = new Text("BATALHA FINALIZADA");
                titleText.setFont(FonteUtil.pixel(32));
                titleText.setFill(Color.WHITE);
                
                Text resultText = new Text(isVitoria ? "VOCÊ VENCEU!" : "VOCÊ PERDEU...");
                resultText.setFont(FonteUtil.pixel(24));
                resultText.setFill(isVitoria ? Color.YELLOW : Color.RED);
                
                finalBox.getChildren().addAll(titleText, resultText);
                dynamicBox.getChildren().add(finalBox);
                break;
        }
    }

    private void atualizarMinigame(float dt) {
        if (minigameCursor != null) {
            minigameCursorX += minigameCursorDir * 1200 * dt; // Velocidade do cursor
            if (minigameCursorX >= 350) {
                minigameCursorX = 350;
                minigameCursorDir = -1;
            } else if (minigameCursorX <= -350) {
                minigameCursorX = -350;
                minigameCursorDir = 1;
            }
            minigameCursor.setTranslateX(minigameCursorX);
        }
    }

    private void atualizarHUDShields() {
        if (batalhaController.getEstadoAtual() == null) return;
        
        int shieldsAtuais = batalhaController.getEstadoAtual().getPlayerProva().getShieldAtual();
        if (shieldsAtuais != escudosAnteriores) {
            escudosAnteriores = shieldsAtuais;
            
            hudShields.getChildren().clear();
            for(int i = 0; i < shieldsAtuais; i++) {
                ImageView shield = new ImageView();
                try {
                    shield.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/batalha/escudo.png"))));
                    shield.setFitWidth(40);
                    shield.setFitHeight(40);
                } catch (Exception e) {
                    Rectangle rect = new Rectangle(40, 40, Color.GOLD);
                    hudShields.getChildren().add(rect);
                    continue;
                }
                hudShields.getChildren().add(shield);
            }
        }
    }

    private void montarTela() {
        // Fundo Preto
        this.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        this.setPadding(new Insets(40));

        VBox layoutPrincipal = new VBox(30);
        layoutPrincipal.setAlignment(Pos.TOP_CENTER);

        // --- 1. CABEÇALHO (Top Section) ---
        BorderPane cabecalho = new BorderPane();
        
        // Esquerda (Nome e Descrição)
        VBox infoInimigo = new VBox(10);
        textNomeInimigo = new Text("NOME");
        textNomeInimigo.setFont(FonteUtil.pixel(40));
        textNomeInimigo.setFill(Color.WHITE);
        
        textDescricaoInimigo = new Text("ELE PARECE\nDESCONFIADO.");
        textDescricaoInimigo.setFont(FonteUtil.pixel(24));
        textDescricaoInimigo.setFill(Color.WHITE);
        textDescricaoInimigo.setTextAlignment(TextAlignment.LEFT);
        
        infoInimigo.getChildren().addAll(textNomeInimigo, textDescricaoInimigo);
        cabecalho.setLeft(infoInimigo);

        // Centro (Sprite)
        spriteInimigo = new ImageView();
        spriteInimigo.setFitWidth(300);
        spriteInimigo.setFitHeight(300);
        spriteInimigo.setPreserveRatio(true);
        cabecalho.setCenter(spriteInimigo);

        // Direita (Turnos)
        textTurno = new Text("TURNOS:\n1/15");
        textTurno.setFont(FonteUtil.pixel(32));
        textTurno.setFill(Color.WHITE);
        textTurno.setTextAlignment(TextAlignment.CENTER);
        cabecalho.setRight(textTurno);

        // --- 2. BARRA DE PROGRESSO (Middle Section) ---
        barraProgressoBackground = new StackPane();
        barraProgressoBackground.setMaxWidth(800);
        barraProgressoBackground.setPrefHeight(40);
        barraProgressoBackground.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        barraProgressoBackground.setBorder(new Border(new BorderStroke(Color.web("#FF69B4"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(4))));

        barraProgressoFill = new Rectangle(0, 40, Color.web("#FF69B4")); // Rosa
        StackPane.setAlignment(barraProgressoFill, Pos.CENTER_LEFT);

        textProgresso = new Text("0% DOMADO");
        textProgresso.setFont(FonteUtil.pixel(20));
        textProgresso.setFill(Color.BLACK);

        barraProgressoBackground.getChildren().addAll(barraProgressoFill, textProgresso);

        // --- 3. CAIXA DINÂMICA (Bottom Section) ---
        dynamicBox = new StackPane();
        dynamicBox.setMaxWidth(1000);
        dynamicBox.setPrefHeight(250);
        dynamicBox.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        
        // --- 4. HUD SHIELDS ---
        hudShields = new HBox(10);
        hudShields.setAlignment(Pos.BOTTOM_RIGHT);
        hudShields.setPickOnBounds(false);

        // --- MONTAGEM FINAL ---
        layoutPrincipal.getChildren().addAll(cabecalho, barraProgressoBackground, dynamicBox);
        
        StackPane.setAlignment(hudShields, Pos.BOTTOM_RIGHT);
        
        this.getChildren().addAll(layoutPrincipal, hudShields);
    }

    private void atualizarDadosIniciais() {
        if (batalhaController.getEstadoAtual() != null && batalhaController.getEstadoAtual().getOponenteAtual() != null) {
            textNomeInimigo.setText(batalhaController.getEstadoAtual().getOponenteAtual().getNome().toUpperCase());
            try {
                String spriteDir = batalhaController.getEstadoAtual().getOponenteAtual().getSpriteUrl();
                if(spriteDir != null && !spriteDir.isEmpty()) {
                    if(!spriteDir.startsWith("/")) spriteDir = "/" + spriteDir;
                    spriteInimigo.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(spriteDir))));
                }
            } catch (Exception e) {
                System.out.println("Não foi possível carregar o sprite do oponente.");
            }
        }
    }

    private void carregarMenuAcoes() {

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(40);
        grid.setVgap(20);

        List<AcaoBatalha> acoes = batalhaController.getAcoes();
        
        int col = 0;
        int row = 0;
        
        for (int i = 0; i < acoes.size(); i++) {
            AcaoBatalha acao = acoes.get(i);
            Button btn = criarBotaoTextoSimples(acao.getNome().toUpperCase());
            int indexFinal = i;
            btn.setOnAction(e -> {
                batalhaController.executarAcao(indexFinal);
                System.out.println("Ação executada: " + acao.getNome());
            });
            
            grid.add(btn, col, row);
            col++;
            if (col > 1) { // 2 colunas
                col = 0;
                row++;
            }
        }

        dynamicBox.getChildren().add(grid);
    }

    private void carregarMenuPrincipal() {
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);

        // Caixa de texto
        StackPane caixaTexto = new StackPane();
        caixaTexto.setPrefSize(1000, 150);
        caixaTexto.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(4))));
        
        if (textDialogo == null) {
            textDialogo = new Text("ELE PARECE DESCONFIADO.");
            textDialogo.setFill(Color.WHITE);
            textDialogo.setFont(FonteUtil.pixel(20));
        }
        caixaTexto.getChildren().add(textDialogo);

        // Botoes
        HBox botoesBox = new HBox(40);
        botoesBox.setAlignment(Pos.CENTER);
        
        Button btnDomar = criarBotaoFundoPretoBordaLaranja("TENTAR DOMAR");
        btnDomar.setOnAction(e -> mudarEstadoUI(UIState.MINIGAME_ATAQUE));
        
        Button btnAcoes = criarBotaoFundoPretoBordaLaranja("AÇÕES DISPONÍVEIS");
        btnAcoes.setOnAction(e -> mudarEstadoUI(UIState.MENU_ACOES));

        botoesBox.getChildren().addAll(btnDomar, btnAcoes);

        container.getChildren().addAll(caixaTexto, botoesBox);
        dynamicBox.getChildren().add(container);
    }

    private void iniciarMinigame() {
        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);

        Text titulo = new Text("TENTANDO DOMAR!");
        titulo.setFill(Color.WHITE);
        titulo.setFont(FonteUtil.pixel(20));

        StackPane minigameBox = new StackPane();
        minigameBox.setPrefSize(800, 150);
        minigameBox.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(4))));

        Rectangle bg = new Rectangle(790, 140, Color.web("#222222"));
        Rectangle greenZone = new Rectangle(100, 140, Color.LIMEGREEN);
        
        minigameCursor = new Rectangle(20, 140, Color.WHITE);
        minigameCursorX = -350;
        minigameCursorDir = 1;
        minigameCursor.setTranslateX(minigameCursorX);

        minigameBox.getChildren().addAll(bg, greenZone, minigameCursor);

        Text instrucao = new Text("CLIQUE NA BARRA PARA PARAR!");
        instrucao.setFill(Color.WHITE);
        instrucao.setFont(FonteUtil.pixel(16));
        
        minigameBox.setOnMouseClicked(e -> {
            if (estadoUI == UIState.MINIGAME_ATAQUE) {
                finalizarMinigame();
            }
        });

        container.getChildren().addAll(titulo, minigameBox, instrucao);
        dynamicBox.getChildren().add(container);
    }

    private void finalizarMinigame() {
        double dist = Math.abs(minigameCursorX);
        float precisao = 0f;
        if (dist <= 50) precisao = 1.5f;
        else if (dist <= 150) precisao = 1.0f;
        else if (dist <= 250) precisao = 0.5f;
        else precisao = 0.0f;

        batalhaController.registrarAtaquePlayer(precisao);
    }

    private Button criarBotaoFundoPretoBordaLaranja(String texto) {
        Button btn = new Button(texto);
        btn.setFont(FonteUtil.pixel(16));
        btn.setTextFill(Color.WHITE);
        btn.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        btn.setBorder(new Border(new BorderStroke(Color.web("#FFA500"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(4))));
        btn.setPrefSize(200, 60);
        btn.setOnMouseEntered(e -> btn.setBackground(new Background(new BackgroundFill(Color.web("#333333"), CornerRadii.EMPTY, Insets.EMPTY))));
        btn.setOnMouseExited(e -> btn.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY))));
        return btn;
    }

    private Button criarBotaoTextoSimples(String texto) {
        Button btn = new Button(texto);
        btn.setFont(FonteUtil.pixel(20));
        btn.setTextFill(Color.WHITE);
        btn.setBackground(Background.EMPTY);
        btn.setBorder(Border.EMPTY);
        
        btn.setOnMouseEntered(e -> btn.setTextFill(Color.web("#FF8C00"))); // Laranja no hover
        btn.setOnMouseExited(e -> btn.setTextFill(Color.WHITE));
        
        return btn;
    }
}

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
    private Text textHpInimigo;

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
    private model.Batalha.Oponente oponenteAnterior = null;
    
    // Minigame variables
    private Rectangle minigameCursor;
    private double minigameCursorX = -350;
    private double minigameCursorDir = 1;

    private boolean renderHitbox = false;
    private Text textDebugHitbox;

    private final AudioService audioService = new AudioService();
    private final Runnable aoVoltar;

    public CenaBatalha(BatalhaController batalhaController, Runnable aoVoltar) {
        this.batalhaController = batalhaController;
        this.aoVoltar = aoVoltar;
        montarTela();
        atualizarDadosIniciais();
        iniciarLoop();
        configurarControles();
        
        // Inicia a música de fundo
        if (batalhaController.getEstadoAtual() != null) {
            String musica = batalhaController.getEstadoAtual().getMusicaDir();
            if (musica != null && !musica.isEmpty()) {
                audioService.playBGM(musica);
            }
        }
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
                        case F2: 
                            renderHitbox = !renderHitbox;
                            textDebugHitbox.setText("HITBOX DEBUG: " + (renderHitbox ? "ON" : "OFF"));
                            textDebugHitbox.setVisible(true);
                            break;
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
                
                // Evita o "pulo" gigantesco ou aceleração quando o jogo minimiza ou dá lag
                if (dt > 0.05f) {
                    dt = 0.05f;
                }
                
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
        model.Batalha.Oponente oponenteAtual = batalhaController.getEstadoAtual().getOponenteAtual();

        if (oponenteAtual != oponenteAnterior) {
            oponenteAnterior = oponenteAtual;
            atualizarDadosIniciais();
            if (turnoAtual == model.Batalha.Turno.TURNO_PLAYER) {
                mudarEstadoUI(UIState.MENU_PRINCIPAL);
            }
        }

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
            int maxTurnos = 15;
            if (batalhaController.getEstadoAtual().getOponenteAtual() != null) {
                maxTurnos = batalhaController.getEstadoAtual().getOponenteAtual().getMaxTurnos();
            }

            if (batalhaController.isBatalhaAnimal()) {
                int turnos = batalhaController.getEstadoAtual().getPlayerProva().getTurnosUsados();
                textTurno.setText("TURNOS:\n" + turnos + "/" + maxTurnos);
            } else {
                float nota = batalhaController.getEstadoAtual().getPlayerProva().getDesempenhoQuestaoAtual();
                int turnos = batalhaController.getEstadoAtual().getPlayerProva().getTurnosUsados();
                textTurno.setText(String.format("NOTA: %.1f\nTURNOS: %d/%d", nota, turnos, maxTurnos));
            }
            
            if (batalhaController.getEstadoAtual().getOponenteAtual() != null) {
                float hpAtual = batalhaController.getEstadoAtual().getOponenteAtual().getHpAtual();
                float hpMax = batalhaController.getEstadoAtual().getOponenteAtual().getHpMaximo();
                if (hpMax > 0) {
                    int porcentagem = (int) ((1f - (hpAtual / hpMax)) * 100);
                    if (porcentagem < 0) porcentagem = 0;
                    if (porcentagem > 100) porcentagem = 100;
                    
                    if (textHpInimigo != null) {
                        textHpInimigo.setText(String.format("HP: %.0f/%.0f", hpAtual, hpMax));
                    }
                    
                    String label = batalhaController.isBatalhaAnimal() ? "% DOMADO" : "% CONCLUÍDO";
                    textProgresso.setText(porcentagem + label);
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
            // Ignorar para o arranhão, pois ele já tem as dimensões físicas em sincronia com o sprite visual
            if (entidade instanceof model.Projetil.Projetil && !sDir.contains("arranhao")) {
                graus += 90;
            }

            if (graus != 0) {
                iv.setRotate(graus);
            }

            iv.setScaleX(2);
            iv.setScaleY(2);

            return iv;
        } catch (Exception e) {
            try {
                Image fallback = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/batalha/oponentes/animais/default.png")));
                ImageView iv = new ImageView(fallback);
                iv.setFitWidth(largura);
                iv.setFitHeight(altura);
                iv.setLayoutX(localX);
                iv.setLayoutY(localY);
                return iv;
            } catch (Exception ex) {
                return null;
            }
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

        while (arenaPane.getChildren().size() > 5) {
            arenaPane.getChildren().remove(5);
        }

        // Player
        model.Player.PlayerProva p = estado.getPlayerProva();
        float pW = p.getHitbox().getTamanho().getX();
        float pH = p.getHitbox().getTamanho().getY();
        
        float visualW = 15f;
        float visualH = 15f;
        float spriteLX = p.getX() - minX - visualW / 2;
        float spriteLY = p.getY() - minY - visualH / 2;
        
        // Desenha o sprite do player com o tamanho visual
        ImageView pSprite = criarSprite(p, visualW, visualH, spriteLX, spriteLY);
        arenaPane.getChildren().add(pSprite != null ? pSprite : buildRect(spriteLX, spriteLY, visualW, visualH, Color.RED));
        
        // Desenha a hitbox do player exatamente onde ela fica fisicamente
        if (renderHitbox) {
            float hitboxLX = p.getX() - minX - pW / 2;
            float hitboxLY = p.getY() - minY - pH / 2;
            Rectangle pHb = buildRect(hitboxLX, hitboxLY, pW, pH, Color.TRANSPARENT);
            pHb.setStroke(Color.BLUE); pHb.setStrokeWidth(2);
            arenaPane.getChildren().add(pHb);
        }
        
        //System.out.println("DEBUG Player Coords: physical(" + p.getX() + ", " + p.getY() + ") -> local(" + pLX + ", " + pLY + ")");

        // Projéteis
        for (model.Projetil.Projetil proj : estado.getAtaqueAtual().getProjeteis()) {
            float prW = proj.getHitbox().getTamanho().getX();
            float prH = proj.getHitbox().getTamanho().getY();
            float prLX = proj.getX() - minX - prW / 2;
            float prLY = proj.getY() - minY - prH / 2;

            ImageView prSprite = criarSprite(proj, prW, prH, prLX, prLY);
            arenaPane.getChildren().add(prSprite != null ? prSprite : buildRect(prLX, prLY, prW, prH, Color.WHITE));
            
            if (renderHitbox) {
                Rectangle prHb = buildRect(prLX, prLY, prW, prH, Color.TRANSPARENT);
                prHb.setStroke(Color.RED); prHb.setStrokeWidth(2);
                
                double grausHitbox = Math.toDegrees(proj.getHitbox().getAnguloRotacao());
                
                if (!proj.getSpriteUrl().contains("arranhao")) {
                    grausHitbox += 90;
                }
                
                if (grausHitbox != 0) {
                    prHb.setRotate(grausHitbox);
                }
                
                arenaPane.getChildren().add(prHb);
            }
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
                if (batalhaController.isBatalhaAnimal()) {
                    montarTelaFinalizadaAnimal();
                } else {
                    montarTelaFinalizadaProva();
                }
                break;
        }
    }

    private void montarTelaFinalizadaAnimal() {
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
        
        Button btnVoltar = criarBotaoFundoPretoBordaLaranja("VOLTAR");
        btnVoltar.setOnAction(e -> {
            parar();
            batalhaController.finalizarBatalha();
            if (aoVoltar != null) aoVoltar.run();
        });
        
        finalBox.getChildren().addAll(titleText, resultText, btnVoltar);
        dynamicBox.getChildren().add(finalBox);
    }

    private void montarTelaFinalizadaProva() {
        dynamicBox.setBorder(new Border(new BorderStroke(Color.web("#8A2BE2"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(4))));
        dynamicBox.setPadding(new Insets(20));
        
        boolean isVitoria = batalhaController.getEstadoAtual().isVitoria();
        model.Player.PlayerProva pp = batalhaController.getEstadoAtual().getPlayerProva();
        
        BorderPane layout = new BorderPane();
        
        // --- TOP ---
        VBox topLeft = new VBox(10);
        Text t1 = new Text(isVitoria ? "PROVA CONCLUÍDA" : "PROVA FALHOU");
        t1.setFont(FonteUtil.pixel(32)); t1.setFill(Color.WHITE);
        Text t2 = new Text(isVitoria ? "\"FOI UM SUCESSO!\"" : "\"VOCÊ FALHOU!\"");
        t2.setFont(FonteUtil.pixel(20)); t2.setFill(Color.WHITE);
        topLeft.getChildren().addAll(t1, t2);
        
        int turnos = pp.getTurnosUsados();
        int maxTurnos = 30;
        if (batalhaController.getEstadoAtual().getOponenteAtual() != null) {
            maxTurnos = batalhaController.getEstadoAtual().getOponenteAtual().getMaxTurnos();
        }
        Text t3 = new Text("TURNOS:\n" + turnos + "/" + maxTurnos);
        t3.setFont(FonteUtil.pixel(24)); t3.setFill(Color.WHITE); t3.setTextAlignment(TextAlignment.CENTER);
        VBox topRight = new VBox(t3);
        topRight.setAlignment(Pos.TOP_RIGHT);
        
        BorderPane top = new BorderPane();
        top.setLeft(topLeft);
        top.setRight(topRight);
        layout.setTop(top);
        
        // --- CENTER ---
        HBox center = new HBox(50);
        center.setAlignment(Pos.CENTER);
        center.setPadding(new Insets(20, 0, 20, 0));
        
        VBox col1 = new VBox(10);
        Text c1t1 = new Text("DESEMPENHO GERAL:");
        c1t1.setFont(FonteUtil.pixel(16)); c1t1.setFill(Color.WHITE);
        col1.getChildren().add(c1t1);
        
        java.util.ArrayList<Float> desempenhos = pp.getDesempenhoQuestoes();
        for (int i = 0; i < desempenhos.size(); i++) {
            float notaQ = desempenhos.get(i);
            Text qT = new Text(String.format("QUESTÃO %d: %.0f%%", (i+1), notaQ * 10f));
            qT.setFont(FonteUtil.pixel(16)); qT.setFill(Color.WHITE);
            col1.getChildren().add(qT);
        }
        
        VBox col2 = new VBox(10);
        col2.setAlignment(Pos.CENTER);
        Text c2t1 = new Text("BÔNUS:");
        c2t1.setFont(FonteUtil.pixel(16)); c2t1.setFill(Color.WHITE);
        col2.getChildren().add(c2t1);
        
        boolean perfeitos = pp.getTodosAcertosPerfeitos();
        boolean nPNota = !pp.getPerdeuNota();
        boolean nLDano = !pp.getLevouAlgumDano();
        boolean m20 = turnos <= 20;
        
        Text b1 = new Text("TODOS OS ATAQUES\nPERFEITOS: " + (perfeitos ? "+25%" : "0%"));
        b1.setFont(FonteUtil.pixel(12)); b1.setFill(Color.WHITE); b1.setTextAlignment(TextAlignment.CENTER);
        Text b2 = new Text("NÃO PERDEU NOTA: " + (nPNota ? "+25%" : "0%"));
        b2.setFont(FonteUtil.pixel(12)); b2.setFill(Color.WHITE); b2.setTextAlignment(TextAlignment.CENTER);
        Text b3 = new Text("NÃO LEVOU NENHUM\nDANO: " + (nLDano ? "+25%" : "0%"));
        b3.setFont(FonteUtil.pixel(12)); b3.setFill(Color.WHITE); b3.setTextAlignment(TextAlignment.CENTER);
        Text b4 = new Text("MENOS DE 20\nTURNOS: " + (m20 ? "+25%" : "0%"));
        b4.setFont(FonteUtil.pixel(12)); b4.setFill(Color.WHITE); b4.setTextAlignment(TextAlignment.CENTER);
        
        col2.getChildren().addAll(b1, b2, b3, b4);
        
        center.getChildren().addAll(col1, col2);
        layout.setCenter(center);
        
        // --- BOTTOM ---
        BorderPane bottom = new BorderPane();
        
        float nf = service.batalha.BatalhaFinalizacaoService.calcularNotaFinal(desempenhos, pp);
        
        Text notaText = new Text(String.format("NOTA FINAL: %.2f", nf));
        notaText.setFont(FonteUtil.pixel(24)); notaText.setFill(Color.WHITE);
        bottom.setLeft(notaText);
        BorderPane.setAlignment(notaText, Pos.CENTER_LEFT);
        
        Button btnCont = criarBotaoFundoPretoBordaLaranja("CONTINUAR");
        btnCont.setOnAction(e -> {
            parar();
            batalhaController.finalizarBatalha();
            if (aoVoltar != null) aoVoltar.run();
        });
        bottom.setRight(btnCont);
        BorderPane.setAlignment(btnCont, Pos.CENTER_RIGHT);
        
        layout.setBottom(bottom);
        dynamicBox.getChildren().add(layout);
    }

    private void atualizarMinigame(float dt) {
        if (minigameCursor != null) {
            minigameCursorX += minigameCursorDir * 600 * dt; // Velocidade do cursor
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
        int maxShields = batalhaController.getEstadoAtual().getPlayerProva().getShieldMaximo();
        
        if (shieldsAtuais != escudosAnteriores) {
            escudosAnteriores = shieldsAtuais;
            
            hudShields.getChildren().clear();
            for(int i = 0; i < maxShields; i++) {
                ImageView shield = new ImageView();
                try {
                    String imgPath = (i < shieldsAtuais) ? "/assets/batalha/player/PlayerProvaShield.png" : "/assets/batalha/player/PlayerProvaShieldQuebrado.png";
                    shield.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imgPath))));
                    shield.setFitWidth(40);
                    shield.setFitHeight(40);
                } catch (Exception e) {
                    Rectangle rect = new Rectangle(40, 40, (i < shieldsAtuais) ? Color.GOLD : Color.GRAY);
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
        infoInimigo.setPrefWidth(400); // Fixed width to balance header
        infoInimigo.setMaxWidth(400);
        
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
        VBox infoTurnoBox = new VBox();
        infoTurnoBox.setPrefWidth(400); // Same width as infoInimigo
        infoTurnoBox.setMaxWidth(400);
        infoTurnoBox.setAlignment(Pos.TOP_RIGHT);
        
        textTurno = new Text("TURNOS:\n1/15");
        textTurno.setFont(FonteUtil.pixel(32));
        textTurno.setFill(Color.WHITE);
        textTurno.setTextAlignment(TextAlignment.RIGHT);
        
        infoTurnoBox.getChildren().add(textTurno);
        cabecalho.setRight(infoTurnoBox);

        // --- 2. BARRA DE PROGRESSO (Middle Section) ---
        barraProgressoBackground = new StackPane();
        barraProgressoBackground.setMaxWidth(800);
        barraProgressoBackground.setPrefHeight(40);
        barraProgressoBackground.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        barraProgressoBackground.setBorder(new Border(new BorderStroke(Color.web("#FF69B4"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(4))));

        barraProgressoFill = new Rectangle(0, 40, Color.web("#FF69B4")); // Rosa
        StackPane.setAlignment(barraProgressoFill, Pos.CENTER_LEFT);

        textProgresso = new Text("0%");
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
        textHpInimigo = new Text("HP: -/-");
        textHpInimigo.setFont(FonteUtil.pixel(20));
        textHpInimigo.setFill(Color.WHITE);

        VBox barraContainer = new VBox(5);
        barraContainer.setAlignment(Pos.CENTER);
        barraContainer.getChildren().addAll(textHpInimigo, barraProgressoBackground);

        layoutPrincipal.getChildren().addAll(cabecalho, barraContainer, dynamicBox);
        
        StackPane.setAlignment(hudShields, Pos.BOTTOM_RIGHT);
        
        textDebugHitbox = new Text("HITBOX DEBUG: OFF");
        textDebugHitbox.setFont(FonteUtil.pixel(16));
        textDebugHitbox.setFill(Color.YELLOW);
        textDebugHitbox.setVisible(false); // Inicia oculto até apertar F2
        StackPane.setAlignment(textDebugHitbox, Pos.BOTTOM_LEFT);
        StackPane.setMargin(textDebugHitbox, new Insets(20));
        
        this.getChildren().addAll(layoutPrincipal, hudShields, textDebugHitbox);
    }

    private void atualizarDadosIniciais() {
        if (batalhaController.getEstadoAtual() != null && batalhaController.getEstadoAtual().getOponenteAtual() != null) {
            model.Batalha.Oponente op = batalhaController.getEstadoAtual().getOponenteAtual();
            textNomeInimigo.setText(op.getNome().toUpperCase());
            
            if (op.getDescricao() != null) {
                textDescricaoInimigo.setText(op.getDescricao().toUpperCase());
            }

            try {
                String spriteDir = op.getSpriteUrl();
                if(spriteDir != null && !spriteDir.isEmpty()) {
                    if(!spriteDir.startsWith("/")) spriteDir = "/" + spriteDir;
                    spriteInimigo.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(spriteDir))));
                }
            } catch (Exception e) {
                System.out.println("Não foi possível carregar o sprite do oponente. Usando fallback...");
                try {
                    spriteInimigo.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/batalha/oponentes/animais/default.png"))));
                } catch (Exception ex) {
                    System.out.println("Fallback também falhou!");
                }
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
        caixaTexto.setBorder(new Border(
                new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(4))));

        textDialogo = new Text();
        textDialogo.setFill(Color.WHITE);
        textDialogo.setFont(FonteUtil.pixel(20));

        if (batalhaController.getEstadoAtual() != null && batalhaController.getEstadoAtual().getOponenteAtual() != null
                && batalhaController.getEstadoAtual().getOponenteAtual().getTextoCaixa() != null) {
            textDialogo.setText(batalhaController.getEstadoAtual().getOponenteAtual().getTextoCaixa().toUpperCase());
        } else {
            textDialogo.setText(
                    batalhaController.isBatalhaAnimal() ? "ELE PARECE DESCONFIADO." : "A QUESTÃO PARECE DIFÍCIL.");
        }
        
        caixaTexto.getChildren().add(textDialogo);

        // Botoes
        HBox botoesBox = new HBox(40);
        botoesBox.setAlignment(Pos.CENTER);
        
        Button btnDomar = criarBotaoFundoPretoBordaLaranja(batalhaController.isBatalhaAnimal() ? "TENTAR DOMAR" : "RESPONDER");
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

        Text titulo = new Text(batalhaController.isBatalhaAnimal() ? "TENTANDO DOMAR!" : "RESPONDENDO!");
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

    public void parar() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
        audioService.stopBGM();
    }
}

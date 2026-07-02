package view.batalha;

import controller.BatalhaController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import view.util.FonteUtil;

import java.util.Objects;

import org.w3c.dom.css.RGBColor;

/**
 * Componente com estado: gerencia todos os elementos fixos do HUD
 * (cabeçalho, barra de HP, escudos, texto de debug).
 * Deve ser instanciado uma vez e mantido vivo.
 * Chame atualizarTextosHUD() e atualizarHUDShields() a cada frame.
 */
public class HUDBatalha {

    private final BatalhaController controller;

    // Textos do cabeçalho
    private Text textNomeInimigo;
    private Text textDescricaoInimigo;
    private Text textTurno;

    // Sprite do oponente (overlay flutuante)
    private ImageView spriteInimigo;

    // Barra de progresso
    private Rectangle barraProgressoFill;
    private Text textProgresso;
    private Text textHpInimigo;

    // Overlays
    private VBox playerHud;
    private HBox hudShields;
    private Rectangle barraNotaFill;
    private Text textPlayerName;
    private Text textPlayerCon;
    private Text textPlayerDano;
    private Text textNotaValor;
    private Text textDebugHitbox;

    // Seção retornada ao CenaBatalha
    private VBox layoutSection;

    private int escudosAnteriores = -1;

    public HUDBatalha(BatalhaController controller) {
        this.controller = controller;
        construir();
    }

    private void construir() {
        // --- Cabeçalho ---
        BorderPane cabecalho = new BorderPane();

        VBox infoInimigo = new VBox(10);
        infoInimigo.setPrefWidth(400);
        infoInimigo.setMaxWidth(400);

        textNomeInimigo = new Text("NOME");
        textNomeInimigo.setFont(FonteUtil.pixel(40));
        textNomeInimigo.setFill(Color.WHITE);

        textDescricaoInimigo = new Text("TEXTO PADRAO AQUI");
        textDescricaoInimigo.setFont(FonteUtil.pixel(24));
        textDescricaoInimigo.setFill(Color.WHITE);
        textDescricaoInimigo.setTextAlignment(TextAlignment.LEFT);

        infoInimigo.getChildren().addAll(textNomeInimigo, textDescricaoInimigo);
        cabecalho.setLeft(infoInimigo);

        // Placeholder central (mantém o BorderPane com altura para o sprite flutuante)
        Pane placeholder = new Pane();
        placeholder.setPrefSize(300, 300);
        cabecalho.setCenter(placeholder);

        // Sprite flutuante (overlay gerenciado por CenaBatalha)
        spriteInimigo = new ImageView();
        spriteInimigo.setFitWidth(300);
        spriteInimigo.setFitHeight(300);
        spriteInimigo.setPreserveRatio(true);

        // Turnos / Nota
        VBox infoTurnoBox = new VBox();
        infoTurnoBox.setPrefWidth(400);
        infoTurnoBox.setMaxWidth(400);
        infoTurnoBox.setAlignment(Pos.TOP_RIGHT);

        textTurno = new Text("TURNOS:\n1/15");
        textTurno.setFont(FonteUtil.pixel(32));
        textTurno.setFill(Color.WHITE);
        textTurno.setTextAlignment(TextAlignment.RIGHT);
        infoTurnoBox.getChildren().add(textTurno);
        cabecalho.setRight(infoTurnoBox);

        // --- Barra de progresso ---
        StackPane barraBackground = new StackPane();
        barraBackground.setMaxWidth(800);
        barraBackground.setPrefHeight(40);
        barraBackground.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        barraBackground.setBorder(new Border(new BorderStroke(Color.web("#FF69B4"),
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(4))));

        barraProgressoFill = new Rectangle(0, 40, Color.web("#FF69B4"));
        StackPane.setAlignment(barraProgressoFill, Pos.CENTER_LEFT);

        textProgresso = new Text("0%");
        textProgresso.setFont(FonteUtil.pixel(20));
        textProgresso.setFill(Color.BLACK);
        barraBackground.getChildren().addAll(barraProgressoFill, textProgresso);

        textHpInimigo = new Text("HP: -/-");
        textHpInimigo.setFont(FonteUtil.pixel(20));
        textHpInimigo.setFill(Color.WHITE);

        VBox barraContainer = new VBox(5);
        barraContainer.setAlignment(Pos.CENTER);
        barraContainer.getChildren().addAll(textHpInimigo, barraBackground);

        // --- Overlays ---
        playerHud = new VBox(5);
        playerHud.setAlignment(Pos.BOTTOM_CENTER);
        playerHud.setPickOnBounds(false);

        hudShields = new HBox(10);
        hudShields.setAlignment(Pos.CENTER);
        hudShields.setPickOnBounds(false);

        HBox notaUILayout = new HBox(15);
        notaUILayout.setAlignment(Pos.CENTER);
        notaUILayout.setPickOnBounds(false);

        HBox leftTextContainer = new HBox(10);
        leftTextContainer.setAlignment(Pos.CENTER_RIGHT);

        textPlayerName = new Text("NOME");
        textPlayerName.setFont(FonteUtil.pixel(16));
        textPlayerName.setFill(Color.WHITE);

        textPlayerCon = new Text("CON: 0");
        textPlayerCon.setFont(FonteUtil.pixel(16));
        textPlayerCon.setFill(Color.WHITE);

        Text divisor1 = new Text("|");
        divisor1.setFont(FonteUtil.pixel(16));
        divisor1.setFill(Color.WHITE);

        leftTextContainer.getChildren().addAll(textPlayerName, divisor1, textPlayerCon);

        StackPane barraNotaBackground = new StackPane();
        barraNotaBackground.setPrefSize(250, 20);
        barraNotaBackground.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        barraNotaBackground.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));

        barraNotaFill = new Rectangle(0, 20, Color.web("#32CD32"));
        StackPane.setAlignment(barraNotaFill, Pos.CENTER_LEFT);

        textNotaValor = new Text("10.0");
        textNotaValor.setFont(FonteUtil.pixel(14));
        textNotaValor.setFill(Color.WHITE);
        barraNotaBackground.getChildren().addAll(barraNotaFill, textNotaValor);

        textPlayerDano = new Text("DANO: 0");
        textPlayerDano.setFont(FonteUtil.pixel(16));
        textPlayerDano.setFill(Color.WHITE);

        Text divisor2 = new Text("|");
        divisor2.setFont(FonteUtil.pixel(16));
        divisor2.setFill(Color.WHITE);

        notaUILayout.getChildren().addAll(leftTextContainer, barraNotaBackground, divisor2, textPlayerDano);
        playerHud.getChildren().addAll(hudShields, notaUILayout);

        textDebugHitbox = new Text("HITBOX DEBUG: OFF");
        textDebugHitbox.setFont(FonteUtil.pixel(16));
        textDebugHitbox.setFill(Color.YELLOW);
        textDebugHitbox.setVisible(false);

        // Seção entregue ao CenaBatalha (sem dynamicBox — ele é adicionado depois)
        layoutSection = new VBox(30);
        layoutSection.setAlignment(Pos.TOP_CENTER);
        layoutSection.getChildren().addAll(cabecalho, barraContainer);
    }

    // ─── Getters para CenaBatalha montar o StackPane ──────────────────────────

    public VBox getLayoutSection()       { return layoutSection; }
    public VBox getPlayerHud()           { return playerHud; }
    public Text getDebugText()           { return textDebugHitbox; }
    public ImageView getSpriteInimigo()  { return spriteInimigo; }

    // ─── Inicialização de dados do oponente ───────────────────────────────────

    /**
     * Atualiza nome, descrição e sprite com o oponente atual.
     * @return float[]{baseX, baseY} para rastreamento do delta de posição, ou null se sem oponente.
     */
    public float[] atualizarDadosIniciais() {
        if (controller.getEstadoController() == null) return null;

        textPlayerName.setText(controller.getEstadoController().getPlayerNome().toUpperCase());
        
        float con = controller.getEstadoController().getPlayerConhecimento();
        textPlayerCon.setText(String.format("CON: %.1f", con).replace(",", "."));
        float dano = controller.getEstadoController().getPlayerDano();
        textPlayerDano.setText(String.format("DANO: %.1f", dano).replace(",", "."));

        String inimigoNome = controller.getEstadoController().getInimigoNome();
        if (inimigoNome == null || inimigoNome.isEmpty()) return null;

        textNomeInimigo.setText(inimigoNome.toUpperCase());
        
        String desc = controller.getEstadoController().getInimigoDescricao();
        if (desc != null) textDescricaoInimigo.setText(desc.toUpperCase());

        try {
            String dir = controller.getEstadoController().getInimigoSprite();
            if (dir != null && !dir.isEmpty()) {
                if (!dir.startsWith("/")) dir = "/" + dir;
                spriteInimigo.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(dir))));
            }
        } catch (Exception e) {
            try {
                spriteInimigo.setImage(new Image(Objects.requireNonNull(
                        getClass().getResourceAsStream("/assets/batalha/oponentes/animais/default.png"))));

            } catch (Exception ignored) {}
        }

        return new float[]{controller.getEstadoController().getInimigoX(), controller.getEstadoController().getInimigoY()};
    }

    // ─── Atualizações por frame ────────────────────────────────────────────────

    public void atualizarTextosHUD(float maxHpOponente, int maxTurnosOponente) {
        if (controller.getEstadoController() == null) return;

        int maxTurnos = maxTurnosOponente;
        if (controller.getEstadoController().getInimigoNome() != null && !controller.getEstadoController().getInimigoNome().isEmpty())
            maxTurnos = controller.getEstadoController().getInimigoMaxTurnos();

        int t = controller.getEstadoController().getPlayerTurnosUsados();
        textTurno.setText("TURNOS:\n" + t + "/" + maxTurnos);

        // Atualiza os textos do jogador dinamicamente (para refletir bônus de ações)
        float con = controller.getEstadoController().getPlayerConhecimento();
        textPlayerCon.setText(String.format("CON: %.1f", con).replace(",", "."));
        float dano = controller.getEstadoController().getPlayerDano();
        textPlayerDano.setText(String.format("DANO: %.1f", dano).replace(",", "."));

        // Atualiza a barra de nota (HP do jogador)
        float nota = controller.getEstadoController().getPlayerNota();
        float pctNota = Math.max(0f, Math.min(1f, nota / 10.0f));
        barraNotaFill.setWidth(pctNota * 250f);
        textNotaValor.setText(String.format("%.2f", nota).replace(",", "."));

        Color cor = Color.rgb((int)(255 * (1 - pctNota)), (int)(255 * pctNota), 0);
        barraNotaFill.setFill(cor);

        if (controller.getEstadoController().getInimigoNome() != null && !controller.getEstadoController().getInimigoNome().isEmpty()) {
            float hpAtual = controller.getEstadoController().getInimigoHpAtual();
            float hpMax   = controller.getEstadoController().getInimigoHpMaximo();
            if (hpMax > 0) {
                int pct = Math.max(0, Math.min(100, (int) ((1f - hpAtual / hpMax) * 100)));
                textHpInimigo.setText(String.format("HP: %.2f/%.2f", hpAtual, hpMax));
                String label = controller.isBatalhaAnimal() ? "% DOMADO" : "% CONCLUÍDO";
                textProgresso.setText(pct + label);
                barraProgressoFill.setWidth((pct / 100f) * 800f);
            }
        } else if (controller.getEstadoController().isFinalizado() && controller.getEstadoController().isVitoria()) {
            textHpInimigo.setText("HP: 0/" + (int) maxHpOponente);
            String label = controller.isBatalhaAnimal() ? "% DOMADO" : "% CONCLUÍDO";
            textProgresso.setText("100" + label);
            barraProgressoFill.setWidth(800f);
        }
    }

    public void atualizarHUDShields() {
        if (controller.getEstadoController() == null) return;

        int atual = controller.getEstadoController().getPlayerShieldAtual();
        int max   = controller.getEstadoController().getPlayerShieldMaximo();
        if (atual == escudosAnteriores) return;
        escudosAnteriores = atual;

        hudShields.getChildren().clear();
        for (int i = 0; i < max; i++) {
            ImageView iv = new ImageView();
            try {
                String path = (i < atual)
                        ? "/assets/batalha/player/PlayerProvaShield.png"
                        : "/assets/batalha/player/PlayerProvaShieldQuebrado.png";
                iv.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(path))));
                iv.setFitWidth(40);
                iv.setFitHeight(40);
                hudShields.getChildren().add(iv);
            } catch (Exception e) {
                hudShields.getChildren().add(new Rectangle(40, 40, i < atual ? Color.GOLD : Color.GRAY));
            }
        }
    }
}

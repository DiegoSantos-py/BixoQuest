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
    private HBox hudShields;
    private Text textDebugHitbox;

    // Seção retornada ao CenaBatalha
    private VBox layoutSection;

    // Estado dos escudos (evita rebuild desnecessário a cada frame)
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

        textDescricaoInimigo = new Text("ELE PARECE\nDESCONFIADO.");
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
        hudShields = new HBox(10);
        hudShields.setAlignment(Pos.BOTTOM_RIGHT);
        hudShields.setPickOnBounds(false);

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
    public HBox getHudShields()          { return hudShields; }
    public Text getDebugText()           { return textDebugHitbox; }
    public ImageView getSpriteInimigo()  { return spriteInimigo; }

    // ─── Inicialização de dados do oponente ───────────────────────────────────

    /**
     * Atualiza nome, descrição e sprite com o oponente atual.
     * @return float[]{baseX, baseY} para rastreamento do delta de posição, ou null se sem oponente.
     */
    public float[] atualizarDadosIniciais() {
        if (controller.getEstadoAtual() == null
                || controller.getEstadoAtual().getOponenteAtual() == null) return null;

        model.Batalha.Oponente op = controller.getEstadoAtual().getOponenteAtual();
        textNomeInimigo.setText(op.getNome().toUpperCase());
        if (op.getDescricao() != null) textDescricaoInimigo.setText(op.getDescricao().toUpperCase());

        try {
            String dir = op.getSpriteUrl();
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

        return new float[]{op.getX(), op.getY()};
    }

    // ─── Atualizações por frame ────────────────────────────────────────────────

    public void atualizarTextosHUD(float maxHpOponente, int maxTurnosOponente) {
        if (controller.getEstadoAtual() == null) return;

        int maxTurnos = maxTurnosOponente;
        if (controller.getEstadoAtual().getOponenteAtual() != null)
            maxTurnos = controller.getEstadoAtual().getOponenteAtual().getMaxTurnos();

        if (controller.isBatalhaAnimal()) {
            int t = controller.getEstadoAtual().getPlayerProva().getTurnosUsados();
            textTurno.setText("TURNOS:\n" + t + "/" + maxTurnos);
        } else {
            float nota = controller.getEstadoAtual().getPlayerProva().getDesempenhoQuestaoAtual();
            int t     = controller.getEstadoAtual().getPlayerProva().getTurnosUsados();
            textTurno.setText(String.format("NOTA: %.1f\nTURNOS: %d/%d", nota, t, maxTurnos));
        }

        if (controller.getEstadoAtual().getOponenteAtual() != null) {
            float hpAtual = controller.getEstadoAtual().getOponenteAtual().getHpAtual();
            float hpMax   = controller.getEstadoAtual().getOponenteAtual().getHpMaximo();
            if (hpMax > 0) {
                int pct = Math.max(0, Math.min(100, (int) ((1f - hpAtual / hpMax) * 100)));
                textHpInimigo.setText(String.format("HP: %.0f/%.0f", hpAtual, hpMax));
                String label = controller.isBatalhaAnimal() ? "% DOMADO" : "% CONCLUÍDO";
                textProgresso.setText(pct + label);
                barraProgressoFill.setWidth((pct / 100f) * 800f);
            }
        } else if (controller.getEstadoAtual().isFinalizado() && controller.getEstadoAtual().isVitoria()) {
            textHpInimigo.setText("HP: 0/" + (int) maxHpOponente);
            String label = controller.isBatalhaAnimal() ? "% DOMADO" : "% CONCLUÍDO";
            textProgresso.setText("100" + label);
            barraProgressoFill.setWidth(800f);
        }
    }

    public void atualizarHUDShields() {
        if (controller.getEstadoAtual() == null) return;

        int atual = controller.getEstadoAtual().getPlayerProva().getShieldAtual();
        int max   = controller.getEstadoAtual().getPlayerProva().getShieldMaximo();
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

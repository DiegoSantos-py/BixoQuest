package view.batalha;

import controller.BatalhaController;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import view.util.FonteUtil;

import java.util.Objects;

/**
 * Componente com estado: gerencia o cursor do minigame de precisão.
 * Chame atualizar(dt) a cada frame enquanto o estado for MINIGAME_ATAQUE.
 */
public class MinigameBatalha {

    private static final double CURSOR_MAX = 350.0;
    private static final double CURSOR_VELOCIDADE = 700.0;

    private final BatalhaController controller;

    private Rectangle cursor;
    private double cursorX = -CURSOR_MAX;
    private double cursorDir = 1;
    private Text feedbackText;

    public MinigameBatalha(BatalhaController controller) {
        this.controller = controller;
    }

    public Node build() {
        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);

        Text titulo = new Text(controller.isBatalhaAnimal() ? "TENTANDO DOMAR!" : "RESPONDENDO!");
        titulo.setFill(Color.WHITE);
        titulo.setFont(FonteUtil.pixel(20));

        StackPane minigameBox = new StackPane();
        minigameBox.setPrefSize(800, 150);
        minigameBox.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, new BorderWidths(4))));

        ImageView bgImage = null;
        try {
            Image img = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream("/assets/batalha/barraAtaque.png")));
            bgImage = new ImageView(img);
            bgImage.setFitWidth(790);
            bgImage.setFitHeight(140);
        } catch (Exception e) {
            System.err.println("Sprite da barra de ataque não encontrado.");
        }

        cursor = new Rectangle(8, 140, Color.WHITE);
        cursorX = -CURSOR_MAX;
        cursorDir = 1;
        cursor.setTranslateX(cursorX);

        if (bgImage != null) {
            minigameBox.getChildren().addAll(bgImage, cursor);
        } else {
            minigameBox.getChildren().addAll(new Rectangle(790, 140, Color.web("#222222")), cursor);
        }

        Text instrucao = new Text("CLIQUE NA BARRA PARA PARAR!");
        instrucao.setFill(Color.WHITE);
        instrucao.setFont(FonteUtil.pixel(16));

        feedbackText = new Text();
        feedbackText.setVisible(false);

        minigameBox.setOnMousePressed(e -> finalizar());

        container.getChildren().addAll(titulo, minigameBox, instrucao, feedbackText);
        return container;
    }

    public void atualizar(float dt) {
        if (cursor == null || cursorDir == 0)
            return;
        cursorX += cursorDir * CURSOR_VELOCIDADE * dt;
        if (cursorX >= CURSOR_MAX) {
            cursorX = CURSOR_MAX;
            cursorDir = -1;
        } else if (cursorX <= -CURSOR_MAX) {
            cursorX = -CURSOR_MAX;
            cursorDir = 1;
        }
        cursor.setTranslateX(cursorX);
    }

    private void finalizar() {
        if (cursorDir == 0)
            return; // evita duplo clique
        cursorDir = 0; // congela cursor visualmente

        final float precisao = controller.calcularPrecisao(Math.abs(cursorX));
        final float danoTexto = controller.getEstadoController().getPlayerDano() * precisao;
        feedbackText.setText(String.format("×(%.2f)" + "(%.2f) DE DANO!", precisao, danoTexto).replace(",", "."));
        feedbackText.setFont(FonteUtil.pixel(16));
        feedbackText.setFill(Color.WHITE);

        if (controller.isAtaqueSuperPerfeito(precisao)) {
            feedbackText.setText("***Ataque Super Perfeito!***\n(wow)\n" + feedbackText.getText());
            feedbackText.setFill(Color.RED);
            feedbackText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        } else if (controller.isAtaquePerfeito(precisao)) {
            feedbackText.setText("*Ataque Perfeito!*\n" + feedbackText.getText());
            feedbackText.setFill(Color.YELLOW);
            feedbackText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        }

        feedbackText.setVisible(true);

        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1));
        pause.setOnFinished(e -> {
            controller.registrarAtaquePlayer(precisao);
        });
        pause.play();
    }
}

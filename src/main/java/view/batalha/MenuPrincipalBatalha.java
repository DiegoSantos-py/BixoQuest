package view.batalha;

import controller.BatalhaController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import view.util.FonteUtil;

public class MenuPrincipalBatalha {

    private final BatalhaController controller;

    public MenuPrincipalBatalha(BatalhaController controller) {
        this.controller = controller;
    }

    public Node build(Runnable onMinigame, Runnable onAcoes) {
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);

        StackPane caixaTexto = new StackPane();
        caixaTexto.setPrefSize(1000, 150);
        caixaTexto.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, new BorderWidths(4))));

        Text textDialogo = new Text();
        textDialogo.setFill(Color.WHITE);
        textDialogo.setFont(FonteUtil.pixel(20));

        if (controller.getEstadoAtual() != null
                && controller.getEstadoAtual().getOponenteAtual() != null
                && controller.getEstadoAtual().getOponenteAtual().getTextoCaixa() != null) {
            textDialogo.setText(controller.getEstadoAtual().getOponenteAtual().getTextoCaixa().toUpperCase());
        } else {
            textDialogo.setText(controller.isBatalhaAnimal() ? "ELE PARECE DESCONFIADO." : "A QUESTÃO PARECE DIFÍCIL.");
        }
        caixaTexto.getChildren().add(textDialogo);

        HBox botoesBox = new HBox(40);
        botoesBox.setAlignment(Pos.CENTER);

        Button btnAcao = criarBotaoLaranja(controller.isBatalhaAnimal() ? "TENTAR DOMAR" : "RESPONDER");
        btnAcao.setOnAction(e -> onMinigame.run());

        Button btnAcoes = criarBotaoLaranja("AÇÕES DISPONÍVEIS");
        btnAcoes.setOnAction(e -> onAcoes.run());

        botoesBox.getChildren().addAll(btnAcao, btnAcoes);
        container.getChildren().addAll(caixaTexto, botoesBox);
        return container;
    }

    private Button criarBotaoLaranja(String texto) {
        Button btn = new Button(texto);
        btn.setFont(FonteUtil.pixel(16));
        btn.setTextFill(Color.WHITE);
        btn.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        btn.setBorder(new Border(new BorderStroke(Color.web("#FFA500"), BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, new BorderWidths(4))));
        btn.setPrefSize(200, 60);
        btn.setOnMouseEntered(e -> btn.setBackground(
                new Background(new BackgroundFill(Color.web("#333333"), CornerRadii.EMPTY, Insets.EMPTY))));
        btn.setOnMouseExited(e -> btn.setBackground(
                new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY))));
        return btn;
    }
}

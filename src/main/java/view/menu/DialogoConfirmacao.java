package view.menu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import view.util.FonteUtil;

import java.util.function.Consumer;

public class DialogoConfirmacao extends StackPane {

    private Runnable aoConfirmar;
    private Label mensagemLabel;

    public DialogoConfirmacao() {
        montarTela();
        setVisible(false);
        setManaged(false);
    }

    private void montarTela() {
        Region overlay = new Region();
        overlay.setBackground(new Background(new BackgroundFill(
                Color.rgb(0, 0, 0, 0.7), CornerRadii.EMPTY, Insets.EMPTY)));
        overlay.prefWidthProperty().bind(widthProperty());
        overlay.prefHeightProperty().bind(heightProperty());

        VBox caixa = new VBox(20);
        caixa.setAlignment(Pos.CENTER);
        caixa.setMaxSize(500, 250);
        caixa.setBackground(new Background(new BackgroundFill(
                Color.rgb(30, 30, 30), new CornerRadii(10), Insets.EMPTY)));
        caixa.setPadding(new Insets(30));

        mensagemLabel = new Label();
        mensagemLabel.setFont(FonteUtil.pixel(22));
        mensagemLabel.setTextFill(Color.WHITE);
        mensagemLabel.setWrapText(true);
        mensagemLabel.setMaxWidth(400);

        HBox botoes = new HBox(20);
        botoes.setAlignment(Pos.CENTER);

        Button btnConfirmar = new Button("Confirmar");
        btnConfirmar.setPrefSize(150, 50);
        btnConfirmar.setOnAction(e -> {
            Runnable acao = aoConfirmar;
            fechar();
            if (acao != null) acao.run();
        });

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setPrefSize(150, 50);
        btnCancelar.setOnAction(e -> fechar());

        botoes.getChildren().addAll(btnConfirmar, btnCancelar);
        caixa.getChildren().addAll(mensagemLabel, botoes);

        getChildren().addAll(overlay, caixa);
    }

    public void abrir(String mensagem, Runnable aoConfirmar) {
        this.mensagemLabel.setText(mensagem);
        this.aoConfirmar = aoConfirmar;
        setVisible(true);
        setManaged(true);
        toFront();
    }

    private void fechar() {
        setVisible(false);
        setManaged(false);
        aoConfirmar = null;
    }
}
package view;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import view.util.FonteUtil;

import java.util.Objects;

public class InicioDiaView extends StackPane {

    private final Runnable aoFinalizar;

    public InicioDiaView(Runnable aoFinalizar) {
        this.aoFinalizar = aoFinalizar;

        montarTela();
        iniciarTrocaAutomatica();
    }

    private void montarTela() {
        Image backgroundImage = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream("/assets/Background.png")
                )
        );

        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(1920);
        backgroundView.setFitHeight(1080);

        VBox inicioItens = new VBox();
        inicioItens.setAlignment(Pos.CENTER);

        Image bg = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream("/menuPersonagens/background_botao.png")
                )
        );


        ImageView bgView = new ImageView(bg);
        bgView.setFitWidth(600);
        bgView.setPreserveRatio(true);
        Text bg_texto = new Text("Dia 1");
        bg_texto.setFont(FonteUtil.pixel(40));
        bg_texto.setFill(Color.WHITE);

        StackPane bgComTexto = new StackPane(
                bgView,
                bg_texto
        );

        bgComTexto.setAlignment(Pos.CENTER);
        bg_texto.setTranslateY(-10);

        Text texto = new Text("Carregando...");
        texto.setFill(Color.WHITE);
        texto.setFont(FonteUtil.pixel(40));

        inicioItens.getChildren().addAll(bgComTexto);

        getChildren().addAll(backgroundView, inicioItens, texto);

        StackPane.setAlignment(texto, Pos.BOTTOM_CENTER);
        StackPane.setMargin(texto, new Insets(0, 0, 100, 0));
    }

    private void iniciarTrocaAutomatica() {
        PauseTransition espera = new PauseTransition(Duration.seconds(1.5));

        espera.setOnFinished(event -> {
            if (aoFinalizar != null) {
                aoFinalizar.run();
            }
        });

        espera.play();
    }
}
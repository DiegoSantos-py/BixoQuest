package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import view.util.FonteUtil;

import java.util.Objects;

public class InicioJogoView extends StackPane {

    private final Runnable aoMoverMouse;

    private boolean primeiroEvento = true;
    private boolean jaMudouTela = false;

    public InicioJogoView(Runnable aoMoverMouse) {
        this.aoMoverMouse = aoMoverMouse;

        montarTela();
    }

    private void montarTela() {
        setPickOnBounds(true);

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

        Image logoImage = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream("/menuPrincipal/BixoQuestLogo.png")
                )
        );

        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(600);
        logoView.setPreserveRatio(true);

        Text texto = new Text("Mova o mouse para continuar");
        texto.setFill(Color.WHITE);
        texto.setFont(FonteUtil.pixel(40));

        inicioItens.getChildren().addAll(logoView);

        getChildren().addAll(backgroundView, inicioItens, texto);

        StackPane.setAlignment(texto, Pos.BOTTOM_CENTER);
        StackPane.setMargin(texto, new Insets(0, 0, 100, 0));

        setOnMouseMoved(event -> {
            if (primeiroEvento) {
                primeiroEvento = false;
                return;
            }

            if (!jaMudouTela) {
                jaMudouTela = true;
                aoMoverMouse.run();
            }
        });
    }
}
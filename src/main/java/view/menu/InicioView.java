package view.menu;

import controller.PersonagemController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import view.menu.util.FonteUtil;

import java.util.Objects;

public class InicioView {

    private final PersonagemController personagemController;

    private boolean primeiroEvento = true;
    private boolean jaMudouTela = false;

    // Constructor Injection
    public InicioView(PersonagemController personagemController) {
        this.personagemController = personagemController;
    }

    public void exibir(Stage stage) {

        StackPane root = new StackPane();
        root.setPickOnBounds(true);

        Image backgroundImage = new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/assets/Background.png")
                )
        );

        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(1920);
        backgroundView.setFitHeight(1080);

        VBox inicioItens = new VBox();
        inicioItens.setAlignment(Pos.CENTER);

        Image logoImage = new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/menuPrincipal/BixoQuestLogo.png")
                )
        );


        Text texto = new Text("Mova o mouse para continuar");
        texto.setFill(Color.WHITE);
        texto.setFont(FonteUtil.pixel(40));

        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(600);
        logoView.setPreserveRatio(true);

        inicioItens.getChildren().addAll(logoView);

        root.getChildren().addAll(backgroundView, inicioItens, texto);
        StackPane.setAlignment(texto, Pos.BOTTOM_CENTER);
        StackPane.setMargin(texto, new Insets(0, 0, 100, 0));

        root.setOnMouseMoved(event -> {

            // Ignora o primeiro evento gerado ao abrir a tela
            if (primeiroEvento) {
                primeiroEvento = false;
                return;
            }

            if (!jaMudouTela) {
                jaMudouTela = true;

                Menu menu = new Menu(personagemController);
                menu.exibir(stage);
            }
        });

        Scene scene = new Scene(root, 1920, 1080);

        stage.setScene(scene);
        stage.show();
    }
}
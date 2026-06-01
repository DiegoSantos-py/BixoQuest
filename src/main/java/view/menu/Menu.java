package view.menu;

import controller.PersonagemController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Objects;

public class Menu {

    private final PersonagemController personagemController;

    private static final Font FONTE_PIXEL = Font.loadFont(
            Objects.requireNonNull(Menu.class.getResourceAsStream(
                            "/fonts/pixel_operator/PixelOperator-Bold.ttf")),
            24
    );

    // Constructor Injection
    public Menu(PersonagemController personagemController) {
        this.personagemController = personagemController;
    }

    public void exibir(Stage primaryStage) {

        StackPane root = new StackPane();

        Image bgImage = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream("/menuPrincipal/BackgroundMenu.png")
                )
        );

        ImageView backgroundView = new ImageView(bgImage);
        backgroundView.setFitWidth(1920);
        backgroundView.setFitHeight(1080);
        backgroundView.setPreserveRatio(false);

        VBox menuItems = new VBox(20);
        menuItems.setAlignment(Pos.CENTER);
        menuItems.setTranslateY(-300);

        Image logoImage = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream("/menuPrincipal/BixoQuestLogo.png")
                )
        );

        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(600);
        logoView.setPreserveRatio(true);

        VBox.setMargin(logoView, new Insets(0, 0, 50, 0));

        VBox botoes = criarBotoes(primaryStage);

        menuItems.getChildren().addAll(logoView, botoes);

        root.getChildren().addAll(backgroundView, menuItems);

        Scene scene = new Scene(root, 1920, 1080);

        StackPane.setMargin(menuItems, new Insets(50, 0, 0, 0));
        primaryStage.setTitle("BixoQuest");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    private VBox criarBotoes(Stage primaryStage) {

        VBox botoes = new VBox(20);
        botoes.setAlignment(Pos.CENTER);

        Button btnStart = new Button("Iniciar Jogo");
        Button btnConfig = new Button("Configurações");
        Button btnExit = new Button("Sair");

        estilizarBotao(btnStart);
        estilizarBotao(btnConfig);
        estilizarBotao(btnExit);

        btnExit.setOnAction(event -> System.exit(0));

        btnStart.setOnAction(event -> {
            MenuPersonagens menuPersonagens =
                    new MenuPersonagens(personagemController);

            menuPersonagens.exibir(primaryStage);
        });

        botoes.getChildren().addAll(
                btnStart,
                btnConfig,
                btnExit
        );

        return botoes;
    }

    private void estilizarBotao(Button botao) {
        botao.setFont(FONTE_PIXEL);
        botao.setTextFill(Color.WHITE);

        botao.setBackground(Background.EMPTY);
        botao.setBorder(Border.EMPTY);

        botao.setPrefWidth(250);
        botao.setFocusTraversable(false);
    }
}
package view.menu;

import controller.PersonagemController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import view.util.FonteUtil;

import java.util.Objects;

public class MenuInicial extends StackPane {

    private final PersonagemController personagemController;
    private final Runnable aoIniciarJogo;
    private final Runnable aoAbrirConfiguracoes;
    private final Runnable aoAbrirSeletorBatalha;
    private final Runnable aoSair;

    public MenuInicial(
            PersonagemController personagemController,
            Runnable aoIniciarJogo,
            Runnable aoAbrirConfiguracoes,
            Runnable aoAbrirSeletorBatalha,
            Runnable aoSair
    ) {
        this.personagemController = personagemController;
        this.aoIniciarJogo = aoIniciarJogo;
        this.aoAbrirConfiguracoes = aoAbrirConfiguracoes;
        this.aoAbrirSeletorBatalha = aoAbrirSeletorBatalha;
        this.aoSair = aoSair;

        montarTela();
    }

    private void montarTela() {
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

        VBox botoes = criarBotoes();

        menuItems.getChildren().addAll(logoView, botoes);

        getChildren().addAll(backgroundView, menuItems);

        StackPane.setMargin(menuItems, new Insets(50, 0, 0, 0));
        
        // F1 Keybind para Seletor de Batalha (Garante que vai pegar após o root carregar)
        this.setFocusTraversable(true);
        this.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.F1) {
                aoAbrirSeletorBatalha.run();
            }
        });
        
        // Adicionando listener para garantir o foco quando clicado ou adicionado à cena
        this.setOnMouseClicked(event -> this.requestFocus());
        this.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                this.requestFocus();
            }
        });
    }

    private VBox criarBotoes() {
        VBox botoes = new VBox(20);
        botoes.setAlignment(Pos.CENTER);

        Button btnStart = new Button("Iniciar Jogo");
        Button btnConfig = new Button("Configurações");
        Button btnExit = new Button("Sair");

        estilizarBotao(btnStart);
        estilizarBotao(btnConfig);
        estilizarBotao(btnExit);

        btnStart.setOnAction(event -> aoIniciarJogo.run());
        btnConfig.setOnAction(event -> aoAbrirConfiguracoes.run());
        btnExit.setOnAction(event -> aoSair.run());

        botoes.getChildren().addAll(
                btnStart,
                btnConfig,
                btnExit
        );

        return botoes;
    }

    private void estilizarBotao(Button botao) {
        botao.setFont(FonteUtil.pixel(24));
        botao.setTextFill(Color.WHITE);

        botao.setBackground(Background.EMPTY);
        botao.setBorder(Border.EMPTY);

        botao.setPrefWidth(250);
        botao.setFocusTraversable(false);
    }
}

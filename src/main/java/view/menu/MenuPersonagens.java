package view.menu;

import controller.PersonagemController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Personagem;
import view.menu.util.FonteUtil;

import java.util.Map;
import java.util.Objects;

public class MenuPersonagens {

    private final PersonagemController personagemController;

    public MenuPersonagens(PersonagemController personagemController) {
        this.personagemController = personagemController;
    }

    public void exibir(Stage window) {

        Map<Integer, Personagem> personagens =
                personagemController.carregarPersonagens();

        StackPane root = new StackPane();

        Image bgImage = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream(
                                "/menuPersonagens/BackgroundMenu.png"
                        )
                )
        );

        ImageView backgroundView = new ImageView(bgImage);
        backgroundView.setFitWidth(1920);
        backgroundView.setFitHeight(1080);
        backgroundView.setPreserveRatio(false);

        VBox menuItems = criarBotoesSlots(personagens, window);

        root.getChildren().addAll(backgroundView, menuItems);

        Scene characterScene = new Scene(root, 1920, 1080);

        window.setScene(characterScene);
    }

    private VBox criarBotoesSlots(Map<Integer, Personagem> personagens,
                                  Stage window) {

        VBox menuItems = new VBox(50);
        menuItems.setAlignment(Pos.CENTER);
        menuItems.setTranslateY(70);

        for (int slotId = 1; slotId <= 3; slotId++) {
            Personagem personagem = personagens.get(slotId);
            Button saveBotao = criarBotaoSlot(personagem, slotId, window);

            menuItems.getChildren().add(saveBotao);
        }

        Button btnVoltar = criarBotaoVoltar(window);
        VBox.setMargin(btnVoltar, new javafx.geometry.Insets(60, 0, 0, 0));

        menuItems.getChildren().add(btnVoltar);

        return menuItems;
    }

    private Button criarBotaoSlot(Personagem personagem,
                                  int slotId,
                                  Stage window) {

        Button saveBotao = new Button();
        estilizarBotao(saveBotao, "/menuPersonagens/background_botao.png" );

        if (personagem != null) {
            saveBotao.setText(
                    "Slot " + slotId + " - Carregar: " + personagem.getNome()
            );

            saveBotao.setOnAction(event -> {
                // todo: fazer o botão de personagem de fato começar o jogo
                System.out.println(personagem);
            });

        } else {
            saveBotao.setText(
                    "Slot " + slotId + " - Criar Personagem"
            );

            saveBotao.setOnAction(event -> {
                MenuCriarPersonagem menuCriarPersonagem =
                        new MenuCriarPersonagem(
                                personagemController,
                                slotId
                        );

                menuCriarPersonagem.exibir(window);
            });
        }

        return saveBotao;
    }

    private Button criarBotaoVoltar(Stage window) {

        Button btnVoltar = new Button("Voltar");
        estilizarBotao(btnVoltar, "/menuPersonagens/background_botao.png" );
        btnVoltar.setOnAction(event -> {
            Menu mainMenu = new Menu(personagemController);
            mainMenu.exibir(window);
        });

        return btnVoltar;
    }

    private void estilizarBotao(Button botao, String caminhoImagem) {

        Image imagem = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream(caminhoImagem)
                )
        );

        botao.setPrefSize(888, 135);
        BackgroundImage backgroundImage = new BackgroundImage(
                imagem,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT
        );

        botao.setFont(FonteUtil.pixel(24));
        botao.setTextFill(Color.WHITE);

        botao.setBackground(
                new Background(backgroundImage)
        );

        botao.setBorder(Border.EMPTY);
    }
}
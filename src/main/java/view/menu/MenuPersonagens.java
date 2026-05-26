package view.menu;

import controller.PersonagemController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Personagem;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class MenuPersonagens {
    private final PersonagemController personagemController;

    // Constructor Injection
    public MenuPersonagens(PersonagemController personagemController) {
        this.personagemController = personagemController;
    }
    // This method takes the existing window (Stage) from the Menu
    public void exibir(Stage window) {

        Map<Integer, Personagem> personagems = personagemController.carregarPersonagens();

        StackPane root = new StackPane();
        Image bgImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/menuPersonagens/BackgroundMenu.png")));

        ImageView backgroundView = new ImageView(bgImage);//adiciona a imagem na view
        backgroundView.setFitWidth(1920);
        backgroundView.setFitHeight(1080);
        backgroundView.setPreserveRatio(false);


        VBox menuItems = new VBox(50);
        menuItems.setAlignment(Pos.CENTER);
        menuItems.setTranslateY(70);
        for (int slotId = 1; slotId <= 3; slotId++) {


            Personagem personagem = personagems.get(slotId);

            Button saveBotao = getButton(personagem, personagemController ,slotId, window);

            menuItems.getChildren().add(saveBotao);
        }


        Button Voltar = new Button("Voltar");
        VBox.setMargin(Voltar, new  javafx.geometry.Insets(60, 0, 0, 0));

        Voltar.setOnAction(event -> {
            Menu mainMenu = new Menu(personagemController);
            mainMenu.exibir(window); // Assuming your Menu class start() method takes the Stage
        });


        Voltar.setPrefSize(888, 115);
        Scene characterScene = new Scene(root, 1920, 1080);

        menuItems.getChildren().addAll(Voltar);


        root.getChildren().addAll(backgroundView, menuItems);

        window.setScene(characterScene);
    }

    private static Button getButton(Personagem personagem, PersonagemController personagemController ,int slotId, Stage window) {
        Button saveBotao = new Button();
        saveBotao.setPrefSize(888, 115);
        if (personagem != null) {

            saveBotao.setText("Slot " + slotId + " - Carregar: " + personagem.getNome());

            saveBotao.setOnAction(event -> {
                //todo: fazer o botão de personagem de fato começar o jogo
                System.out.println(personagem);
            });

        } else {

            saveBotao.setText("Slot " + slotId + " - Criar Personagem");

            saveBotao.setOnAction(event -> {
                    MenuCriarPersonagem menuCriarPersonagem = new MenuCriarPersonagem(personagemController, slotId);
                    menuCriarPersonagem.exibir(window);
            });
        }
        return saveBotao;
    }

}
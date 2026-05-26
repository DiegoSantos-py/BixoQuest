package view.menu;

import controller.PersonagemController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuCriarPersonagem {
    private final PersonagemController personagemController;
    private final int slotId;

    public MenuCriarPersonagem(PersonagemController personagemController, int slotId) {
        this.personagemController = personagemController;
        this.slotId = slotId;
    }

    public void exibir(Stage window) {
        StackPane root = new StackPane();

        VBox formContainer = new VBox(20);
        formContainer.setAlignment(Pos.CENTER);
        Label lblTitulo = new Label("Criando personagem para o Slot " + slotId);


        TextField campoNome = new TextField();
        campoNome.setPromptText("Digite o nome do estudante...");
        campoNome.setMaxWidth(400);
        Label lblAparencia = new Label("Selecione a Aparência:");

        ToggleGroup grupoSprite = new ToggleGroup();

        RadioButton rbMasculino = new RadioButton("Masculino");
        rbMasculino.setToggleGroup(grupoSprite);
        rbMasculino.setUserData("spriteMasculino.png");
        rbMasculino.setSelected(true); // Default

        RadioButton rbFeminino = new RadioButton("Feminino");
        rbFeminino.setToggleGroup(grupoSprite);
        rbFeminino.setUserData("spriteFeminino.png");

        HBox radioContainer = new HBox(30, rbMasculino, rbFeminino);
        radioContainer.setAlignment(Pos.CENTER);


        Button btnCriar = new Button("Confirmar e Criar");
        btnCriar.setPrefSize(200, 50);

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setPrefSize(200, 50);

        HBox botoesContainer = new HBox(20, btnCriar, btnCancelar);
        botoesContainer.setAlignment(Pos.CENTER);


        btnCriar.setOnAction(event -> {
            String nome = campoNome.getText();

            if (nome.isEmpty()) {
                campoNome.setPromptText("O NOME É OBRIGATÓRIO!");
                return;
            }

            String spriteEscolhido = grupoSprite.getSelectedToggle().getUserData().toString();

            // Build the character


            // Send to Controller to save
            personagemController.criarESalvarPersonagem(nome, 40.0, 40.0, 40.0, 40.0, spriteEscolhido, null, 0,0,slotId);

            // Go back to the selection screen
            MenuPersonagens menu = new MenuPersonagens(personagemController);
            menu.exibir(window);
        });

        btnCancelar.setOnAction(event -> {
            MenuPersonagens menu = new MenuPersonagens(personagemController);
            menu.exibir(window);
        });

        formContainer.getChildren().addAll(lblTitulo, campoNome, lblAparencia, radioContainer, botoesContainer);
        root.getChildren().add(formContainer);

        window.setScene(new Scene(root, 1920, 1080));
    }
}
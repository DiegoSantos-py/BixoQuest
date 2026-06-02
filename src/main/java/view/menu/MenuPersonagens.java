package view.menu;

import controller.PersonagemController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import model.Personagem;
import view.util.FonteUtil;

import java.util.Map;
import java.util.Objects;

public class MenuPersonagens extends StackPane {

    private final PersonagemController personagemController;
    private final Runnable aoVoltar;
    private final Runnable aoCarregarPersonagem;
    private final AcaoSlot aoCriarPersonagem;

    public MenuPersonagens(
            PersonagemController personagemController,
            Runnable aoVoltar,
            Runnable aoCarregarPersonagem,
            AcaoSlot aoCriarPersonagem
    ) {
        this.personagemController = personagemController;
        this.aoVoltar = aoVoltar;
        this.aoCarregarPersonagem = aoCarregarPersonagem;
        this.aoCriarPersonagem = aoCriarPersonagem;

        montarTela();
    }

    private void montarTela() {
        Map<Integer, Personagem> personagens =
                personagemController.carregarPersonagens();

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

        VBox menuItems = criarBotoesSlots(personagens);

        getChildren().addAll(backgroundView, menuItems);
    }

    private VBox criarBotoesSlots(Map<Integer, Personagem> personagens) {
        VBox menuItems = new VBox(50);
        menuItems.setAlignment(Pos.CENTER);
        menuItems.setTranslateY(70);

        for (int slotId = 1; slotId <= 3; slotId++) {
            Personagem personagem = personagens.get(slotId);
            Button saveBotao = criarBotaoSlot(personagem, slotId);
            if(slotId == 1) {
                VBox.setMargin(saveBotao, new javafx.geometry.Insets(60, 0, 0, 0));
            }
            menuItems.getChildren().add(saveBotao);
        }

        Button btnVoltar = criarBotaoVoltar();
        VBox.setMargin(btnVoltar, new javafx.geometry.Insets(30, 0, 0, 0));

        menuItems.getChildren().add(btnVoltar);

        return menuItems;
    }

    private Button criarBotaoSlot(Personagem personagem, int slotId) {
        Button saveBotao = new Button();
        estilizarBotao(saveBotao, "/menuPersonagens/background_botao.png");

        if (personagem != null) {
            saveBotao.setText(
                    "Slot " + slotId + " - Carregar: " + personagem.getNome()
            );

            saveBotao.setOnAction(event -> aoCarregarPersonagem.run());

        } else {
            saveBotao.setText(
                    "Slot " + slotId + " - Criar Personagem"
            );

            saveBotao.setOnAction(event -> aoCriarPersonagem.executar(slotId));
        }

        return saveBotao;
    }

    private Button criarBotaoVoltar() {
        Button btnVoltar = new Button("Voltar");
        estilizarBotao(btnVoltar, "/menuPersonagens/background_botao.png");

        btnVoltar.setOnAction(event -> aoVoltar.run());

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
        botao.setFocusTraversable(false);
    }

    @FunctionalInterface
    public interface AcaoSlot {
        void executar(int slotId);
    }
}
package view.menu;

import controller.PersonagemController;
import javafx.geometry.Insets;
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
    private final AcaoSlot aoCarregarPersonagem;
    private final AcaoSlot aoCriarPersonagem;

    private final DialogoConfirmacao dialogoConfirmacao = new DialogoConfirmacao();

    public MenuPersonagens(
            PersonagemController personagemController,
            Runnable aoVoltar,
            AcaoSlot aoCarregarPersonagem,
            AcaoSlot aoCriarPersonagem
    ) {
        this.personagemController = personagemController;
        this.aoVoltar = aoVoltar;
        this.aoCarregarPersonagem = aoCarregarPersonagem;
        this.aoCriarPersonagem = aoCriarPersonagem;

        montarTela();
    }

    private void montarTela() {
        getChildren().clear();

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

        getChildren().addAll(backgroundView, menuItems, dialogoConfirmacao);
    }

    private VBox criarBotoesSlots(Map<Integer, Personagem> personagens) {
        VBox menuItems = new VBox(50);
        menuItems.setAlignment(Pos.CENTER);
        menuItems.setTranslateY(70);

        for (int slotId = 1; slotId <= 3; slotId++) {
            Personagem personagem = personagens.get(slotId);
            StackPane linhaSlot = criarLinhaSlot(personagem, slotId);
            if (slotId == 1) {
                VBox.setMargin(linhaSlot, new Insets(60, 0, 0, 0));
            }
            menuItems.getChildren().add(linhaSlot);
        }

        Button btnVoltar = criarBotaoVoltar();
        VBox.setMargin(btnVoltar, new Insets(30, 0, 0, 0));

        menuItems.getChildren().add(btnVoltar);

        return menuItems;
    }

    private StackPane criarLinhaSlot(Personagem personagem, int slotId) {
        StackPane linha = new StackPane();

        Button saveBotao = criarBotaoSlot(personagem, slotId);
        linha.getChildren().add(saveBotao);

        if (personagem != null) {
            Button btnDeletar = criarBotaoDeletar(slotId, personagem.getNome());
            StackPane.setAlignment(btnDeletar, Pos.CENTER_RIGHT);
            StackPane.setMargin(btnDeletar, new Insets(0, 500, 0, 0)); // ajuste esse valor conforme necessário
            linha.getChildren().add(btnDeletar);
        }

        return linha;
    }

    private Button criarBotaoSlot(Personagem personagem, int slotId) {
        Button saveBotao = new Button();
        estilizarBotao(saveBotao, "/menuPersonagens/background_botao.png");

        if (personagem != null) {
            saveBotao.setText(
                    "Slot " + slotId + " - Carregar: " + personagem.getNome()
            );
            saveBotao.setOnAction(event -> aoCarregarPersonagem.executar(slotId));
        } else {
            saveBotao.setText(
                    "Slot " + slotId + " - Criar Personagem"
            );
            saveBotao.setOnAction(event -> aoCriarPersonagem.executar(slotId));
        }

        return saveBotao;
    }

    private Button criarBotaoDeletar(int slotId, String nomePersonagem) {
        Button btnDeletar = new Button("Deletar");
        btnDeletar.setFont(FonteUtil.pixel(18));
        btnDeletar.setTextFill(Color.WHITE);
        btnDeletar.setBackground(new Background(new BackgroundFill(
                Color.web("#8B0000"), new CornerRadii(6), Insets.EMPTY)));
        btnDeletar.setBorder(Border.EMPTY);
        btnDeletar.setPrefSize(120, 60);
        btnDeletar.setFocusTraversable(false);

        btnDeletar.setOnAction(event -> confirmarEDeletar(slotId, nomePersonagem));

        return btnDeletar;
    }

    private void confirmarEDeletar(int slotId, String nomePersonagem) {
        dialogoConfirmacao.abrir(
                "Tem certeza que deseja deletar \"" + nomePersonagem + "\"?\nEsta ação não pode ser desfeita.",
                () -> {
                    boolean sucesso = personagemController.deletarPersonagem(slotId);
                    if (sucesso) {
                        montarTela();
                    }
                }
        );
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
package view.menu;

import controller.PersonagemController;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import view.util.FonteUtil;

import java.util.Objects;

public class MenuCriarPersonagem extends StackPane {

    private final PersonagemController personagemController;
    private final int slotId;
    private final AcaoSlot aoConfirmar;
    private final Runnable aoCancelar;

    private String spriteEscolhido = null;

    public MenuCriarPersonagem(
            PersonagemController personagemController,
            int slotId,
            AcaoSlot aoConfirmar,
            Runnable aoCancelar
    ) {
        this.personagemController = personagemController;
        this.slotId = slotId;
        this.aoConfirmar = aoConfirmar;
        this.aoCancelar = aoCancelar;

        montarTela();
    }

    private void montarTela() {
        VBox formContainer = new VBox(20);
        formContainer.setAlignment(Pos.CENTER);

        Image backgroundImage = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream("/assets/background/background.png")
                )
        );

        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(1920);
        backgroundView.setFitHeight(1080);

        Label lblTitulo = new Label("Criando personagem para o Slot " + slotId);
        lblTitulo.setFont(FonteUtil.pixel(24));

        TextField campoNome = new TextField();
        campoNome.setPromptText("Digite o nome do estudante...");
        campoNome.setMaxWidth(400);
        campoNome.setFont(FonteUtil.pixel(24));

        Label lblAparencia = new Label("Selecione a Aparência:");
        lblAparencia.setFont(FonteUtil.pixel(24));

        Button btnMasculino = criarBotaoPersonagem(
                "/menuPersonagens/botaoEscolherPersonagem.png",
                "/Jogador/Jogador1/rotations/south.png"
        );

        Button btnFeminino = criarBotaoPersonagem(
                "/menuPersonagens/botaoEscolherPersonagem.png",
                "/Jogador/Jogador2/rotations/south.png"
        );

        btnMasculino.setOnAction(event -> {
            spriteEscolhido = "/Jogador/Jogador1/";
        });

        btnFeminino.setOnAction(event -> {
            spriteEscolhido = "/Jogador/Jogador2/";
        });

        HBox botoesAparencia = new HBox(30, btnMasculino, btnFeminino);
        botoesAparencia.setAlignment(Pos.CENTER);

        HBox botoesContainer = criarBotoes(campoNome);

        formContainer.getChildren().addAll(
                lblTitulo,
                campoNome,
                lblAparencia,
                botoesAparencia,
                botoesContainer
        );

        getChildren().addAll(backgroundView, formContainer);
    }

    private HBox criarBotoes(TextField campoNome) {
        Button btnCriar = new Button("Confirmar e Criar");
        btnCriar.setPrefSize(350, 100);
        btnCriar.setFont(FonteUtil.pixel(24));
        btnCriar.setBackground(Background.EMPTY);

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setPrefSize(350, 100);
        btnCancelar.setFont(FonteUtil.pixel(24));
        btnCancelar.setBackground(Background.EMPTY);

        btnCriar.setOnAction(event -> {
            String nome = campoNome.getText();

            if (nome.isEmpty()) {
                campoNome.setPromptText("O NOME É OBRIGATÓRIO!");
                return;
            }

            if (spriteEscolhido == null) {
                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Personagem");
                alerta.setHeaderText(null);
                alerta.setContentText("Selecione uma aparência antes de continuar.");
                alerta.showAndWait();
                return;
            }


            //TODO: adicionar alerta caso criar e/ou salvar de errado
            personagemController.criarESalvarPersonagem(
                    nome,
                    40.0,
                    40.0,
                    40.0,
                    40.0,
                    spriteEscolhido,
                    null,
                    0,
                    0,
                    slotId
            );

            aoConfirmar.executar(this.slotId);
        });

        btnCancelar.setOnAction(event -> aoCancelar.run());

        HBox botoesContainer = new HBox(20, btnCriar, btnCancelar);
        botoesContainer.setAlignment(Pos.CENTER);

        return botoesContainer;
    }

    private Button criarBotaoPersonagem(
            String caminhoMoldura,
            String caminhoSprite
    ) {
        ImageView moldura = new ImageView(
                new Image(
                        Objects.requireNonNull(
                                getClass().getResourceAsStream(caminhoMoldura)
                        )
                )
        );

        moldura.setFitWidth(300);
        moldura.setFitHeight(300);
        moldura.setPreserveRatio(true);

        ImageView sprite = new ImageView(
                new Image(
                        Objects.requireNonNull(
                                getClass().getResourceAsStream(caminhoSprite)
                        )
                )
        );

        sprite.setFitWidth(200);
        sprite.setFitHeight(200);
        sprite.setPreserveRatio(true);

        StackPane conteudo = new StackPane();
        StackPane.setAlignment(sprite, Pos.CENTER);

        sprite.setTranslateX(15);

        conteudo.getChildren().addAll(
                moldura,
                sprite
        );

        Button botao = new Button();

        botao.setGraphic(conteudo);
        botao.setFont(FonteUtil.pixel(24));
        botao.setBackground(Background.EMPTY);
        botao.setBorder(Border.EMPTY);

        botao.setPrefSize(300, 300);
        botao.setFocusTraversable(false);

        return botao;
    }

    @FunctionalInterface
    public interface AcaoSlot {
        void executar(int slotId);
    }
}
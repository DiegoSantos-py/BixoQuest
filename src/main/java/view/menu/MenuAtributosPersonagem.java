package view.menu;

import controller.PersonagemController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import view.util.FonteUtil;

import java.util.Map;
import java.util.Objects;

public class MenuAtributosPersonagem extends StackPane {

    private final PersonagemController personagemController;
    private final int personagemId;
    private final Runnable aoVoltar;

    private VBox atributosContainer; // guardamos referência pra poder atualizar depois

    public MenuAtributosPersonagem(
            PersonagemController personagemController,
            int personagemId,
            Runnable aoVoltar) {

        this.personagemController = personagemController;
        this.personagemId = personagemId;
        this.aoVoltar = aoVoltar;

        montarTela();
        setVisible(false);
        setManaged(false); // não ocupa espaço nem aparece até abrir()
    }

    private void montarTela() {

        ImageView background = new ImageView(
                new Image(
                        Objects.requireNonNull(
                                getClass().getResourceAsStream("/menuPersonagens/BackgroundMenu.png")
                        )
                )
        );
        // acompanha o tamanho real do container pai (StackPane da CenaJogo)
        background.fitWidthProperty().bind(widthProperty());
        background.fitHeightProperty().bind(heightProperty());
        background.setPreserveRatio(false);

        VBox painel = new VBox(30);
        painel.setAlignment(Pos.CENTER);

        Label titulo = new Label("ATRIBUTOS");
        titulo.setFont(FonteUtil.pixel(36));
        titulo.setTextFill(Color.WHITE);

        atributosContainer = new VBox(20);
        atributosContainer.setAlignment(Pos.CENTER_LEFT);
        atualizarAtributos(); // popula a lista já na criação

        Button btnVoltar = criarBotaoVoltar();

        painel.getChildren().addAll(
                titulo,
                atributosContainer,
                btnVoltar
        );

        getChildren().addAll(background, painel);

        StackPane.setMargin(painel, new Insets(40));
    }

    /**
     * Reconstrói a lista de atributos a partir do estado atual do personagem.
     * Chamado na criação e sempre que o menu é reaberto (abrir()).
     */
    private void atualizarAtributos() {
        atributosContainer.getChildren().clear();

        Map<String, Double> atributos =
                personagemController.getAtributos(personagemId);

        for (Map.Entry<String, Double> atributo : atributos.entrySet()) {
            atributosContainer.getChildren().add(
                    criarLinhaAtributo(
                            atributo.getKey(),
                            atributo.getValue()
                    )
            );
        }
    }

    private HBox criarLinhaAtributo(String nome, double valor) {

        HBox linha = new HBox(15);
        linha.setAlignment(Pos.CENTER);

        ImageView icone = criarIcone(nome);

        Label texto = new Label(nome);
        texto.setFont(FonteUtil.pixel(22));
        texto.setTextFill(Color.WHITE);
        texto.setMinWidth(180);

        ProgressBar barra = new ProgressBar(valor / 100.0);
        barra.setPrefWidth(350);
        barra.setPrefHeight(20);

        linha.getChildren().addAll(
                icone,
                texto,
                barra
        );

        return linha;
    }

    private ImageView criarIcone(String atributo) {


        String caminho =
                "/assets/atributos/" +
                        atributo.toLowerCase() +
                        ".png";

        ImageView view = new ImageView(
                new Image(
                        Objects.requireNonNull(
                                getClass().getResourceAsStream(caminho)
                        )
                )
        );
        view.setFitWidth(32);
        view.setFitHeight(32);

        return view;
    }

    private Button criarBotaoVoltar() {
        Button botao = new Button("Voltar");
        estilizarBotao(botao);
        botao.setOnAction(e -> fechar());
        return botao;
    }

    private void estilizarBotao(Button botao) {
        botao.setFont(FonteUtil.pixel(24));
        botao.setTextFill(Color.WHITE);
        botao.setBackground(Background.EMPTY);
        botao.setBorder(Border.EMPTY);
        botao.setPrefWidth(220);
        botao.setFocusTraversable(false);
    }

    public void abrir() {
        atualizarAtributos(); // garante dados frescos antes de mostrar
        setVisible(true);
        setManaged(true);
        toFront();
    }

    public void fechar() {
        setVisible(false);
        setManaged(false);
        aoVoltar.run();
    }
}
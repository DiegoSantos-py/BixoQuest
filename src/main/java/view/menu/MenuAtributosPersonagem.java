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

    public MenuAtributosPersonagem(
            PersonagemController personagemController,
            int personagemId,
            Runnable aoVoltar) {

        this.personagemController = personagemController;
        this.personagemId = personagemId;
        this.aoVoltar = aoVoltar;

        montarTela();
    }

    private void montarTela() {

        Image bgImage = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream("/background.png")
                )
        );

        ImageView background = new ImageView(bgImage);
        background.setFitWidth(1920);
        background.setFitHeight(1080);
        background.setPreserveRatio(false);

        VBox painel = new VBox(30);
        painel.setAlignment(Pos.CENTER);

        Label titulo = new Label("ATRIBUTOS");
        titulo.setFont(FonteUtil.pixel(36));
        titulo.setTextFill(Color.WHITE);

        VBox atributos = criarListaAtributos();

        Button btnVoltar = criarBotaoVoltar();

        painel.getChildren().addAll(
                titulo,
                atributos,
                btnVoltar
        );

        getChildren().addAll(background, painel);

        StackPane.setMargin(painel, new Insets(40));
    }

    private VBox criarListaAtributos() {

        VBox lista = new VBox(20);
        lista.setAlignment(Pos.CENTER_LEFT);

        Map<String, Double> atributos =
                personagemController.getAtributos(personagemId);

        for (Map.Entry<String, Double> atributo : atributos.entrySet()) {

            lista.getChildren().add(
                    criarLinhaAtributo(
                            atributo.getKey(),
                            atributo.getValue()
                    )
            );
        }

        return lista;
    }

    private HBox criarLinhaAtributo(String nome, double valor) {

        HBox linha = new HBox(15);
        linha.setAlignment(Pos.CENTER_LEFT);

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
                "/atributos/" +
                        atributo.toLowerCase() +
                        ".png";

        Image imagem = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream(caminho)
                )
        );

        ImageView view = new ImageView(imagem);
        view.setFitWidth(32);
        view.setFitHeight(32);

        return view;
    }

    private Button criarBotaoVoltar() {

        Button botao = new Button("Voltar");

        estilizarBotao(botao);

        botao.setOnAction(e -> aoVoltar.run());

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
}
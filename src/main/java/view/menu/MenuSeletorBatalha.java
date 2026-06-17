package view.menu;

import controller.BatalhaController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.Batalha.Oponente;
import view.util.FonteUtil;

import java.util.List;
import java.util.Objects;

public class MenuSeletorBatalha extends StackPane {

    private final BatalhaController batalhaController;
    private final Runnable aoIniciarBatalha;
    private final Runnable aoVoltar;

    public MenuSeletorBatalha(BatalhaController batalhaController, Runnable aoIniciarBatalha, Runnable aoVoltar) {
        this.batalhaController = batalhaController;
        this.aoIniciarBatalha = aoIniciarBatalha;
        this.aoVoltar = aoVoltar;

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

        Text titulo = new Text("Selecione o Oponente para Batalha");
        titulo.setFont(FonteUtil.pixel(48));
        titulo.setFill(Color.WHITE);

        VBox botoes = criarBotoes();

        ScrollPane scrollPane = new ScrollPane(botoes);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollPane.setPrefViewportHeight(600);
        scrollPane.setMaxWidth(600);
        scrollPane.setPadding(new Insets(20));

        Button btnVoltar = new Button("Voltar");
        estilizarBotao(btnVoltar);
        btnVoltar.setOnAction(e -> aoVoltar.run());

        menuItems.getChildren().addAll(titulo, scrollPane, btnVoltar);

        getChildren().addAll(backgroundView, menuItems);
    }

    private VBox criarBotoes() {
        VBox botoes = new VBox(15);
        botoes.setAlignment(Pos.CENTER);

        List<model.Npc.Animal> animais = batalhaController.getAnimaisDisponiveis();

        if (animais == null || animais.isEmpty()) {
            Text vazio = new Text("Nenhum animal disponível no repositório.");
            vazio.setFill(Color.WHITE);
            vazio.setFont(FonteUtil.pixel(20));
            botoes.getChildren().add(vazio);
        } else {
            for (model.Npc.Animal animal : animais) {
                Button btnOponente = new Button("Batalhar com " + animal.getNome() + " (" + animal.getEspecie() + ")");
                estilizarBotao(btnOponente);
                
                btnOponente.setOnAction(event -> {
                    batalhaController.iniciarBatalhaTeste(animal);
                    aoIniciarBatalha.run(); 
                });
                
                botoes.getChildren().add(btnOponente);
            }
        }

        return botoes;
    }

    private void estilizarBotao(Button botao) {
        botao.setFont(FonteUtil.pixel(24));
        botao.setTextFill(Color.WHITE);

        botao.setBackground(Background.EMPTY);
        botao.setBorder(Border.EMPTY);

        botao.setPrefWidth(500);
        botao.setFocusTraversable(false);
    }
}

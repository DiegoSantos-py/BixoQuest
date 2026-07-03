package view.menu;

import controller.BatalhaController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.Evento.Prova.ProvaIDs;
import model.Npc.Animal;
import view.util.FonteUtil;

import java.util.List;
import java.util.Objects;

public class MenuSeletorBatalha extends StackPane {

    private final BatalhaController batalhaController;
    private final Runnable aoIniciarBatalha;
    private final Runnable aoVoltar;
    private TextField inputConhecimento;

    public MenuSeletorBatalha(BatalhaController batalhaController, Runnable aoIniciarBatalha, Runnable aoVoltar) {
        this.batalhaController = batalhaController;
        this.aoIniciarBatalha = aoIniciarBatalha;
        this.aoVoltar = aoVoltar;

        montarTela();
    }

    private void montarTela() {
        Image bgImage = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream("/menuPrincipal/BackgroundMenu.png")));

        ImageView backgroundView = new ImageView(bgImage);
        backgroundView.setFitWidth(1920);
        backgroundView.setFitHeight(1080);
        backgroundView.setPreserveRatio(false);

        VBox menuItems = new VBox(20);
        menuItems.setAlignment(Pos.CENTER);

        Text titulo = new Text("Selecione o Oponente para Batalha");
        titulo.setFont(FonteUtil.pixel(48));
        titulo.setFill(Color.WHITE);

        HBox tabs = new HBox(20);
        tabs.setAlignment(Pos.CENTER);

        Button btnAnimais = new Button("Animais");
        estilizarBotao(btnAnimais);
        btnAnimais.setPrefWidth(200);

        Button btnProvas = new Button("Provas");
        estilizarBotao(btnProvas);
        btnProvas.setPrefWidth(200);

        tabs.getChildren().addAll(btnAnimais, btnProvas);

        inputConhecimento = new TextField("0");
        inputConhecimento.setPromptText("Bônus Global de Conhecimento");
        inputConhecimento.setMaxWidth(300);
        inputConhecimento.setFont(FonteUtil.pixel(16));
        inputConhecimento.setStyle("-fx-alignment: center;");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollPane.setPrefViewportHeight(600);
        scrollPane.setMaxWidth(600);
        scrollPane.setPadding(new Insets(20));

        btnAnimais.setOnAction(e -> scrollPane.setContent(criarBotoesAnimais()));
        btnProvas.setOnAction(e -> scrollPane.setContent(criarBotoesProvas()));

        // Aba inicial padrão
        scrollPane.setContent(criarBotoesAnimais());

        Button btnVoltar = new Button("Voltar");
        estilizarBotao(btnVoltar);
        btnVoltar.setOnAction(e -> aoVoltar.run());

        menuItems.getChildren().addAll(titulo, inputConhecimento, tabs, scrollPane, btnVoltar);

        getChildren().addAll(backgroundView, menuItems);
    }

    private float lerBonus() {
        try {
            String texto = inputConhecimento.getText().replace(",", ".").trim();
            if (!texto.isEmpty()) return Float.parseFloat(texto);
        } catch (NumberFormatException ex) {
            System.out.println("Valor inválido de conhecimento. Assumindo 0.");
        }
        return 0f;
    }

    private VBox criarBotoesAnimais() {
        VBox botoes = new VBox(15);
        botoes.setAlignment(Pos.CENTER);

        List<Animal> animais = batalhaController.getAnimaisDisponiveis();

        if (animais == null || animais.isEmpty()) {
            Text vazio = new Text("Nenhum animal disponível no repositório.");
            vazio.setFill(Color.WHITE);
            vazio.setFont(FonteUtil.pixel(20));
            botoes.getChildren().add(vazio);
        } else {
            for (Animal animal : animais) {
                Button btnOponente = new Button("Batalhar com " + animal.getNome() + " (" + animal.getEspecie() + ")");
                estilizarBotao(btnOponente);

                btnOponente.setOnAction(event -> {
                    batalhaController.iniciarBatalhaTeste(animal, lerBonus());
                    aoIniciarBatalha.run();
                });

                botoes.getChildren().add(btnOponente);
            }
        }

        return botoes;
    }

    private VBox criarBotoesProvas() {
        VBox botoes = new VBox(15);
        botoes.setAlignment(Pos.CENTER);

        for (ProvaIDs provaId : ProvaIDs.values()) {
            Button btnProva = new Button("Iniciar " + provaId.name());
            estilizarBotao(btnProva);

            btnProva.setOnAction(event -> {
                batalhaController.iniciarProvaTeste(provaId, lerBonus());
                aoIniciarBatalha.run();
            });

            botoes.getChildren().add(btnProva);
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

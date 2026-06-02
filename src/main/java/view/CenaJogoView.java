package view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class CenaJogoView {

    private final StackPane root;

    private final Pane backgroundLayer;
    private final Pane decoracaoLayer;
    private final Pane objetosLayer;
    private final Pane personagensLayer;
    private final Pane hudLayer;

    private final ImageView background;
    private final ImageView arvore;
    private final ImageView pontoOnibus;
    private final ImageView arbustoSemFlor;
    private final ImageView arbustoComFlor;
    private final ImageView placa;
    private final ImageView jogador;

    public CenaJogoView(double largura, double altura) {

        root = new StackPane();

        backgroundLayer = new Pane();
        decoracaoLayer = new Pane();
        objetosLayer = new Pane();
        personagensLayer = new Pane();
        hudLayer = new Pane();

        // Background
        background = criarBackground(
                "/assets/Background_ponto_onibus.png",
                largura,
                altura
        );

        // Decoração
        arvore = criarImagem(
                "/assets/arvore.png",
                700,
                50,
                150
        );

        arbustoSemFlor = criarImagem(
                "/assets/arbusto_sem_flor.png",
                300,
                50,
                850
        );

        arbustoComFlor = criarImagem(
                "/assets/arbusto_com_flor.png",
                450,
                350,
                50
        );

        placa = criarImagem(
                "/assets/placa_ponto1.png",
                300,
                1000,
                650
        );

        // Objetos
        pontoOnibus = criarImagem(
                "/assets/ponto_de_onibus.png",
                800,
                450,
                350
        );

        // Jogador
        jogador = criarImagem(
                "/Jogador/Jogador1/rotations/south.png",
                350,
                700,
                700
        );

        adicionarElementosNasCamadas();

        root.getChildren().addAll(
                backgroundLayer,
                decoracaoLayer,
                objetosLayer,
                personagensLayer,
                hudLayer
        );
    }

    private ImageView criarBackground(
            String caminho,
            double largura,
            double altura
    ) {
        var stream = getClass().getResourceAsStream(caminho);

        if (stream == null) {
            throw new IllegalArgumentException(
                    "Imagem não encontrada: " + caminho
            );
        }

        ImageView imageView = new ImageView(new Image(stream));

        imageView.setFitWidth(largura);
        imageView.setFitHeight(altura);
        imageView.setPreserveRatio(false);

        return imageView;
    }

    private ImageView criarImagem(
            String caminho,
            double largura,
            double x,
            double y
    ) {
        var stream = getClass().getResourceAsStream(caminho);

        if (stream == null) {
            throw new IllegalArgumentException(
                    "Imagem não encontrada: " + caminho
            );
        }

        ImageView imageView = new ImageView(new Image(stream));

        imageView.setPreserveRatio(true);
        imageView.setFitWidth(largura);

        imageView.setLayoutX(x);
        imageView.setLayoutY(y);

        return imageView;
    }

    private void adicionarElementosNasCamadas() {

        backgroundLayer.getChildren().add(background);

        decoracaoLayer.getChildren().addAll(
                arvore,
                arbustoSemFlor,
                arbustoComFlor,
                placa
        );

        objetosLayer.getChildren().add(pontoOnibus);

        personagensLayer.getChildren().add(jogador);
    }

    public StackPane getRoot() {
        return root;
    }

    public ImageView getJogador() {
        return jogador;
    }

    public Pane getHudLayer() {
        return hudLayer;
    }
}
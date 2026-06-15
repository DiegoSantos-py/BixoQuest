package view.animacao;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import view.util.FonteUtil;

import java.util.List;
import java.util.Objects;

public class AnimacaoInicioView extends StackPane {

    private final Runnable aoFinalizar;

    private final List<Node> frames = List.of(
            criarImagem("/animacao/Animacao_inicio1.png"),
            criarImagem("/animacao/Animacao_inicio2.png"),
            montarTelaInicioDia()

    );

    private int indiceAtual = 0;

    private ImageView criarImagem (String sprite){
        Image backgroundImage = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream(sprite)
                )
        );
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(1920);
        backgroundView.setFitHeight(1080);
        return backgroundView;
    }
    private StackPane montarTelaInicioDia() {
        ImageView backgroundView = criarImagem("/assets/Background.png");

        VBox inicioItens = new VBox();
        inicioItens.setAlignment(Pos.CENTER);

        Image bg = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream("/menuPersonagens/background_botao.png")
                )
        );

        ImageView bgView = new ImageView(bg);
        bgView.setFitWidth(600);
        bgView.setPreserveRatio(true);

        Text bg_texto = new Text("Dia ");
        bg_texto.setFont(FonteUtil.pixel(40));
        bg_texto.setFill(Color.WHITE);

        StackPane bgComTexto = new StackPane(bgView, bg_texto);
        bgComTexto.setAlignment(Pos.CENTER);
        bg_texto.setTranslateY(-10);

        Text texto = new Text("Carregando...");
        texto.setFill(Color.WHITE);
        texto.setFont(FonteUtil.pixel(40));

        inicioItens.getChildren().add(bgComTexto);

        StackPane tela = new StackPane();
        StackPane.setAlignment(texto, Pos.BOTTOM_CENTER);
        StackPane.setMargin(texto, new Insets(0, 0, 100, 0));
        tela.getChildren().addAll(backgroundView, inicioItens, texto);

        return tela;
    }

    public AnimacaoInicioView(
            double largura,
            double altura,
            Runnable aoFinalizar
    ) {
        this.aoFinalizar = aoFinalizar;

        getChildren().addAll(frames.get(0));

        iniciarAnimacao();
    }

    private void iniciarAnimacao() {
        PauseTransition espera = new PauseTransition(Duration.seconds(3));

        espera.setOnFinished(event -> trocarImagem());

        espera.play();
    }

    private void trocarImagem() {
        int proximoIndice = indiceAtual + 1;

        if (proximoIndice >= frames.size()) {
            aoFinalizar.run();
            return;
        }

        Node atual = frames.get(indiceAtual);
        Node proximo = frames.get(proximoIndice);

        proximo.setOpacity(0);
        getChildren().add(proximo); // adiciona o próximo

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), atual);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), proximo);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ParallelTransition crossFade = new ParallelTransition(fadeOut, fadeIn);

        crossFade.setOnFinished(event -> {
            getChildren().remove(atual); // remove o anterior
            indiceAtual = proximoIndice;
            iniciarAnimacao();
        });

        crossFade.play();
    }
}
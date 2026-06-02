package view.animacao;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.List;

public class AnimacaoInicioView extends StackPane {

    private final ImageView imagemAtual;
    private final ImageView proximaImagem;
    private final Runnable aoFinalizar;

    private final List<Image> imagens = List.of(
            new Image("/animacao/Animacao_inicio1.png"),
            new Image("/animacao/Animacao_inicio2.png")
    );

    private int indiceAtual = 0;

    public AnimacaoInicioView(
            double largura,
            double altura,
            Runnable aoFinalizar
    ) {
        this.aoFinalizar = aoFinalizar;

        imagemAtual = criarImageView(largura, altura);
        proximaImagem = criarImageView(largura, altura);

        imagemAtual.setImage(imagens.get(0));
        proximaImagem.setOpacity(0);

        getChildren().addAll(imagemAtual, proximaImagem);

        iniciarAnimacao();
    }

    private ImageView criarImageView(double largura, double altura) {
        ImageView iv = new ImageView();

        iv.setFitWidth(largura);
        iv.setFitHeight(altura);
        iv.setPreserveRatio(false);

        return iv;
    }

    private void iniciarAnimacao() {
        PauseTransition espera = new PauseTransition(Duration.seconds(3));

        espera.setOnFinished(event -> trocarImagem());

        espera.play();
    }

    private void trocarImagem() {
        int proximoIndice = indiceAtual + 1;

        if (proximoIndice >= imagens.size()) {
            System.out.println("Animação finalizada");
            aoFinalizar.run();
            return;
        }

        proximaImagem.setImage(imagens.get(proximoIndice));
        proximaImagem.setOpacity(0);

        FadeTransition fadeOut =
                new FadeTransition(Duration.seconds(1), imagemAtual);

        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        FadeTransition fadeIn =
                new FadeTransition(Duration.seconds(1), proximaImagem);

        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ParallelTransition crossFade =
                new ParallelTransition(fadeOut, fadeIn);

        crossFade.setOnFinished(event -> {
            imagemAtual.setImage(imagens.get(proximoIndice));
            imagemAtual.setOpacity(1);

            proximaImagem.setOpacity(0);

            indiceAtual = proximoIndice;

            iniciarAnimacao();
        });

        crossFade.play();
    }
}
package view.animacao;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public final class AnimacaoFramesUtil {

    private AnimacaoFramesUtil() {
    }

    public static ImageView criarImagem(String sprite) {
        Image backgroundImage = new Image(
                Objects.requireNonNull(
                        AnimacaoFramesUtil.class.getResourceAsStream(sprite)
                )
        );

        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(1920);
        backgroundView.setFitHeight(1080);

        return backgroundView;
    }

    public static void iniciarAnimacao(
            StackPane container,
            List<Node> frames,
            Runnable aoFinalizar
    ) {
        iniciarAnimacao(
                container,
                frames,
                indice -> aoFinalizar.run()
        );
    }

    public static void iniciarAnimacao(
            StackPane container,
            List<Node> frames,
            Consumer<Integer> aoFinalizar
    ) {
        iniciarAnimacao(
                container,
                frames,
                0,
                aoFinalizar
        );
    }

    private static void iniciarAnimacao(
            StackPane container,
            List<Node> frames,
            int indiceAtual,
            Consumer<Integer> aoFinalizar
    ) {
        PauseTransition espera = new PauseTransition(Duration.seconds(3));

        espera.setOnFinished(event ->
                trocarImagem(
                        container,
                        frames,
                        indiceAtual,
                        aoFinalizar
                )
        );

        espera.play();
    }

    private static void trocarImagem(
            StackPane container,
            List<Node> frames,
            int indiceAtual,
            Consumer<Integer> aoFinalizar
    ) {
        int proximoIndice = indiceAtual + 1;

        if (proximoIndice >= frames.size()) {
            aoFinalizar.accept(indiceAtual);
            return;
        }

        Node atual = frames.get(indiceAtual);
        Node proximo = frames.get(proximoIndice);

        proximo.setOpacity(0);
        container.getChildren().add(proximo);

        FadeTransition fadeOut = new FadeTransition(
                Duration.seconds(0.1),
                atual
        );
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        FadeTransition fadeIn = new FadeTransition(
                Duration.seconds(0.1),
                proximo
        );
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ParallelTransition crossFade = new ParallelTransition(
                fadeOut,
                fadeIn
        );

        crossFade.setOnFinished(event -> {
            container.getChildren().remove(atual);

            iniciarAnimacao(
                    container,
                    frames,
                    proximoIndice,
                    aoFinalizar
            );
        });

        crossFade.play();
    }
}
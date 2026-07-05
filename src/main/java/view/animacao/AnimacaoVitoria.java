package view.animacao;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.List;

public class AnimacaoVitoria extends StackPane {

    private final Runnable aoFinalizar;
    private final List<Node> frames;

    public AnimacaoVitoria(Runnable aoFinalizar) {
        this.aoFinalizar = aoFinalizar;

        this.frames = List.of(
                AnimacaoFramesUtil.criarImagem("/animacao/blank.png"),
                AnimacaoFramesUtil.criarImagem("/animacao/Animacao_conclusao_jogo.png")
        );

        getChildren().add(frames.get(0));

        AnimacaoFramesUtil.iniciarAnimacao(
                this,
                frames,
                Duration.seconds(3), // ajuste conforme o tempo que você quer só pra vitória
                this.aoFinalizar
        );
    }
}
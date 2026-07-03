package view.animacao;

import controller.GameController;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.util.List;
import java.util.function.Consumer;

public class AnimacaoFimView extends StackPane {
    private final Runnable aoFinalizar;
    private final List<Node> frames;

    public AnimacaoFimView(Runnable aoFinalizar){
        this.aoFinalizar = () -> aoFinalizar.run();
        this.frames = List.of(
                AnimacaoFramesUtil.criarImagem("/animacao/Animacao_fim.png"));
        getChildren().add(frames.get(0));

        AnimacaoFramesUtil.iniciarAnimacao(
                this,
                frames,
                this.aoFinalizar
        );
    }


}

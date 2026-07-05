package view.animacao;

import controller.GameController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import view.util.FonteUtil;

import java.util.List;
import java.util.Objects;

public class AnimacaoInicioView extends StackPane {

    private final Runnable aoFinalizar;
    private final GameController gameController;
    private final List<Node> frames;

    public AnimacaoInicioView(
            Runnable aoFinalizar,
            Runnable aoConcluirJogo,
            GameController gameController,
            int sessaoAtual
    ) {
        this.aoFinalizar = () -> {
            if (gameController.isJogoConcluido()) {
                aoConcluirJogo.run();
                return;
            }

            if (!gameController.precisaEscolherDisciplinas()) {
                gameController.iniciarProximoDia();
            }
            // Se precisar escolher disciplinas, quem inicia o dia é
            // confirmarEscolhaDisciplinas(), chamado após a escolha.
            aoFinalizar.run();
        };

        this.gameController = gameController;

        // Apenas carrega/retoma o estado do jogo.
        gameController.iniciarJogo(sessaoAtual);

        this.frames = List.of(
                AnimacaoFramesUtil.criarImagem("/animacao/Animacao_inicio1.png"),
                AnimacaoFramesUtil.criarImagem("/animacao/Animacao_inicio2.png"),
                montarTelaInicioDia()
        );

        getChildren().add(frames.get(0));

        AnimacaoFramesUtil.iniciarAnimacao(
                this,
                frames,
                this.aoFinalizar
        );
    }

    private StackPane montarTelaInicioDia() {
        ImageView backgroundView =
                AnimacaoFramesUtil.criarImagem("/assets/background/background.png");

        VBox inicioItens = new VBox();
        inicioItens.setAlignment(Pos.CENTER);

        Image bg = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream(
                                "/menuPersonagens/background_botao.png"
                        )
                )
        );

        ImageView bgView = new ImageView(bg);
        bgView.setFitWidth(600);
        bgView.setPreserveRatio(true);

        String labelDia = gameController.isJogoConcluido()
                ? "Vitória!"
                : gameController.precisaEscolherDisciplinas()
                ? "Novo semestre"
                : "Dia " + gameController.getDiaAtual();

        Text bgTexto = new Text(labelDia);
        bgTexto.setFont(FonteUtil.pixel(40));
        bgTexto.setFill(Color.WHITE);

        StackPane bgComTexto = new StackPane(bgView, bgTexto);
        bgComTexto.setAlignment(Pos.CENTER);
        bgTexto.setTranslateY(-10);

        Text texto = new Text("Carregando...");
        texto.setFill(Color.WHITE);
        texto.setFont(FonteUtil.pixel(40));

        inicioItens.getChildren().add(bgComTexto);

        StackPane tela = new StackPane();
        StackPane.setAlignment(texto, Pos.BOTTOM_CENTER);
        StackPane.setMargin(texto, new Insets(0, 0, 100, 0));

        tela.getChildren().addAll(
                backgroundView,
                inicioItens,
                texto
        );

        return tela;
    }
}
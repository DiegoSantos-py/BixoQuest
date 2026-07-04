package view.menu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import model.Disciplina.AreaConhecimento;
import model.Evento.Evento;
import view.animacao.AnimacaoFramesUtil;
import view.util.FonteUtil;

import java.util.List;
import java.util.Map;

public class MenuFeedbackEvento extends StackPane {

    private final Runnable aoFechar;

    private StackPane containerImagens;
    private VBox painelEfeitos;
    private Button btnContinuar;

    public MenuFeedbackEvento(Runnable aoFechar) {
        this.aoFechar = aoFechar;
        montarTela();
        setVisible(false);
        setManaged(false);
    }

    private void montarTela() {
        Region overlay = new Region();
        overlay.setBackground(new Background(new BackgroundFill(
                Color.rgb(0, 0, 0, 0.75), CornerRadii.EMPTY, Insets.EMPTY)));
        overlay.prefWidthProperty().bind(widthProperty());
        overlay.prefHeightProperty().bind(heightProperty());

        VBox conteudo = new VBox(20);
        conteudo.setAlignment(Pos.CENTER);
        conteudo.setMaxSize(900, 600);

        containerImagens = new StackPane();
        containerImagens.setPrefSize(700, 400);

        painelEfeitos = new VBox(10);
        painelEfeitos.setAlignment(Pos.CENTER);

        btnContinuar = new Button("Continuar");
        btnContinuar.setFont(FonteUtil.pixel(22));
        btnContinuar.setTextFill(Color.WHITE);
        btnContinuar.setBackground(Background.EMPTY);
        btnContinuar.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID,
                new CornerRadii(6), new BorderWidths(2))));
        btnContinuar.setPrefSize(200, 50);
        btnContinuar.setVisible(false); // só aparece quando a animação terminar
        btnContinuar.setOnAction(e -> fechar());

        conteudo.getChildren().addAll(containerImagens, painelEfeitos, btnContinuar);

        getChildren().addAll(overlay, conteudo);
    }

    public void abrir(Evento evento) {
        containerImagens.getChildren().clear();
        btnContinuar.setVisible(false);
        montarEfeitos(evento);

        List<Node> frames = evento.getImagensAnimacao().stream()
                .map(AnimacaoFramesUtil::criarImagem)
                .map(iv -> { // redimensiona pro tamanho do container, em vez do fitWidth/Height fixo de 1920x1080
                    iv.setFitWidth(1100);
                    iv.setFitHeight(700);
                    return (Node) iv;
                })
                .toList();

        if (frames.isEmpty()) {
            btnContinuar.setVisible(true);
        } else {
            containerImagens.getChildren().add(frames.get(0));
            AnimacaoFramesUtil.iniciarAnimacao(
                    containerImagens,
                    frames,
                    () -> btnContinuar.setVisible(true) // só libera o botão ao terminar a sequência
            );
        }

        setVisible(true);
        setManaged(true);
        toFront();
    }

    private void montarEfeitos(Evento evento) {
        painelEfeitos.getChildren().clear();

        Label titulo = new Label(evento.getNome());
        titulo.setFont(FonteUtil.pixel(28));
        titulo.setTextFill(Color.WHITE);
        painelEfeitos.getChildren().add(titulo);

        adicionarLinhaEfeito("Energia", evento.getEfeitoEnergia());
        adicionarLinhaEfeito("Motivação", evento.getEfeitoMotivacao());
        adicionarLinhaEfeito("Saúde", evento.getEfeitoSaude());
        adicionarLinhaEfeito("Dinheiro", evento.getEfeitoDinheiro());

        if (evento.getEfeitosConhecimento() != null) {
            for (Map.Entry<AreaConhecimento, Double> entry : evento.getEfeitosConhecimento().entrySet()) {
                adicionarLinhaEfeito("Conhecimento (" + entry.getKey() + ")", entry.getValue());
            }
        }
    }

    private void adicionarLinhaEfeito(String nome, double valor) {
        if (valor == 0) return;

        String sinal = valor > 0 ? "+" : "";
        Label label = new Label(nome + ": " + sinal + valor);
        label.setFont(FonteUtil.pixel(18));
        label.setTextFill(valor > 0 ? Color.LIGHTGREEN : Color.SALMON);
        painelEfeitos.getChildren().add(label);
    }

    private void fechar() {
        setVisible(false);
        setManaged(false);
        aoFechar.run();
    }
}
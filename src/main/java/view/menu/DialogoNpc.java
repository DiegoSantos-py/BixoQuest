package view.menu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.Npc.Npc;
import view.util.FonteUtil;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class DialogoNpc extends StackPane {

    private final Consumer<Npc> aoFinalizar;

    private Npc npcAtual;
    private int indiceFala;
    private Label textoLabel;

    // Dimensões da área de texto DENTRO da imagem de fundo — ajuste conforme o design real
    private static final double CAIXA_LARGURA = 1200;
    private static final double CAIXA_ALTURA = 500;
    private static final double TEXTO_LARGURA = 400;   // largura útil da "bolha" de texto na imagem
    private static final double TEXTO_ALTURA = 300;    // altura útil da "bolha" de texto na imagem
    private static final double TEXTO_OFFSET_X = 40;  // deslocamento até a área de texto na imagem
    private static final double TEXTO_OFFSET_Y = 120;

    public DialogoNpc(Consumer<Npc> aoFinalizar) {
        this.aoFinalizar = aoFinalizar;
        montarTela();
        setVisible(false);
        setManaged(false);
    }

    private void montarTela() {
        ImageView caixaFundo = new ImageView(
                new Image(Objects.requireNonNull(
                        getClass().getResourceAsStream("/menuPersonagens/background_interacao_npcs.png")
                ))
        );
        caixaFundo.setFitWidth(CAIXA_LARGURA);
        caixaFundo.setFitHeight(CAIXA_ALTURA);
        caixaFundo.setPreserveRatio(false);
        caixaFundo.setTranslateY(50);

        textoLabel = new Label();
        textoLabel.setFont(FonteUtil.pixel(32)); // reduzido de 48 — texto grande demais estoura mais fácil
        textoLabel.setTextFill(Color.WHITE);
        textoLabel.setWrapText(true);
        textoLabel.setAlignment(Pos.TOP_LEFT);

        // Área fixa de texto — o Label nunca cresce além disso
        textoLabel.setMinSize(TEXTO_LARGURA, TEXTO_ALTURA);
        textoLabel.setMaxSize(TEXTO_LARGURA, TEXTO_ALTURA);
        textoLabel.setPrefSize(TEXTO_LARGURA, TEXTO_ALTURA);
        textoLabel.setTranslateX(TEXTO_OFFSET_X);
        textoLabel.setTranslateY(TEXTO_OFFSET_Y);

        // Clip garante que nada vaze visualmente, mesmo se o texto for maior que a área
        Rectangle clip = new Rectangle(TEXTO_LARGURA, TEXTO_ALTURA);
        textoLabel.setClip(clip);

        StackPane.setAlignment(caixaFundo, Pos.BOTTOM_CENTER);
        StackPane.setAlignment(textoLabel, Pos.BOTTOM_CENTER);
        StackPane.setMargin(caixaFundo, new Insets(0, 0, 60, 0));
        StackPane.setMargin(textoLabel, new Insets(0, 0, 130, 0));

        getChildren().addAll(caixaFundo, textoLabel);

        addEventHandler(MouseEvent.MOUSE_CLICKED, e -> onClique());
    }

    public void abrir(Npc npc) {
        this.npcAtual = npc;
        this.indiceFala = 0;
        mostrarFalaAtual();
        setVisible(true);
        setManaged(true);
        toFront();
    }

    private void mostrarFalaAtual() {
        List<String> falas = npcAtual.getFalas();
        if (indiceFala < falas.size()) {
            textoLabel.setText(falas.get(indiceFala));
        }
    }

    private void onClique() {
        indiceFala++;
        if (indiceFala >= npcAtual.getFalas().size()) {
            finalizarDialogo();
        } else {
            mostrarFalaAtual();
        }
    }

    private void finalizarDialogo() {
        setVisible(false);
        setManaged(false);
        Npc npc = npcAtual;
        npcAtual = null;
        aoFinalizar.accept(npc);
    }
}
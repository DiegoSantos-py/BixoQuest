package view.menu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
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

    public DialogoNpc(Consumer<Npc> aoFinalizar) {
        this.aoFinalizar = aoFinalizar;
        montarTela();
        setVisible(false);
        setManaged(false);
    }

    private void montarTela() {
        ImageView caixaFundo = new ImageView(
                new Image(Objects.requireNonNull(
                        getClass().getResourceAsStream("/menuPersonagens/background_botao.png")
                ))
        );
        caixaFundo.setFitWidth(1200);
        caixaFundo.setFitHeight(250);
        caixaFundo.setPreserveRatio(false);

        textoLabel = new Label();
        textoLabel.setFont(FonteUtil.pixel(22));
        textoLabel.setTextFill(Color.WHITE);
        textoLabel.setWrapText(true);
        textoLabel.setMaxWidth(1000);

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
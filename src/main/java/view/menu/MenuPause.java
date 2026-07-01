package view.menu;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import view.util.FonteUtil;

import java.util.Objects;

public class MenuPause extends StackPane {
    private final Runnable onPressionarContinuar;
    private final Runnable onPressionarSair;

    public MenuPause(Runnable onPressionarContinuar, Runnable onPressionarSair) {
        this.onPressionarContinuar = onPressionarContinuar;
        this.onPressionarSair = onPressionarSair;

        montarTela();
        setVisible(false);
        setManaged(false); // não ocupa espaço no layout enquanto escondido
    }

    private void montarTela() {
        VBox formContainer = new VBox(20);
        formContainer.setAlignment(Pos.CENTER);

        ImageView backgroundView = new ImageView(
                new Image(
                        Objects.requireNonNull(
                                getClass().getResourceAsStream("/menuPersonagens/BackgroundMenu.png")
                        )
                )
        );
        // acompanha o tamanho real do StackPane pai, em vez de valor fixo
        backgroundView.fitWidthProperty().bind(widthProperty());
        backgroundView.fitHeightProperty().bind(heightProperty());

        Button btnContinuar = criarBotao("/menuPersonagens/background_botao.png", "Continuar", onPressionarContinuar);
        Button btnSair = criarBotao("/menuPersonagens/background_botao.png", "Sair", onPressionarSair);

        formContainer.getChildren().addAll(btnContinuar, btnSair);

        getChildren().addAll(backgroundView, formContainer);
    }

    private Button criarBotao(String caminhoMoldura, String texto, Runnable acao) {

        ImageView moldura = new ImageView(
                new Image(
                        Objects.requireNonNull(
                                getClass().getResourceAsStream(caminhoMoldura)
                        )
                )
        );

        moldura.setFitWidth(300);
        moldura.setPreserveRatio(true);

        Label label = new Label(texto);
        label.setFont(FonteUtil.pixel(24));
        label.setStyle("-fx-text-fill: white;");

        StackPane conteudo = new StackPane(
                moldura,
                label
        );

        Button btn = new Button();
        btn.setFocusTraversable(false);
        btn.setGraphic(conteudo);
        btn.setBackground(Background.EMPTY);
        btn.setBorder(Border.EMPTY);
        btn.setOnAction(e -> acao.run());

        return btn;
    }

    public void abrir() {
        setVisible(true);
        setManaged(true);
        toFront();
    }

    public void fechar() {
        setVisible(false);
        setManaged(false);

    }
}
package view.menu;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import view.util.FonteUtil;

import java.util.Objects;

public class MenuPause extends StackPane {
    private final Runnable onPressionarSair;

    public MenuPause(Runnable onPressionarSair){
        this.onPressionarSair = onPressionarSair;

        montarTela();
    }

    private void montarTela() {
        VBox formContainer = new VBox(20);
        formContainer.setAlignment(Pos.BOTTOM_CENTER);

        Image backgroundImage = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream("/menuPersonagens/BackgroundMenu.png")
                )
        );

        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(1920);
        backgroundView.setFitHeight(1080);

        Button btnSair = criarBotao("/menuPersonagens/background_botao.png");

        formContainer.getChildren().addAll(
                btnSair
        );

        getChildren().addAll(backgroundView, formContainer);
    }

    private Button criarBotao(String caminhoMoldura) {
        ImageView moldura = new ImageView(
                new Image(
                        Objects.requireNonNull(
                                getClass().getResourceAsStream(caminhoMoldura)
                        )
                )
        );

        moldura.setFitWidth(300);
        moldura.setFitHeight(300);
        moldura.setPreserveRatio(true);

        StackPane conteudo = new StackPane();
        StackPane.setAlignment(moldura, Pos.CENTER);

        conteudo.getChildren().addAll(moldura);

        Button btnSair = new Button("Sair");
        btnSair.setPrefSize(350, 100);
        btnSair.setFont(FonteUtil.pixel(24));
        btnSair.setBackground(Background.EMPTY);
        btnSair.setBorder(Border.EMPTY);
        btnSair.setGraphic(conteudo);

        btnSair.setOnAction(event -> {
            onPressionarSair.run();
        });


        return btnSair;
    }
}

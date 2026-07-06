package view.batalha;

import controller.BatalhaController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import view.util.FonteUtil;

public class MenuPrincipalBatalha {

    private final BatalhaController controller;

    public MenuPrincipalBatalha(BatalhaController controller) {
        this.controller = controller;
    }
//a caixa de texto e os 2 botoes principais( ataque e acoes)
    public Node build(Runnable onMinigame, Runnable onAcoes) {
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        //gera o container da caixa de texto com as acoes
        StackPane caixaTexto = new StackPane();
        caixaTexto.setPrefSize(1000, 150);
        caixaTexto.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, new BorderWidths(4))));

        Text textDialogo = new Text();
        textDialogo.setFill(Color.WHITE);
        textDialogo.setFont(FonteUtil.pixel(20));
        //tenta pegar os textos do oponente pra botar na caixinha
        if (controller.getEstadoAtual() != null
                && controller.getEstadoAtual().getOponenteAtual() != null
                && controller.getEstadoAtual().getOponenteAtual().getTextoCaixa() != null) {
            textDialogo.setText(controller.getEstadoAtual().getOponenteAtual().getTextoCaixa().toUpperCase());
        } else {
            textDialogo.setText(controller.isBatalhaAnimal() ? "ELE PARECE COM FOME." : "A QUESTÃO PARECE DIFÍCIL."); //defaults
        }
        caixaTexto.getChildren().add(textDialogo);

        HBox botoesBox = new HBox(40);
        botoesBox.setAlignment(Pos.CENTER);
        //gera os botões principais
        Button btnAcao = criarBotaoLaranja(controller.isBatalhaAnimal() ? "TENTAR DOMAR" : "RESPONDER");
        btnAcao.setOnAction(e -> onMinigame.run());//atrobui

        Button btnAcoes = criarBotaoLaranja("AÇÕES DISPONÍVEIS");
        btnAcoes.setOnAction(e -> onAcoes.run());//as acoes( minigame e acoes)

        botoesBox.getChildren().addAll(btnAcao, btnAcoes);
        container.getChildren().addAll(caixaTexto, botoesBox);
        return container;
    }


    //importaet pra criar especificamente o botão laranja
    private Button criarBotaoLaranja(String texto) {
        Button btn = new Button(texto);
        btn.setFont(FonteUtil.pixel(16));
        btn.setTextFill(Color.WHITE);
        btn.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        btn.setBorder(new Border(new BorderStroke(Color.web("#FFA500"), BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, new BorderWidths(4))));
        btn.setPrefSize(200, 60);
        btn.setOnMouseEntered(e -> btn.setBackground(
                new Background(new BackgroundFill(Color.web("#333333"), CornerRadii.EMPTY, Insets.EMPTY))));  //muda a cor sob hover
        btn.setOnMouseExited(e -> btn.setBackground(
                new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)))); //muda de volta qndo sai
        return btn;
    }
}

package view.batalha;

import controller.BatalhaController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import view.util.FonteUtil;

import java.util.ArrayList;

/** Builder puro para as telas de finalização de batalha (animal e prova). */
public class TelaFinalBatalha {

    private final BatalhaController controller;

    public TelaFinalBatalha(BatalhaController controller) {
        this.controller = controller;
    }

    //gera a tela pronta com o runnable pra eu usar ou no menu seletor de batalha ou no jogo em si
    public Node buildAnimal(Runnable aoVoltar) {
        VBox box = new VBox(20);
        box.setAlignment(Pos.CENTER);

        boolean vitoria = controller.getEstadoAtual().isVitoria();

        Text titulo = new Text("BATALHA FINALIZADA");
        titulo.setFont(FonteUtil.pixel(32));
        titulo.setFill(Color.WHITE);

        Text resultado = new Text(vitoria ? "VOCÊ VENCEU!" : "VOCÊ PERDEU...");
        resultado.setFont(FonteUtil.pixel(24));
        resultado.setFill(vitoria ? Color.YELLOW : Color.RED);

        Button btn = criarBotaoLaranja("VOLTAR");
        btn.setOnAction(e -> aoVoltar.run());

        box.getChildren().addAll(titulo, resultado, btn);
        return box;
    }

    public Node buildProva(Runnable aoVoltar) {
        boolean vitoria = controller.getEstadoAtual().isVitoria();
        model.Player.PlayerProva pp = controller.getEstadoAtual().getPlayerProva();

        BorderPane layout = new BorderPane();


        VBox topLeft = new VBox(10);
        Text t1 = new Text(vitoria ? "PROVA CONCLUÍDA" : "PROVA FALHOU!");
        t1.setFont(FonteUtil.pixel(32));
        t1.setFill(Color.WHITE);
        Text t2 = new Text(vitoria ? "\"FOI UM SUCESSO!\"" : "\"É... VOCE PERDEU!\"");
        t2.setFont(FonteUtil.pixel(20));
        t2.setFill(Color.WHITE);
        topLeft.getChildren().addAll(t1, t2);

        int turnos = pp.getTurnosUsados();
        int maxTurnos = 30;
        if (controller.getEstadoAtual().getOponenteAtual() != null)
            maxTurnos = controller.getEstadoAtual().getOponenteAtual().getMaxTurnos();

        Text t3 = new Text("TURNOS:\n" + turnos + "/" + maxTurnos);
        t3.setFont(FonteUtil.pixel(24));
        t3.setFill(Color.WHITE);
        t3.setTextAlignment(TextAlignment.CENTER);
        VBox topRight = new VBox(t3);
        topRight.setAlignment(Pos.TOP_RIGHT);

        BorderPane top = new BorderPane();
        top.setLeft(topLeft);
        top.setRight(topRight);
        layout.setTop(top);

        HBox center = new HBox(50);
        center.setAlignment(Pos.CENTER);
        center.setPadding(new Insets(20, 0, 20, 0));

        VBox col1 = new VBox(10);
        Text c1t1 = new Text("DESEMPENHO GERAL:");
        c1t1.setFont(FonteUtil.pixel(16));
        c1t1.setFill(Color.WHITE);
        col1.getChildren().add(c1t1);

        ArrayList<Float> desempenhos = pp.getDesempenhoQuestoes();
        for (int i = 0; i < desempenhos.size(); i++) {
            Text qT = new Text(String.format("QUESTÃO %d: %.0f%%", (i + 1), desempenhos.get(i) * 10f));
            qT.setFont(FonteUtil.pixel(16));
            qT.setFill(Color.WHITE);
            col1.getChildren().add(qT);
        }

        VBox col2 = new VBox(10);
        col2.setAlignment(Pos.CENTER);
        Text c2t1 = new Text("BÔNUS:");
        c2t1.setFont(FonteUtil.pixel(16));
        c2t1.setFill(Color.WHITE);
        col2.getChildren().add(c2t1);

        boolean perfeitos = pp.getTodosAcertosPerfeitos();
        boolean nPNota    = !pp.getPerdeuNota();
        boolean nLDano    = !pp.getLevouAlgumDano();
        boolean m10       = turnos <= 10;
        //monta os textos dos bonuses
        for (String[] par : new String[][]{
                {"TODOS OS ATAQUES\nPERFEITOS:", perfeitos ? "+25%" : "0%"},
                {"NÃO PERDEU NOTA:",             nPNota   ? "+25%" : "0%"},
                {"NÃO LEVOU NENHUM\nDANO:",      nLDano   ? "+25%" : "0%"},
                {"MENOS DE 10\nTURNOS:",          m10      ? "+25%" : "0%"}}) {
            Text b = new Text(par[0] + " " + par[1]);
            b.setFont(FonteUtil.pixel(12));
            b.setFill(Color.WHITE);
            b.setTextAlignment(TextAlignment.CENTER);
            col2.getChildren().add(b);
        }

        center.getChildren().addAll(col1, col2);
        layout.setCenter(center);


        float nf = service.batalha.BatalhaFinalizacaoService.calcularNotaFinal(desempenhos, pp);
        Text notaText = new Text(String.format("NOTA FINAL: %.2f", nf));
        notaText.setFont(FonteUtil.pixel(24));
        notaText.setFill(Color.WHITE);

        Button btn = criarBotaoLaranja("CONTINUAR");
        btn.setOnAction(e -> aoVoltar.run());

        BorderPane bottom = new BorderPane();
        bottom.setLeft(notaText);
        bottom.setRight(btn);
        BorderPane.setAlignment(notaText, Pos.CENTER_LEFT);
        BorderPane.setAlignment(btn, Pos.CENTER_RIGHT);
        layout.setBottom(bottom);

        return layout;
    }

    private Button criarBotaoLaranja(String texto) {
        Button btn = new Button(texto);
        btn.setFont(FonteUtil.pixel(16));
        btn.setTextFill(Color.WHITE);
        btn.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        btn.setBorder(new Border(new BorderStroke(Color.web("#FFA500"), BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, new BorderWidths(4))));
        btn.setPrefSize(200, 60);
        btn.setOnMouseEntered(e -> btn.setBackground(
                new Background(new BackgroundFill(Color.web("#333333"), CornerRadii.EMPTY, Insets.EMPTY))));
        btn.setOnMouseExited(e -> btn.setBackground(
                new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY))));
        return btn;
    }
}

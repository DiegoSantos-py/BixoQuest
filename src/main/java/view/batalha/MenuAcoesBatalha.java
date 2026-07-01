package view.batalha;

import controller.BatalhaController;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.Player.AcaoBatalha;
import view.util.FonteUtil;

import java.util.List;
import java.util.function.Consumer;

public class MenuAcoesBatalha {

    private final BatalhaController controller;

    public MenuAcoesBatalha(BatalhaController controller) {
        this.controller = controller;
    }

    public Node build(Consumer<String> onFeedbackPronto) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(40);
        grid.setVgap(20);

        List<AcaoBatalha> acoes = controller.getAcoes();
        int col = 0, row = 0;

        for (int i = 0; i < acoes.size(); i++) {
            AcaoBatalha acao = acoes.get(i);
            Button btn = criarBotaoTextoSimples(acao.getNome().toUpperCase());
            int indexFinal = i;

            btn.setOnAction(e -> {
                boolean sucesso = controller.executarAcao(indexFinal);
                onFeedbackPronto.accept(montarTextoFeedback(acao, sucesso));
            });

            grid.add(btn, col, row);
            col++;
            if (col > 1) { col = 0; row++; }
        }

        return grid;
    }

    public Node buildFeedback(String textoFeedback) {
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);

        StackPane caixaTexto = new StackPane();
        caixaTexto.setPrefSize(1000, 150);
        caixaTexto.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, new BorderWidths(4))));

        Text feedback = new Text(textoFeedback);
        feedback.setFill(Color.WHITE);
        feedback.setFont(FonteUtil.pixel(20));

        caixaTexto.getChildren().add(feedback);
        container.getChildren().add(caixaTexto);
        return container;
    }

    private String montarTextoFeedback(AcaoBatalha acao, boolean sucesso) {
        StringBuilder sb = new StringBuilder("VOCÊ TENTOU \"")
                .append(acao.getNome().toUpperCase()).append("\".\n\n");
        if (sucesso) {
            sb.append("E CONSEGUIU! ATRIBUTOS MODIFICADOS:\n");
            if (acao.getBonusDano() > 0)         sb.append("+ ").append(acao.getBonusDano()).append(" DANO\n");
            if (acao.getBonusShield() > 0)       sb.append("+ ").append(acao.getBonusShield()).append(" ESCUDO\n");
            if (acao.getBonusConhecimento() > 0)  sb.append("+ ").append(acao.getBonusConhecimento()).append(" CONHECIMENTO\n");
            if (acao.getBonusNota() > 0)          sb.append("+ ").append(acao.getBonusNota()).append(" NOTA\n");
        } else {
            sb.append("MAS FALHOU... NADA ACONTECEU.");
        }
        return sb.toString();
    }

    private Button criarBotaoTextoSimples(String texto) {
        Button btn = new Button(texto);
        btn.setFont(FonteUtil.pixel(20));
        btn.setTextFill(Color.WHITE);
        btn.setBackground(Background.EMPTY);
        btn.setBorder(Border.EMPTY);
        btn.setOnMouseEntered(e -> btn.setTextFill(Color.web("#FF8C00")));
        btn.setOnMouseExited(e -> btn.setTextFill(Color.WHITE));
        return btn;
    }
}

package view.batalha;

import controller.BatalhaController;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
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
        List<AcaoBatalha> acoes = controller.getEstadoController().getAcoesBatalha();//pega as acoes
        int col = 0, row = 0;
        //monta o grid
        for (int i = 0; i < acoes.size(); i++) {
            AcaoBatalha acao = acoes.get(i);
            Button btn = criarBotaoTextoSimples(acao);
            int indexFinal = i;

            btn.setOnAction(e ->
            //atribui a acao em si via via lambda
            {
                float danoOld = acao.getBonusDano();
                int shieldOld = acao.getBonusShield();
                float conhecimentoOld = acao.getBonusConhecimento();
                float notaOld = acao.getBonusNota();
                //^^  pro texto do feedback n usar os dados DEPOIS da acao se atualizar(reduzir cahcen de acerto e bonus)
                
                boolean sucesso = controller.executarAcao(indexFinal); //tenta executar a acao

                onFeedbackPronto.accept(montarTextoFeedback(acao.getNome(), danoOld, shieldOld, conhecimentoOld, notaOld, sucesso)); //inia a geracao do texto de feedback
                //com os dados
            });

            grid.add(btn, col, row);
            col++;
            if (col > 1) {
                col = 0;
                row++;
            }
        }

        return grid;
    }

    //funcao pra montar o texto em caso ed sucesso
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

    private String montarTextoFeedback(String acaoNome, float bonusDano, int bonusShield, float bonusConhecimento, float bonusNota, boolean sucesso) {
        StringBuilder textoFeedback = new StringBuilder("VOCÊ TENTOU \"")
                .append(acaoNome.toUpperCase()).append("\".\n\n");
        if (sucesso) {
            textoFeedback.append("E CONSEGUIU! ATRIBUTOS MODIFICADOS:\n");
            if (bonusDano > 0)         textoFeedback.append("+ ").append(bonusDano).append(" DANO\n");
            if (bonusShield > 0)       textoFeedback.append("+ ").append(bonusShield).append(" ESCUDO\n");
            if (bonusConhecimento > 0)  textoFeedback.append("+ ").append(bonusConhecimento).append(" CONHECIMENTO\n");
            if (bonusNota > 0)          textoFeedback.append("+ ").append(bonusNota).append(" NOTA\n");
        } else {
            textoFeedback.append("MAS FALHOU... NADA ACONTECEU.");
        }
        return textoFeedback.toString();
    }

    private Button criarBotaoTextoSimples(AcaoBatalha acao) {
        String textoBotao = acao.getNome().toUpperCase() + "\n" + "(" + (acao.getChanceAcerto() * 100)
                + "% DE CHANCE DE ACERTO" + ")";
        StringBuilder textoOnHover = new StringBuilder();

        textoOnHover.append("ATRIBUTOS:\n");

        if (acao.getBonusDano() > 0)
            textoOnHover.append("+ ").append(acao.getBonusDano()).append(" DANO\n");

        if (acao.getBonusShield() > 0)
            textoOnHover.append("+ ").append(acao.getBonusShield()).append(" ESCUDO\n");

        if (acao.getBonusConhecimento() > 0)
            textoOnHover.append("+ ").append(acao.getBonusConhecimento()).append(" CONHECIMENTO\n");

        if (acao.getBonusNota() > 0)
            textoOnHover.append("+ ").append(acao.getBonusNota()).append(" NOTA\n");

        if (textoOnHover.toString().equals("ATRIBUTOS:\n")) {
            textoOnHover.append("SEM BÔNUS");
        }

        Button btn = new Button(textoBotao);
        btn.setFont(FonteUtil.pixel(20));
        btn.setTextFill(Color.WHITE);
        btn.setBackground(Background.EMPTY);
        btn.setBorder(Border.EMPTY);
        btn.setOnMouseEntered(e -> {
            btn.setTextFill(Color.web("#FF8C00"));
            btn.setText(textoOnHover.toString());
        });
        btn.setOnMouseExited(e -> {
            btn.setTextFill(Color.WHITE);
            btn.setText(textoBotao);
        });
        btn.setAlignment(Pos.CENTER);
        btn.setTextAlignment(TextAlignment.CENTER);
        btn.setContentDisplay(ContentDisplay.CENTER);
        btn.setMinSize(290, 100);
        btn.setPrefSize(290, 100);
        btn.setMaxSize(290, 100);

        return btn;
    }
}

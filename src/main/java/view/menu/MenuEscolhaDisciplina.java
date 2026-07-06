package view.menu;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.Disciplina.Disciplina;
import view.animacao.AnimacaoFramesUtil;
import view.util.FonteUtil;
import view.util.ImagemCache;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MenuEscolhaDisciplina extends StackPane {

    private static final int MAX_DISCIPLINAS = 3;

    private final Consumer<List<Disciplina>> onConfirmar;

    private final GridPane painelOpcoes;
    private final List<ItemDisciplina> itens = new ArrayList<>();
    private final List<Disciplina> disciplinasAtuais = new ArrayList<>();

    private Text aviso;

    private static class ItemDisciplina {

        private final StackPane view;
        private final Rectangle fundo;
        private final Disciplina disciplina;

        private boolean selecionado;
        private boolean desabilitado;

        ItemDisciplina(Disciplina disciplina) {

            this.disciplina = disciplina;

            fundo = new Rectangle(260, 90);
            fundo.setArcWidth(20);
            fundo.setArcHeight(20);
            fundo.setFill(Color.rgb(255,255,255,0.85));
            fundo.setStroke(Color.DARKGRAY);
            fundo.setStrokeWidth(3);

            Text texto = new Text(
                    disciplina.getNome() + "\nCódigo: " + (int) disciplina.getCodigo());

            texto.setFont(FonteUtil.pixel(26));
            texto.setFill(Color.BLACK);

            view = new StackPane(fundo, texto);
        }

        public StackPane getView() {
            return view;
        }

        public Disciplina getDisciplina() {
            return disciplina;
        }

        public boolean isSelecionado() {
            return selecionado;
        }

        public void setSelecionado(boolean valor) {
            selecionado = valor;

            fundo.setStrokeWidth(3);

            if (valor) {
                fundo.setStroke(Color.GOLD);
            } else {
                fundo.setStroke(Color.DARKGRAY);
            }
        }

        public void setDesabilitado(boolean valor) {
            desabilitado = valor;

            if (!selecionado) {
                view.setOpacity(valor ? 0.35 : 1.0);
            }
        }

        public boolean isDesabilitado() {
            return desabilitado;
        }
    }

    public MenuEscolhaDisciplina(Consumer<List<Disciplina>> onConfirmar) {
        this.onConfirmar = onConfirmar;

        painelOpcoes = new GridPane();
        painelOpcoes.setHgap(20);
        painelOpcoes.setVgap(20);
        painelOpcoes.setAlignment(Pos.CENTER);

        montarTela();

        setVisible(false);
        setManaged(false);
    }

    private void montarTela() {

        ImageView backgroundView = new ImageView(ImagemCache.get("/assets/background/backgroud_matricula.png"));
        backgroundView.setFitWidth(1920);
        backgroundView.setFitHeight(1080);
        backgroundView.setPreserveRatio(false);

        VBox conteudo = new VBox(20);
        conteudo.setAlignment(Pos.CENTER);
        conteudo.setTranslateY(35);
        conteudo.setTranslateX(115);

        Text titulo = new Text("Escolha suas disciplinas");
        titulo.setFont(FonteUtil.pixel(44));
        titulo.setFill(Color.BLACK);

        aviso = new Text("Escolha até " + MAX_DISCIPLINAS + " disciplinas");
        aviso.setFont(FonteUtil.pixel(26));
        aviso.setFill(Color.BLACK);

        StackPane painelCentral = new StackPane();
        painelCentral.setPrefSize(600, 500);
        painelCentral.getChildren().add(painelOpcoes);
        painelCentral.setTranslateY(-50);

        Button btnConfirmar = new Button("Confirmar");
        estilizarBotao(btnConfirmar);

        btnConfirmar.setOnAction(event -> {
            List<Disciplina> selecionadas = obterSelecionadas();
            if (selecionadas.isEmpty()) {
                aviso.setText("Escolha ao menos uma disciplina");
                aviso.setFill(Color.ORANGE);
                return;
            }
            onConfirmar.accept(selecionadas);
        });

        conteudo.getChildren().addAll(
                titulo,
                aviso,
                painelCentral,
                btnConfirmar
        );

        getChildren().addAll(backgroundView, conteudo);

        StackPane.setMargin(conteudo, new Insets(40, 0, 0, 0));
    }

    public void abrir(List<Disciplina> opcoes) {

        painelOpcoes.getChildren().clear();
        disciplinasAtuais.clear();
        disciplinasAtuais.addAll(opcoes);

        aviso.setText("Escolha até " + MAX_DISCIPLINAS + " disciplinas");
        aviso.setFill(Color.BLACK);

        itens.clear();

        for (int i = 0; i < opcoes.size(); i++) {

            Disciplina disciplina = opcoes.get(i);

            ItemDisciplina item = new ItemDisciplina(disciplina);

            item.getView().setOnMouseClicked(e -> {

                if (item.isDesabilitado())
                    return;

                item.setSelecionado(!item.isSelecionado());

                atualizarEstadoItens();
            });

            itens.add(item);

            int coluna = i % 4;
            int linha = i / 4;

            painelOpcoes.add(item.getView(), coluna, linha);

            GridPane.setHalignment(item.getView(), HPos.CENTER);
            GridPane.setValignment(item.getView(), VPos.CENTER);
        }

        setVisible(true);
        setManaged(true);
        toFront();
    }

    /**
     * Desabilita checkboxes não marcados quando o limite máximo já foi atingido,
     * dando feedback visual imediato sem precisar esperar a confirmação.
     */
    private void atualizarEstadoItens() {

        long selecionados = itens.stream()
                .filter(ItemDisciplina::isSelecionado)
                .count();

        boolean limite = selecionados >= MAX_DISCIPLINAS;

        for (ItemDisciplina item : itens) {

            if (!item.isSelecionado())
                item.setDesabilitado(limite);
            else
                item.setDesabilitado(false);
        }

        if (limite) {
            aviso.setText("Limite de " + MAX_DISCIPLINAS + " disciplinas atingido");
        } else {
            aviso.setText("Escolha até " + MAX_DISCIPLINAS + " disciplinas");
        }

        aviso.setFill(Color.BLACK);
    }

    private List<Disciplina> obterSelecionadas() {

        List<Disciplina> lista = new ArrayList<>();

        for (ItemDisciplina item : itens) {

            if (item.isSelecionado())
                lista.add(item.getDisciplina());
        }

        return lista;
    }

    public void fechar() {
        setVisible(false);
        setManaged(false);
    }

    private void estilizarBotao(Button botao) {
        botao.setFont(FonteUtil.pixel(32));
        botao.setTextFill(Color.BLACK);
        botao.setTranslateY(-140);
        botao.setBackground(Background.EMPTY);
        botao.setBorder(Border.EMPTY);

        botao.setBorder(new Border(
                new BorderStroke(
                        Color.TRANSPARENT,
                        BorderStrokeStyle.SOLID,
                        new CornerRadii(8),
                        new BorderWidths(3)
                )
        ));

        botao.setPrefWidth(250);
        botao.setFocusTraversable(false);

        botao.setOnMouseEntered(e ->
                botao.setBorder(new Border(
                        new BorderStroke(
                                Color.GOLD,
                                BorderStrokeStyle.SOLID,
                                new CornerRadii(8),
                                new BorderWidths(3)
                        )
                ))
        );

        botao.setOnMouseExited(e ->
                botao.setBorder(new Border(
                        new BorderStroke(
                                Color.TRANSPARENT,
                                BorderStrokeStyle.SOLID,
                                new CornerRadii(8),
                                new BorderWidths(3)
                        )
                ))
        );
    }
}
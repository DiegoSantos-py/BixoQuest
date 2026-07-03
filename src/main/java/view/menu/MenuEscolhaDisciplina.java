package view.menu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.Disciplina.Disciplina;
import view.animacao.AnimacaoFramesUtil;
import view.util.FonteUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MenuEscolhaDisciplina extends StackPane {

    private static final int MAX_DISCIPLINAS = 3;

    private final Consumer<List<Disciplina>> onConfirmar;

    private final VBox painelOpcoes;
    private final List<CheckBox> checkBoxes = new ArrayList<>();
    private final List<Disciplina> disciplinasAtuais = new ArrayList<>();

    private Text aviso;

    public MenuEscolhaDisciplina(Consumer<List<Disciplina>> onConfirmar) {
        this.onConfirmar = onConfirmar;

        painelOpcoes = new VBox(15);
        painelOpcoes.setAlignment(Pos.CENTER);

        montarTela();

        setVisible(false);
        setManaged(false);
    }

    private void montarTela() {

        ImageView backgroundView =
                AnimacaoFramesUtil.criarImagem("/menuPrincipal/BackgroundMenu.png");

        VBox conteudo = new VBox(35);
        conteudo.setAlignment(Pos.CENTER);
        conteudo.setTranslateY(-120);

        Text titulo = new Text("Escolha suas disciplinas");
        titulo.setFont(FonteUtil.pixel(36));
        titulo.setFill(Color.WHITE);

        aviso = new Text("Escolha até " + MAX_DISCIPLINAS + " disciplinas");
        aviso.setFont(FonteUtil.pixel(18));
        aviso.setFill(Color.LIGHTGRAY);

        StackPane painelCentral = new StackPane();
        painelCentral.setPrefSize(800, 500);

        ScrollPane scroll = new ScrollPane(painelOpcoes);
        scroll.setFitToWidth(true);
        scroll.setPrefSize(800, 500);
        scroll.setBackground(Background.EMPTY);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        painelCentral.getChildren().add(scroll);

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

        StackPane.setMargin(conteudo, new Insets(50, 0, 0, 0));
    }

    public void abrir(List<Disciplina> opcoes) {

        painelOpcoes.getChildren().clear();
        checkBoxes.clear();
        disciplinasAtuais.clear();
        disciplinasAtuais.addAll(opcoes);

        aviso.setText("Escolha até " + MAX_DISCIPLINAS + " disciplinas");
        aviso.setFill(Color.LIGHTGRAY);

        for (Disciplina disciplina : opcoes) {
            CheckBox checkBox = criarCheckBox(disciplina);
            checkBoxes.add(checkBox);
            painelOpcoes.getChildren().add(checkBox);
        }

        setVisible(true);
        setManaged(true);
        toFront();
    }

    private CheckBox criarCheckBox(Disciplina disciplina) {
        String label = disciplina.getNome() + " " + (int) disciplina.getCodigo();

        CheckBox checkBox = new CheckBox(label);
        checkBox.setFont(FonteUtil.pixel(20));
        checkBox.setTextFill(Color.WHITE);
        checkBox.setUserData(disciplina);

        checkBox.selectedProperty().addListener((obs, foiMarcado, marcado) -> {
            atualizarEstadoCheckBoxes();
        });

        return checkBox;
    }

    /**
     * Desabilita checkboxes não marcados quando o limite máximo já foi atingido,
     * dando feedback visual imediato sem precisar esperar a confirmação.
     */
    private void atualizarEstadoCheckBoxes() {
        long marcados = checkBoxes.stream().filter(CheckBox::isSelected).count();

        boolean limiteAtingido = marcados >= MAX_DISCIPLINAS;

        for (CheckBox cb : checkBoxes) {
            if (!cb.isSelected()) {
                cb.setDisable(limiteAtingido);
            }
        }

        if (limiteAtingido) {
            aviso.setText("Limite de " + MAX_DISCIPLINAS + " disciplinas atingido");
            aviso.setFill(Color.LIGHTGRAY);
        } else {
            aviso.setText("Escolha até " + MAX_DISCIPLINAS + " disciplinas");
            aviso.setFill(Color.LIGHTGRAY);
        }
    }

    private List<Disciplina> obterSelecionadas() {
        List<Disciplina> selecionadas = new ArrayList<>();

        for (CheckBox cb : checkBoxes) {
            if (cb.isSelected()) {
                selecionadas.add((Disciplina) cb.getUserData());
            }
        }

        return selecionadas;
    }

    public void fechar() {
        setVisible(false);
        setManaged(false);
    }

    private void estilizarBotao(Button botao) {
        botao.setFont(FonteUtil.pixel(24));
        botao.setTextFill(Color.WHITE);

        botao.setBackground(Background.EMPTY);
        botao.setBorder(Border.EMPTY);

        botao.setPrefWidth(250);
        botao.setFocusTraversable(false);
    }
}
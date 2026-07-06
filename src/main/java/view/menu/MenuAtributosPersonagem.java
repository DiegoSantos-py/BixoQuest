package view.menu;

import controller.GameController;
import controller.PersonagemController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.Disciplina.AreaConhecimento;
import model.Disciplina.Disciplina;
import model.Tempo.Semestre;
import view.util.FonteUtil;
import view.util.ImagemCache;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MenuAtributosPersonagem extends StackPane {

    private final PersonagemController personagemController;
    private final int personagemId;
    private final GameController gameController;
    private final Runnable aoVoltar;

    private VBox atributosContainer;
    private VBox disciplinasContainer;

    public MenuAtributosPersonagem(
            PersonagemController personagemController,
            int personagemId,
            GameController gameController,
            Runnable aoVoltar) {

        this.personagemController = personagemController;
        this.personagemId = personagemId;
        this.gameController = gameController;
        this.aoVoltar = aoVoltar;

        montarTela();
        setVisible(false);
        setManaged(false);
    }

    private void montarTela() {

        ImageView background = new ImageView(ImagemCache.get("/menuPersonagens/BackgroundMenu.png"));
        background.fitWidthProperty().bind(widthProperty());
        background.fitHeightProperty().bind(heightProperty());
        background.setPreserveRatio(false);

        VBox painel = new VBox(30);
        painel.setAlignment(Pos.CENTER);
        painel.setTranslateY(10);

        Label titulo = new Label("ATRIBUTOS");
        titulo.setFont(FonteUtil.pixel(36));
        titulo.setTextFill(Color.WHITE);

        atributosContainer = new VBox(20);
        atributosContainer.setAlignment(Pos.CENTER);

        Label tituloDisciplinas = new Label("DISCIPLINAS ATUAIS");
        tituloDisciplinas.setFont(FonteUtil.pixel(28));
        tituloDisciplinas.setTextFill(Color.WHITE);

        disciplinasContainer = new VBox(20);
        disciplinasContainer.setAlignment(Pos.CENTER);

        atualizarAtributos();
        atualizarDisciplinas();

        Button btnVoltar = criarBotaoVoltar();

        painel.getChildren().addAll(
                titulo,
                atributosContainer,
                tituloDisciplinas,
                disciplinasContainer,
                btnVoltar
        );

        getChildren().addAll(background, painel);

        StackPane.setMargin(painel, new Insets(100, 40, 40, 40));
    }

    private void atualizarAtributos() {
        atributosContainer.getChildren().clear();

        Map<String, Double> atributos =
                personagemController.getAtributos(personagemId);

        for (Map.Entry<String, Double> atributo : atributos.entrySet()) {
            atributosContainer.getChildren().add(
                    criarLinhaBarra(
                            atributo.getKey(),
                            atributo.getValue(),
                            "/assets/atributos/" + atributo.getKey().toLowerCase() + ".png"
                    )
            );
        }
    }

    /**
     * Reconstrói a lista de disciplinas do semestre atual, exibindo o nome
     * e o progresso de conhecimento (por área) de cada uma.
     */
    private void atualizarDisciplinas() {
        disciplinasContainer.getChildren().clear();

        Semestre semestre = gameController.getSemestre();
        if (semestre == null) return; // aguardando escolha de disciplinas, nada a exibir

        List<Disciplina> disciplinas = semestre.getDisciplinas();
        Map<AreaConhecimento, Double> conhecimentos =
                personagemController.getConhecimentos(personagemId);

        for (Disciplina disciplina : disciplinas) {
            double valor = conhecimentos.getOrDefault(disciplina.getArea(), 0.0);
            String label = disciplina.getNome() + " " + (int) disciplina.getCodigo();

            disciplinasContainer.getChildren().add(
                    criarLinhaBarra(label, valor, "/assets/atributos/conhecimento.png")
            );
        }
    }

    private HBox criarLinhaBarra(String nome, double valor, String caminhoIcone) {
        HBox linha = new HBox(15);
        linha.setAlignment(Pos.CENTER);
        linha.setMinHeight(50);
        linha.setPadding(new Insets(8, 0, 8, 0));

        if (caminhoIcone != null) {
            ImageView icone = criarIcone(caminhoIcone);
            icone.setFitWidth(48);
            icone.setFitHeight(48);
            linha.getChildren().add(icone);
        }

        Label texto = new Label(nome);
        texto.setFont(FonteUtil.pixel(28));
        texto.setTextFill(Color.WHITE);
        texto.setMinWidth(220);

        ProgressBar barra = new ProgressBar(valor / 100.0);
        barra.setPrefWidth(450);
        barra.setPrefHeight(30);

        linha.getChildren().addAll(texto, barra);

        return linha;
    }

    private ImageView criarIcone(String caminho) {
        ImageView view = new ImageView(ImagemCache.get(caminho));
        view.setFitWidth(32);
        view.setFitHeight(32);
        return view;
    }

    private Button criarBotaoVoltar() {
        Button botao = new Button("Voltar");
        estilizarBotao(botao);
        botao.setOnAction(e -> aoVoltar.run());
        return botao;
    }

    private void estilizarBotao(Button botao) {
        botao.setFont(FonteUtil.pixel(24));
        botao.setTextFill(Color.WHITE);
        botao.setBackground(Background.EMPTY);
        botao.setBorder(Border.EMPTY);
        botao.setPrefWidth(220);
        botao.setFocusTraversable(false);
    }

    public void abrir() {
        atualizarAtributos();
        atualizarDisciplinas();
        setVisible(true);
        setManaged(true);
        toFront();
    }

    public void fechar() {
        setVisible(false);
        setManaged(false);
    }
}
package view.controleTelas;

import controller.*;
import javafx.scene.Parent;
import model.Evento.Prova.ProvaIDs;
import model.Evento.ResultadoZona;
import view.InicioJogoView;
import view.animacao.AnimacaoFimView;
import view.animacao.AnimacaoInicioView;
import view.cena.CenaBatalha;
import view.construtores.ConstrutorCenaJogo;
import view.construtores.DiretorCena;
import view.menu.*;
import view.cena.CenaJogo;
import view.util.Borda;
import java.util.function.Consumer;

public class ControllerTelas {

    private final GerenciadorTelas gerenciador;
    private final PersonagemController personagemController;
    private final MapaController mapaController;
    private final GameController gameController;
    private final BatalhaController batalhaController;
    private final DiretorCena diretorCena;

    private static final int TEMPO_PROVA_MINUTOS = 5;

    private CenaJogo cenaAtual;
    private Borda ultimaBorda;
    private int sessaoAtual;

    public ControllerTelas(
            GerenciadorTelas gerenciador,
            PersonagemController personagemController,
            MapaController mapaController,
            GameController gameController,
            BatalhaController batalhaController
    ) {
        this.gerenciador           = gerenciador;
        this.personagemController  = personagemController;
        this.mapaController        = mapaController;
        this.gameController        = gameController;
        this.batalhaController     = batalhaController;
        this.diretorCena           = new DiretorCena();
    }

    public Parent criarMenuInicial() {
        pararCenaAtual();
        return new MenuInicial(
                personagemController,
                () -> gerenciador.mostrarMenuPersonagens(),
                () -> gerenciador.mostrarConfiguracoes(),
                () -> gerenciador.mostrarSeletorBatalha(),
                () -> System.exit(0)
        );
    }

    public Parent criarMenuPersonagens() {
        return new MenuPersonagens(
                personagemController,
                () -> gerenciador.mostrarMenuInicial(),
                slotId -> gerenciador.mostrarAnimacaoInicio(slotId),
                slotId -> gerenciador.mostrarCriacaoPersonagem(slotId)
        );
    }

    public Parent criarCriacaoPersonagem(int slotId) {
        return new MenuCriarPersonagem(
                personagemController,
                slotId,
                sessao -> gerenciador.mostrarAnimacaoInicio(sessao),
                () -> gerenciador.mostrarMenuPersonagens()
        );
    }

    public Parent criarSeletorBatalha() {
        return new MenuSeletorBatalha(
                batalhaController,
                () -> gerenciador.mostrarTelaBatalha(), // aoIniciarBatalha
                () -> gerenciador.mostrarMenuInicial()  // aoVoltar
        );
    }

    public Parent criarCenaMenuMorte(String texto) {
        // TODO: Implementar MenuMorte
        javafx.scene.layout.StackPane placeholder = new javafx.scene.layout.StackPane();
        javafx.scene.control.Label label = new javafx.scene.control.Label("MENU MORTE: " + texto);
        label.setTextFill(javafx.scene.paint.Color.RED);
        placeholder.getChildren().add(label);
        return placeholder;
    }

    public Parent criarTelaBatalha() {
        int semestreNumero = gameController.getSemestre().getNumeroSemestre();
        return new CenaBatalha(batalhaController, () -> gerenciador.mostrarSeletorBatalha(), semestreNumero);
    }

    public Parent criarTelaBatalha(ProvaIDs provaId, String nomeLocalRetorno) {
        batalhaController.iniciarProva(provaId, gameController.getPersonagem());

        int semestreNumero = gameController.getSemestre().getNumeroSemestre();

        return new CenaBatalha(batalhaController, () -> {
            boolean aprovado = batalhaController.getEstadoAtual().isVitoria();
            gameController.confirmarResultadoProva(provaId, aprovado);
            gameController.consumirTempoProva(TEMPO_PROVA_MINUTOS);
            gameController.retomarDia();
            gerenciador.mostrarCenaPorNome(nomeLocalRetorno);
        }, semestreNumero);
    }

    public Parent criarAnimacaoInicio(int sessaoAtual) {
        this.sessaoAtual = sessaoAtual;
        return new AnimacaoInicioView(
                () -> gerenciador.mostrarCenaPorNome("Sala módulo 7"),
                gameController,
                sessaoAtual
        );
    }

    public Parent criarAnimacaoFim() {
        return new AnimacaoFimView(
                () -> gerenciador.mostrarAnimacaoInicio(sessaoAtual)
        );
    }

    public Parent criarInicio() {
        return new InicioJogoView(
                () -> gerenciador.mostrarMenuInicial()
        );
    }

    public Parent criarCena(String nomeLocal, Consumer<String> onZona) {
        pararCenaAtual();
        ConstrutorCenaJogo construtor = new ConstrutorCenaJogo();

        double[] pos = calcularPosicaoInicial(ultimaBorda);
        String spriteBase = personagemController.getSpriteBase(sessaoAtual);

        construtor.setOnBordaAtingida(borda -> {
            ultimaBorda = borda;
            mapaController.getVizinho(nomeLocal, borda.getDirecao())
                    .ifPresent(nome -> gerenciador.mostrarCenaPorNome(nome));
        });

        construtor.setPersonagem(personagemController, sessaoAtual);
        construtor.setGameController(gameController);
        construtor.setOnSairParaMenuPrincipal(gerenciador::mostrarMenuInicial);
        construtor.setOnFinalizar(gerenciador::mostrarAnimacaoFim);

        Consumer<String> onZonaComEvento = nomeZona -> {
            ResultadoZona resultado = gameController.processarZona(nomeZona);

            switch (resultado.getStatus()) {
                case SEM_EVENTO -> {
                    onZona.accept(nomeZona);
                    System.out.println("sem evento");
                }
                case EXECUTADO -> {
                    cenaAtual.abrirFeedbackEvento(resultado.getEvento());
                    System.out.println("executado");
                }
                case REQUISITO_NAO_ATENDIDO -> {
                    cenaAtual.exibirFeedbackMotivo(resultado.getMotivo());
                    System.out.println("requisito nao atendido");
                }
                case PROVA_BATALHA -> {
                    cenaAtual.parar();              // para o AnimationTimer da CenaJogo atual
                    gameController.pausarDia();     // pausa o cronômetro do dia (mesmo mecanismo dos menus)
                    gerenciador.mostrarTelaBatalha(resultado.getProvaId(), nomeLocal);
                }
            }
        };

        diretorCena.construirCena(construtor, mapaController,
                onZonaComEvento,
                nome -> System.out.println("NPC: " + nome),
                pos[0], pos[1], spriteBase, nomeLocal);

        cenaAtual = construtor.getResult();

        return cenaAtual.buildPane();
    }

    private void pararCenaAtual() {
        if (cenaAtual != null) {
            cenaAtual.parar();
            cenaAtual = null;
        }
    }

    private double[] calcularPosicaoInicial(Borda borda) {
        return switch (borda) {
            case NORTE -> new double[]{600, 800};
            case SUL   -> new double[]{700, -90};
            case LESTE -> new double[]{-90,  400};
            case OESTE -> new double[]{1700, 500};
            case null  -> new double[]{700, 700};
        };
    }

}
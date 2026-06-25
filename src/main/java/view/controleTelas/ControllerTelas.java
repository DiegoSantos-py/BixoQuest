package view.controleTelas;

import controller.GameController;
import controller.MapaController;
import controller.NpcController;
import controller.PersonagemController;
import javafx.scene.Parent;
import view.InicioJogoView;
import view.animacao.AnimacaoInicioView;
import view.construtores.ConstrutorCenaJogo;
import view.construtores.DiretorCena;
import view.menu.MenuCriarPersonagem;
import view.menu.MenuInicial;
import view.menu.MenuPersonagens;
import view.cena.CenaJogo;
import view.util.Borda;

// TODO: refatorar essa classe. Tá muito acoplada
public class ControllerTelas {

    private final GerenciadorTelas gerenciador;
    private final PersonagemController personagemController;
    private final MapaController mapaController;
    private final NpcController npcController;
    private final GameController gameController;
    private final DiretorCena diretorCena;

    private CenaJogo cenaAtual;
    private Borda ultimaBorda;

    public ControllerTelas(
            GerenciadorTelas gerenciador,
            PersonagemController personagemController,
            MapaController mapaController,
            NpcController npcController, GameController gameController
    ) {
        this.gerenciador           = gerenciador;
        this.personagemController  = personagemController;
        this.mapaController        = mapaController;
        this.npcController         = npcController;
        this.gameController        = gameController;
        this.diretorCena           = new DiretorCena();
    }

    public Parent criarMenuInicial() {
        pararCenaAtual();
        return new MenuInicial(
                personagemController,
                () -> gerenciador.mostrarMenuPersonagens(),
                () -> gerenciador.mostrarConfiguracoes(),
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

    public Parent criarAnimacaoInicio(int sessaoAtual) {
        return new AnimacaoInicioView(
                () -> gerenciador.mostrarTelaPonto(),
                gameController,
                sessaoAtual
        );
    }

    public Parent criarInicio() {
        return new InicioJogoView(
                //() -> gerenciador.mostrarTelaJogo()
                () -> gerenciador.mostrarMenuInicial()
        );
    }

    public Parent criarCenaPonto() {
        pararCenaAtual();
        ConstrutorCenaJogo construtor = new ConstrutorCenaJogo();

        double[] pos = calcularPosicaoInicial(ultimaBorda);
        diretorCena.construirCenaPontoOnibus(
                construtor,
                mapaController,
                npcController,
                id -> {
                    switch (id) {
                        case "Banco de ônibus 1" -> gerenciador.mostrarMenuInicial();
                        default -> System.err.println("Zona não encontrada");
                    }
                },
                nome -> System.out.println("NPC atingido: " + nome), // temporário
                pos[0], pos[1]);

        construtor.setOnBordaAtingida(borda -> {
            ultimaBorda = borda;
            mapaController.getVizinho("Ponto de ônibus 1", borda.getDirecao())
                    .ifPresent(nome -> gerenciador.mostrarCenaPorNome(nome));
        });

        cenaAtual  = construtor.getResult();
        return cenaAtual.buildPane();
    }

    public Parent criarCenaEntradaModulo() {
        pararCenaAtual();
        ConstrutorCenaJogo construtor = new ConstrutorCenaJogo();

        double[] pos = calcularPosicaoInicial(ultimaBorda);
        diretorCena.construirCenaEntradaModulo(
                construtor,
                mapaController,
                npcController,
                pos[0], pos[1]);

        construtor.setOnBordaAtingida(borda -> {
                    ultimaBorda = borda;
                    mapaController.getVizinho("Entrada módulo 1", borda.getDirecao())
                            .ifPresent(nome -> gerenciador.mostrarCenaPorNome(nome));
                }
        );
        cenaAtual  = construtor.getResult();
        return cenaAtual.buildPane();
    }

    public Parent criarCenaCantina() {
        pararCenaAtual();
        ConstrutorCenaJogo construtor = new ConstrutorCenaJogo();

        double[] pos = calcularPosicaoInicial(ultimaBorda);
        diretorCena.construirCenaCantina(
                construtor,
                mapaController,
                npcController,
                pos[0], pos[1]);

        construtor.setOnBordaAtingida(borda -> {
                    ultimaBorda = borda;
                    mapaController.getVizinho("Cantina módulo 1", borda.getDirecao())
                            .ifPresent(nome -> gerenciador.mostrarCenaPorNome(nome));
                }
        );
        cenaAtual  = construtor.getResult();
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
            case NORTE -> new double[]{700, 800};
            case SUL   -> new double[]{700, 50};
            case LESTE -> new double[]{50,  500};
            case OESTE -> new double[]{1800, 500};
            case null  -> new double[]{700, 700};
        };
    }
}
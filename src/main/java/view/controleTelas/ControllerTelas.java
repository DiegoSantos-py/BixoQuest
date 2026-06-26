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
import java.util.function.Consumer;

public class ControllerTelas {

    private final GerenciadorTelas gerenciador;
    private final PersonagemController personagemController;
    private final MapaController mapaController;
    private final NpcController npcController;
    private final GameController gameController;
    private final DiretorCena diretorCena;

    private CenaJogo cenaAtual;
    private Borda ultimaBorda;
    private int sessaoAtual;

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
        this.sessaoAtual = sessaoAtual;
        return new AnimacaoInicioView(
                () -> gerenciador.mostrarCenaPorNome("Ponto de ônibus 1"),
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
        String spriteBase = personagemController.getSpriteBase(sessaoAtual);
        diretorCena.construirCena(
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
                pos[0], pos[1], spriteBase, "Ponto de ônibus 1");

        construtor.setOnBordaAtingida(borda -> {
            ultimaBorda = borda;
            mapaController.getVizinho("Ponto de ônibus 1", borda.getDirecao())
                    .ifPresent(nome -> gerenciador.mostrarCenaPorNome(nome));
        });

        cenaAtual  = construtor.getResult();
        return cenaAtual.buildPane();
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


        diretorCena.construirCena(construtor, mapaController, npcController,
                onZona,
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
            case NORTE -> new double[]{700, 800};
            case SUL   -> new double[]{700, 150};
            case LESTE -> new double[]{50,  500};
            case OESTE -> new double[]{1700, 500};
            case null  -> new double[]{700, 700};
        };
    }
}
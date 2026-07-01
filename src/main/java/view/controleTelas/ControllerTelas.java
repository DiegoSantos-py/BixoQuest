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
import view.menu.*;
import view.cena.CenaJogo;
import view.util.Borda;
import java.util.function.Consumer;

public class ControllerTelas {

    private final GerenciadorTelas gerenciador;
    private final PersonagemController personagemController;
    private final MapaController mapaController;
    private final GameController gameController;
    private final DiretorCena diretorCena;

    private CenaJogo cenaAtual;
    private Borda ultimaBorda;
    private int sessaoAtual;

    public ControllerTelas(
            GerenciadorTelas gerenciador,
            PersonagemController personagemController,
            MapaController mapaController, GameController gameController
    ) {
        this.gerenciador           = gerenciador;
        this.personagemController  = personagemController;
        this.mapaController        = mapaController;
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

        construtor.setPersonagem(personagemController, sessaoAtual); // ajuste conforme seu id real
        construtor.setOnSairParaMenuPrincipal(() -> gerenciador.mostrarMenuInicial()); // ajuste ao seu GerenciadorTelas

        diretorCena.construirCena(construtor, mapaController,
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
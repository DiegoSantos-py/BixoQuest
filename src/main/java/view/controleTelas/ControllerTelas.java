package view.controleTelas;

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

// TODO: refatorar essa classe. Tá muito acoplada
public class ControllerTelas {

    private final GerenciadorTelas gerenciador;
    private final PersonagemController personagemController;
    private final MapaController mapaController;
    private final NpcController npcController;
    private final DiretorCena diretorCena;

    private CenaJogo cenaAtual;

    public ControllerTelas(
            GerenciadorTelas gerenciador,
            PersonagemController personagemController,
            MapaController mapaController,
            NpcController npcController
    ) {
        this.gerenciador           = gerenciador;
        this.personagemController  = personagemController;
        this.mapaController        = mapaController;
        this.npcController         = npcController;
        this.diretorCena           = new DiretorCena();
    }

    public Parent criarMenuInicial() {
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
                1920,
                1080,
                () -> gerenciador.mostrarTelaJogo()
        );
    }

    public Parent criarInicio() {
        return new InicioJogoView(
                //() -> gerenciador.mostrarTelaJogo()
                () -> gerenciador.mostrarMenuInicial()
        );
    }

    public Parent criarCenaJogo() {
        pararCenaAtual();
        ConstrutorCenaJogo construtor = new ConstrutorCenaJogo();
        diretorCena.construirCenaPontoOnibus(construtor, mapaController, npcController);
        construtor.setOnBordaAtingida(borda -> {
            switch (borda) {
//                case CenaJogo.BORDA_NORTE -> gerenciador.mostrarCenaAcima();
//                case CenaJogo.BORDA_SUL   -> gerenciador.mostrarCenaAbaixo();
                case CenaJogo.BORDA_LESTE -> gerenciador.mostrarTelaJogoEntrada();
//                case CenaJogo.BORDA_OESTE -> gerenciador.mostrarCenaEsquerda();
            }
        });
        cenaAtual  = construtor.getResult();
        return cenaAtual.buildPane();
    }

    public Parent criarCenaEntradaModulo() {
        pararCenaAtual();
        ConstrutorCenaJogo construtor = new ConstrutorCenaJogo();
        diretorCena.construirCenaEntradaModulo(construtor, mapaController, npcController);
        construtor.setOnBordaAtingida(borda -> {
            switch (borda) {
                //case CenaJogo.BORDA_NORTE -> gerenciador.mostrarTelaJogo();
                //case CenaJogo.BORDA_SUL   -> gerenciador.mostrarTelaJogo();
                case CenaJogo.BORDA_LESTE -> gerenciador.mostrarTelaCantina();
                //case CenaJogo.BORDA_OESTE -> gerenciador.mostrarTelaJogo();
            }
        });
        cenaAtual  = construtor.getResult();
        return cenaAtual.buildPane();
    }

    public Parent criarCenaCantina() {
        pararCenaAtual();
        ConstrutorCenaJogo construtor = new ConstrutorCenaJogo();
        diretorCena.construirCenaCantina(construtor, mapaController, npcController);
        construtor.setOnBordaAtingida(borda -> {
            switch (borda) {
                //case CenaJogo.BORDA_NORTE -> gerenciador.mostrarTelaJogo();
                //case CenaJogo.BORDA_SUL   -> gerenciador.mostrarTelaJogo();
                case CenaJogo.BORDA_LESTE -> gerenciador.mostrarTelaJogo();
                //case CenaJogo.BORDA_OESTE -> gerenciador.mostrarTelaJogo();
            }
        });
        cenaAtual  = construtor.getResult();
        return cenaAtual.buildPane();
    }

    private void pararCenaAtual() {
        if (cenaAtual != null) {
            cenaAtual.parar();
            cenaAtual = null;
        }
    }
}
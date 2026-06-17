package view.controleTelas;

import controller.MapaController;
import controller.NpcController;
import controller.PersonagemController;
import controller.BatalhaController;
import javafx.scene.Parent;
import view.InicioDiaView;
import view.InicioJogoView;
import view.animacao.AnimacaoInicioView;
import view.construtores.ConstrutorCenaJogo;
import view.construtores.DiretorCena;
import view.menu.MenuCriarPersonagem;
import view.menu.MenuInicial;
import view.menu.MenuPersonagens;
import view.menu.MenuSeletorBatalha;
import view.cena.CenaJogo;
import view.cena.CenaBatalha;

// TODO: refatorar essa classe. Tá muito acoplada
public class CriadorCenas {

    private final GerenciadorTelas gerenciador;
    private final PersonagemController personagemController;
    private final MapaController mapaController;
    private final NpcController npcController;
    private final BatalhaController batalhaController;
    private final DiretorCena diretorCena;

    private CenaJogo cenaAtual;

    public CriadorCenas(
            GerenciadorTelas gerenciador,
            PersonagemController personagemController,
            MapaController mapaController,
            NpcController npcController,
            BatalhaController batalhaController
    ) {
        this.gerenciador           = gerenciador;
        this.personagemController  = personagemController;
        this.mapaController        = mapaController;
        this.npcController         = npcController;
        this.batalhaController     = batalhaController;
        this.diretorCena           = new DiretorCena();
    }

    public Parent criarMenuInicial() {
        return new MenuInicial(
                personagemController,
                () -> gerenciador.mostrarMenuPersonagens(),
                () -> gerenciador.mostrarConfiguracoes(),
                () -> gerenciador.mostrarSeletorBatalha(),
                () -> System.exit(0)
        );
    }

    public Parent criarSeletorBatalha() {
        return new MenuSeletorBatalha(
                batalhaController,
                () -> gerenciador.mostrarTelaBatalha(), // aoIniciarBatalha
                () -> gerenciador.mostrarMenuInicial()  // aoVoltar
        );
    }

    public Parent criarMenuPersonagens() {
        return new MenuPersonagens(
                personagemController,
                () -> gerenciador.mostrarMenuInicial(),
                () -> gerenciador.mostrarAnimacaoInicio(),
                slotId -> gerenciador.mostrarCriacaoPersonagem(slotId)
        );
    }

    public Parent criarCriacaoPersonagem(int slotId) {
        return new MenuCriarPersonagem(
                personagemController,
                slotId,
                () -> gerenciador.mostrarAnimacaoInicio(),
                () -> gerenciador.mostrarMenuPersonagens()
        );
    }

    public Parent criarAnimacaoInicio() {
        return new AnimacaoInicioView(
                1920,
                1080,
                () -> gerenciador.mostrarInicioDia()
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
        return new CenaBatalha(batalhaController, () -> gerenciador.mostrarSeletorBatalha());
    }

    public Parent criarInicio() {
        return new InicioJogoView(
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

    public Parent criarInicioDia() {
        return new InicioDiaView(
                () -> gerenciador.mostrarTelaJogo()
        );
    }

    private void pararCenaAtual() {
        if (cenaAtual != null) {
            cenaAtual.parar();
            cenaAtual = null;
        }
    }
}
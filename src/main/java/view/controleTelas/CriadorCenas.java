package view.controleTelas;

import controller.MapaController;
import controller.NpcController;
import controller.PersonagemController;
import javafx.scene.Parent;
import view.InicioDiaView;
import view.InicioJogoView;
import view.animacao.AnimacaoInicioView;
import view.construtores.ConstrutorCenaJogo;
import view.construtores.DiretorCena;
import view.menu.MenuCriarPersonagem;
import view.menu.MenuInicial;
import view.menu.MenuPersonagens;
import view.cena.CenaJogo;

// TODO: refatorar essa classe. Tá muito acoplada
public class CriadorCenas {

    private final GerenciadorTelas gerenciador;
    private final PersonagemController personagemController;
    private final MapaController mapaController;
    private final NpcController npcController;
    private final DiretorCena diretorCena;

    public CriadorCenas(
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

    public Parent criarInicio() {
        return new InicioJogoView(
                () -> gerenciador.mostrarMenuInicial()
        );
    }

    public Parent criarCenaJogo() {
        ConstrutorCenaJogo construtor = new ConstrutorCenaJogo();
        diretorCena.construirCenaPontoOnibus(construtor, mapaController, npcController);
        CenaJogo cenaJogo = construtor.getResult();
        return cenaJogo.buildPane();
    }

    public Parent criarInicioDia() {
        return new InicioDiaView(
                () -> gerenciador.mostrarTelaJogo()
        );
    }
}
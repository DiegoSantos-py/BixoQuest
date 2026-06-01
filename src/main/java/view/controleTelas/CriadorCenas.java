package view.controleTelas;

import controller.PersonagemController;
import javafx.scene.Parent;
import view.InicioView;
import view.animacao.AnimacaoInicioView;
import view.menu.MenuCriarPersonagem;
import view.menu.MenuInicial;
import view.menu.MenuPersonagens;

public class CriadorCenas {

    private final GerenciadorTelas gerenciador;
    private final PersonagemController personagemController;

    public CriadorCenas(
            GerenciadorTelas gerenciador,
            PersonagemController personagemController
    ) {
        this.gerenciador = gerenciador;
        this.personagemController = personagemController;
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
                1960,
                1080,
                () -> gerenciador.mostrarMenuInicial()
        );
    }

    public Parent criarInicio() {
        return new InicioView(
                () -> gerenciador.mostrarMenuInicial()
        );
    }

//    public Parent criarTelaJogo() {
//        return new TelaJogoView();
//    }
}
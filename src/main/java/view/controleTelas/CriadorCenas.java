package view.controleTelas;

import controller.PersonagemController;
import javafx.scene.Scene;
import view.animacao.AnimacaoInicioView;
import view.InicioView;
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

    public Scene criarMenuInicial() {
        MenuInicial menu = new MenuInicial(
                personagemController,
                () -> gerenciador.mostrarMenuPersonagens(),
                () -> gerenciador.mostrarConfiguracoes(),
                () -> System.exit(0)
        );

        return new Scene(menu, 1960, 1080);
    }

    public Scene criarMenuPersonagens() {
        MenuPersonagens menuPersonagens = new MenuPersonagens(
                personagemController,
                () -> gerenciador.mostrarMenuInicial(),
                () -> gerenciador.mostrarAnimacaoInicio(),
                slotId -> gerenciador.mostrarCriacaoPersonagem(slotId)
        );

        return new Scene(menuPersonagens, 1960, 1080);
    }

    public Scene criarCriacaoPersonagem(int slotId) {
        MenuCriarPersonagem menuCriar = new MenuCriarPersonagem(
                personagemController,
                slotId,
                () -> gerenciador.mostrarAnimacaoInicio(),
                () -> gerenciador.mostrarMenuPersonagens()
        );

        return new Scene(menuCriar, 1960, 1080);
    }

    public Scene criarAnimacaoInicio() {
        AnimacaoInicioView animacao = new AnimacaoInicioView(
                1960,
                1080,
                () -> gerenciador.mostrarMenuInicial()
        );

        return new Scene(animacao, 1960, 1080);
    }

    public Scene criarInicio() {
        InicioView inicio = new InicioView(
                () -> gerenciador.mostrarMenuInicial()
        );

        return new Scene(inicio, 1960, 1080);
    }
//    public Scene criarTelaJogo() {
//        TelaJogoView telaJogo = new TelaJogoView();
//
//        return new Scene(telaJogo, 1960, 1080);
//    }
}
package view.cena;

import controller.GameController;
import controller.PersonagemController;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import model.Disciplina.Disciplina;
import model.Evento.Evento;
import view.menu.MenuAtributosPersonagem;
import view.menu.MenuEscolhaDisciplina;
import view.menu.MenuFeedbackEvento;
import view.menu.MenuPause;

import java.util.List;
import java.util.function.Consumer;

public class GerenciadorMenus {
    private final StackPane raizCena;
    private final MenuPause menuPause;
    private final MenuAtributosPersonagem menuAtributos;
    private final MenuEscolhaDisciplina menuEscolhaDisciplina;
    private final MenuFeedbackEvento menuFeedbackEvento;
    private final Consumer<Boolean> onPausarJogo;

    public GerenciadorMenus(StackPane raizCena,
                            PersonagemController personagemController,
                            GameController gameController,
                            int personagemId,
                            Runnable onSairParaMenuPrincipal,
                            Consumer<List<Disciplina>> onConfirmarEscolhaDisciplina,
                            Consumer<Boolean> onPausarJogo) {
        this.raizCena = raizCena;
        this.onPausarJogo = onPausarJogo;

        this.menuPause = new MenuPause(
                this::fecharPause,
                onSairParaMenuPrincipal
        );

        this.menuEscolhaDisciplina = new MenuEscolhaDisciplina(
                onConfirmarEscolhaDisciplina);

        this.menuAtributos = new MenuAtributosPersonagem(
                personagemController,
                personagemId,
                gameController,
                this::fecharAtributos
        );

        this.menuFeedbackEvento = new MenuFeedbackEvento(
                this::fecharFeedbackEvento);

        raizCena.getChildren().addAll(menuPause, menuAtributos, menuEscolhaDisciplina, menuFeedbackEvento);
    }

    /** Centraliza a notificação de pausa/retomada, garantindo que o foco
     * de teclado sempre volte ao raizCena quando o jogo é retomado. */
    private void notificarPausa(boolean pausado) {
        onPausarJogo.accept(pausado);

        if (!pausado) {
            Platform.runLater(raizCena::requestFocus);
        }
    }

    public boolean pauseAberto() {
        return menuPause.isVisible();
    }

    public void abrirPause() {
        menuPause.abrir();
        notificarPausa(true);
    }

    public void fecharPause() {
        menuPause.fechar();
        notificarPausa(false);
    }

    public boolean atributosAberto() {
        return menuAtributos.isVisible();
    }

    public void abrirAtributos() {
        menuAtributos.abrir();
        notificarPausa(true);
    }

    public void fecharAtributos() {
        menuAtributos.fechar();
        notificarPausa(false);
    }

    public void abrirEscolhaDisciplina(List<Disciplina> opcoes) {
        menuEscolhaDisciplina.abrir(opcoes);
        notificarPausa(true);
    }

    public void fecharEscolhaDisciplina() {
        menuEscolhaDisciplina.fechar();
        notificarPausa(false);
    }

    public void abrirFeedbackEvento(Evento evento) {
        menuFeedbackEvento.abrir(evento);
        notificarPausa(true);
    }

    public void fecharFeedbackEvento() {
        notificarPausa(false);
    }
}
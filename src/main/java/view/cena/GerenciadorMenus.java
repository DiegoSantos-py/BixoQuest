package view.cena;

import controller.GameController;
import controller.PersonagemController;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import model.Disciplina.Disciplina;
import model.Evento.Evento;
import model.Npc.Npc;
import view.menu.*;

import java.util.List;
import java.util.function.Consumer;

public class GerenciadorMenus {
    private final MenuPause menuPause;
    private final MenuAtributosPersonagem menuAtributos;
    private final MenuEscolhaDisciplina menuEscolhaDisciplina;
    private final MenuFeedbackEvento menuFeedbackEvento;
    private final DialogoNpc dialogoNpc; // novo
    private final Consumer<Boolean> onPausarJogo;
    private final StackPane raizCena;
    private final String spriteBase;

    public GerenciadorMenus(StackPane raizCena,
                            PersonagemController personagemController,
                            GameController gameController,
                            int personagemId,
                            String spriteBase,
                            Runnable onSairParaMenuPrincipal,
                            Consumer<List<Disciplina>> onConfirmarEscolhaDisciplina,
                            Consumer<Npc> onDialogoFinalizado, // novo
                            Consumer<Boolean> onPausarJogo) {
        this.onPausarJogo = onPausarJogo;

        this.raizCena = raizCena;
        this.menuPause = new MenuPause(this::fecharPause, onSairParaMenuPrincipal);
        this.menuAtributos = new MenuAtributosPersonagem(personagemController, personagemId, gameController, this::fecharAtributos);
        this.menuEscolhaDisciplina = new MenuEscolhaDisciplina(onConfirmarEscolhaDisciplina);
        this.menuFeedbackEvento = new MenuFeedbackEvento(this::fecharFeedbackEvento);
        this.dialogoNpc = new DialogoNpc(onDialogoFinalizado); // recebe o callback direto, sem indireção interna
        this.spriteBase = spriteBase;
        raizCena.getChildren().addAll(
                menuPause, menuAtributos, menuEscolhaDisciplina, menuFeedbackEvento, dialogoNpc
        );
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
        menuFeedbackEvento.abrir(evento, spriteBase);
        notificarPausa(true);
    }

    public void fecharFeedbackEvento() {
        notificarPausa(false);
    }

    public boolean dialogoAberto() {
        return dialogoNpc.isVisible();
    }

    public void abrirDialogo(Npc npc) {
        dialogoNpc.abrir(npc);
        // sem notificarPausa — jogo continua rodando, só o movimento é travado externamente
    }
}
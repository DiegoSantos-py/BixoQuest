package view.construtores;

import controller.GameController;
import controller.PersonagemController;
import model.Npc.Npc;
import view.cena.CenaJogo;
import view.util.Borda;

import java.util.function.Consumer;

/**
 * Define o contrato mínimo que qualquer construtor de cena deve respeitar.
 */
public interface Construtor {
    void setBackground(String imagePath, double largura, double altura);

    void addElement(String imagePath, double largura, double x, double y,
                    double hitboxOffsetX, double hitboxOffsetY,
                    double hitboxLargura, double hitboxAltura);

    void addNPC(Npc npc, double largura, double x, double y,
                double hitboxOffsetX, double hitboxOffsetY,
                double hitboxLargura, double hitboxAltura);

    void addInteractiveZone(String id, String imagePath,
                            double spriteLargura, double spriteAltura,
                            double spriteX, double spriteY,
                            double hitboxX, double hitboxY,
                            double hitboxLargura, double hitboxAltura,
                            Consumer<String> onEnter);

    void setPlayer(String imagePath, double largura, double x, double y,
                   double hitboxOffsetX, double hitboxOffsetY,
                   double hitboxLargura, double hitboxAltura);

    void setOnBordaAtingida(Consumer<Borda> onBordaAtingida);
    void setOnNpcAtingido(Consumer<String> onNpcAtingido);
    void setOnFinalizar(Runnable onFinalizar);
    void setOnDialogoFinalizado(Consumer<Npc> onDialogoFinalizado);

    void setPersonagem(PersonagemController personagemController, int personagemId);
    void setOnSairParaMenuPrincipal(Runnable onSairParaMenuPrincipal);
    void setGameController(GameController gameController);

    CenaJogo getResult();
}

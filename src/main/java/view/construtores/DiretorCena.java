package view.construtores;

import controller.MapaController;
import controller.NpcController;

import java.util.function.Consumer;

/**
 * Conhece a estrutura (receita) de cada cena
 * Recebe o Construtor e os Controllers como parâmetro — nunca acessa o produto diretamente
 */
public class DiretorCena {

    public void construirCena(Construtor construtor,
                              MapaController mapaController,
                              NpcController npcController,
                              Consumer<String> onZona,
                              Consumer<String> onNpc,
                              double playerX,
                              double playerY,
                              String spriteBase,
                              String nomeLocal) {

        construtor.setBackground(mapaController.buscarSpritePorNome(nomeLocal), 1920, 1080);

        mapaController.getElementos(nomeLocal).forEach(e ->
                construtor.addElement(e.getSprite(), e.getLargura(), e.getX(), e.getY(),
                        e.getHitboxOffsetX(), e.getHitboxOffsetY(),
                        e.getHitboxLargura(), e.getHitboxAltura())
        );

        mapaController.getNpcsDoLocal(nomeLocal).forEach(npc ->
                construtor.addNPC(npc.getSpriteDir(), npc.getNome(), npc.getLargura(),
                        npc.getcX(), npc.getcY(),
                        npc.getHitboxOffsetX(), npc.getHitboxOffsetY(),
                        npc.getHitboxLargura(), npc.getHitboxAltura())
        );

        mapaController.getZonasDoLocal(nomeLocal).forEach(zona ->
                construtor.addInteractiveZone(
                        zona.getNome(), zona.getSprite(),
                        zona.getSpriteLargura(), zona.getSpriteAltura(),
                        zona.getSpriteX(), zona.getSpriteY(),
                        zona.getX(), zona.getY(),
                        zona.getLargura(), zona.getAltura(),
                        onZona
                )
        );

        construtor.setOnNpcAtingido(onNpc);
        construtor.setPlayer(spriteBase, 350, playerX, playerY, 150, 150, 48, 96);
    }

    public void construirCenaEntradaModulo(Construtor construtor,
                                           MapaController mapaController,
                                           NpcController npcController,
                                           double playerX,
                                           double playerY,
                                           String spriteBase){
    construtor.setBackground("/assets/background/background_entrada_modulo.png", 1920,1080);

    construtor.addElement("/assets/placa_modulo_2.png", 500, 600, 500, 140,100, 150, 75);

    construtor.setPlayer(spriteBase, 350, playerX, playerY,
                150, 150, 48, 96);
    }

    public void construirCenaCantina(Construtor construtor,
                                     MapaController mapaController,
                                     NpcController npcController,
                                     double playerX,
                                     double playerY,
                                     String spriteBase){
    construtor.setBackground("/assets/background/background_cantina.png", 1920, 1080);

        construtor.setPlayer(spriteBase, 350, playerX, playerY,
                150, 150, 48, 96);

    }
}
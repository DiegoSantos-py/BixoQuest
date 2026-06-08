package view.construtores;

import controller.MapaController;
import controller.NpcController;

/**
 * Conhece a estrutura (receita) de cada cena.
 * Recebe o Construtor e os Controllers como parâmetro — nunca acessa o produto diretamente.
 */
public class DiretorCena {

    public void construirCena(Construtor construtor,
                              MapaController mapaController,
                              NpcController npcController) {

        // TODO: substituir pelo background vindo do MapaController
        construtor.setBackground("/assets/backgrounds/forest.png", 1920, 1080);

        // TODO: substituir pela lista de elementos vindo do MapaController
        // mapaController.getElementos().forEach(e ->
        //     construtor.addElement(e.getSprite(), e.getLargura(), e.getX(), e.getY())
        // );
        construtor.addElement("/assets/elements/tree.png", 150, 700, 50);
        construtor.addElement("/assets/elements/sign.png", 300, 1000, 650);

        // TODO: substituir pela lista de NPCs vindo do NpcController
        // npcController.getNpcs().forEach(npc ->
        //     construtor.addNPC(npc.getSprite(), npc.getLargura(), npc.getX(), npc.getY())
        // );
        construtor.addNPC("/assets/npcs/guard.png", 80, 500, 350);

        // TODO: substituir pela lista de zonas vindo do MapaController
        // mapaController.getZonas().forEach(zona ->
        //     construtor.addInteractiveZone(
        //         zona.getId(),
        //         zona.getSprite(),
        //         zona.getLargura(), zona.getAltura(),
        //         zona.getX(), zona.getY(),
        //         id -> {}
        //     )
        // );
        construtor.addInteractiveZone(
                "zona_entrada",
                "/assets/zones/entrada.png",
                60, 90,
                700, 300,
                id -> {}
        );

        // TODO: substituir pelo player vindo de um PlayerController
        construtor.setPlayer("/Jogador/Jogador1/rotations/south.png", 350, 700, 700);
    }

    public void construirCenaPontoOnibus(Construtor construtor,
                                         MapaController mapaController,
                                         NpcController npcController) {

        // TODO: substituir pelo background vindo do MapaController
        construtor.setBackground("/assets/Background_ponto_onibus.png", 1920, 1080);

        // TODO: substituir pela lista de elementos vindo do MapaController
        // mapaController.getElementos().forEach(e ->
        //     construtor.addElement(e.getSprite(), e.getLargura(), e.getX(), e.getY())
        // );

        // Decoração
        construtor.addElement("/assets/arvore.png",           700, 50,   150);
        construtor.addElement("/assets/arbusto_sem_flor.png", 300, 50,   850);
        construtor.addElement("/assets/arbusto_com_flor.png", 450, 350,  50);
        construtor.addElement("/assets/placa_ponto1.png",     300, 1000, 650);

        // Objetos
        construtor.addElement("/assets/ponto_de_onibus.png",  800, 450,  350);

        // TODO: substituir pela lista de NPCs vindo do NpcController
        // npcController.getNpcs().forEach(npc ->
        //     construtor.addNPC(npc.getSprite(), npc.getLargura(), npc.getX(), npc.getY())
        // );

        // TODO: substituir pela lista de zonas vindo do MapaController
        // mapaController.getZonas().forEach(zona ->
        //     construtor.addInteractiveZone(
        //         zona.getId(),
        //         zona.getSprite(),
        //         zona.getLargura(), zona.getAltura(),
        //         zona.getX(), zona.getY(),
        //         id -> {}
        //     )
        // );

        // TODO: substituir pelo player vindo de um PlayerController
        construtor.setPlayer("/Jogador/Jogador1/rotations/south.png", 350, 700, 700);
    }
}
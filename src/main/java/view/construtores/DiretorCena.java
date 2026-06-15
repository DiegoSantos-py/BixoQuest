package view.construtores;

import controller.MapaController;
import controller.NpcController;

/**
 * Conhece a estrutura (receita) de cada cena
 * Recebe o Construtor e os Controllers como parâmetro — nunca acessa o produto diretamente
 */
public class DiretorCena {

    public void construirCena(Construtor construtor,
                              MapaController mapaController,
                              NpcController npcController) {

        // TODO: substituir pelo background vindo do MapaController
        construtor.setBackground("/assets/backgrounds/forest.png", 1280, 720);

        // TODO: substituir pela lista de elementos vindo do MapaController
        // mapaController.getElementos().forEach(e ->
        //     construtor.addElement(e.getSprite(), e.getLargura(), e.getX(), e.getY(),
        //             e.getHitboxOffsetX(), e.getHitboxOffsetY(),
        //             e.getHitboxLargura(), e.getHitboxAltura())
        // );
        construtor.addElement("/assets/elements/tree.png", 150, 700, 50,
                45, 80, 60, 70);
        construtor.addElement("/assets/elements/sign.png", 300, 1000, 650,
                10, 5, 40, 55);

        // TODO: substituir pela lista de NPCs vindo do NpcController
        // npcController.getNpcs().forEach(npc ->
        //     construtor.addNPC(npc.getSprite(), npc.getLargura(), npc.getX(), npc.getY(),
        //             npc.getHitboxOffsetX(), npc.getHitboxOffsetY(),
        //             npc.getHitboxLargura(), npc.getHitboxAltura())
        // );
        construtor.addNPC("/assets/npcs/guard.png", 80, 500, 350,
                10, 20, 60, 60);

        // TODO: substituir pela lista de zonas vindo do MapaController
        // mapaController.getZonas().forEach(zona ->
        //     construtor.addInteractiveZone(
        //         zona.getId(), zona.getSprite(),
        //         zona.getLargura(), zona.getAltura(),
        //         zona.getX(), zona.getY(),
        //         id -> {}
        //     )
        // );
        construtor.addInteractiveZone(
                "zona_entrada",
                "/assets/zones/entrada.png",
                60, 90, 700, 300,
                id -> {}
        );

        // TODO: substituir pelo player vindo de um PlayerController
        construtor.setPlayer("/Jogador/Jogador1/rotations/south.png", 350, 700, 700,
                125, 200, 100, 150);
    }

    public void construirCenaPontoOnibus(Construtor construtor,
                                         MapaController mapaController,
                                         NpcController npcController) {

        // TODO: substituir pelo background vindo do MapaController
        construtor.setBackground("/assets/Background_ponto_onibus.png", 1920, 1080);

        // TODO: substituir pela lista de elementos vindo do MapaController
        // mapaController.getElementos().forEach(e ->
        //     construtor.addElement(e.getSprite(), e.getLargura(), e.getX(), e.getY(),
        //             e.getHitboxOffsetX(), e.getHitboxOffsetY(),
        //             e.getHitboxLargura(), e.getHitboxAltura())
        // );

        // Decoração
        construtor.addElement("/assets/arvore.png",           700,  50,  150, 285, 190, 100, 150);
        construtor.addElement("/assets/arbusto_sem_flor.png", 300,  50,  850,  80, 70, 100, 100);
        construtor.addElement("/assets/arbusto_com_flor.png", 450, 350,   50,  170, 110, 100, 100);
        construtor.addElement("/assets/placa_ponto1.png",     300, 1000, 650,  130,   200, 50, 100);

        // Objetos
        construtor.addElement("/assets/ponto_de_onibus.png",  800, 450,  350, 300, 250, 200, 100);

        // TODO: substituir pela lista de NPCs vindo do NpcController
        // npcController.getNpcs().forEach(npc ->
        //     construtor.addNPC(npc.getSprite(), npc.getLargura(), npc.getX(), npc.getY(),
        //             npc.getHitboxOffsetX(), npc.getHitboxOffsetY(),
        //             npc.getHitboxLargura(), npc.getHitboxAltura())
        // );

        // TODO: substituir pela lista de zonas vindo do MapaController
        // mapaController.getZonas().forEach(zona ->
        //     construtor.addInteractiveZone(
        //         zona.getId(), zona.getSprite(),
        //         zona.getLargura(), zona.getAltura(),
        //         zona.getX(), zona.getY(),
        //         id -> {}
        //     )
        // );

        // TODO: conectar ao MapaService
        construtor.setOnBordaAtingida(borda -> System.out.println("Borda atingida: " + borda));

        // TODO: substituir pelo player vindo de um PlayerController
        construtor.setPlayer("/Jogador/Jogador1/rotations/south.png", 350, 700, 700,
                150, 150, 48, 96);
    }

    public void construirCenaEntradaModulo(Construtor construtor,
                                           MapaController mapaController,
                                           NpcController npcController){
    construtor.setBackground("/assets/background_entrada_modulo.png", 1920,1080);

    construtor.addElement("/assets/placa_modulo_2.png", 500, 600, 500, 140,100, 150, 75);

    construtor.setPlayer("/Jogador/Jogador1/rotations/south.png", 350, 500, 700,
                150, 150, 48, 96);
    }

    public void construirCenaCantina(Construtor construtor,
                                     MapaController mapaController,
                                     NpcController npcController){
    construtor.setBackground("/assets/background_cantina.png", 1920, 1080);

        construtor.setPlayer("/Jogador/Jogador1/rotations/south.png", 350, 500, 700,
                150, 150, 48, 96);

    }
}
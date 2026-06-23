package view.construtores;

import controller.MapaController;
import controller.NpcController;

/**
 * Conhece a estrutura (receita) de cada cena
 * Recebe o Construtor e os Controllers como parâmetro — nunca acessa o produto diretamente
 */
public class DiretorCena {

    public void construirCenaPontoOnibus(Construtor construtor,
                                         MapaController mapaController,
                                         NpcController npcController) {

        construtor.setBackground(mapaController.buscarSpritePorNome("Ponto de ônibus 1"), 1920, 1080);

        mapaController.getElementos("Ponto de ônibus 1").forEach(e ->
                construtor.addElement(e.getSprite(), e.getLargura(), e.getX(), e.getY(),
                        e.getHitboxOffsetX(), e.getHitboxOffsetY(),
                        e.getHitboxLargura(), e.getHitboxAltura())
        );

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
    construtor.setBackground("/assets/background/background_entrada_modulo.png", 1920,1080);

    construtor.addElement("/assets/placa_modulo_2.png", 500, 600, 500, 140,100, 150, 75);

    construtor.setPlayer("/Jogador/Jogador1/rotations/south.png", 350, 500, 700,
                150, 150, 48, 96);
    }

    public void construirCenaCantina(Construtor construtor,
                                     MapaController mapaController,
                                     NpcController npcController){
    construtor.setBackground("/assets/background/background_cantina.png", 1920, 1080);

        construtor.setPlayer("/Jogador/Jogador1/rotations/south.png", 350, 500, 700,
                150, 150, 48, 96);

    }
}
package model.Evento.Prova;

import model.Disciplina.AreaConhecimento;
import model.Evento.Prova.Questao.QuestaoFactory;
import model.Player.AcaoBatalha;

import java.util.ArrayList;

public class ProvaFactory {

    /**
     * Cria uma ProvaBatalha completa a partir do ID e nivel da disciplina.
     * As questoes chegam com Ataque de owner=null — injetado por Oponente.adicionarAtaque.
     */
    public static ProvaBatalha criar(ProvaIDs id, int nivelDisciplina) {
        switch (id) {
            case MAT_01: return criarMatematica(1);
            case MAT_02: return criarMatematica(2);
            case MAT_03: return criarMatematica(3);
            default:
                throw new IllegalArgumentException("ProvaIDs sem implementacao na factory: " + id);
        }
    }

    public static ProvaBatalha criarMatematica(int nivel) {
        switch (nivel) {
            case 1:
                return criarMatematicaNivel1();
            case 2:
               return criarMatematicaNivel2();
            case 3:
                // return criarMatematicaNivel3();
                throw new IllegalArgumentException("Nivel 3 de matematica em construcao.");
            default:
                throw new IllegalArgumentException("Nivel de matematica nao implementado: " + nivel);
        }
    }

    // --- Matematica Niveis ---------------------------------------------------

    private static ProvaBatalha criarMatematicaNivel1() {
        int nivel = 1;
        ArrayList<AcaoBatalha> acoes = new ArrayList<>();
        // favor nao botar mais de 4 acoes em prol da UI
        acoes.add(new AcaoBatalha("USAR CALCULADORA",  0.9f, 2, 0, 1, 0f));
        acoes.add(new AcaoBatalha("CHUTAR A RESPOSTA", 0.2f, 0, 0, 0, 0f));
        acoes.add(new AcaoBatalha("REVISAR ANOTACOES", 0.6f, 1, 1, 0, 0f));
        acoes.add(new AcaoBatalha("DESISTIR",          1.0f, 0, 0, 0, 5f));

        ProvaBatalha prova = new ProvaBatalha(
                "Matematica 1",
                "aaaaaa",
                AreaConhecimento.MAT,
                nivel,
                0, // quantidadeQuestoes começa em 0; addQuestao incrementa
                acoes,
                "/assets/batalha/oponentes/provas/matematica.png",
                "/assets/audio/provas/matematica1.mp3"
        );

        // 1 questao de demonstracao com ataque tipo cachorro (Latido)
        prova.addQuestao(QuestaoFactory.criarFuncaoAfim(nivel));

        prova.addQuestao(QuestaoFactory.criarSenoidal(nivel));
        
        prova.addQuestao(QuestaoFactory.criarRetaTangente(nivel));

        prova.addQuestao(QuestaoFactory.criarFuncaoIntegral(nivel));
        return prova;
    }

    
    private static ProvaBatalha criarMatematicaNivel2() {
        int nivel = 2;
        ArrayList<AcaoBatalha> acoes = new ArrayList<>();
        // favor nao botar mais de 4 acoes em prol da UI
        acoes.add(new AcaoBatalha("USAR CALCULADORA",  0.9f, 2, 0, 1, 0f));
        acoes.add(new AcaoBatalha("CHUTAR A RESPOSTA", 0.2f, 0, 0, 0, 0f));
        acoes.add(new AcaoBatalha("REVISAR ANOTACOES", 0.6f, 1, 1, 0, 0f));
        acoes.add(new AcaoBatalha("DESISTIR",          1.0f, 0, 0, 0, 5f));

        ProvaBatalha prova = new ProvaBatalha(
                "Matematica 2",
                "sim",
                AreaConhecimento.MAT,
                nivel,
                0, // quantidadeQuestoes começa em 0; addQuestao incrementa
                acoes,
                "/assets/batalha/oponentes/provas/matematica.png",
                "/assets/audio/provas/matematica2.mp3"
        );

        // 1 questao de demonstracao com ataque tipo cachorro (Latido)
        prova.addQuestao(QuestaoFactory.criarQuestaoVetores(nivel));

        prova.addQuestao(QuestaoFactory.criarFuncaoIntegral2(nivel));

        return prova;
    }
}

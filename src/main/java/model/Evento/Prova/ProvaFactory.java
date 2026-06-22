package model.Evento.Prova;

import model.Disciplina.AreaConhecimento;
import model.Evento.Prova.Questao.QuestaoFactory;
import model.Evento.Prova.Questao.QuestaoIDs;
import model.Player.AcaoBatalha;

import java.util.ArrayList;

public class ProvaFactory {

    /**
     * Cria uma ProvaBatalha completa a partir do ID e nivel da disciplina.
     * As questoes chegam com Ataque de owner=null — injetado por Oponente.adicionarAtaque.
     */
    public static ProvaBatalha criar(ProvaIDs id, int nivelDisciplina) {
        switch (id) {
            case MAT_01: return criarMatematica01(nivelDisciplina);
            default:
                throw new IllegalArgumentException("ProvaIDs sem implementacao na factory: " + id);
        }
    }

    // --- Matematica 1 --------------------------------------------------------

    private static ProvaBatalha criarMatematica01(int nivel) {
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
        prova.addQuestao(QuestaoFactory.criar(QuestaoIDs.MAT_01_FUNCAO_AFIM, nivel));


        prova.addQuestao(QuestaoFactory.criar(QuestaoIDs.MAT_O1_SENOIDAL, nivel));

        prova.addQuestao(QuestaoFactory.criar(QuestaoIDs.MAT_01_FUNCAO_ATIRANDO, nivel));
        return prova;
    }
}

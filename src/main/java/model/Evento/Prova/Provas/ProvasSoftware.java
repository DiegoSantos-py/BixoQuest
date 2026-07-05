package model.Evento.Prova.Provas;

import model.Disciplina.AreaConhecimento;
import model.Evento.Prova.ProvaBatalha;
import model.Evento.Prova.Questao.Questao;
import model.Player.AcaoBatalha;
import model.Ataque.Ataques.Prova.Software.AtaqueAlgoritimo;
import model.Ataque.Ataques.Prova.Software.AtaqueArvoreBinaria;

import java.util.ArrayList;

public class ProvasSoftware {

    public static ProvaBatalha criarSoftwareNivel1() {
        int nivel = 1;
        ArrayList<AcaoBatalha> acoes = new ArrayList<>();
        
        acoes.add(new AcaoBatalha("DEBUGAR CODIGO",  0.9f, 2, 0, 1, 0));
        acoes.add(new AcaoBatalha("PEDIR AJUDA PRO TIO CLAUDIO", 0.5f, 1, 1, 0.1f, 0.5f));
        acoes.add(new AcaoBatalha("CHUTAR SOLUCAO", 0.2f, 0, 0, 0, 0));
        acoes.add(new AcaoBatalha("DESISTIR",          1.0f, 0, 0, 0, 0));

        ProvaBatalha prova = new ProvaBatalha(
                "Software 1",
                "Você encarou o exame de software...",
                AreaConhecimento.SOF,
                nivel,
                0,
                acoes,
                "software.png",
                "software1.mp3"
        );

        prova.addQuestao(new Questao(
                "Complexidade O(n^2)", 70f, AreaConhecimento.SOF, 10f,
                new AtaqueAlgoritimo(null, null, 10f),
                "Um algoritimo ineficiente aparece!", "Seu cerebro começa a esquentar..."
        ));

        return prova;
    }

    public static ProvaBatalha criarSoftwareNivel2() {
        int nivel = 2;
        ArrayList<AcaoBatalha> acoes = new ArrayList<>();
        
        acoes.add(new AcaoBatalha("REVISAR CÓDIGO",  0.9f, 2, 0, 1, 0));
        acoes.add(new AcaoBatalha("DEBUGAR PONTEIROS", 0.5f, 1, 1, 0.1f, 0.5f));
        acoes.add(new AcaoBatalha("CHUTAR SOLUCAO", 0.2f, 0, 0, 0, 0));
        acoes.add(new AcaoBatalha("DESISTIR",          1.0f, 0, 0, 0, 0));

        ProvaBatalha prova = new ProvaBatalha(
                "Software 2",
                "Estruturas de Dados",
                AreaConhecimento.SOF,
                nivel,
                0,
                acoes,
                "software.png",
                "software2.mp3"
        );

        prova.addQuestao(new Questao(
                "Árvore Binária de Busca!", 60f, AreaConhecimento.SOF, 12f,
                new AtaqueArvoreBinaria(null, null, 12f),
                "Os nós começam a se multiplicar!", "Cuidado com a recursão!"
        ));

        return prova;
    }

    public static ProvaBatalha criarSoftwareNivel3() {
        int nivel = 3;
        ArrayList<AcaoBatalha> acoes = new ArrayList<>();
        
        acoes.add(new AcaoBatalha("REFATORAR O SISTEMA",  0.9f, 2, 0, 1, 0));
        acoes.add(new AcaoBatalha("LIGAR PRO SENIOR", 0.5f, 1, 1, 0.1f, 0.5f));
        acoes.add(new AcaoBatalha("REESCREVER DO ZERO", 0.2f, 0, 0, 0, 0));
        acoes.add(new AcaoBatalha("DESISTIR",          1.0f, 0, 0, 0, 0));

        ProvaBatalha prova = new ProvaBatalha(
                "Software 3",
                "Projeto de Software Avançado",
                AreaConhecimento.SOF,
                nivel,
                0,
                acoes,
                "software.png",
                "software3.mp3"
        );

        prova.addQuestao(new Questao(
                "Algoritmo Desotimizado", 70f, AreaConhecimento.SOF, 20f,
                new AtaqueAlgoritimo(null, null, 20f),
                "O código legado contra-ataca!", "Tente não enlouquecer."
        ));

        prova.addQuestao(new Questao(
                "Árvore Binária Desbalanceada", 70f, AreaConhecimento.SOF, 20f,
                new AtaqueArvoreBinaria(null, null, 20f),
                "Cuidado com o estouro de pilha!", "Essa recursão vai doer."
        ));

        return prova;
    }
}

package model.Evento.Prova.Provas;

import model.Disciplina.AreaConhecimento;
import model.Evento.Prova.ProvaBatalha;
import model.Evento.Prova.Questao.Questao;
import model.Player.AcaoBatalha;
import model.Ataque.Ataques.Prova.Software.AtaqueAlgoritimo;

import java.util.ArrayList;

public class ProvasSoftware {

    public static ProvaBatalha criarSoftwareNivel1() {
        int nivel = 1;
        ArrayList<AcaoBatalha> acoes = new ArrayList<>();
        
        acoes.add(new AcaoBatalha("DEBUGAR CODIGO",  0.9f, 2, 0, 1, 0));
        acoes.add(new AcaoBatalha("PESQUISAR NO STACK", 0.6f, 1, 1, 0, 0));
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
}

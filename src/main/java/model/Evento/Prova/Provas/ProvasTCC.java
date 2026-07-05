package model.Evento.Prova.Provas;

import model.Disciplina.AreaConhecimento;
import model.Evento.Prova.ProvaBatalha;
import model.Evento.Prova.Questao.Questao;
import model.Player.AcaoBatalha;
import model.Ataque.Ataques.Prova.Matematica.*;
import model.Ataque.Ataques.Prova.Naturezas.*;
import model.Ataque.Ataques.Prova.Software.*;
import model.Ataque.Ataques.Prova.Hardware.*;

import java.util.ArrayList;

public class ProvasTCC {

    public static ProvaBatalha criarTCC() {
        ArrayList<AcaoBatalha> acoes = new ArrayList<>();
        acoes.add(new AcaoBatalha("DEFENDER A TESE",        0.90f, 1, 1, 0.25f, 1.0f));
        acoes.add(new AcaoBatalha("CORTAR SLIDES",          0.55f, 2, 0, 0.00f, 5.5f));
        acoes.add(new AcaoBatalha("BEBER CAFÉ",             0.35f, 2, 2, 0.55f, 1.5f));
        acoes.add(new AcaoBatalha("CHORAR EM POSIÇÃO FETAL",1.00f, 0, 1, 0.00f, 2.5f));

        ProvaBatalha prova = new ProvaBatalha(
                "Trabalho de Conclusão",
                "A banca final está te aguardando!",
                AreaConhecimento.ESTAGIO, 
                4,
                0,
                acoes,
                "tcc.png",
                "tcc.mp3"
        );

        float dif = 22f;


        prova.addQuestao(new Questao(
                "Matemática Avançada", 125f , AreaConhecimento.MAT, dif,
                "O avaliador de exatas abriu o microfone!", "'É intuitivo' não é uma demonstração.",
                new AtaqueFuncaoAfim(null, null, dif),
                new AtaqueVetores(null, null, dif),
                new AtaqueEstatistica(null, null, dif),
                new AtaqueRetaTangente(null, null, dif),
                new AtaqueGradiente(null, null, dif),
                new AtaqueAmplitudeMaxima(null, null, dif),
                new AtaqueIntegral3(null, null, dif)
        ));

        // Questão 1: NATUREZAS (100 HP)
        prova.addQuestao(new Questao(
                "Fisica final", 125f    , AreaConhecimento.NAT, dif,
                "Agora é a vez do professor de Física.", "Anomalia detectada!",
                new AtaqueCarroAviao(null, null, dif),
                new AtaqueGravidade(null, null, dif),
                new AtaqueMagnetico(null, null, dif),
                new AtaqueColisaoElastica(null, null, dif),
                new AtaqueAtomosGravidade(null, null, dif)
        ));

        prova.addQuestao(new Questao(
                "Estruturas de Dados e Algoritmos", 125f    , AreaConhecimento.SOF, dif,
                "O avaliador está analisando seu código...", "'Na minha maquina funciona' não é uma resposta",
                new AtaqueAlgoritimo(null, null, dif),
                new AtaqueArvoreBinaria(null, null, dif)
        ));

        prova.addQuestao(new Questao(
                "Aplicação em hardware do seu tcc", 125f    , AreaConhecimento.HAR, dif,
                "Ele percebeu que você pulou um detalhe importante.!", "\"Infelizmente, era o detalhe da área dele.\"",
                new AtaquePulsos(null, null, dif),
                new AtaqueBarramento(null, null, dif),
                new AtaqueOverflow(null, null, dif)
        ));

        return prova;
    }
}

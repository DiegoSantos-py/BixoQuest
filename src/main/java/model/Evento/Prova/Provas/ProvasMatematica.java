package model.Evento.Prova.Provas;

import model.Disciplina.AreaConhecimento;
import model.Evento.Prova.ProvaBatalha;
import model.Evento.Prova.Questao.Questao;
import model.Ataque.Ataques.Prova.Matematica.AtaqueFuncaoAfim;
import model.Ataque.Ataques.Prova.Matematica.AtaqueAmplitudeMaxima;
import model.Ataque.Ataques.Prova.Matematica.AtaqueRetaTangente;
import model.Ataque.Ataques.Prova.Matematica.AtaqueIntegral;
import model.Ataque.Ataques.Prova.Matematica.AtaqueVetores;
import model.Ataque.Ataques.Prova.Matematica.AtaqueGradiente;
import model.Ataque.Ataques.Prova.Matematica.AtaqueIntegral2;
import model.Player.AcaoBatalha;

import java.util.ArrayList;

public class ProvasMatematica {


    public static ProvaBatalha criarMatematicaNivel1() {
        int nivel = 1;
        ArrayList<AcaoBatalha> acoes = new ArrayList<>();
        // favor nao botar mais de 4 acoes em prol da UI
        acoes.add(new AcaoBatalha("USAR CALCULADORA",  0.9f, 2, 0, 1, 0));
        acoes.add(new AcaoBatalha("CHUTAR A RESPOSTA", 0.2f, 0, 0, 0, 0));
        acoes.add(new AcaoBatalha("TENTAR COLAR DO COLEGA", 0.35f, 1, 1, 0, 0.5f));
        acoes.add(new AcaoBatalha("DESISTIR",          1.0f, 0, 0, 0, 0));

        ProvaBatalha prova = new ProvaBatalha(
                "Matematica 1",
                "aaaaaa",
                AreaConhecimento.MAT,
                nivel,
                0, // quantidadeQuestoes começa em 0; addQuestao incrementa
                acoes,
                "matematica.png",
                "matematica1.mp3"
        );

        // 1 questao de demonstracao com ataque tipo cachorro (Latido)
        prova.addQuestao(new Questao(
                "Questao de funcao afim", 15f, AreaConhecimento.MAT, 7f,
                new AtaqueFuncaoAfim(null, null, 7f),
                "Funcao afim de seu Fim", "Voce sente seu fim proximo"
        ));

        prova.addQuestao(new Questao(
                "Amplitude Maxima", 15f, AreaConhecimento.MAT, 9f,
                new AtaqueAmplitudeMaxima(null, null, 9f),
                "ciclicamente aterrorizante", "Voce sente seu sangue pulsar."
        ));

        prova.addQuestao(new Questao(
                "Questao de Reta Tangente", 20f, AreaConhecimento.MAT, 9f,
                new AtaqueRetaTangente(null, null, 9f),
                "e VOCE é a curva", "ela sabe pra onde voce vai..."
        ));

        prova.addQuestao(new Questao(
                "Desintegrando Notas.", 25f, AreaConhecimento.MAT, 12f,
                new AtaqueIntegral(null, null, 12f),
                "Pelo menos é so uma integral...", "Não se esqueça do +C"
        ));
        return prova;
    }


    public static ProvaBatalha criarMatematicaNivel2() {
        int nivel = 2;
        ArrayList<AcaoBatalha> acoes = new ArrayList<>();
        // favor nao botar mais de 4 acoes em prol da UI
        acoes.add(new AcaoBatalha("USAR CALCULADORA",  0.9f, 0.5f, 0, 0, 0));
        acoes.add(new AcaoBatalha("CHUTAR A RESPOSTA", 0.2f, 0.5f, 0, 0.2f, 0.2f));
        acoes.add(new AcaoBatalha("TENTAR COLAR DO COLEGA", 0.35f, 1, 1, 0, 0.5f));
        acoes.add(new AcaoBatalha("DESISTIR",          1.0f, 0, 0, 0, 0));

        ProvaBatalha prova = new ProvaBatalha(
                "Matematica 2",
                "sim",
                AreaConhecimento.MAT,
                nivel,
                0, // quantidadeQuestoes começa em 0; addQuestao incrementa
                acoes,
                "matematica.png",
                "matematica2.mp3"
        );

        // 1 questao de demonstracao com ataque tipo cachorro (Latido)
        prova.addQuestao(new Questao(
                "Introduzido ao mundo vetorial!", 32f, AreaConhecimento.MAT, 14f,
                new AtaqueVetores(null, null, 12f),
                "E voce nao gosta disso..", "é so lembrar da regra da mao direita"
        ));

        prova.addQuestao(new Questao(
                "Gradiente", 35f, AreaConhecimento.MAT, 16f,
                new AtaqueGradiente(null, null, 16f),
                "É a funcao gradiente", "O ponto de maior crscimento é o centro."
        ));

        prova.addQuestao(new Questao(
                "Integrais duplas", 37f, AreaConhecimento.MAT, 16f,
                new AtaqueIntegral2(null, null, 16f),
                "NÃO É MAIS SO UMA INTEGRAL.", "Voce tenta lembrar das regras de integração..."
        ));
        return prova;
    }

}

package model.Evento.Prova.Provas;

import model.Disciplina.AreaConhecimento;
import model.Evento.Prova.ProvaBatalha;
import model.Evento.Prova.Questao.Questao;
import model.Ataque.Ataques.Prova.Estagio.AtaqueGoogle;
import model.Ataque.Ataques.Prova.Estagio.AtaquePalavras;
import model.Player.AcaoBatalha;

import java.util.ArrayList;

public class ProvasEstagio {

    public static ProvaBatalha criarEstagio() {
        ArrayList<AcaoBatalha> acoes = new ArrayList<>();
        acoes.add(new AcaoBatalha("ENVIAR CURRÍCULO", 0.9f, 2, 0, 0.25f, 5f));
        acoes.add(new AcaoBatalha("FAZER NETWORKING", 0.5f, 0, 1, 0, 6f));
        
        ProvaBatalha prova = new ProvaBatalha(
                "Processo Seletivo",
                "sim",
                AreaConhecimento.ESTAGIO,
                1,
                0,
                acoes,
                "estagio.png",
                "estagio.mp3"
        );

        prova.addQuestao(new Questao(
                "Entrevista Google", 
                50f, AreaConhecimento.ESTAGIO,
                10f,
                new AtaqueGoogle(null, null, 10f),
                "voce encara o linkedin desesperançoso", "Você precisa achar um emprego!"
        ));

        prova.addQuestao(new Questao(
                "Dinâmica de Grupo", 
                50f, AreaConhecimento.ESTAGIO, 
                10f,
                new AtaquePalavras(null, null, 10f),
                "Avaliando o seu perfil...", "Atire na palavra positiva!"
        ));

        return prova;
    }
}

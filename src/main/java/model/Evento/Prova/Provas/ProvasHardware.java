package model.Evento.Prova.Provas;

import model.Disciplina.AreaConhecimento;
import model.Evento.Prova.ProvaBatalha;
import model.Evento.Prova.Questao.Questao;
import model.Player.AcaoBatalha;
import model.Ataque.Ataques.Prova.Hardware.AtaquePulsos;

import java.util.ArrayList;

public class ProvasHardware {

    public static ProvaBatalha criarHardwareNivel1() {
        int nivel = 1;
        ArrayList<AcaoBatalha> acoes = new ArrayList<>();
        
        acoes.add(new AcaoBatalha("LER O DATASHEET",  0.9f, 2, 0, 1, 0));
        acoes.add(new AcaoBatalha("MEXER NA PROTOBOARD", 0.5f, 1, 1, 0.1f, 0.5f));
        acoes.add(new AcaoBatalha("CHUTAR O CIRCUITO", 0.2f, 0, 0, 0, 0));
        acoes.add(new AcaoBatalha("DESISTIR",          1.0f, 0, 0, 0, 0));

        ProvaBatalha prova = new ProvaBatalha(
                "Hardware 1",
                "Você está montando um circuito...",
                AreaConhecimento.HAR,
                nivel,
                0,
                acoes,
                "hardware.png",
                "hardware1.mp3"
        );

        prova.addQuestao(new Questao(
                "Curto Circuito!", 50f, AreaConhecimento.HAR, 10f,
                new AtaquePulsos(null, null, 10f),
                "A protoboard está em curto!", "Cuidado com a fonte..."
        ));

        return prova;
    }
}

package model.Evento.Prova.Provas;

import model.Disciplina.AreaConhecimento;
import model.Evento.Prova.ProvaBatalha;
import model.Evento.Prova.Questao.Questao;
import model.Player.AcaoBatalha;
import model.Ataque.Ataques.Prova.Hardware.AtaquePulsos;
import model.Ataque.Ataques.Prova.Hardware.AtaqueOverflow;
import model.Ataque.Ataques.Prova.Hardware.AtaqueBarramento;

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

        prova.addQuestao(new Questao(
                "Overflow de Dados!", 50f, AreaConhecimento.HAR, 10f,
                new AtaqueOverflow(null, null, 10f),
                "Muitos dados no barramento!", "Desvie dos bits!"
        ));

        return prova;
    }

    public static ProvaBatalha criarHardwareNivel2() {
        int nivel = 2;
        ArrayList<AcaoBatalha> acoes = new ArrayList<>();
        
        acoes.add(new AcaoBatalha("LER O DATASHEET",  0.9f, 2, 0, 1, 0));
        acoes.add(new AcaoBatalha("MEXER NA PROTOBOARD", 0.5f, 1, 1, 0.1f, 0.5f));
        acoes.add(new AcaoBatalha("CHUTAR O CIRCUITO", 0.2f, 0, 0, 0, 0));
        acoes.add(new AcaoBatalha("DESISTIR",          1.0f, 0, 0, 0, 0));

        ProvaBatalha prova = new ProvaBatalha(
                "Hardware 2",
                "Arquitetura de Computadores",
                AreaConhecimento.HAR,
                nivel,
                0,
                acoes,
                "hardware.png",
                "hardware2.mp3"
        );

        prova.addQuestao(new Questao(
                "Barramento de Dados (Data Bus)", 60f, AreaConhecimento.HAR, 15f,
                new AtaqueBarramento(null, null, 15f),
                "O barramento paralelo iniciou transmissão!", "Passe pelos zeros (0) para sobreviver!"
        ));

        prova.addQuestao(new Questao(
                "Stack Overflow Crítico!", 60f, AreaConhecimento.HAR, 15f,
                new AtaqueOverflow(null, null, 15f), // 1.5x de dificuldade (10 * 1.5 = 15)
                "O buffer está transbordando!", "0s e 1s para todo lado!"
        ));

        return prova;
    }

    public static ProvaBatalha criarHardwareNivel3() {
        int nivel = 3;
        ArrayList<AcaoBatalha> acoes = new ArrayList<>();
        
        acoes.add(new AcaoBatalha("OTIMIZAR ASSEMBLY",  0.9f, 2, 0, 1, 0));
        acoes.add(new AcaoBatalha("TROCAR PASTA TÉRMICA", 0.5f, 1, 1, 0.1f, 0.5f));
        acoes.add(new AcaoBatalha("FAZER OVERCLOCK", 0.2f, 0, 0, 0, 0));

        ProvaBatalha prova = new ProvaBatalha(
                "Hardware 3",
                "Microcontroladores e Assembly",
                AreaConhecimento.HAR,
                nivel,
                0,
                acoes,
                "hardware.png",
                "hardware3.mp3"
        );

        prova.addQuestao(new Questao(
                "Sobrecarga de Pinos!", 70f, AreaConhecimento.HAR, 20f,
                new AtaquePulsos(null, null, 20f),
                "O microcontrolador entrou em curto!", "Desvie das descargas elétricas!"
        ));

        prova.addQuestao(new Questao(
                "Barramento Paralelo de Alta Velocidade", 70f, AreaConhecimento.HAR, 20f,
                new AtaqueBarramento(null, null, 20f),
                "Transferência de 64 bits iniciada!", "Os dados estão acelerando muito..."
        ));

        prova.addQuestao(new Questao(
                "Falha de Segmentação (Core Dumped)", 70f, AreaConhecimento.HAR, 20f,
                new AtaqueOverflow(null, null, 20f),
                "O processador está despejando memória!", "Tente sobreviver ao caos!"
        ));

        return prova;
    }
}

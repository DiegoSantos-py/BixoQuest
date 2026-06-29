package model.Evento.Prova.Questao;

import model.Ataque.Ataques.AtaqueArranhao;
import model.Ataque.Ataques.Provas.Matematica.AtaqueIntegral2;
import model.Ataque.Ataques.Provas.Matematica.AtaqueVetores;
import model.Ataque.Ataques.Provas.Matematica.AtaqueRetaTangente;
import model.Ataque.Ataques.AtaqueLatido;
import model.Ataque.Ataques.AtaqueMordida;
import model.Ataque.Ataques.Provas.Matematica.AtaqueAmplitudeMaxima;
import model.Ataque.Ataques.Provas.Matematica.AtaqueFuncaoAfim;
import model.Disciplina.AreaConhecimento;

public class QuestaoFactory {



    // --- Matematica 1 --------------------------------------------------------

    // Ataque: cachorro (Latido) — projeteis homing simples
    public static Questao criarFuncaoAfim(int nivel) {
        return new Questao(
                "Questao de funcao afim",
                10f + nivel * 5f,
                AreaConhecimento.MAT,
                5f + nivel * 2f,
                new AtaqueFuncaoAfim(null, null, 5f + nivel * 2f),
                "Funcao afim de seu Fim",
                "Voce sente seu fim proximo"
        );
    }

    // Ataque: gato (Arranhao) — chuva de arranhoes com previa
    public static Questao criarSenoidal(int nivel) {
        return new Questao(
                "Amplitude Maxima",
                10f + nivel * 5f,
                AreaConhecimento.MAT,
                6f + nivel * 3f,
                new AtaqueAmplitudeMaxima(null, null, 6f + nivel * 3f),
                "ciclicamente aterrorizante",
                "Voce sente seu sangue pulsar."
        );
    }

    public static Questao criarRetaTangente(int nivel) {
        return new Questao(
                "Questao de Reta Tangente",
                10f + nivel * 10f,
                AreaConhecimento.MAT,
                6f + nivel * 3f,
                new AtaqueRetaTangente(null, null, 6f + nivel * 3f),
                "e VOCE é a curva",
                "ela sabe pra onde voce vai..."
        );
    }

    public static Questao criarFuncaoIntegral(int nivel) {
        return new Questao(
                "Desintegrando Notas.",
                10f + nivel * 15f,
                AreaConhecimento.MAT,
                8f + nivel * 4f,
                new AtaqueIntegral2(null, null, 8f + nivel * 4f),
                "Pelo menos é so uma integral...",
                "Não se esqueça do +C"
        );
    }
    public static Questao criarFuncaoIntegral2(int nivel) {
        return new Questao(
                "Como assim sao duas",
                15f + nivel * 10f,
                AreaConhecimento.MAT,
                8f + nivel * 4f,
                new AtaqueIntegral2(null, null, 8f + nivel * 4f),
                "NÃO É MAIS SO UMA INTEGRAL.",
                "é o fim."
        );
    }

    public static Questao criarQuestaoVetores(int nivel) {
        return new Questao(
                "Introduzido ao mundo vetorial!",
                12f + nivel * 10f,
                AreaConhecimento.MAT,
                8f + nivel * 3f,
                new AtaqueVetores(null, null, 6f + nivel * 3f),
                "E voce nao gosta disso..",
                "é so lembrar da regra da mao direita"
        );
    }
}
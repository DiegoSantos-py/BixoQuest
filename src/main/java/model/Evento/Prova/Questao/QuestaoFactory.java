package model.Evento.Prova.Questao;

import model.Ataque.Ataques.AtaqueArranhao;
import model.Ataque.Ataques.AtaqueLatido;
import model.Ataque.Ataques.AtaqueMordida;
import model.Ataque.Ataques.Provas.Matematica.AtaqueFuncaoAfim;
import model.Disciplina.AreaConhecimento;

public class QuestaoFactory {

    /**
     * Cria uma Questao com stats escalados pelo nivel da disciplina.
     * O Ataque é construído com owner=null — o owner real é injetado
     * automaticamente por Oponente.adicionarAtaque(ataque).
     */
    public static Questao criar(QuestaoIDs id, int nivel) {
        switch (id) {
            case MAT_01_FUNCAO_AFIM:     return criarFuncaoAfim(nivel);
            case MAT_O1_SENOIDAL:        return criarSenoidal(nivel);
            case MAT_01_FUNCAO_ATIRANDO: return criarFuncaoAtirando(nivel);
            default:
                throw new IllegalArgumentException("QuestaoIDs sem implementacao na factory: " + id);
        }
    }

    // --- Matematica 1 --------------------------------------------------------

    // Ataque: cachorro (Latido) — projeteis homing simples
    private static Questao criarFuncaoAfim(int nivel) {
        return new Questao(
                "Questao de funcao afim",
                5f + nivel * 5f,
                AreaConhecimento.MAT,
                5f + nivel * 2f,
                new AtaqueFuncaoAfim(null, null, 5f + nivel * 2f),
                "Funcao afim de seu Fim",
                "Voce sente seu fim proximo"
        );
    }

    // Ataque: gato (Arranhao) — chuva de arranhoes com previa
    private static Questao criarSenoidal(int nivel) {
        return new Questao(
                "Amplitude Maxima",
                5f + nivel * 5f,
                AreaConhecimento.MAT,
                6f + nivel * 3f,
                new AtaqueArranhao(null, null, 6f + nivel * 3f),
                "ciclicamente aterrorizante",
                "Voce sente seu sangue pulsar."
        );
    }

    // Ataque: mordida — projeteis rapidos em salva
    private static Questao criarFuncaoAtirando(int nivel) {
        return new Questao(
                "Funcao Composta",
                5f + nivel * 5f,
                AreaConhecimento.MAT,
                8f + nivel * 4f,
                new AtaqueMordida(null, null, 8f + nivel * 4f),
                "Funcoes compostas e suas derivadas",
                "Calcule a derivada de h(x) = f(g(x))."
        );
    }
}
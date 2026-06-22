package service.batalha;

import java.util.ArrayList;

import model.Batalha.EstadoBatalha;
import model.Evento.Prova.ProvaBatalha;
import model.Evento.Prova.ResultadoProva;
import model.Npc.Animal;
import model.Personagem;
import model.Player.PlayerProva;

public class BatalhaFinalizacaoService {

    public void salvarResultadoProvaNoRepositorio(EstadoBatalha estado, ResultadoProva resultadoProva) {
        Personagem personagem = estado.getPersonagem();
        int totalSemestres = personagem.getSemestres().size();
        int semestreNumero = personagem.getSemestres().get(totalSemestres - 1).getNumeroSemestre();
        estado.getResultadoProvaRepository().adicionarResultadoProva(personagem.getPersonagemId(), semestreNumero,
                resultadoProva);
        // PEGA O PERSONAGEM, O TOTAL DE SEMESTRES Q ELE FEZ, O ULTIMO SEMESTRER Q ELE
        // POSSUI(O ATUAL), E O NUMERO DESSE SEMESTRE, E PASSSA PRO REPOSITORIO ADICONAR
        // A
        // LISTA DE RESULTADOS
    }

    public void salvarAnimalNoRepositorio(EstadoBatalha estado, Animal animal) {
        estado.getNpcRepository().atualizarNpc(animal);
    }

    public void finalizarBatalha(EstadoBatalha estado, ProvaBatalha provaBatalha) {
        PlayerProva playerProva = estado.getPlayerProva();
        ArrayList<Float> desempenho = playerProva.getDesempenhoQuestoes();
        // se for prova, finaliza a batalha gerando o resultado, o qual será salvo
        // posteriormente pelo repositorio)
        float notaFinal = calcularNotaFinal(desempenho, playerProva);
        // calcula a nota final baseado noq o player conseguiu tipo desempenho
        // individual em cada questão + bonuses
        ResultadoProva resultadoProva = new ResultadoProva(
                estado.getPersonagem(), provaBatalha.getNome(),
                notaFinal,
                playerProva.getTurnosUsados(),
                playerProva.getTodosAcertosPerfeitos(),
                playerProva.getLevouAlgumDano(),
                playerProva.getPerdeuNota());
        salvarResultadoProvaNoRepositorio(estado, resultadoProva);
        // retorna o resultado pro controller salvar ele na memoria pra dps salvar de
        // vez no fim do dia
    }

    public void finalizarBatalha(EstadoBatalha estado, Animal animal) {
        animal.setDomado(true);
        salvarAnimalNoRepositorio(estado, animal);
        // atualmente no fim de batalha animal, ele so fica domado, pra q entao ele
        // possa regerenar motivacao ou energia ou qualquer coisa do player
    }

    public static float calcularNotaFinal(ArrayList<Float> desempenho, PlayerProva playerProva) {
        float notaFinal = 0;
        for (float nota : desempenho) {
            notaFinal += nota;
        }
        // se n tiver elemtnos na lista(improvavel mas vai que) divide por 1 se nao pelo
        // tamanho
        // nota final é a media de todos os desempenhos individuais de cada questão
        notaFinal /= (desempenho.isEmpty()) ? 1 : desempenho.size();
        // ganha PONTO SE
        notaFinal += (playerProva.getTurnosUsados() <= 20) ? 0.25f : 0;// TERMINAR A PROVA RAPIDO,
        notaFinal += (playerProva.getTodosAcertosPerfeitos()) ? 0.25f : 0;// SE TERMINAR COM TODOS OS ACERTOS
                                                                          // PERFEITOS(no meio do slider)
        notaFinal += (!playerProva.getPerdeuNota()) ? 0.25f : 0; // SEM PERDER NOTA(pode levar dano no shield)
        notaFinal += (!playerProva.getLevouAlgumDano()) ? 0.25f : 0; // SEM LEVAR DANO NO GERAL(por isso o maximo é 11,q
                                                                     // nem numa prova normal questaoe xtra)
        return notaFinal;
    }
}

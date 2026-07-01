package model.Evento.Prova;

import model.Disciplina.AreaConhecimento;
import model.Evento.Prova.Provas.ProvasNaturezas;
import model.Player.AcaoBatalha;
import model.Evento.Prova.Provas.ProvasMatematica;
import model.Evento.Prova.Provas.ProvasSoftware;
import java.util.ArrayList;

public class ProvaFactory {

    /**
     * Cria uma ProvaBatalha completa a partir do ID e nivel da disciplina.
     * As questoes chegam com Ataque de owner=null — injetado por Oponente.adicionarAtaque.
     */
    public static ProvaBatalha criar(ProvaIDs id, int nivelDisciplina) {
        switch (id) {
            case MAT_01: return criarMatematica(1);
            case MAT_02: return criarMatematica(2);
            case MAT_03: return criarMatematica(3);
            case NAT_01: return criarNatureza(1);
            case SOFT_01: return criarSoftware(1);
            default:
                throw new IllegalArgumentException("ProvaIDs sem implementacao na factory: " + id);
        }
    }

    public static ProvaBatalha criarMatematica(int nivel) {
        switch (nivel) {
            case 1:
                return ProvasMatematica.criarMatematicaNivel1();
            case 2:
               return ProvasMatematica.criarMatematicaNivel2();
            case 3:
                // return criarMatematicaNivel3();
                throw new IllegalArgumentException("Nivel 3 de matematica em construcao.");
            default:
                throw new IllegalArgumentException("Nivel de matematica nao implementado: " + nivel);
        }
    }

    public static ProvaBatalha criarNatureza(int nivel){
        switch (nivel){
            case 1:
                return ProvasNaturezas.criarNaturezas1();
            case 2:
                throw new  IllegalArgumentException("Nivel 2 de nat em construcao.");
            case 3:
                throw new IllegalArgumentException("Nivel 3 de naturezas em construcao.");
            default:
                throw new IllegalArgumentException("Niveis so vao de 1 a 3.");
        }
    }



    public static ProvaBatalha criarSoftware(int nivel){
        switch (nivel){
            case 1:
                return ProvasSoftware.criarSoftwareNivel1();
            case 2:
                throw new IllegalArgumentException("Nivel 2 de software em construcao.");
            case 3:
                throw new IllegalArgumentException("Nivel 3 de software em construcao.");
            default:
                throw new IllegalArgumentException("Niveis so vao de 1 a 3.");
        }
    }

}

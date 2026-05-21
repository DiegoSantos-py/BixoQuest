package model.Evento.Prova.Questao;

import model.Ataque.Ataques.AtaqueArranhao;
import model.Ataque.Ataques.AtaqueLatido;
import model.Ataque.Ataques.AtaqueMordida;

import model.Disciplina.AreaConhecimento;
import model.Evento.Prova.Questao.Questao;
import model.Evento.Prova.Questao.QuestaoIDs;

//
//public class QuestaoFactory {

//    public static Questao criar( QuestaoIDs tipo, int nivel){
//        return new Questao();
//    }
//}

//        switch (tipo) {
//
//            case QuestaoIDs.:
//                return criarCalculoBasico(nivel);
//
//            default:
//                throw new IllegalArgumentException(
//                        "Tipo de questão inválido"
//                );
//        }
//        }
//
//    private static Questao criarCalculoBasico(int nivel) {
//
//        return new Questao(
//                "Integral Simples",
//                50 + nivel * 10,
//                "Resolva a integral",
//                AreaConhecimento.MAT,
//                1.0f + nivel * 0.2f,
//                new AtaqueArranhao()
//        );
//    }
//
//    private static Questao criarProgramacao(int nivel) {
//
//        return new Questao(
//                "Ponteiros em C",
//                80 + nivel * 15,
//                "Explique ponteiros",
//                AreaConhecimento.PROGRAMACAO,
//                2.0f + nivel * 0.3f,
//                new AtaqueMordida()
//        );
//    }
//
//    private static Questao criarFisicaBoss(int nivel) {
//
//        return new Questao(
//                "Tensor de Einstein",
//                300 + nivel * 50,
//                "Explique relatividade geral",
//                AreaConhecimento.FISICA,
//                5.0f + nivel,
//                new AtaqueLatido()
//        );
//    }
//}
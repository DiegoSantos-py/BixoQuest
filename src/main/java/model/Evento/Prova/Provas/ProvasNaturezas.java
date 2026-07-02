package model.Evento.Prova.Provas;

import model.Ataque.Ataques.Prova.Matematica.AtaqueAmplitudeMaxima;
import model.Disciplina.AreaConhecimento;
import model.Evento.Prova.ProvaBatalha;
import model.Evento.Prova.Questao.Questao;
import model.Ataque.Ataques.Prova.Naturezas.AtaqueAtomo;
import model.Ataque.Ataques.Prova.Naturezas.AtaqueCarroAviao;
import model.Ataque.Ataques.Prova.Naturezas.AtaqueColisaoElastica;
import model.Ataque.Ataques.Prova.Naturezas.AtaqueGravidade;
import model.Player.AcaoBatalha;

import java.util.ArrayList;

public class ProvasNaturezas {

    public static ProvaBatalha criarNaturezas1() {
        int nivel = 1;
        ArrayList<AcaoBatalha> acoes = new ArrayList<>();
        // favor nao botar mais de 4 acoes em prol da UI
        acoes.add(new AcaoBatalha("USAR CALCULADORA",  0.9f, 2, 0, 0.25f, 1));
        acoes.add(new AcaoBatalha("CHUTAR A RESPOSTA", 0.25f, 0, 1, 0, 1));
        acoes.add(new AcaoBatalha("TENTAR COLAR DO COLEGA", 0.35f, 1, 1, 0, 2.25f));
        acoes.add(new AcaoBatalha("RELER A QUESTÃO",          1.0f, 0, 0, 0, 0.75F));

        ProvaBatalha prova = new ProvaBatalha(
                "Naturezas 1",
                "sim",
                AreaConhecimento.NAT,
                nivel,
                0,
                acoes,
                "naturezas.png",
                "naturezas1.mp3"
        );

        prova.addQuestao(new Questao(
                "Questão de núcleo atómico", 
                 13f, AreaConhecimento.NAT, 
                 11f,
                new AtaqueAtomo(null, null, 9f),
                "O núcleo está cedendo!", "Você sente a radiação no ar..."
        ));

        prova.addQuestao(new Questao(
                "Elétron em onda", 14,
                 AreaConhecimento.NAT, 
                 11f,
                new AtaqueAmplitudeMaxima(null, null, 9f),
                "Ondas de radiacao", "Voce snete alguma coisa "
        ));
        prova.addQuestao(new Questao(
                "Colisão Elástica", 
                14, 
                AreaConhecimento.NAT, 11f,
                new AtaqueColisaoElastica(null, null, 10f),
                "É oq tem pra hj de fisica", "colisoes perfeitamente elasticas.."
        ));

        return prova;
    }

    public static ProvaBatalha criarNaturezas2() {
        int nivel = 2;
        ArrayList<AcaoBatalha> acoes = new ArrayList<>();
        acoes.add(new AcaoBatalha("USAR CALCULADORA",  0.9f, 2, 0, 0.25f, 3f));
        acoes.add(new AcaoBatalha("CHUTAR A RESPOSTA", 0.25f, 0, 1, 0, 4f));
        acoes.add(new AcaoBatalha("TENTAR COLAR DO COLEGA", 0.35f, 1, 1, 0, 4.5f));
        acoes.add(new AcaoBatalha("RELER A QUESTÃO",          1.0f, 0, 0, 0, 1.5F));

        ProvaBatalha prova = new ProvaBatalha(
                "Naturezas 2",
                "sim",
                AreaConhecimento.NAT,
                nivel,
                0,
                acoes,
                "naturezas.png",
                "naturezas2.mp3" // Assumindo que tenha uma música naturezas2.mp3
        );

        prova.addQuestao(new Questao(
                "A Queda da Maçã",
                25f, AreaConhecimento.NAT,
                10f,
                new AtaqueGravidade(null, null, 10f),
                "Uma força invisível te puxa...", "Cuidado com a gravidade!"
        ));

        prova.addQuestao(new Questao(
                "Cinemática",
                60f, AreaConhecimento.NAT,
                10f,
                new AtaqueCarroAviao(null, null, 10f),
                "Formulas e vetores.", "transito de feira de santana."
        ));

        return prova;
    }
}

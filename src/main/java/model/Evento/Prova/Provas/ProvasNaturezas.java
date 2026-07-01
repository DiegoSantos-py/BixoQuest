package model.Evento.Prova.Provas;

import model.Ataque.Ataques.Prova.Matematica.AtaqueAmplitudeMaxima;
import model.Disciplina.AreaConhecimento;
import model.Evento.Prova.ProvaBatalha;
import model.Evento.Prova.Questao.Questao;
import model.Ataque.Ataques.Prova.Naturezas.AtaqueAtomo;
import model.Ataque.Ataques.Prova.Naturezas.AtaqueColisaoElastica;
import model.Player.AcaoBatalha;

import java.util.ArrayList;

public class ProvasNaturezas {

    public static ProvaBatalha criarNaturezas1() {
        int nivel = 1;
        ArrayList<AcaoBatalha> acoes = new ArrayList<>();
        // favor nao botar mais de 4 acoes em prol da UI
        acoes.add(new AcaoBatalha("USAR CALCULADORA",  0.9f, 2, 0, 1, 0));
        acoes.add(new AcaoBatalha("CHUTAR A RESPOSTA", 0.2f, 0, 1, 0, 0));
        acoes.add(new AcaoBatalha("TENTAR COLAR DO COLEGA", 0.35f, 1, 1, 0, 0.5f));
        acoes.add(new AcaoBatalha("DESISTIR",          1.0f, 0, 0, 0, 0));

        ProvaBatalha prova = new ProvaBatalha(
                "Naturezas 1",
                "sim",
                AreaConhecimento.NAT,
                nivel,
                0,
                acoes,
                "naturezas.png",
                "naturezas.mp3"
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
}

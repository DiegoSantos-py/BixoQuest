package service;

import model.Dia;
import model.Semestre;

import java.time.Duration;
import java.util.List;

public class DiaService {

    public Dia criarDia(){
        return new Dia();
    }

    public void avancarDia(Semestre semestre, int indiceDia) {
        Dia dia = semestre.getDias().get(indiceDia);

        if (!dia.isStatus()) {
            throw new IllegalStateException("Dia ainda não foi concluído");
        }

    }

    public void reduzirDuracaoDia(Dia dia,long duracao){
        if (duracao < 0) {throw new IllegalArgumentException("Valor inválido.");}

        long minutosRestantes = dia.getDuracao().toMinutes();
        if (duracao > minutosRestantes) {
            dia.setDuracao(Duration.ofMinutes(0));
            return;
        }

        dia.setDuracao(dia.getDuracao().minusMinutes(duracao));
    }
}

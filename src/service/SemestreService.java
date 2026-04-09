package service;

import model.Dia;
import model.Semestre;

public class SemestreService {

    public Semestre criarSemestre() {
        return new Semestre();
    }

    public Dia avancarDia(Semestre semestre) {

        if (semestre.terminou()) {
            return null;
        }

        Dia novoDia = new Dia();
        semestre.adicionarDia(novoDia);

        return novoDia;
    }

    public boolean terminouSemestre(Semestre semestre) {
        return semestre.terminou();
    }

    public void encerrarSemestre(Semestre semestre) {
        // lógica de encerramento


        // - calcular desempenho do jogador
        // - salvar progresso
        // - liberar próximo semestre
    }
}
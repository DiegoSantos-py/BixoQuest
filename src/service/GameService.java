package service;

import model.Tempo.Dia;
import model.Tempo.Semestre;

class GameService {

    private DiaService diaService;
    private SemestreService semestreService;

    private Semestre semestre;
    private Dia diaAtual;

    public void iniciarJogo() {
        semestre = semestreService.criarSemestre();

        diaAtual = semestreService.avancarDia(semestre);
        diaService.iniciarDia(diaAtual);
    }

    public void atualizar() {

        if (diaService.isDiaEncerrado()) {

            diaService.encerrarDia(diaAtual);

            if (!semestreService.terminouSemestre(semestre)) {

                diaAtual = semestreService.avancarDia(semestre);
                diaService.iniciarDia(diaAtual);

            } else {
                semestreService.encerrarSemestre(semestre);
            }
        }
    }
}
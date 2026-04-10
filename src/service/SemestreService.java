package service;

import model.Dia;
import model.Disciplina;
import model.Semestre;
import repository.SemestreRepository;

public class SemestreService {
    private SemestreRepository semestreRepo = new SemestreRepository();

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


    public void adicionarDisciplina(Semestre semestre, Disciplina disciplina){
        if (semestre.getDisciplinasObrigatorias().contains(disciplina)){
            throw new IllegalArgumentException("Não é possível adicionar a mesma disciplina ao semestre duas vezes.");
        }
    }

    public boolean terminouSemestre(Semestre semestre){ return semestre.terminou();}

    public void encerrarSemestre(Semestre semestre) {
        if (semestre.terminou()){
            semestreRepo.adicionarSemestre(semestre);
        }

        //Implementar lógica de passagem de disciplina

        // - calcular desempenho do jogador
        // - liberar próximo semestre
    }
}
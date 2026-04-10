package service;

import model.Dia;
import model.Disciplina;
import model.Semestre;
import repository.DisciplinaRepository;
import repository.SemestreRepository;

import java.util.ArrayList;
import java.util.List;

public class SemestreService {
    private SemestreRepository semestreRepo;
    private DisciplinaRepository disciplinaRepo;

    public SemestreService(SemestreRepository semestreRepo,
                           DisciplinaRepository disciplinaRepo) {
        this.semestreRepo = semestreRepo;
        this.disciplinaRepo = disciplinaRepo;
    }

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
        if (semestre.getDisciplinas().contains(disciplina)){
            throw new IllegalArgumentException("Não é possível adicionar a mesma disciplina ao semestre duas vezes.");
        }
    }

    public boolean terminouSemestre(Semestre semestre){ return semestre.terminou();}

    public void encerrarSemestre(Semestre semestre) {

        if (semestre == null) {
            throw new IllegalArgumentException("Semestre inválido");
        }

        if (!semestre.terminou()) {
            return;
        }

        // salva histórico
        this.semestreRepo.adicionarSemestre(semestre);

        // cria novo semestre
        Semestre novoSemestre = new Semestre();

        List<Disciplina> novasDisciplinas = new ArrayList<>();

        for (Disciplina atual : semestre.getDisciplinas()) {

            if (semestre.foiAprovado(atual)) {

                Disciplina proxima = disciplinaRepo.proximaDisciplina(
                        atual.getNome(),
                        atual.getCodigo()
                );

                if (proxima != null) {
                    novasDisciplinas.add(proxima);
                }

            } else {
                // 🔹 reprovou → repete a mesma
                novasDisciplinas.add(atual);
            }
        }

        novoSemestre.setDisciplinas(novasDisciplinas);

        // salva novo semestre
        this.semestreRepo.adicionarSemestre(novoSemestre);
    }

}
package repository;

import model.Semestre;

import java.util.ArrayList;
import java.util.List;

public class SemestreRepository {
    private List<Semestre> semestres;

    public SemestreRepository(){
        semestres = new ArrayList<>();
    }

    public List<Semestre> getSemestres(){
        return semestres;
    }

    public void adicionarSemestre(Semestre semestre){
        semestres.add(semestre);
    }
}

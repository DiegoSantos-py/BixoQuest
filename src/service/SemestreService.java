package service;
import model.Semestre;

public class SemestreService {

    public Semestre criarSemestre(){
        return new Semestre();
    }

    public boolean verificarConclusaoSemestre(Semestre semestre){
        if (!semestre.estaConcluido()){
            return false;
        }
        return semestre.getDisciplinasObrigatorias().isEmpty();
    }

}

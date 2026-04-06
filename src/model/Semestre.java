package model;

import java.util.ArrayList;

public class Semestre {
    private static final int MAX_DIAS = 28;
    private ArrayList<Dia> dias;
    private ArrayList<Disciplina> disciplinasObrigatorias;

    public Semestre(){
        this.dias = new ArrayList<Dia>();
        for (int i = 0; i < MAX_DIAS; i++){
            this.dias.add(new Dia());
        }
    }

    public ArrayList<Disciplina> getDisciplinasObrigatorias() {
        return disciplinasObrigatorias;
    }

    public void setDisciplinasObrigatorias(ArrayList<Disciplina> disciplinasObrigatorias) {
        this.disciplinasObrigatorias = disciplinasObrigatorias;
    }

    public static int getMaxDias() {
        return MAX_DIAS;
    }

    public ArrayList<Dia> getDias() {
        return dias;
    }


    public boolean estaConcluido() {
        for (Dia dia : dias) {
            if (!dia.isStatus()) {
                return false;
            }
        }
        return true;
    }
}

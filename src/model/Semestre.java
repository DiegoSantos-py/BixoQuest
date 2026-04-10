package model;

import java.util.ArrayList;

public class Semestre {
    private static final int MAX_DIAS = 20;
    private ArrayList<Dia> dias;
    private ArrayList<Disciplina> disciplinasObrigatorias;

    public Semestre(){
        this.dias = new ArrayList<Dia>();
    }

    public ArrayList<Disciplina> getDisciplinasObrigatorias() {
        return disciplinasObrigatorias;
    }

    public void adicionarDisciplinasObrigatorias(Disciplina d) {
        this.disciplinasObrigatorias.add(d);
    }

    public static int getMaxDias() {
        return MAX_DIAS;
    }

    public ArrayList<Dia> getDias() {
        return dias;
    }

    public int getQuantidadeDias() {return this.dias.size();}

    public boolean terminou() {
        return dias.size() >= MAX_DIAS;
    }

    public void adicionarDia(Dia dia){
        dias.add(dia);}


}

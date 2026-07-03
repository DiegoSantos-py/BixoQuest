package model.Tempo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.Disciplina.Disciplina;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Semestre {
    private static final int MAX_DIAS = 12;
    private List<Dia> dias;
    private List<Disciplina> disciplinas;
    @JsonIgnore
    private Map<Disciplina, Boolean> resultados;
    private static int numeroSemestreGlobal = 0;
    private int numeroSemestre;
    private int diaAtual;
    private Map<String, Boolean> resultadosNomes = new HashMap<>();

    public Semestre(){
        this.dias = new ArrayList<Dia>();
        this.disciplinas = new ArrayList<>();
        this.resultados = new HashMap<>();
        this.numeroSemestre = numeroSemestreGlobal++;
    }

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }

    public void adicionarDisciplinas(Disciplina d) {
        this.disciplinas.add(d);
    }

    public int getNumeroSemestre() {
        return this.numeroSemestre;
    }

    public static int getMaxDias() {
        return MAX_DIAS;
    }

    public List<Dia> getDias() {
        return dias;
    }

    public void setDias(List<Dia> dias) {
        this.dias = dias;
    }

    public int getDiaAtual() {return this.diaAtual;}

    public boolean terminou() {
        return dias.size() >= MAX_DIAS;
    }

    public void adicionarDia(Dia dia){
        dias.add(dia);
        this.diaAtual = dias.size();
    }

    public Map<Disciplina, Boolean> getResultados() {
        return resultados;
    }

    public void setDiaAtual(int diaAtual) {
        this.diaAtual = diaAtual;
    }

    public boolean foiAprovado(Disciplina d) {
        if (resultados.containsKey(d)) {
            return resultados.get(d);
        }
        // fallback para resultadosNomes
        String chave = d.getNome() + ":" + d.getCodigo();
        return resultadosNomes.getOrDefault(chave, false);
    }

    public Map<String, Boolean> getResultadosNomes() { return resultadosNomes; }
    public void setResultadosNomes(Map<String, Boolean> r) { this.resultadosNomes = r; }

    public void registrarResultado(Disciplina d, boolean aprovado) {
        if (d == null) {
            throw new IllegalArgumentException("Disciplina inválida");
        }

        // verifica se a disciplina passada como parâmetro é do semestre
        if (!disciplinas.contains(d)) {
            throw new IllegalArgumentException("Disciplina não pertence ao semestre");
        }

        resultados.put(d, aprovado);
        resultadosNomes.put(d.getNome() + ":" + d.getCodigo(), aprovado);
    }


}

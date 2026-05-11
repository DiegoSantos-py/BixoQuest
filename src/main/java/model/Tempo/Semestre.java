package model.Tempo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.Disciplina.Disciplina;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Semestre {
    private static final int MAX_DIAS = 20;
    private List<Dia> dias;
    private List<Disciplina> disciplinas;
    @JsonIgnore
    private Map<Disciplina, Boolean> resultados;

    private Map<String, Boolean> resultadosNomes = new HashMap<>();

    public Semestre(){
        this.dias = new ArrayList<Dia>();
        this.disciplinas = new ArrayList<>();
        this.resultados = new HashMap<>();
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

    public static int getMaxDias() {
        return MAX_DIAS;
    }

    public List<Dia> getDias() {
        return dias;
    }

    public int getQuantidadeDias() {return this.dias.size();}

    public boolean terminou() {
        return dias.size() >= MAX_DIAS;
    }

    public void adicionarDia(Dia dia){
        dias.add(dia);}

    public Map<Disciplina, Boolean> getResultados() {
        return resultados;
    }

    public boolean foiAprovado(Disciplina d) {
        return resultados.getOrDefault(d, false);
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

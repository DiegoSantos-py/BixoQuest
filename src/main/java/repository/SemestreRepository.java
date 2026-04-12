package repository;

import model.Tempo.Semestre;

import java.util.*;

public class SemestreRepository {

    private Map<Integer, List<Semestre>> semestresPorJogador;

    public SemestreRepository() {
        this.semestresPorJogador = new HashMap<>();
    }

    public List<Semestre> getSemestresPorJogador(int jogadorId) {

        List<Semestre> lista = semestresPorJogador.get(jogadorId);

        if (lista == null) {
            return new ArrayList<>();
        }

        return lista;
    }

    public void adicionarSemestre(int jogadorId, Semestre semestre) {

        if (semestre == null) {
            throw new IllegalArgumentException("Semestre inválido");
        }

        List<Semestre> lista = semestresPorJogador.get(jogadorId);

        // se não existir lista, cria
        if (lista == null) {
            lista = new ArrayList<>();
            semestresPorJogador.put(jogadorId, lista);
        }

        lista.add(semestre);

    }
}
package repository;

import model.Disciplina.AreaConhecimento;
import model.Disciplina.Disciplina;

import java.util.*;

public class DisciplinaRepository {

    private Map<String, List<Disciplina>> disciplinas;

    public DisciplinaRepository() {
        this.disciplinas = new HashMap<>();
    }

    public void adicionar(Disciplina d) {
        if (d == null || d.getNome() == null) {
            throw new IllegalArgumentException("Disciplina inválida");
        }

        List<Disciplina> lista = disciplinas.get(d.getNome());

        if (lista == null) {
            lista = new ArrayList<>();
            disciplinas.put(d.getNome(), lista);
        }

        if (!lista.contains(d)) {
            lista.add(d);
            ordenarLista(lista);
        }
    }

    public List<Disciplina> buscarPorNome(String nome) {
        List<Disciplina> lista = disciplinas.get(nome);

        if (lista == null) {
            return new ArrayList<>();
        }

        return lista;
    }

    public List<Disciplina> buscarPorArea(AreaConhecimento area) {
        List<Disciplina> resultado = new ArrayList<>();

        for (List<Disciplina> lista : disciplinas.values()) {
            for (Disciplina d : lista) {
                if (d.getArea() == area) {
                    resultado.add(d);
                }
            }
        }

        return resultado;
    }

    public Disciplina buscar(String nome, float codigo) {
        List<Disciplina> lista = disciplinas.get(nome);

        if (lista == null) return null;

        for (Disciplina d : lista) {
            if (d.getCodigo() == codigo) {
                return d;
            }
        }

        return null;
    }

    public boolean existe(Disciplina d) {
        List<Disciplina> lista = disciplinas.get(d.getNome());

        if (lista == null) return false;

        return lista.contains(d);
    }

    private void ordenarLista(List<Disciplina> lista) {
        Collections.sort(lista, new Comparator<Disciplina>() {
            @Override
            public int compare(Disciplina d1, Disciplina d2) {
                return Float.compare(d1.getCodigo(), d2.getCodigo());
            }
        });
    }

    // busca no repositorio a disciplina com o próximo codigo e a retorna
    public Disciplina proximaDisciplina(String nome, float codigoAtual) {
        List<Disciplina> lista = disciplinas.get(nome);

        if (lista == null) return null;

        for (Disciplina d : lista) {
            if (d.getCodigo() == codigoAtual + 1) {
                return d;
            }
        }

        return null;
    }

    public List<Disciplina> buscarDisciplinasIniciais() {

        List<Disciplina> iniciais = new ArrayList<>();

        for (List<Disciplina> lista : disciplinas.values()) {
            for (Disciplina d : lista) {
                if (d.getCodigo() == 1) {
                    iniciais.add(d);
                }
            }
        }

        return iniciais;
    }

    public Map<String, List<Disciplina>> carregarDisciplinas (){ return this.disciplinas;}
}

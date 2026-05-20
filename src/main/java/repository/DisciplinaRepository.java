package repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import exception.Disciplina.DisciplinaDuplicadaException;
import exception.Disciplina.DisciplinaInvalidaException;
import exception.Disciplina.DisciplinaNaoEncontradaException;
import exception.OperacaoPersistencia;
import exception.PersistenciaException;
import model.Disciplina.AreaConhecimento;
import model.Disciplina.Disciplina;

import java.io.File;
import java.util.*;

public class DisciplinaRepository {

    private Map<String, List<Disciplina>> disciplinas;

    private static final ObjectMapper mapper = criarMapper();
    private static final File ARQUIVO = new File("disciplinas.json");

    public DisciplinaRepository() {
        this.disciplinas = new HashMap<>();
    }

    private static ObjectMapper criarMapper() {
        ObjectMapper m = new ObjectMapper();
        m.registerModule(new JavaTimeModule());
        m.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return m;
    }


    // Persistência
    public void salvar() throws PersistenciaException {
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(ARQUIVO, disciplinas);
        } catch (Exception e) {
            throw new PersistenciaException(OperacaoPersistencia.SALVAR, e);
        }
    }

    public void carregar() throws PersistenciaException {
        if (!ARQUIVO.exists()) return;

        try {
            this.disciplinas = mapper.readValue(ARQUIVO,
                    mapper.getTypeFactory().constructMapType(
                            HashMap.class,
                            mapper.getTypeFactory().constructType(String.class),
                            mapper.getTypeFactory().constructCollectionType(List.class, Disciplina.class)
                    ));
        } catch (Exception e) {
            throw new PersistenciaException(OperacaoPersistencia.CARREGAR, e);
        }
    }


    // Escrita
    /**
      @throws DisciplinaInvalidaException  se a disciplina ou seu nome forem nulos/vazios
      @throws DisciplinaDuplicadaException se já existir disciplina com mesmo nome e código
     */
    public void adicionar(Disciplina d) {
        validarDisciplina(d);

        List<Disciplina> lista = disciplinas.computeIfAbsent(d.getNome(), k -> new ArrayList<>());

        if (lista.contains(d)) {
            throw new DisciplinaDuplicadaException(d.getNome(), d.getCodigo());
        }

        lista.add(d);
        ordenarLista(lista);
    }


    // Leitura
    /**
      @throws DisciplinaNaoEncontradaException se não houver disciplinas com o nome
     */
    public List<Disciplina> buscarPorNome(String nome) {
        List<Disciplina> lista = disciplinas.get(nome);

        if (lista == null || lista.isEmpty()) {
            throw new DisciplinaNaoEncontradaException(nome);
        }

        return Collections.unmodifiableList(lista);
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

        return resultado; // lista vazia é resposta válida para uma área sem disciplinas
    }

    /**
      @throws DisciplinaNaoEncontradaException se não encontrar a combinação nome + código
     */
    public Disciplina buscar(String nome, float codigo) {
        List<Disciplina> lista = disciplinas.get(nome);

        if (lista != null) {
            for (Disciplina d : lista) {
                if (d.getCodigo() == codigo) {
                    return d;
                }
            }
        }

        throw new DisciplinaNaoEncontradaException(nome, codigo);
    }

    public boolean existe(Disciplina d) {
        List<Disciplina> lista = disciplinas.get(d.getNome());
        return lista != null && lista.contains(d);
    }

    /**
      @throws DisciplinaNaoEncontradaException se não existir disciplina com código + 1
     */
    public Disciplina proximaDisciplina(String nome, float codigoAtual) {
        List<Disciplina> lista = disciplinas.get(nome);

        if (lista != null) {
            for (Disciplina d : lista) {
                if (d.getCodigo() == codigoAtual + 1) {
                    return d;
                }
            }
        }

        throw new DisciplinaNaoEncontradaException(nome, codigoAtual + 1);
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

        return iniciais; // lista vazia é resposta válida
    }

    public Map<String, List<Disciplina>> carregarDisciplinas() {
        return Collections.unmodifiableMap(disciplinas);
    }


    // Helpers privados
    private void validarDisciplina(Disciplina d) {
        if (d == null) {
            throw new DisciplinaInvalidaException("disciplina", "não pode ser nula");
        }
        if (d.getNome() == null || d.getNome().isBlank()) {
            throw new DisciplinaInvalidaException("nome", "não pode ser nulo ou vazio");
        }
    }

    private void ordenarLista(List<Disciplina> lista) {
        lista.sort(Comparator.comparingDouble(Disciplina::getCodigo));
    }
}
package repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import model.Disciplina.Disciplina;
import model.Tempo.Semestre;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SemestreRepository {

    private Map<Integer, List<Semestre>> semestresPorJogador;

    private static final ObjectMapper mapper = criarMapper();
    private static final File ARQUIVO = new File("semestres.json");

    public SemestreRepository() {
        this.semestresPorJogador = new HashMap<>();
    }

    private static ObjectMapper criarMapper() {
        ObjectMapper m = new ObjectMapper();
        m.registerModule(new JavaTimeModule());
        m.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return m;
    }

    public void salvar() throws IOException {
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(ARQUIVO, semestresPorJogador);
    }

    public void carregar() throws IOException {
        if (!ARQUIVO.exists()) return;

        this.semestresPorJogador = mapper.readValue(ARQUIVO,
                mapper.getTypeFactory().constructMapType(
                        HashMap.class,
                        mapper.getTypeFactory().constructType(Integer.class),
                        mapper.getTypeFactory().constructCollectionType(List.class, Semestre.class)
                ));
    }

    // Retorna todos semestres associados a um jogador
    public List<Semestre> getSemestresPorJogador(int jogadorId) {
        List<Semestre> lista = semestresPorJogador.get(jogadorId);
        if (lista == null) return new ArrayList<>();
        return lista;
    }

    public void adicionarSemestre(int jogadorId, Semestre semestre) {
        if (semestre == null) {
            throw new IllegalArgumentException("Semestre inválido");
        }

        List<Semestre> lista = semestresPorJogador.get(jogadorId);
        if (lista == null) {
            lista = new ArrayList<>();
            semestresPorJogador.put(jogadorId, lista);
        }

        lista.add(semestre);
    }

    public Map<Integer, List<Semestre>> carregarSemestres() {
        return semestresPorJogador;
    }
}
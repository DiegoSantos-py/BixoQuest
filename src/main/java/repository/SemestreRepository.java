package repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import exception.OperacaoPersistencia;
import exception.PersistenciaException;
import exception.Semestre.SemestreInvalidoException;
import exception.Semestre.SemestreNaoEncontradoException;
import model.Tempo.Semestre;

import java.io.File;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // Persistência
    /**@throws PersistenciaException se ocorrer falha ao salvar o arquivo*/
    public void salvar() throws PersistenciaException {
        try {
            var type = mapper.getTypeFactory()
                    .constructMapType(HashMap.class, Integer.class, List.class);

            mapper.writerFor(type)
                    .withDefaultPrettyPrinter()
                    .writeValue(ARQUIVO, semestresPorJogador);
        } catch (Exception e) {
            throw new PersistenciaException(OperacaoPersistencia.SALVAR, e);
        }
    }

    /**@throws PersistenciaException se ocorrer falha ao carregar o arquivo*/
    public void carregar() throws PersistenciaException {
        if (!ARQUIVO.exists()) return;

        try {
            this.semestresPorJogador = mapper.readValue(ARQUIVO,
                    mapper.getTypeFactory().constructMapType(
                            HashMap.class,
                            mapper.getTypeFactory().constructType(Integer.class),
                            mapper.getTypeFactory().constructCollectionType(List.class, Semestre.class)
                    ));
        } catch (Exception e) {
            throw new PersistenciaException(OperacaoPersistencia.CARREGAR, e);
        }
    }

    // Escrita
    /**@throws SemestreInvalidoException se o semestre for nulo*/
    public void adicionarSemestre(int jogadorId, Semestre semestre) {
        if (semestre == null) {
            throw new SemestreInvalidoException("semestre", "não pode ser nulo");
        }

        semestresPorJogador
                .computeIfAbsent(jogadorId, k -> new ArrayList<>())
                .add(semestre);
    }

    // Leitura
    /**@throws SemestreNaoEncontradoException se não houver semestres para o jogador*/
    public List<Semestre> getSemestresPorJogador(int jogadorId) {
        List<Semestre> lista = semestresPorJogador.get(jogadorId);

        if (lista == null || lista.isEmpty()) {
            throw new SemestreNaoEncontradoException(jogadorId);
        }

        return Collections.unmodifiableList(lista);
    }

    public Map<Integer, List<Semestre>> carregarSemestres() {
        return Collections.unmodifiableMap(semestresPorJogador);
    }
}
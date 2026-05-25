package repository;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import exception.OperacaoPersistencia;
import exception.PersistenciaException;
import exception.Evento.Prova.ResultadoProvaInvalidoException;
import exception.Evento.Prova.ResultadoProvaDuplicadoException;
import model.Evento.Prova.ResultadoProva;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultadoProvaRepository {

    // player id -> semestreNumero -> resultados de prova
    private Map<Integer, Map<Integer, List<ResultadoProva>>> resultadoProvaPorPlayer;

    private static final ObjectMapper mapper = criarMapper();
    private static final File ARQUIVO = new File("resultadoProvas.json");

    public ResultadoProvaRepository() {
        this.resultadoProvaPorPlayer = new HashMap<>();
    }

    private static ObjectMapper criarMapper() {
        ObjectMapper m = new ObjectMapper();
        return m;
    }

    // gera o tipo
    private JavaType getMapType() {
        var typeFactory = mapper.getTypeFactory();

        JavaType listType =
                typeFactory.constructCollectionType(List.class,ResultadoProva.class
                );

        JavaType innerMapType =
                typeFactory.constructMapType(HashMap.class,typeFactory.constructType(Integer.class),listType
                );

        return typeFactory.constructMapType(
                HashMap.class,
                typeFactory.constructType(Integer.class),
                innerMapType
        );
    }

    // Persistência

    /**
     * lançaPersistenciaException se ocorrer falha ao salvar o arquivo
     */
    public void salvar() throws PersistenciaException {
        try {

            mapper.writerFor(getMapType())
                    .withDefaultPrettyPrinter()
                    .writeValue(ARQUIVO, resultadoProvaPorPlayer);

        } catch (Exception e) {

            throw new PersistenciaException(
                    OperacaoPersistencia.SALVAR,
                    e
            );
        }
    }

    /**
     * lançaPersistenciaException se ocorrer falha ao carregar o arquivo
     */
    public void carregar() throws PersistenciaException {

        if (!ARQUIVO.exists()) {
            return;
        }

        try {

            this.resultadoProvaPorPlayer =
                    mapper.readValue(ARQUIVO,getMapType());

        } catch (Exception e) {

            throw new PersistenciaException(OperacaoPersistencia.CARREGAR, e);
        }
    }

    // Escrita

    /**
     * lançaResultadoProvaInvalidoException se resultado for inválido
     * lançaResultadoProvaDuplicadoException se a prova já existir
     */
    public void adicionarResultadoProva(
            int jogadorId,
            int semestreNumero,
            ResultadoProva resultadoProva
    ) {
        if (resultadoProva == null) {
            throw new ResultadoProvaInvalidoException( "resultadoProva", "não pode ser nulo");
        }

        if (jogadorId < 0) {

            throw new ResultadoProvaInvalidoException("jogadorId", "não pode ser negativo");
        }

        if (semestreNumero <= 0) {
            throw new ResultadoProvaInvalidoException("semestreNumero", "deve ser maior que zero");
        }

        Map<Integer, List<ResultadoProva>> semestreComProvas = resultadoProvaPorPlayer.get(jogadorId);

        if (semestreComProvas == null) {
            semestreComProvas = new HashMap<>();
            resultadoProvaPorPlayer.put(jogadorId, semestreComProvas
            );
        }

        List<ResultadoProva> provas =
                semestreComProvas.get(semestreNumero);

        if (provas == null) {
            provas = new ArrayList<>();
            semestreComProvas.put(semestreNumero, provas);
        }

        if (provas.contains(resultadoProva)) {
            throw new ResultadoProvaDuplicadoException(resultadoProva.getProvaNome());
        }

        provas.add(resultadoProva);
    }

    // leitura dos semestres

    public Map<Integer, List<ResultadoProva>> getResultadosProvaPorJogador(int jogadorId) {
        Map<Integer, List<ResultadoProva>> provas = resultadoProvaPorPlayer.get(jogadorId);
        if (provas == null) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(provas);
    }

    // query pra pegar as provas do jogador tal desse semestre especifico

    public List<ResultadoProva> getResultadoPorJogadorESemestre(int jogadorId,int semestreNumero){
        Map<Integer, List<ResultadoProva>> mapaInterno =
                getResultadosProvaPorJogador(jogadorId);
        List<ResultadoProva> lista = mapaInterno.get(semestreNumero);

        if (lista == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(lista);
    }

    public Map<Integer, Map<Integer, List<ResultadoProva>>> carregarResultadoProvas() {
        return Collections.unmodifiableMap( resultadoProvaPorPlayer);
    }
}
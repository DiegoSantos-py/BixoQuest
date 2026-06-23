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

/**
 Repositório responsável por armazenar, buscar e persistir os semestres do jogo.

 Os semestres são organizados por jogador, utilizando o id do personagem
 como chave e uma lista de semestres como valor.
 Também é responsável por salvar e carregar esses dados em arquivo JSON.
 */
public class SemestreRepository {

    // Estrutura principal de armazenamento:
    // chave -> id do jogador
    // valor -> lista de semestres do jogador
    private Map<Integer, List<Semestre>> semestresPorJogador;

    // ObjectMapper utilizado para converter objetos Java em JSON e JSON em objetos Java
    private static final ObjectMapper mapper = criarMapper();

    // Arquivo onde os semestres serão salvos/carregados
    private static final File ARQUIVO = new File("semestres.json");

    /**
     Cria o repositório inicializando o mapa de semestres vazio.
     */
    public SemestreRepository() {
        this.semestresPorJogador = new HashMap<>();
    }

    /**
     Cria e configura o ObjectMapper utilizado na persistência.

     O JavaTimeModule adiciona suporte para serialização de tipos
     de data e hora do pacote java.time.

     A configuração WRITE_DATES_AS_TIMESTAMPS desabilitada faz com que
     datas sejam armazenadas em formato textual ao invés de timestamps.
     */
    private static ObjectMapper criarMapper() {
        ObjectMapper m = new ObjectMapper();

        // Adiciona suporte para LocalDate, Instant, Duration etc.
        m.registerModule(new JavaTimeModule());

        // Salva datas como texto ao invés de timestamps numéricos
        m.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return m;
    }

    // Persistência

    /**
     Salva o mapa de semestres no arquivo JSON.
     lança PersistenciaException se ocorrer falha ao salvar o arquivo
     */
    public void salvar() throws PersistenciaException {
        try {

            // Define explicitamente o tipo:
            // HashMap<Integer, List>
            var type = mapper.getTypeFactory()
                    .constructMapType(HashMap.class, Integer.class,
                            List.class
                    );

            // Converte os dados para JSON formatado
            mapper.writerFor(type).withDefaultPrettyPrinter()
                    .writeValue(ARQUIVO, semestresPorJogador);

        } catch (Exception e) {

            // Encapsula erros em exceção própria da camada de persistência
            throw new PersistenciaException(
                    OperacaoPersistencia.SALVAR,
                    e
            );
        }
    }

    /**
     Carrega os semestres do arquivo JSON para memória.

     Reconstrói a estrutura:
     Map<Integer, List<Semestre>>

     lança PersistenciaException se ocorrer falha ao carregar o arquivo
     */
    public void carregar() throws PersistenciaException {
        if (!ARQUIVO.exists() || ARQUIVO.length() == 0) return;

        // Se o arquivo ainda não existir,
        // não há estado salvo para recuperar.
        if (!ARQUIVO.exists()) return;

        try {
            this.semestresPorJogador = mapper.readValue(
                    ARQUIVO,mapper.getTypeFactory().constructMapType(HashMap.class,
                            mapper.getTypeFactory().constructType(Integer.class),
                            mapper.getTypeFactory().constructCollectionType(
                                    List.class,
                                    Semestre.class)));

        } catch (Exception e) {

            throw new PersistenciaException(
                    OperacaoPersistencia.CARREGAR,
                    e
            );
        }
    }

    /**
     Adiciona um novo semestre ao jogador informado.
     Caso o jogador ainda não possua semestres registrados,
     uma lista vazia é criada automaticamente.
     lança SemestreInvalidoException se o semestre for nulo
     */
    public void adicionarSemestre(int jogadorId, Semestre semestre) {

        if (semestre == null) {
            throw new SemestreInvalidoException("semestre", "não pode ser nulo");}

        // Obtém a lista existente ou cria uma nova
        // antes de adicionar o semestre
        semestresPorJogador
                .computeIfAbsent(jogadorId, k -> new ArrayList<>())
                .add(semestre);
    }

    // Leitura

    /**
     Retorna a lista de semestres associados ao jogador.

     lança SemestreNaoEncontradoException se não houver semestres para o jogador
     */
    public List<Semestre> getSemestresPorJogador(int jogadorId) {

        List<Semestre> lista =
                semestresPorJogador.get(jogadorId);

        if (lista == null || lista.isEmpty()) {
            throw new SemestreNaoEncontradoException(
                    jogadorId
            );}

        // Retorna lista protegida contra modificações externas
        return Collections.unmodifiableList(lista);
    }

    /**
     Retorna uma visão somente leitura do mapa de semestres.
     Isso impede alterações externas na estrutura interna
     do repositório.
     */
    public Map<Integer, List<Semestre>> carregarSemestres() {
        return Collections.unmodifiableMap(semestresPorJogador);
    }
}
package repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import exception.Evento.EventoDuplicadoException;
import exception.Evento.EventoInvalidoException;
import exception.Evento.EventoNaoEncontradoException;
import exception.OperacaoPersistencia;
import exception.PersistenciaException;
import model.Evento.Evento;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
  Repositório responsável por armazenar e recuperar eventos.

  Mantém os dados em memória através de um Map e
  oferece persistência em arquivo JSON utilizando Jackson.
 */
public class EventoRepository {

    // Estrutura principal de armazenamento:
    // chave → nome do evento
    // valor → objeto Evento
    private Map<String, Evento> eventos;

    // Mapper compartilhado utilizado para serialização/desserialização JSON
    private static final ObjectMapper mapper = criarMapper();

    // Arquivo onde os eventos serão persistidos
    private static final File ARQUIVO = new File("eventos.json");

    /**
      Inicializa o repositório com um mapa vazio.
     */
    public EventoRepository() {
        this.eventos = new HashMap<>();
    }

    /**
      Cria e configura o ObjectMapper.

      Atualmente não possui configurações adicionais,
      mas centralizar sua criação facilita futuras extensões.
     */
    private static ObjectMapper criarMapper() {
        ObjectMapper m = new ObjectMapper();

        return m;
    }



    /**
      Salva todos os eventos em arquivo JSON.

      O Map<String, Evento> é convertido para JSON e escrito em eventos.json.

      lança PersistenciaException se ocorrer falha ao salvar
     */
    public void salvar() throws PersistenciaException {
        try {

            // Define explicitamente o tipo:
            // HashMap<String, Evento>
            var type = mapper.getTypeFactory()
                    .constructMapType(
                            HashMap.class,
                            String.class,
                            Evento.class
                    );

            // Serializa o mapa para JSON formatado
            mapper.writerFor(type)
                    .withDefaultPrettyPrinter()
                    .writeValue(ARQUIVO, eventos);

        } catch (Exception e) {

            // Encapsula qualquer erro em exceção de persistência
            throw new PersistenciaException(
                    OperacaoPersistencia.SALVAR,
                    e
            );
        }
    }

    /**
      Carrega os eventos do arquivo JSON.

      Reconstrói o Map<String, Evento> e também
      reconecta dependências entre eventos.

      lança PersistenciaException se ocorrer falha ao carregar
     */
    public void carregar() throws PersistenciaException {

        // Caso ainda não exista arquivo salvo,
        // mantém o mapa vazio.
        if (!ARQUIVO.exists()) return;

        try {

            // Converte JSON para:
            // HashMap<String, Evento>
            this.eventos = mapper.readValue(
                    ARQUIVO,

                    mapper.getTypeFactory()
                            .constructMapType(
                                    HashMap.class,
                                    String.class,
                                    Evento.class
                            )
            );

            /**
              Reconstrói referências entre eventos.

              Durante a serialização normalmente salva-se apenas
              o nome do evento requisito.

              Exemplo:
              evento A → requisito = "Estudar"

              Após carregar:
              evento.setEventoRequisito(objetoEvento)
             */
            for (Evento evento : eventos.values()) {

                String requisito =
                        evento.getEventoRequisitoNome();

                if (requisito != null) {
                    evento.setEventoRequisito(
                            eventos.get(requisito)
                    );
                }
            }

        } catch (Exception e) {

            throw new PersistenciaException(
                    OperacaoPersistencia.CARREGAR,
                    e
            );
        }
    }


    /**
      Adiciona um novo evento ao repositório.

      Valida:
      - evento não nulo
      - nome válido
      - ausência de duplicidade

     lança EventoInvalidoException se dados inválidos
     lança EventoDuplicadoException se nome já existir
     */
    public void adicionarEvento(Evento evento) {

        if (evento == null) {
            throw new EventoInvalidoException(
                    "evento",
                    "não pode ser nulo"
            );
        }

        if (
                evento.getNome() == null ||
                        evento.getNome().isBlank()
        ) {
            throw new EventoInvalidoException(
                    "nome",
                    "não pode ser nulo ou vazio"
            );
        }

        if (
                eventos.containsKey(
                        evento.getNome()
                )
        ) {
            throw new EventoDuplicadoException(
                    evento.getNome()
            );
        }

        // Insere no mapa usando nome como chave
        eventos.put(
                evento.getNome(),
                evento
        );
    }


    /**
      lança EventoNaoEncontradoException
      caso não exista
     */
    public Evento buscarPorNome(String nome) {

        Evento evento =
                eventos.get(nome);

        if (evento == null) {
            throw new EventoNaoEncontradoException(
                    nome
            );
        }

        return evento;
    }

    /**
      Retorna visualização somente leitura
      do conjunto de eventos.
      Evita modificações externas ao repositório.
     */
    public Map<String, Evento> carregarEventos() {

        return Collections.unmodifiableMap(
                eventos
        );
    }
}
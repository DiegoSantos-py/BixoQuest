package repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

public class EventoRepository {

    private Map<String, Evento> eventos;

    private static final ObjectMapper mapper = criarMapper();
    private static final File ARQUIVO = new File("eventos.json");

    public EventoRepository() {
        this.eventos = new HashMap<>();
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
                    .constructMapType(HashMap.class, String.class, Evento.class);
            mapper.writerFor(type)
                    .withDefaultPrettyPrinter()
                    .writeValue(ARQUIVO, eventos);
        } catch (Exception e) {
            throw new PersistenciaException(OperacaoPersistencia.SALVAR, e);
        }
    }

    /**@throws PersistenciaException se ocorrer falha ao carregar o arquivo*/
    public void carregar() throws PersistenciaException {
        if (!ARQUIVO.exists()) return;

        try {
            this.eventos = mapper.readValue(ARQUIVO,
                    mapper.getTypeFactory().constructMapType(
                            HashMap.class, String.class, Evento.class));

            for (Evento evento : eventos.values()) {
                String requisito = evento.getEventoRequisitoNome();
                if (requisito != null) {
                    evento.setEventoRequisito(eventos.get(requisito));
                }
            }
        } catch (Exception e) {
            throw new PersistenciaException(OperacaoPersistencia.CARREGAR, e);
        }
    }

    // Escrita
    /**@throws EventoInvalidoException  se o evento ou seu nome forem nulos
     @throws EventoDuplicadoException se já existir evento com o mesmo nome*/
    public void adicionarEvento(Evento evento) {
        if (evento == null) {
            throw new EventoInvalidoException("evento", "não pode ser nulo");
        }
        if (evento.getNome() == null || evento.getNome().isBlank()) {
            throw new EventoInvalidoException("nome", "não pode ser nulo ou vazio");
        }
        if (eventos.containsKey(evento.getNome())) {
            throw new EventoDuplicadoException(evento.getNome());
        }

        eventos.put(evento.getNome(), evento);
    }

    // Leitura
    /**@throws EventoNaoEncontradoException se não existir evento com o nome informado*/
    public Evento buscarPorNome(String nome) {
        Evento evento = eventos.get(nome);

        if (evento == null) {
            throw new EventoNaoEncontradoException(nome);
        }

        return evento;
    }

    public Map<String, Evento> carregarEventos() {
        return Collections.unmodifiableMap(eventos);
    }
}
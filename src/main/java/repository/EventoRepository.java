package repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import model.Evento.Evento;

import java.io.File;
import java.io.IOException;
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

    public void salvar() throws IOException {
        // instrui o Jackson a serializar como Map<String, Evento> com polimorfismo
        var type = mapper.getTypeFactory()
                .constructMapType(HashMap.class, String.class, Evento.class);

        mapper.writerFor(type)
                .withDefaultPrettyPrinter()
                .writeValue(ARQUIVO, eventos);
    }

    public void carregar() throws IOException {
        if (!ARQUIVO.exists()) return;

        this.eventos = mapper.readValue(ARQUIVO,
                mapper.getTypeFactory().constructMapType(
                        HashMap.class, String.class, Evento.class));

        // Reconstrói referências de eventoRequisito pelos nomes
        for (Evento evento : eventos.values()) {
            String requisito = evento.getEventoRequisitoNome();
            if (requisito != null) {
                evento.setEventoRequisito(eventos.get(requisito));
            }
        }
    }

    public void adicionarEvento(Evento evento) {
        if (evento == null || evento.getNome() == null) return;
        if (eventos.containsKey(evento.getNome())) return;
        eventos.put(evento.getNome(), evento);
    }

    public Evento buscarPorNome(String nome) {
        return eventos.get(nome);
    }

    public Map<String, Evento> carregarEventos() {
        return eventos;
    }
}
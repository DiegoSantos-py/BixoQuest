package model;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class Dia {
    private static final int MAX_DURACAO = 22;
    private Duration duracao;
    private Instant inicio;
    private boolean eventoExecutando;
    private Map<String, String> eventosObrigatorios;
    private Map<String, String> eventosAleatorios;

    public Dia(){
        this.duracao = Duration.ofMinutes(MAX_DURACAO);
        this.inicio = Instant.now();
        this.eventosObrigatorios = new HashMap<>();
        this.eventosAleatorios = new HashMap<>();
    }

    public Map<String, String> getEventosObrigatorios() {
        return eventosObrigatorios;
    }

    public void addEventosObrigatorios(Map<String, String> eventos) {
        for (String chave: eventosObrigatorios.keySet()) {
            if (!this.eventosObrigatorios.containsKey(chave)) {
                this.eventosObrigatorios.put(chave, eventos.get(chave));
            }
        }
    }

    public Map<String, String> getEventosAleatorios() {
        return eventosAleatorios;
    }

    public void addEventosAleatorios(Map<String, String> eventos) {
        for (String chave: eventos.keySet()) {
            if (!this.eventosAleatorios.containsKey(chave)) {
                this.eventosAleatorios.put(chave, eventos.get(chave));
            }
        }
    }

    public Duration getDuracao() {
        return duracao;
    }

    public void setDuracao(Duration duracao) {
        this.duracao = duracao;
    }

    public Instant getInicio() {
        return inicio;
    }

    public void setInicio(Instant inicio) {
        this.inicio = inicio;
    }

    public boolean isEventoExecutando() {
        return eventoExecutando;
    }

    public void setEventoExecutando(boolean eventoExecutando) {
        this.eventoExecutando = eventoExecutando;
    }
}

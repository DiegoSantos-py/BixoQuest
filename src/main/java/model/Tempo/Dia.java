package model.Tempo;
import model.Evento.Evento;
import model.Evento.EventoAleatorio;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class Dia {
    private static final int MAX_DURACAO = 22;
    private Duration duracao;
    private Instant inicio;
    private Map<String, Evento> eventosObrigatorios;
    private Map<String, Evento> eventosAleatorios;
    private boolean saiuDoPonto;

    public Dia(){
        this.duracao = Duration.ofMinutes(MAX_DURACAO); // Define duração máxima do dia
        this.inicio = Instant.now();
        this.eventosObrigatorios = new HashMap<>();
        this.eventosAleatorios = new HashMap<>();
    }

    public Map<String, Evento> getEventosObrigatorios() {
        return eventosObrigatorios;
    }

    public void addEventosObrigatorios(Map<String, Evento> eventos) {
        for (String chave: eventosObrigatorios.keySet()) {
            if (!this.eventosObrigatorios.containsKey(chave)) {
                this.eventosObrigatorios.put(chave, eventos.get(chave));
            }
        }
    }

    public Map<String, Evento> getEventosAleatorios() {
        return eventosAleatorios;
    }

    public void addEventosAleatorios(Map<String, Evento> eventos) {
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

    public boolean isSaiuDoPonto() {
        return saiuDoPonto;
    }

    public void setSaiuDoPonto(boolean saiuDoPonto) {
        this.saiuDoPonto = saiuDoPonto;
    }

    public void setEventosObrigatorios(Map<String, Evento> eventosObrigatorios) {
        this.eventosObrigatorios = eventosObrigatorios;
    }

    public void setEventosAleatorios(Map<String, Evento> eventosAleatorios) {
        this.eventosAleatorios = eventosAleatorios;
    }
}

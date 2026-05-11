package model.Tempo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.Evento.Evento;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dia {
    private static final int MAX_DURACAO = 22;
    private Duration duracao;
    @JsonIgnore
    private Instant inicio;

    @JsonIgnore
    private Map<String, Evento> eventosObrigatorios;
    private List<String> eventosObrigatoriosNomes = new ArrayList<>();

    @JsonIgnore
    private Map<String, Evento> eventosAleatorios;
    private List<String> eventosAleatoriosNomes = new ArrayList<>();

    private boolean saiuDoPonto;

    public Dia() {
        this.duracao = Duration.ofMinutes(MAX_DURACAO);
        this.inicio = Instant.now();
        this.eventosObrigatorios = new HashMap<>();
        this.eventosAleatorios = new HashMap<>();
    }

    public Map<String, Evento> getEventosObrigatorios() {
        return eventosObrigatorios;
    }

    public void setEventosObrigatorios(Map<String, Evento> eventosObrigatorios) {
        this.eventosObrigatorios = eventosObrigatorios;
        this.eventosObrigatoriosNomes = new ArrayList<>(eventosObrigatorios.keySet());
    }

    public void addEventosObrigatorios(Map<String, Evento> eventos) {
        for (String chave : eventos.keySet()) {
            if (!this.eventosObrigatorios.containsKey(chave)) {
                this.eventosObrigatorios.put(chave, eventos.get(chave));
                this.eventosObrigatoriosNomes.add(chave); // sincroniza
            }
        }
    }

    public Map<String, Evento> getEventosAleatorios() {
        return eventosAleatorios;
    }

    public void setEventosAleatorios(Map<String, Evento> eventosAleatorios) {
        this.eventosAleatorios = eventosAleatorios;
        this.eventosAleatoriosNomes = new ArrayList<>(eventosAleatorios.keySet());
    }

    public void addEventosAleatorios(Map<String, Evento> eventos) {
        for (String chave : eventos.keySet()) {
            if (!this.eventosAleatorios.containsKey(chave)) {
                this.eventosAleatorios.put(chave, eventos.get(chave));
                this.eventosAleatoriosNomes.add(chave); // sincroniza
            }
        }
    }

    public List<String> getEventosObrigatoriosNomes() { return eventosObrigatoriosNomes; }
    public void setEventosObrigatoriosNomes(List<String> nomes) { this.eventosObrigatoriosNomes = nomes; }

    public List<String> getEventosAleatoriosNomes() { return eventosAleatoriosNomes; }
    public void setEventosAleatoriosNomes(List<String> nomes) { this.eventosAleatoriosNomes = nomes; }

    public Duration getDuracao() { return duracao; }
    public void setDuracao(Duration duracao) { this.duracao = duracao; }

    public Instant getInicio() { return inicio; }
    public void setInicio(Instant inicio) { this.inicio = inicio; }

    public boolean isSaiuDoPonto() { return saiuDoPonto; }
    public void setSaiuDoPonto(boolean saiuDoPonto) { this.saiuDoPonto = saiuDoPonto; }
}
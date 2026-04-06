package model;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dia {
    private static final int MAX_DURACAO = 22;
    private Duration duracao;
    private Map<String, String> eventosObrigatorios;
    private Map<String, String> eventosAleatorios;
    private boolean status;

    public Dia(){
        duracao = Duration.ofMinutes(MAX_DURACAO);
        eventosObrigatorios = new HashMap<>();
        eventosAleatorios = new HashMap<>();
    }

    public Map<String, String> getEventosObrigatorios() {
        return eventosObrigatorios;
    }

    public void setEventosObrigatorios(Map<String, String> eventosObrigatorios) {
        for (String chave: eventosObrigatorios.keySet()) {
            if (!this.eventosObrigatorios.keySet().contains(chave)) {
                this.eventosObrigatorios.put(chave, eventosObrigatorios.get(chave));
            }
        }
    }

    public Map<String, String> getEventosAleatorios() {
        return eventosAleatorios;
    }

    public void setEventosAleatorios(Map<String, String> eventosAleatorios) {
        for (String chave: eventosAleatorios.keySet()) {
            if (!this.eventosAleatorios.keySet().contains(chave)) {
                this.eventosAleatorios.put(chave, eventosAleatorios.get(chave));
            }
        }
    }

    public Duration getDuracao() {
        return duracao;
    }

    public void setDuracao(Duration duracao) {
        this.duracao = duracao;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}

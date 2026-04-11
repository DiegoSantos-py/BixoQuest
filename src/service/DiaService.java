package service;

import model.Evento.Evento;
import model.Evento.EventoAleatorio;
import model.Tempo.Dia;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DiaService {

    private ScheduledExecutorService scheduler;
    private boolean diaEncerrado = false;

    public void iniciarDia(Dia dia) {

        diaEncerrado = false;

        if (scheduler != null && !scheduler.isShutdown()) {
            return;
        }

        scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> atualizarDia(dia),
                0, 1, TimeUnit.SECONDS);
    }

    private synchronized void atualizarDia(Dia dia) {
        if (diaEncerrado) return;

        if (getTempoRestanteSegundos(dia) <= 0) {
            diaEncerrado = true;
            pararTempo(); // apenas para o tempo
        }
    }

    public boolean isDiaEncerrado() {
        return diaEncerrado;
    }

    public synchronized void pularTempo(Dia dia, long minutos) {

        long tempoRestanteMin = getTempoRestanteSegundos(dia) / 60;

        if (minutos > tempoRestanteMin) {
            throw new IllegalArgumentException("Tempo insuficiente no dia");
        }

        pararTempo();
        avancarTempo(dia, minutos);
        iniciarDia(dia);
    }

    public void consumirTempoEvento(Dia dia, int minutos) {
        pularTempo(dia, minutos);
    }

    public void pararTempo() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }

    public void avancarTempo(Dia dia, long minutos) {
        dia.setInicio(dia.getInicio().minusSeconds(minutos * 60));
    }

    public void encerrarDia(Dia dia) {
        pararTempo();
    }

    private long getTempoRestanteSegundos(Dia dia) {
        return Math.max(0,
                dia.getDuracao().minus(
                        Duration.between(dia.getInicio(), Instant.now())
                ).getSeconds()
        );
    }

    public long getTempoRestante(Dia dia) {
        return getTempoRestanteSegundos(dia);
    }

    public void gerarEventosDoDia(Dia dia,
                                  List<Evento> obrigatoriosBase,
                                  List<EventoAleatorio> eventosAleatoriosBase) {

        adicionarEventosObrigatorios(dia, obrigatoriosBase);
        adicionarEventosAleatorios(dia, eventosAleatoriosBase);
    }

    private void adicionarEventosObrigatorios(Dia dia, List<Evento> eventos) {

        for (Evento e : eventos) {
            dia.getEventosObrigatorios().put(e.getNome(), e);
        }
    }

    private void adicionarEventosAleatorios(Dia dia, List<EventoAleatorio> eventos) {

        for (EventoAleatorio ea : eventos) {

            if (ea.deveAtivar()) { // baseado na chance
                dia.getEventosAleatorios().put(ea.getNome(), ea);
            }
        }
    }

}
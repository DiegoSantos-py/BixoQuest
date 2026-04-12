package service;

import model.Evento.Evento;
import model.Evento.EventoAleatorio;
import model.Local.TipoLocal;
import model.Local.ZonaInterativa;
import model.Personagem;
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
        dia.setSaiuDoPonto(false);


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
            pararTempo();
        }
    }

    public void verificarRetornoAoPonto(Dia dia, Personagem personagem) {

        if (diaEncerrado) return;

        boolean estaNoPonto =
                personagem.getLocalAtual().getTipo() == TipoLocal.PONTO_ONIBUS;

        // saiu do ponto
        if (!estaNoPonto) {
            dia.setSaiuDoPonto(true);
        }

        // saiu e voltou
        if (dia.isSaiuDoPonto() && estaNoPonto) {
            encerrarDia(dia);
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

        // NÃO reinicia o dia!
        avancarTempo(dia, minutos);
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
        diaEncerrado = true;
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

            ZonaInterativa zona = e.getZona();

            if (zona == null) {
                throw new IllegalArgumentException("Evento sem zona definida: " + e.getNome());
            }

            dia.getEventosObrigatorios().put(zona.getNome(), e);
        }
    }

    private void adicionarEventosAleatorios(Dia dia, List<EventoAleatorio> eventos) {

        for (EventoAleatorio ea : eventos) {

            if (!ea.deveAtivar()) {
                continue;
            }

            ZonaInterativa zona = ea.getZona();

            if (zona == null) {
                throw new IllegalArgumentException("Evento sem zona definida: " + ea.getNome());
            }


            dia.getEventosAleatorios().put(zona.getNome(), ea);
        }
    }
}
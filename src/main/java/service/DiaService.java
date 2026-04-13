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

    // Responsável por executar a atualização automática do dia
    private ScheduledExecutorService scheduler;

    // Indica se o dia já terminou
    private boolean diaEncerrado = false;

    public void iniciarDia(Dia dia) {

        diaEncerrado = false;

        // No início do dia, o personagem ainda não saiu do ponto
        dia.setSaiuDoPonto(false);

        if (scheduler != null && !scheduler.isShutdown()) {
            return;
        }

        scheduler = Executors.newSingleThreadScheduledExecutor();

        // A cada 1 segundo, atualiza o estado do dia
        scheduler.scheduleAtFixedRate(() -> atualizarDia(dia),
                0, 1, TimeUnit.SECONDS);
    }

    private synchronized void atualizarDia(Dia dia) {
        // Se o dia já terminou, não faz mais nada
        if (diaEncerrado) return;

        // Se o tempo acabou, encerra o dia e para o contador
        if (getTempoRestanteSegundos(dia) <= 0) {
            diaEncerrado = true;
            pararTempo();
        }
    }

    public void verificarRetornoAoPonto(Dia dia, Personagem personagem) {

        if (diaEncerrado) return;

        // Verifica se o personagem está no ponto de ônibus
        boolean estaNoPonto =
                personagem.getLocalAtual().getTipo() == TipoLocal.PONTO_ONIBUS;

        if (!estaNoPonto) {
            dia.setSaiuDoPonto(true);
        }

        if (dia.isSaiuDoPonto() && estaNoPonto) {
            encerrarDia(dia);
        }
    }

    public boolean isDiaEncerrado() {
        return diaEncerrado;
    }

    public synchronized void pularTempo(Dia dia, long minutos) {

        // Calcula quantos minutos ainda restam no dia
        long tempoRestanteMin = getTempoRestanteSegundos(dia) / 60;

        // Não permite pular mais tempo do que o disponível
        if (minutos > tempoRestanteMin) {
            throw new IllegalArgumentException("Tempo insuficiente no dia");
        }

        avancarTempo(dia, minutos);
    }

    public void consumirTempoEvento(Dia dia, int minutos) {
        pularTempo(dia, minutos);
    }

    public void pararTempo() {
        // Para a execução automática do tempo, se ela estiver ativa
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }

    public void avancarTempo(Dia dia, long minutos) {
        dia.setInicio(dia.getInicio().minusSeconds(minutos * 60));
    }

    public void encerrarDia(Dia dia) {
        // Marca o dia como encerrado
        diaEncerrado = true;

        // Para a atualização automática do tempo
        pararTempo();
    }

    private long getTempoRestanteSegundos(Dia dia) {
        // Calcula quanto tempo ainda falta no dia.
        return Math.max(0,
                dia.getDuracao().minus(
                        Duration.between(dia.getInicio(), Instant.now())
                ).getSeconds()
        );
    }

    public long getTempoRestante(Dia dia) {
        // Retorna o tempo restante em segundos
        return getTempoRestanteSegundos(dia);
    }

    public void gerarEventosDoDia(Dia dia,
                                  List<Evento> obrigatoriosBase,
                                  List<EventoAleatorio> eventosAleatoriosBase) {

        // Adiciona os eventos obrigatórios do dia
        adicionarEventosObrigatorios(dia, obrigatoriosBase);

        // Adiciona os eventos aleatórios que forem ativados
        adicionarEventosAleatorios(dia, eventosAleatoriosBase);
    }

    private void adicionarEventosObrigatorios(Dia dia, List<Evento> eventos) {

        for (Evento e : eventos) {

            // Obtém a zona onde o evento acontece
            ZonaInterativa zona = e.getZona();

            if (zona == null) {
                throw new IllegalArgumentException("Evento sem zona definida: " + e.getNome());
            }

            // Guarda o evento usando o nome da zona como chave
            dia.getEventosObrigatorios().put(zona.getNome(), e);
        }
    }

    private void adicionarEventosAleatorios(Dia dia, List<EventoAleatorio> eventos) {

        for (EventoAleatorio ea : eventos) {

            // Se o evento aleatório não deve ativar, ignora
            if (!ea.deveAtivar()) {
                continue;
            }


            ZonaInterativa zona = ea.getZona();

            // Não permite evento aleatório sem zona definida
            if (zona == null) {
                throw new IllegalArgumentException("Evento sem zona definida: " + ea.getNome());
            }

            dia.getEventosAleatorios().put(zona.getNome(), ea);
        }
    }
}
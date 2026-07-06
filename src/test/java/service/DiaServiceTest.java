package service;

import model.Evento.Evento;
import model.Evento.EventoAleatorio;
import model.Local.ZonaInterativa;
import model.Tempo.Dia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DiaServiceTest {

    private DiaService diaService;
    private Dia dia;

    @BeforeEach
    void setUp() {
        diaService = new DiaService();

        dia = new Dia();
        dia.setInicio(Instant.now());
        dia.setDuracao(Duration.ofMinutes(22));
        dia.setSaiuDoPonto(false);
    }

    @Test
    void deveIniciarDia() {
        diaService.iniciarDia(dia);

        assertFalse(diaService.isDiaEncerrado());
        assertFalse(dia.isSaiuDoPonto());

        diaService.pararTempo();
    }

    @Test
    void devePularTempo() {
        long antes = diaService.getTempoRestante(dia);

        diaService.pularTempo(dia, 10);

        long depois = diaService.getTempoRestante(dia);

        assertTrue(depois < antes);
    }

    @Test
    void deveLancarExcecaoAoPularTempoMaiorQueORestante() {
        assertThrows(IllegalArgumentException.class, () -> {
            diaService.pularTempo(dia, 30);
        });
    }

    @Test
    void consumirTempoEventoDevePularTempo() {
        long antes = diaService.getTempoRestante(dia);

        diaService.consumirTempoEvento(dia, 10);

        long depois = diaService.getTempoRestante(dia);

        assertTrue(depois < antes);
    }

    @Test
    void deveAvancarTempo() {
        Instant inicioAntes = dia.getInicio();

        diaService.avancarTempo(dia, 5);

        Instant inicioDepois = dia.getInicio();

        assertEquals(inicioAntes.minusSeconds(5 * 60), inicioDepois);
    }

    @Test
    void deveEncerrarDiaManual() {
        diaService.encerrarDia(dia);

        assertTrue(diaService.isDiaEncerrado());
    }

    @Test
    void deveRetornarTempoRestante() {
        long restante = diaService.getTempoRestante(dia);

        assertTrue(restante > 0);
        assertTrue(restante <= 22 * 60);
    }

    @Test
    void deveAdicionarEventosObrigatoriosEAleatorios() {
        ZonaInterativa zonaObrigatoria = new ZonaInterativa();
        zonaObrigatoria.setNome("Sala 1");

        ZonaInterativa zonaAleatoria = new ZonaInterativa();
        zonaAleatoria.setNome("Cantina");

        Evento obrigatorio = new Evento();
        obrigatorio.setNome("Prova");
        obrigatorio.setZona(zonaObrigatoria);

        EventoAleatorio aleatorio = new EventoAleatorio();
        aleatorio.setNome("NPC especial");
        aleatorio.setZona(zonaAleatoria);
        aleatorio.setChanceAtivacao(1.0);

        List<Evento> obrigatorios = new ArrayList<>();
        obrigatorios.add(obrigatorio);

        List<EventoAleatorio> aleatorios = new ArrayList<>();
        aleatorios.add(aleatorio);

        diaService.gerarEventosDoDia(dia, obrigatorios, aleatorios);

        assertEquals(1, dia.getEventosObrigatorios().size());
        assertEquals(1, dia.getEventosAleatorios().size());
        assertTrue(dia.getEventosObrigatorios().containsKey("Sala 1"));
        assertTrue(dia.getEventosAleatorios().containsKey("Cantina"));
    }

    @Test
    void deveAdicionarEventoAleatorioIndependenteDeDeveAtivar() {
        ZonaInterativa zona = new ZonaInterativa();
        zona.setNome("Biblioteca");

        EventoAleatorio aleatorio = new EventoAleatorio() {
            @Override
            public boolean deveAtivar() {
                return false;
            }
        };
        aleatorio.setNome("Evento já decidido externamente");
        aleatorio.setZona(zona);

        List<Evento> obrigatorios = new ArrayList<>();
        List<EventoAleatorio> aleatorios = new ArrayList<>();
        aleatorios.add(aleatorio);

        diaService.gerarEventosDoDia(dia, obrigatorios, aleatorios);

        assertFalse(dia.getEventosAleatorios().isEmpty());
        assertTrue(dia.getEventosAleatorios().containsKey("Biblioteca"));
    }

    @Test
    void deveLancarExcecaoQuandoEventoObrigatorioNaoTemZona() {
        Evento obrigatorio = new Evento();
        obrigatorio.setNome("Sem zona");
        obrigatorio.setZona(null);

        List<Evento> obrigatorios = new ArrayList<>();
        obrigatorios.add(obrigatorio);

        List<EventoAleatorio> aleatorios = new ArrayList<>();

        assertThrows(IllegalArgumentException.class, () -> {
            diaService.gerarEventosDoDia(dia, obrigatorios, aleatorios);
        });
    }
}
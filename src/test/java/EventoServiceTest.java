
import model.Disciplina.AreaConhecimento;
import model.Evento.Evento;
import model.Local.ZonaInterativa;
import model.Personagem;
import model.Tempo.Dia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.DiaService;
import service.EventoService;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EventoServiceTest {

    private EventoService eventoService;
    private DiaService diaService;
    private Personagem personagem;
    private Dia dia;
    private ZonaInterativa zona;

    @BeforeEach
    void setUp() {
        eventoService = new EventoService();
        diaService = new DiaService();

        personagem = new Personagem();
        personagem.setEnergia(100);
        personagem.setMotivacao(50);
        personagem.setSaude(50);
        personagem.setDinheiro(100);

        dia = new Dia();
        dia.setInicio(Instant.now());
        dia.setDuracao(Duration.ofMinutes(22));
        dia.setSaiuDoPonto(false);

        zona = new ZonaInterativa();
        zona.setNome("Biblioteca");
    }

    @Test
    void deveCriarEvento() {
        Map<AreaConhecimento, Double> efeitosConhecimento = new HashMap<>();
        efeitosConhecimento.put(AreaConhecimento.MAT, 10.0);

        Evento evento = eventoService.criarEvento(
                "Estudar",
                "Sessão de estudos",
                -10,
                efeitosConhecimento,
                5,
                0,
                -20,
                5,
                0,
                null,
                20,
                20,
                false,
                zona
        );

        assertNotNull(evento);
        assertEquals(20, evento.getCustaDinheiro());
        assertFalse(evento.isRepetivel());
        assertEquals(zona, evento.getZona());
        assertFalse(evento.isStatus());
    }

    @Test
    void deveLancarExcecaoAoCriarEventoComNomeNulo() {
        assertThrows(IllegalArgumentException.class, () ->
                eventoService.criarEvento(
                        null,
                        "desc",
                        0,
                        null,
                        0,
                        0,
                        0,
                        5,
                        0,
                        null,
                        0,
                        0,
                        false,
                        zona
                )
        );
    }

    @Test
    void deveLancarExcecaoAoCriarEventoComNomeEmBranco() {
        assertThrows(IllegalArgumentException.class, () ->
                eventoService.criarEvento(
                        "   ",
                        "desc",
                        0,
                        null,
                        0,
                        0,
                        0,
                        5,
                        0,
                        null,
                        0,
                        0,
                        false,
                        zona
                )
        );
    }

    @Test
    void podeExecutarDeveRetornarTrueQuandoTodasAsCondicoesForemAtendidas() {
        Evento evento = new Evento();
        evento.setStatus(false);
        evento.setRepetivel(false);
        evento.setEnergiaMinima(20);
        evento.setCustaDinheiro(10);
        evento.setEfeitoTempo(5);

        boolean resultado = eventoService.podeExecutar(evento, personagem, dia, diaService);

        assertTrue(resultado);
    }

    @Test
    void naoPodeExecutarQuandoEventoJaFoiExecutadoENaoEhRepetivel() {
        Evento evento = new Evento();
        evento.setStatus(true);
        evento.setRepetivel(false);
        evento.setEnergiaMinima(10);
        evento.setCustaDinheiro(0);
        evento.setEfeitoTempo(1);

        boolean resultado = eventoService.podeExecutar(evento, personagem, dia, diaService);

        assertFalse(resultado);
    }

    @Test
    void naoPodeExecutarQuandoEnergiaForInsuficiente() {
        Evento evento = new Evento();
        evento.setStatus(false);
        evento.setRepetivel(true);
        evento.setEnergiaMinima(150);
        evento.setCustaDinheiro(0);
        evento.setEfeitoTempo(1);

        boolean resultado = eventoService.podeExecutar(evento, personagem, dia, diaService);

        assertFalse(resultado);
    }

    @Test
    void naoPodeExecutarQuandoDinheiroForInsuficiente() {
        Evento evento = new Evento();
        evento.setStatus(false);
        evento.setRepetivel(true);
        evento.setEnergiaMinima(10);
        evento.setCustaDinheiro(150);
        evento.setEfeitoTempo(1);

        boolean resultado = eventoService.podeExecutar(evento, personagem, dia, diaService);

        assertFalse(resultado);
    }

    @Test
    void naoPodeExecutarQuandoEventoRequisitoNaoFoiConcluido() {
        Evento requisito = new Evento();
        requisito.setStatus(false);

        Evento evento = new Evento();
        evento.setStatus(false);
        evento.setRepetivel(true);
        evento.setEnergiaMinima(10);
        evento.setCustaDinheiro(0);
        evento.setEfeitoTempo(1);
        evento.setEventoRequisito(requisito);

        boolean resultado = eventoService.podeExecutar(evento, personagem, dia, diaService);

        assertFalse(resultado);
    }

    @Test
    void naoPodeExecutarQuandoTempoForInsuficiente() {
        Evento evento = new Evento();
        evento.setStatus(false);
        evento.setRepetivel(true);
        evento.setEnergiaMinima(10);
        evento.setCustaDinheiro(0);
        evento.setEfeitoTempo(10);

        dia.setDuracao(Duration.ofMinutes(5));
        dia.setInicio(Instant.now());

        boolean resultado = eventoService.podeExecutar(evento, personagem, dia, diaService);

        assertFalse(resultado);
    }

    @Test
    void deveExecutarEventoObrigatorio() {
        Evento evento = new Evento();
        evento.setZona(zona);
        evento.setStatus(false);
        evento.setRepetivel(false);
        evento.setEnergiaMinima(10);
        evento.setCustaDinheiro(0);
        evento.setEfeitoTempo(5);
        evento.setEfeitoEnergia(-10);
        evento.setEfeitoMotivacao(5);
        evento.setEfeitoSaude(-2);
        evento.setEfeitoDinheiro(15);

        dia.getEventosObrigatorios().put(zona.getNome(), evento);

        double energiaAntes = personagem.getEnergia();
        double motivacaoAntes = personagem.getMotivacao();
        double saudeAntes = personagem.getSaude();
        double dinheiroAntes = personagem.getDinheiro();
        long tempoAntes = diaService.getTempoRestante(dia);

        eventoService.executarEvento(evento, personagem, dia, diaService);

        long tempoDepois = diaService.getTempoRestante(dia);

        assertTrue(evento.isStatus());
        assertEquals(energiaAntes - 10, personagem.getEnergia());
        assertEquals(motivacaoAntes + 5, personagem.getMotivacao());
        assertEquals(saudeAntes - 2, personagem.getSaude());
        assertEquals(dinheiroAntes + 15, personagem.getDinheiro());
        assertTrue(tempoDepois < tempoAntes);
    }

    @Test
    void deveExecutarEventoAleatorio() {
        Evento evento = new Evento();
        evento.setZona(zona);
        evento.setStatus(false);
        evento.setRepetivel(true);
        evento.setEnergiaMinima(10);
        evento.setCustaDinheiro(0);
        evento.setEfeitoTempo(3);

        dia.getEventosAleatorios().put(zona.getNome(), evento);

        long tempoAntes = diaService.getTempoRestante(dia);

        eventoService.executarEvento(evento, personagem, dia, diaService);

        long tempoDepois = diaService.getTempoRestante(dia);

        assertTrue(evento.isStatus());
        assertTrue(tempoDepois < tempoAntes);
    }

    @Test
    void deveLancarExcecaoQuandoNaoExisteEventoNaZona() {
        Evento evento = new Evento();
        evento.setZona(zona);
        evento.setStatus(false);
        evento.setRepetivel(true);
        evento.setEnergiaMinima(10);
        evento.setCustaDinheiro(0);
        evento.setEfeitoTempo(1);

        assertThrows(IllegalStateException.class, () ->
                eventoService.executarEvento(evento, personagem, dia, diaService)
        );
    }

    @Test
    void deveLancarExcecaoQuandoEventoNaoCorrespondeAZonaAtual() {
        Evento eventoNoMapa = new Evento();
        eventoNoMapa.setZona(zona);

        Evento outroEvento = new Evento();
        outroEvento.setZona(zona);
        outroEvento.setStatus(false);
        outroEvento.setRepetivel(true);
        outroEvento.setEnergiaMinima(10);
        outroEvento.setCustaDinheiro(0);
        outroEvento.setEfeitoTempo(1);

        dia.getEventosObrigatorios().put(zona.getNome(), eventoNoMapa);

        assertThrows(IllegalStateException.class, () ->
                eventoService.executarEvento(outroEvento, personagem, dia, diaService)
        );
    }

    @Test
    void deveLancarExcecaoQuandoEventoNaoPodeSerExecutado() {
        Evento evento = new Evento();
        evento.setZona(zona);
        evento.setStatus(true);
        evento.setRepetivel(false);
        evento.setEnergiaMinima(10);
        evento.setCustaDinheiro(0);
        evento.setEfeitoTempo(1);

        dia.getEventosObrigatorios().put(zona.getNome(), evento);

        assertThrows(IllegalStateException.class, () ->
                eventoService.executarEvento(evento, personagem, dia, diaService)
        );
    }

    @Test
    void deveAplicarEfeitosDeConhecimento() {
        Map<AreaConhecimento, Double> efeitosConhecimento = new HashMap<>();
        efeitosConhecimento.put(AreaConhecimento.MAT, 8.0);

        Evento evento = new Evento();
        evento.setZona(zona);
        evento.setStatus(false);
        evento.setRepetivel(true);
        evento.setEnergiaMinima(10);
        evento.setCustaDinheiro(0);
        evento.setEfeitoTempo(2);
        evento.setEfeitosConhecimento(efeitosConhecimento);

        dia.getEventosObrigatorios().put(zona.getNome(), evento);

        personagem.adicionarConhecimento(AreaConhecimento.MAT, 0.0);
        double conhecimentoAntes = personagem.getConhecimento(AreaConhecimento.MAT);

        eventoService.executarEvento(evento, personagem, dia, diaService);

        double conhecimentoDepois = personagem.getConhecimento(AreaConhecimento.MAT);

        assertEquals(conhecimentoAntes + 8.0, conhecimentoDepois);
    }

    @Test
    void naoDeveDeixarAtributosNegativos() {
        Evento evento = new Evento();
        evento.setZona(zona);
        evento.setStatus(false);
        evento.setRepetivel(true);
        evento.setEnergiaMinima(0);
        evento.setCustaDinheiro(0);
        evento.setEfeitoTempo(1);
        evento.setEfeitoEnergia(-500);
        evento.setEfeitoMotivacao(-500);
        evento.setEfeitoSaude(-500);
        evento.setEfeitoDinheiro(-500);

        dia.getEventosObrigatorios().put(zona.getNome(), evento);

        eventoService.executarEvento(evento, personagem, dia, diaService);

        assertEquals(0, personagem.getEnergia());
        assertEquals(0, personagem.getMotivacao());
        assertEquals(0, personagem.getSaude());
        assertEquals(0, personagem.getDinheiro());
    }
}
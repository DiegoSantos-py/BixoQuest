package service;

import exception.Evento.EventoInvalidoException;
import model.Disciplina.AreaConhecimento;
import model.Evento.Evento;
import model.Evento.ResultadoZona;
import model.Local.ZonaInterativa;
import model.Personagem;
import model.Tempo.Dia;
import model.Tempo.Semestre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.EventoRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EventoServiceTest {

    private EventoService eventoService;
    private DiaService diaService;
    private Personagem personagem;
    private Semestre semestre;
    private Dia dia;
    private ZonaInterativa zona;

    @BeforeEach
    void setUp() {
        eventoService = new EventoService(new EventoRepository());
        diaService = new DiaService();

        personagem = new Personagem();
        personagem.setEnergia(100);
        personagem.setMotivacao(50);
        personagem.setSaude(50);
        personagem.setDinheiro(100);

        semestre = new Semestre();

        dia = new Dia();
        dia.setInicio(Instant.now());
        dia.setDuracao(Duration.ofMinutes(22));
        dia.setSaiuDoPonto(false);

        zona = new ZonaInterativa();
        zona.setNome("Biblioteca");
    }

    private Map<String, Double> requisito(String atributo, double valor) {
        Map<String, Double> requisitos = new HashMap<>();
        requisitos.put(atributo, valor);
        return requisitos;
    }

    // criarEvento

    @Test
    void deveCriarEvento() throws Exception {
        Map<AreaConhecimento, Double> efeitosConhecimento = new HashMap<>();
        efeitosConhecimento.put(AreaConhecimento.MAT, 10.0);

        Evento evento = eventoService.criarEvento(
                "Estudar", "Sessão de estudos",
                -10, efeitosConhecimento, 5, 0, -20, 5, 0, null, 20, false, zona);

        assertNotNull(evento);
        assertEquals(20, evento.getCustaDinheiro());
        assertFalse(evento.isRepetivel());
        assertEquals(zona, evento.getZona());
        assertFalse(evento.isStatus());
    }

    @Test
    void deveLancarExcecaoAoCriarEventoComNomeNulo() {
        assertThrows(EventoInvalidoException.class, () ->
                eventoService.criarEvento(
                        null, "desc", 0, null, 0, 0, 0, 5, 0, null, 0, false, zona));
    }

    @Test
    void deveLancarExcecaoAoCriarEventoComNomeEmBranco() {
        assertThrows(EventoInvalidoException.class, () ->
                eventoService.criarEvento(
                        "   ", "desc", 0, null, 0, 0, 0, 5, 0, null, 0, false, zona));
    }

    @Test
    void deveLancarExcecaoAoCriarEventoComZonaNula() {
        assertThrows(EventoInvalidoException.class, () ->
                eventoService.criarEvento(
                        "Estudar", "desc", 0, null, 0, 0, 0, 5, 0, null, 0, false, null));
    }

    // podeExecutar

    @Test
    void podeExecutarDeveRetornarNuloQuandoTodasAsCondicoesForemAtendidas() {
        Evento evento = new Evento();
        evento.setStatus(false);
        evento.setRepetivel(false);
        evento.setCustaDinheiro(10);
        evento.setEfeitoTempo(5);

        assertNull(eventoService.podeExecutar(evento, personagem, semestre, dia, diaService));
    }

    @Test
    void naoPodeExecutarQuandoEventoJaFoiExecutadoENaoEhRepetivel() {
        Evento evento = new Evento();
        evento.setStatus(true);
        evento.setRepetivel(false);
        evento.setCustaDinheiro(0);
        evento.setEfeitoTempo(1);

        ResultadoZona resultado = eventoService.podeExecutar(evento, personagem, semestre, dia, diaService);

        assertNotNull(resultado);
        assertEquals(ResultadoZona.Status.REQUISITO_NAO_ATENDIDO, resultado.getStatus());
    }

    @Test
    void naoPodeExecutarQuandoRequisitoDeAtributoNaoForAtendido() {
        Evento evento = new Evento();
        evento.setStatus(false);
        evento.setRepetivel(true);
        evento.setRequisitosAtributo(requisito("ENERGIA", 150.0));
        evento.setCustaDinheiro(0);
        evento.setEfeitoTempo(1);

        ResultadoZona resultado = eventoService.podeExecutar(evento, personagem, semestre, dia, diaService);

        assertNotNull(resultado);
        assertEquals(ResultadoZona.Status.REQUISITO_NAO_ATENDIDO, resultado.getStatus());
    }

    @Test
    void naoPodeExecutarQuandoDinheiroForInsuficiente() {
        Evento evento = new Evento();
        evento.setStatus(false);
        evento.setRepetivel(true);
        evento.setCustaDinheiro(150);
        evento.setEfeitoTempo(1);

        ResultadoZona resultado = eventoService.podeExecutar(evento, personagem, semestre, dia, diaService);

        assertNotNull(resultado);
        assertEquals(ResultadoZona.Status.REQUISITO_NAO_ATENDIDO, resultado.getStatus());
    }

    @Test
    void naoPodeExecutarQuandoEventoRequisitoNaoFoiConcluido() {
        Evento requisito = new Evento();
        requisito.setStatus(false);

        Evento evento = new Evento();
        evento.setStatus(false);
        evento.setRepetivel(true);
        evento.setCustaDinheiro(0);
        evento.setEfeitoTempo(1);
        evento.setEventoRequisito(requisito);

        ResultadoZona resultado = eventoService.podeExecutar(evento, personagem, semestre, dia, diaService);

        assertNotNull(resultado);
        assertEquals(ResultadoZona.Status.REQUISITO_NAO_ATENDIDO, resultado.getStatus());
    }

    @Test
    void naoPodeExecutarQuandoDisciplinaRequisitoNaoForCursada() {
        Evento evento = new Evento();
        evento.setStatus(false);
        evento.setRepetivel(true);
        evento.setCustaDinheiro(0);
        evento.setEfeitoTempo(1);
        evento.setDisciplinaRequisitoNome("Matematica");

        ResultadoZona resultado = eventoService.podeExecutar(evento, personagem, semestre, dia, diaService);

        assertNotNull(resultado);
        assertEquals(ResultadoZona.Status.REQUISITO_NAO_ATENDIDO, resultado.getStatus());
    }

    @Test
    void naoPodeExecutarQuandoTempoForInsuficiente() {
        Evento evento = new Evento();
        evento.setStatus(false);
        evento.setRepetivel(true);
        evento.setCustaDinheiro(0);
        evento.setEfeitoTempo(10);

        dia.setDuracao(Duration.ofMinutes(5));
        dia.setInicio(Instant.now());

        ResultadoZona resultado = eventoService.podeExecutar(evento, personagem, semestre, dia, diaService);

        assertNotNull(resultado);
        assertEquals(ResultadoZona.Status.REQUISITO_NAO_ATENDIDO, resultado.getStatus());
    }

    // executarEvento

    @Test
    void deveExecutarEventoEAplicarEfeitos() {
        Evento evento = new Evento();
        evento.setZona(zona);
        evento.setStatus(false);
        evento.setRepetivel(false);
        evento.setCustaDinheiro(0);
        evento.setEfeitoTempo(5);
        evento.setEfeitoEnergia(-10);
        evento.setEfeitoMotivacao(5);
        evento.setEfeitoSaude(-2);
        evento.setEfeitoDinheiro(15);

        double energiaAntes    = personagem.getEnergia();
        double motivacaoAntes  = personagem.getMotivacao();
        double saudeAntes      = personagem.getSaude();
        double dinheiroAntes   = personagem.getDinheiro();
        long tempoAntes        = diaService.getTempoRestante(dia);

        eventoService.executarEvento(evento, personagem, dia, diaService);

        assertTrue(evento.isStatus());
        assertEquals(energiaAntes   - 10, personagem.getEnergia());
        assertEquals(motivacaoAntes + 5,  personagem.getMotivacao());
        assertEquals(saudeAntes     - 2,  personagem.getSaude());
        assertEquals(dinheiroAntes  + 15, personagem.getDinheiro());
        assertTrue(diaService.getTempoRestante(dia) < tempoAntes);
    }

    @Test
    void deveAplicarEfeitosDeConhecimento() {
        Map<AreaConhecimento, Double> efeitosConhecimento = new HashMap<>();
        efeitosConhecimento.put(AreaConhecimento.MAT, 8.0);

        Evento evento = new Evento();
        evento.setZona(zona);
        evento.setStatus(false);
        evento.setRepetivel(true);
        evento.setCustaDinheiro(0);
        evento.setEfeitoTempo(2);
        evento.setEfeitosConhecimento(efeitosConhecimento);

        personagem.atualizarConhecimento(AreaConhecimento.MAT, 0.0);
        double conhecimentoAntes = personagem.getConhecimento(AreaConhecimento.MAT);

        eventoService.executarEvento(evento, personagem, dia, diaService);

        assertEquals(conhecimentoAntes + 8.0, personagem.getConhecimento(AreaConhecimento.MAT));
    }

    @Test
    void naoDeveDeixarAtributosNegativos() {
        Evento evento = new Evento();
        evento.setZona(zona);
        evento.setStatus(false);
        evento.setRepetivel(true);
        evento.setCustaDinheiro(0);
        evento.setEfeitoTempo(1);
        evento.setEfeitoEnergia(-500);
        evento.setEfeitoMotivacao(-500);
        evento.setEfeitoSaude(-500);
        evento.setEfeitoDinheiro(-500);

        eventoService.executarEvento(evento, personagem, dia, diaService);

        assertEquals(0, personagem.getEnergia());
        assertEquals(0, personagem.getMotivacao());
        assertEquals(0, personagem.getSaude());
        assertEquals(0, personagem.getDinheiro());
    }
}
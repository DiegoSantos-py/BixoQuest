import model.Disciplina.Disciplina;
import model.Evento.Evento;
import model.Local.Area;
import model.Local.Direcao;
import model.Local.Local;
import model.Local.TipoLocal;
import model.Local.ZonaInterativa;
import model.Personagem;
import model.Tempo.Dia;
import model.Tempo.Semestre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.DiaService;
import service.EventoService;
import service.PersonagemService;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class PersonagemServiceTest {

    private PersonagemService personagemService;
    private EventoService eventoService;
    private DiaService diaService;

    private Local localInicial;
    private Dia dia;

    @BeforeEach
    void setUp() {
        eventoService = new EventoService();
        personagemService = new PersonagemService(eventoService);
        diaService = new DiaService();

        localInicial = new Local(
                "Ponto de ônibus",
                new Area(10, -10, 10, -10),
                TipoLocal.PONTO_ONIBUS
        );

        dia = new Dia();
        dia.setInicio(Instant.now());
        dia.setDuracao(Duration.ofMinutes(22));
        dia.setSaiuDoPonto(false);
    }

    @Test
    void deveCriarPersonagem() {
        Personagem personagem = personagemService.criarPersonagem(
                "Fulano",
                100,
                80,
                90,
                50,
                "sprite.png",
                localInicial,
                0,
                0
        );

        assertNotNull(personagem);
        assertEquals("Fulano", personagem.getNome());
        assertEquals(100, personagem.getEnergia());
        assertEquals(80, personagem.getMotivacao());
        assertEquals(90, personagem.getSaude());
        assertEquals(50, personagem.getDinheiro());
        assertEquals("sprite.png", personagem.getSpriteDir());
        assertEquals(localInicial, personagem.getLocalAtual());
        assertEquals(0, personagem.getcX());
        assertEquals(0, personagem.getcY());
    }

    @Test
    void deveLancarExcecaoAoCriarPersonagemComNomeNulo() {
        assertThrows(IllegalArgumentException.class, () ->
                personagemService.criarPersonagem(
                        null, 100, 80, 90, 50,
                        "sprite.png", localInicial, 0, 0
                )
        );
    }

    @Test
    void deveLancarExcecaoAoCriarPersonagemComNomeEmBranco() {
        assertThrows(IllegalArgumentException.class, () ->
                personagemService.criarPersonagem(
                        "   ", 100, 80, 90, 50,
                        "sprite.png", localInicial, 0, 0
                )
        );
    }

    @Test
    void deveLancarExcecaoAoCriarPersonagemComAtributosNegativos() {
        assertThrows(IllegalArgumentException.class, () ->
                personagemService.criarPersonagem(
                        "Fulano", -1, 80, 90, 50,
                        "sprite.png", localInicial, 0, 0
                )
        );

        assertThrows(IllegalArgumentException.class, () ->
                personagemService.criarPersonagem(
                        "Fulano", 100, -1, 90, 50,
                        "sprite.png", localInicial, 0, 0
                )
        );

        assertThrows(IllegalArgumentException.class, () ->
                personagemService.criarPersonagem(
                        "Fulano", 100, 80, -1, 50,
                        "sprite.png", localInicial, 0, 0
                )
        );

        assertThrows(IllegalArgumentException.class, () ->
                personagemService.criarPersonagem(
                        "Fulano", 100, 80, 90, -1,
                        "sprite.png", localInicial, 0, 0
                )
        );
    }

    @Test
    void deveMoverDentroDoMesmoLocalParaDireita() {
        Personagem personagem = personagemService.criarPersonagem(
                "Fulano", 100, 80, 90, 50,
                "sprite.png", localInicial, 0, 0
        );

        personagemService.mover(personagem, Direcao.DIREITA, dia, diaService);

        assertEquals(localInicial, personagem.getLocalAtual());
        assertEquals(1, personagem.getcX());
        assertEquals(0, personagem.getcY());
    }

    @Test
    void naoDeveMoverParaForaSeNaoHouverVizinho() {
        Local localPequeno = new Local(
                "Local pequeno",
                new Area(1, -1, 1, -1),
                TipoLocal.SALA
        );

        Personagem personagem = personagemService.criarPersonagem(
                "Fulano", 100, 80, 90, 50,
                "sprite.png", localPequeno, 1, 0
        );

        personagemService.mover(personagem, Direcao.DIREITA, dia, diaService);

        assertEquals(localPequeno, personagem.getLocalAtual());
        assertEquals(1, personagem.getcX());
        assertEquals(0, personagem.getcY());
    }

    @Test
    void deveMoverParaLocalVizinho() {
        Local origem = new Local(
                "Origem",
                new Area(1, -1, 1, -1),
                TipoLocal.PONTO_ONIBUS
        );

        Local destino = new Local(
                "Destino",
                new Area(5, -5, 5, -5),
                TipoLocal.SALA
        );

        origem.adicionarVizinho(Direcao.DIREITA, destino);

        Personagem personagem = personagemService.criarPersonagem(
                "Fulano", 100, 80, 90, 50,
                "sprite.png", origem, 1, 0
        );

        personagemService.mover(personagem, Direcao.DIREITA, dia, diaService);

        assertEquals(destino, personagem.getLocalAtual());
        assertEquals(destino.getArea().getMinX(), personagem.getcX());
    }

    @Test
    void deveAjustarPosicaoAoEntrarPorCima() {
        Local origem = new Local(
                "Origem",
                new Area(1, -1, 1, -1),
                TipoLocal.PONTO_ONIBUS
        );

        Local destino = new Local(
                "Destino",
                new Area(5, -5, 5, -5),
                TipoLocal.SALA
        );

        origem.adicionarVizinho(Direcao.CIMA, destino);

        Personagem personagem = personagemService.criarPersonagem(
                "Fulano", 100, 80, 90, 50,
                "sprite.png", origem, 0, -1
        );

        personagemService.mover(personagem, Direcao.CIMA, dia, diaService);

        assertEquals(destino, personagem.getLocalAtual());
        assertEquals(destino.getArea().getMinY(), personagem.getcY());
    }

    @Test
    void deveExecutarEventoObrigatorioAoEntrarNaZona() {
        Personagem personagem = personagemService.criarPersonagem(
                "Fulano", 100, 50, 50, 100,
                "sprite.png", localInicial, 0, 0
        );

        ZonaInterativa zona = new ZonaInterativa(
                new Area(2, -2, 2, -2),
                "Zona de estudo"
        );
        localInicial.getZonaInterativasDisponiveis().add(zona);

        Evento evento = new Evento();
        evento.setZona(zona);
        evento.setStatus(false);
        evento.setRepetivel(true);
        evento.setEnergiaMinima(10);
        evento.setCustaDinheiro(0);
        evento.setEfeitoTempo(1);
        evento.setEfeitoEnergia(-10);

        dia.getEventosObrigatorios().put(zona.getNome(), evento);

        personagemService.atualizarPersonagem(personagem, Direcao.DIREITA, dia, diaService);

        assertTrue(evento.isStatus());
        assertEquals(90, personagem.getEnergia());
    }

    @Test
    void deveExecutarEventoAleatorioAoEntrarNaZona() {
        Personagem personagem = personagemService.criarPersonagem(
                "Fulano", 100, 50, 50, 100,
                "sprite.png", localInicial, 0, 0
        );

        ZonaInterativa zona = new ZonaInterativa(
                new Area(2, -2, 2, -2),
                "Zona aleatoria"
        );
        localInicial.getZonaInterativasDisponiveis().add(zona);

        Evento evento = new Evento();
        evento.setZona(zona);
        evento.setStatus(false);
        evento.setRepetivel(true);
        evento.setEnergiaMinima(10);
        evento.setCustaDinheiro(0);
        evento.setEfeitoTempo(1);
        evento.setEfeitoMotivacao(10);

        dia.getEventosAleatorios().put(zona.getNome(), evento);

        personagemService.atualizarPersonagem(personagem, Direcao.DIREITA, dia, diaService);

        assertTrue(evento.isStatus());
        assertEquals(60, personagem.getMotivacao());
    }

    @Test
    void naoDeveExecutarEventoSePersonagemNaoEntrarNaZona() {
        Personagem personagem = personagemService.criarPersonagem(
                "Fulano", 100, 50, 50, 100,
                "sprite.png", localInicial, 9, 9
        );

        ZonaInterativa zona = new ZonaInterativa(
                new Area(2, -2, 2, -2),
                "Zona central"
        );
        localInicial.getZonaInterativasDisponiveis().add(zona);

        Evento evento = new Evento();
        evento.setZona(zona);
        evento.setStatus(false);
        evento.setRepetivel(true);
        evento.setEnergiaMinima(10);
        evento.setCustaDinheiro(0);
        evento.setEfeitoTempo(1);

        dia.getEventosObrigatorios().put(zona.getNome(), evento);

        personagemService.atualizarPersonagem(personagem, Direcao.DIREITA, dia, diaService);

        assertFalse(evento.isStatus());
    }

    @Test
    void deveEncerrarDiaQuandoSaiEDepoisVoltaAoPonto() {
        Local ponto = new Local(
                "Ponto",
                new Area(1, -1, 1, -1),
                TipoLocal.PONTO_ONIBUS
        );

        Local sala = new Local(
                "Sala",
                new Area(5, -5, 5, -5),
                TipoLocal.SALA
        );

        ponto.adicionarVizinho(Direcao.DIREITA, sala);
        sala.adicionarVizinho(Direcao.ESQUERDA, ponto);

        Personagem personagem = personagemService.criarPersonagem(
                "Fulano", 100, 50, 50, 100,
                "sprite.png", ponto, 1, 0
        );

        personagemService.mover(personagem, Direcao.DIREITA, dia, diaService);
        assertTrue(dia.isSaiuDoPonto());

        personagem.setcX(sala.getArea().getMinX());
        personagemService.mover(personagem, Direcao.ESQUERDA, dia, diaService);

        assertTrue(diaService.isDiaEncerrado());
    }

    @Test
    void calcularDesempenhoGeralDeveRetornarZeroSemSemestres() {
        Personagem personagem = personagemService.criarPersonagem(
                "Fulano", 100, 80, 90, 50,
                "sprite.png", localInicial, 0, 0
        );

        double desempenho = personagemService.calcularDesempenhoGeral(personagem);

        assertEquals(0.0, desempenho);
    }

    @Test
    void calcularDesempenhoGeralDeveRetornarUmQuandoTodasForemAprovadas() {
        Personagem personagem = personagemService.criarPersonagem(
                "Fulano", 100, 80, 90, 50,
                "sprite.png", localInicial, 0, 0
        );

        Disciplina d1 = new Disciplina();
        d1.setNome("Matematica");
        d1.setCodigo(1);

        Disciplina d2 = new Disciplina();
        d2.setNome("Fisica");
        d2.setCodigo(1);

        Semestre semestre = new Semestre();
        semestre.adicionarDisciplinas(d1);
        semestre.adicionarDisciplinas(d2);

        semestre.registrarResultado(d1, true);
        semestre.registrarResultado(d2, true);

        personagem.adicionarSemestre(semestre);

        double desempenho = personagemService.calcularDesempenhoGeral(personagem);

        assertEquals(1.0, desempenho);
    }

    @Test
    void calcularDesempenhoGeralDeveRetornarMetadeQuandoMetadeForAprovada() {
        Personagem personagem = personagemService.criarPersonagem(
                "Fulano", 100, 80, 90, 50,
                "sprite.png", localInicial, 0, 0
        );

        Disciplina d1 = new Disciplina();
        d1.setNome("Matematica");
        d1.setCodigo(1);

        Disciplina d2 = new Disciplina();
        d2.setNome("Fisica");
        d2.setCodigo(1);

        Semestre semestre = new Semestre();
        semestre.adicionarDisciplinas(d1);
        semestre.adicionarDisciplinas(d2);

        semestre.registrarResultado(d1, true);
        semestre.registrarResultado(d2, false);

        personagem.adicionarSemestre(semestre);

        double desempenho = personagemService.calcularDesempenhoGeral(personagem);

        assertEquals(0.5, desempenho);
    }

    @Test
    void calcularDesempenhoGeralDeveConsiderarMultiplosSemestres() {
        Personagem personagem = personagemService.criarPersonagem(
                "Fulano", 100, 80, 90, 50,
                "sprite.png", localInicial, 0, 0
        );

        Disciplina d1 = new Disciplina();
        d1.setNome("Matematica");
        d1.setCodigo(1);

        Disciplina d2 = new Disciplina();
        d2.setNome("Fisica");
        d2.setCodigo(1);

        Disciplina d3 = new Disciplina();
        d3.setNome("Quimica");
        d3.setCodigo(1);

        Semestre semestre1 = new Semestre();
        semestre1.adicionarDisciplinas(d1);
        semestre1.adicionarDisciplinas(d2);

        Semestre semestre2 = new Semestre();
        semestre2.adicionarDisciplinas(d3);

        semestre1.registrarResultado(d1, true);
        semestre1.registrarResultado(d2, false);
        semestre2.registrarResultado(d3, true);

        personagem.adicionarSemestre(semestre1);
        personagem.adicionarSemestre(semestre2);

        double desempenho = personagemService.calcularDesempenhoGeral(personagem);

        assertEquals(2.0 / 3.0, desempenho);
    }
}
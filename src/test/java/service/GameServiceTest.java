package service;

import exception.PersistenciaException;
import model.Disciplina.AreaConhecimento;
import model.Disciplina.Disciplina;
import model.Local.Area;
import model.Local.Local;
import model.Local.TipoLocal;
import model.Personagem;
import model.Tempo.Semestre;
import org.junit.jupiter.api.*;
import repository.*;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GameServiceTest {

    private DiaService diaService;
    private SemestreService semestreService;
    private DisciplinaService disciplinaService;
    private PersonagemService personagemService;
    private InicializacaoService inicializacaoService;
    private GameService gameService;

    private LocalRepository localRepo;
    private DisciplinaRepository disciplinaRepo;
    private SemestreRepository semestreRepo;
    private PersonagemRepository personagemRepo;
    private EventoRepository eventoRepo;
    private NpcRepository npcRepo;

    private Personagem personagem;

    @BeforeEach
    void setUp() throws PersistenciaException {
        localRepo = new LocalRepository();
        disciplinaRepo = new DisciplinaRepository();
        semestreRepo = new SemestreRepository();
        personagemRepo = new PersonagemRepository();
        eventoRepo = new EventoRepository();
        npcRepo = new NpcRepository();

        diaService = new DiaService();
        disciplinaService = new DisciplinaService(disciplinaRepo);
        semestreService = new SemestreService(semestreRepo, disciplinaRepo);
        EventoService eventoService = new EventoService(eventoRepo);
        personagemService = new PersonagemService(personagemRepo, eventoService);

        inicializacaoService = new InicializacaoService(
                localRepo, disciplinaRepo, eventoRepo,
                npcRepo, semestreRepo, personagemRepo
        );

        gameService = new GameService(
                diaService,
                semestreService,
                personagemService,
                inicializacaoService,
                localRepo,
                semestreRepo,
                disciplinaRepo,
                eventoRepo,
                npcRepo,
                personagemRepo
        );

        personagem = new Personagem("Jogador", 40.0, 40.0, 40.0, 40.0, "sprites/jogador.png");
    }

    @AfterEach
    void limpar() {
        new File("locais.json").delete();
        new File("disciplinas.json").delete();
        new File("eventos.json").delete();
        new File("npcs.json").delete();
        new File("semestres.json").delete();
        new File("personagens.json").delete();
        diaService.pararTempo();
    }

    // -------------------------------------------------------------------------
    // iniciarJogo
    // -------------------------------------------------------------------------

    @Test
    @Order(1)
    @DisplayName("Deve iniciar jogo corretamente com ponto de ônibus")
    void deveIniciarJogoCorretamente() throws PersistenciaException {
        Local ponto = new Local("Ponto 1", new Area(10, -10, 10, -10), TipoLocal.PONTO_ONIBUS);
        localRepo.adicionarLocal(ponto);

        gameService.iniciarJogo(personagem);

        assertEquals(40.0, personagem.getEnergia());
        assertEquals(ponto, personagem.getLocalAtual());
        assertEquals(0, personagem.getcX());
        assertEquals(0, personagem.getcY());
    }

    @Test
    @Order(2)
    @DisplayName("Deve lançar exceção ao iniciar jogo sem ponto de ônibus")
    void deveLancarExcecaoAoIniciarJogoSemPontoDeOnibus() {
        assertThrows(Exception.class, () -> gameService.iniciarJogo(personagem));
    }

    @Test
    @Order(3)
    @DisplayName("Deve criar primeiro semestre ao iniciar novo jogo")
    void deveCriarPrimeiroSemestreAoIniciarNovoJogo() throws PersistenciaException {
        Local ponto = new Local("Ponto 1", new Area(10, -10, 10, -10), TipoLocal.PONTO_ONIBUS);
        localRepo.adicionarLocal(ponto);

        disciplinaService.criarDisciplinasPorNivel("Matemática", 1, AreaConhecimento.MAT);

        gameService.iniciarJogo(personagem);

        assertNotNull(gameService.getSemestre());
        assertNotNull(gameService.getDiaAtual());
    }

    @Test
    @Order(4)
    @DisplayName("Deve retomar sessão anterior ao iniciar jogo com semestres salvos")
    void deveRetomarSessaoAnterior() throws PersistenciaException {
        Local ponto = new Local("Ponto 1", new Area(10, -10, 10, -10), TipoLocal.PONTO_ONIBUS);
        localRepo.adicionarLocal(ponto);

        Semestre semestreAnterior = new Semestre();
        semestreRepo.adicionarSemestre(personagem.getPersonagemId(), semestreAnterior);
        semestreRepo.salvar();

        gameService.iniciarJogo(personagem);

        List<Semestre> semestres = semestreRepo.getSemestresPorJogador(personagem.getPersonagemId());
        assertFalse(semestres.isEmpty());
    }

    // -------------------------------------------------------------------------
    // encerrarJogo
    // -------------------------------------------------------------------------

    @Test
    @Order(5)
    @DisplayName("Deve retornar false se não passou em todas as disciplinas")
    void deveRetornarFalseSeNaoPassouEmTodasDisciplinas() throws PersistenciaException {
        disciplinaService.criarDisciplinasPorNivel("Matemática", 1, AreaConhecimento.MAT);
        disciplinaService.criarDisciplinasPorNivel("Física", 1, AreaConhecimento.MAT);

        Disciplina matematica = new Disciplina();
        matematica.setNome("Matemática");
        matematica.setCodigo(1);

        Semestre semestre = new Semestre();
        semestre.adicionarDisciplinas(matematica);
        semestre.registrarResultado(matematica, true);
        semestreRepo.adicionarSemestre(personagem.getPersonagemId(), semestre);

        gameService.setPersonagem(personagem);

        assertFalse(gameService.encerrarJogo());
    }

    @Test
    @Order(6)
    @DisplayName("Deve retornar true quando passou em todas as disciplinas")
    void deveRetornarTrueQuandoPassouEmTodasDisciplinas() throws PersistenciaException {
        disciplinaService.criarDisciplinasPorNivel("Matemática", 1, AreaConhecimento.MAT);
        disciplinaService.criarDisciplinasPorNivel("Física", 1, AreaConhecimento.MAT);

        Disciplina matematica = new Disciplina();
        matematica.setNome("Matemática");
        matematica.setCodigo(1);

        Disciplina fisica = new Disciplina();
        fisica.setNome("Física");
        fisica.setCodigo(1);

        Semestre semestre1 = new Semestre();
        semestre1.adicionarDisciplinas(matematica);
        semestre1.registrarResultado(matematica, true);

        Semestre semestre2 = new Semestre();
        semestre2.adicionarDisciplinas(fisica);
        semestre2.registrarResultado(fisica, true);

        semestreRepo.adicionarSemestre(personagem.getPersonagemId(), semestre1);
        semestreRepo.adicionarSemestre(personagem.getPersonagemId(), semestre2);

        gameService.setPersonagem(personagem);

        assertTrue(gameService.encerrarJogo());
    }

    @Test
    @Order(7)
    @DisplayName("Deve retornar false quando personagem não tem semestres")
    void deveRetornarFalseQuandoPersonagemNaoTemSemestres() throws PersistenciaException {
        disciplinaService.criarDisciplinasPorNivel("Matemática", 1, AreaConhecimento.MAT);

        gameService.setPersonagem(personagem);

        assertFalse(gameService.encerrarJogo());
    }
}
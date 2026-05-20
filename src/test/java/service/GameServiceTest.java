package service;

import model.Disciplina.AreaConhecimento;
import model.Disciplina.Disciplina;
import model.Local.Area;
import model.Local.Local;
import model.Local.TipoLocal;
import model.Personagem;
import model.Tempo.Semestre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.DisciplinaRepository;
import repository.LocalRepository;
import repository.SemestreRepository;

import static org.junit.jupiter.api.Assertions.*;

/*class GameServiceTest {

    private DiaService diaService;
    private SemestreService semestreService;
    private DisciplinaService disciplinaService;
    private LocalRepository localRepo;

    private Personagem personagem;
    private GameService gameService;

    @BeforeEach
    void setUp() {
        diaService = new DiaService();
        semestreService = new SemestreService(
                new SemestreRepository(),
                new DisciplinaRepository()
        );
        disciplinaService = new DisciplinaService(new DisciplinaRepository());
        localRepo = new LocalRepository();

        personagem = new Personagem();

        gameService = new GameService(
                diaService,
                semestreService,
                disciplinaService,
                localRepo,
                personagem
        );
    }

    // ========================
    // iniciarJogo
    // ========================

    @Test
    void deveIniciarJogoCorretamente() {
        Local ponto = new Local(
                "Ponto 1",
                new Area(10, -10, 10, -10),
                TipoLocal.PONTO_ONIBUS
        );

        localRepo.adicionarLocal(ponto);

        gameService.iniciarJogo(personagem);

        assertEquals(40.0, personagem.getEnergia());
        assertEquals(ponto, personagem.getLocalAtual());
        assertEquals(0, personagem.getcX());
        assertEquals(0, personagem.getcY());
    }

    @Test
    void deveLancarExcecaoAoIniciarJogoSemPontoDeOnibus() {
        assertThrows(IllegalStateException.class, () ->
                gameService.iniciarJogo(personagem)
        );
    }

    // ========================
    // encerrarJogo
    // ========================

    @Test
    void deveRetornarFalseSeNaoPassouEmTodasDisciplinas() {
        // cria disciplinas no sistema
        disciplinaService.criarDisciplinasPorNivel(
                "Matematica", 1, AreaConhecimento.MAT
        );
        disciplinaService.criarDisciplinasPorNivel(
                "Fisica", 1, AreaConhecimento.MAT
        );

        // personagem só passou em uma
        Disciplina matematica = new Disciplina();
        matematica.setNome("Matematica");
        matematica.setCodigo(1);

        Semestre semestre = new Semestre();
        semestre.adicionarDisciplinas(matematica);
        semestre.registrarResultado(matematica, true);

        personagem.adicionarSemestre(semestre);

        assertFalse(gameService.encerrarJogo(personagem));
    }

    @Test
    void deveRetornarTrueQuandoPassouEmTodasDisciplinas() {
        disciplinaService.criarDisciplinasPorNivel(
                "Matematica", 1, AreaConhecimento.MAT
        );
        disciplinaService.criarDisciplinasPorNivel(
                "Fisica", 1, AreaConhecimento.MAT
        );

        Disciplina matematica = new Disciplina();
        matematica.setNome("Matematica");
        matematica.setCodigo(1);

        Disciplina fisica = new Disciplina();
        fisica.setNome("Fisica");
        fisica.setCodigo(1);

        Semestre semestre1 = new Semestre();
        semestre1.adicionarDisciplinas(matematica);
        semestre1.registrarResultado(matematica, true);

        Semestre semestre2 = new Semestre();
        semestre2.adicionarDisciplinas(fisica);
        semestre2.registrarResultado(fisica, true);

        personagem.adicionarSemestre(semestre1);
        personagem.adicionarSemestre(semestre2);

        assertTrue(gameService.encerrarJogo(personagem));
    }

    @Test
    void deveRetornarFalseQuandoPersonagemNaoTemSemestres() {
        disciplinaService.criarDisciplinasPorNivel(
                "Matematica", 1, AreaConhecimento.MAT
        );

        assertFalse(gameService.encerrarJogo(personagem));
    }
}*/
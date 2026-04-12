import model.Disciplina.AreaConhecimento;
import model.Disciplina.Disciplina;
import model.Personagem;
import model.Tempo.Dia;
import model.Tempo.Semestre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.DisciplinaRepository;
import repository.SemestreRepository;
import service.SemestreService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SemestreServiceTest {

    private SemestreService service;
    private SemestreRepository semestreRepo;
    private DisciplinaRepository disciplinaRepo;

    @BeforeEach
    void setUp() {
        semestreRepo = new SemestreRepository();
        disciplinaRepo = new DisciplinaRepository();
        service = new SemestreService(semestreRepo, disciplinaRepo);
    }

    @Test
    void deveCriarSemestre() {
        Semestre semestre = service.criarSemestre();

        assertNotNull(semestre);
    }

    @Test
    void deveIniciarPrimeiroSemestreComDisciplinasIniciais() {
        Disciplina d1 = new Disciplina();
        d1.setNome("Matematica");
        d1.setCodigo(1);
        d1.setArea(AreaConhecimento.MAT);

        Disciplina d2 = new Disciplina();
        d2.setNome("Fisica");
        d2.setCodigo(1);
        d2.setArea(AreaConhecimento.MAT);

        disciplinaRepo.adicionar(d1);
        disciplinaRepo.adicionar(d2);

        Semestre semestre = service.iniciarPrimeiroSemestre(1);

        assertNotNull(semestre);
        assertEquals(2, semestre.getDisciplinas().size());
        assertTrue(semestre.getDisciplinas().contains(d1));
        assertTrue(semestre.getDisciplinas().contains(d2));
    }

    @Test
    void deveAvancarDiaQuandoSemestreNaoTerminou() {
        Semestre semestre = new Semestre();

        Dia dia = service.avancarDia(semestre);

        assertNotNull(dia);
        assertEquals(1, semestre.getDias().size());
    }

    @Test
    void deveRetornarNullAoAvancarDiaQuandoSemestreTerminou() {
        Semestre semestre = new Semestre() {
            @Override
            public boolean terminou() {
                return true;
            }
        };

        Dia dia = service.avancarDia(semestre);

        assertNull(dia);
    }

    @Test
    void deveAdicionarDisciplinaAoSemestre() {
        Semestre semestre = new Semestre();

        Disciplina disciplina = new Disciplina();
        disciplina.setNome("Quimica");
        disciplina.setCodigo(1);
        disciplina.setArea(AreaConhecimento.MAT);

        service.adicionarDisciplina(semestre, disciplina);

        assertEquals(1, semestre.getDisciplinas().size());
        assertTrue(semestre.getDisciplinas().contains(disciplina));
    }

    @Test
    void naoDeveAdicionarDisciplinaDuplicadaAoSemestre() {
        Semestre semestre = new Semestre();

        Disciplina disciplina = new Disciplina();
        disciplina.setNome("Quimica");
        disciplina.setCodigo(1);
        disciplina.setArea(AreaConhecimento.MAT);

        semestre.adicionarDisciplinas(disciplina);

        assertThrows(IllegalArgumentException.class, () ->
                service.adicionarDisciplina(semestre, disciplina)
        );
    }

    @Test
    void deveInformarQuandoSemestreTerminou() {
        Semestre semestre = new Semestre() {
            @Override
            public boolean terminou() {
                return true;
            }
        };

        assertTrue(service.terminouSemestre(semestre));
    }

    @Test
    void deveLancarExcecaoAoEncerrarSemestreNulo() {
        Personagem personagem = new Personagem();

        assertThrows(IllegalArgumentException.class, () ->
                service.encerrarSemestre(personagem, null)
        );
    }

    @Test
    void deveRetornarMesmoSemestreSeAindaNaoTerminou() {
        Personagem personagem = new Personagem();

        Semestre semestre = new Semestre() {
            @Override
            public boolean terminou() {
                return false;
            }
        };

        Semestre resultado = service.encerrarSemestre(personagem, semestre);

        assertSame(semestre, resultado);
    }

    @Test
    void deveEncerrarSemestreEAprovarParaProximaDisciplina() {
        Disciplina matematica1 = new Disciplina();
        matematica1.setNome("Matematica");
        matematica1.setCodigo(1);
        matematica1.setArea(AreaConhecimento.MAT);

        Disciplina matematica2 = new Disciplina();
        matematica2.setNome("Matematica");
        matematica2.setCodigo(2);
        matematica2.setArea(AreaConhecimento.MAT);

        disciplinaRepo.adicionar(matematica1);
        disciplinaRepo.adicionar(matematica2);

        Semestre semestre = new Semestre() {
            @Override
            public boolean terminou() {
                return true;
            }

            @Override
            public boolean foiAprovado(Disciplina d) {
                return true;
            }
        };
        semestre.setDisciplinas(new ArrayList<>(List.of(matematica1)));

        Personagem personagem = new Personagem();
        personagem.setPersonagemId(10);

        Semestre novoSemestre = service.encerrarSemestre(personagem, semestre);

        assertNotNull(novoSemestre);
        assertEquals(1, novoSemestre.getDisciplinas().size());
        assertEquals(matematica2, novoSemestre.getDisciplinas().get(0));

        assertEquals(2, semestreRepo.getSemestresPorJogador(10).size());
        assertTrue(semestreRepo.getSemestresPorJogador(10).contains(semestre));
        assertTrue(semestreRepo.getSemestresPorJogador(10).contains(novoSemestre));
    }

    @Test
    void deveManterDisciplinaQuandoReprovado() {
        Disciplina fisica1 = new Disciplina();
        fisica1.setNome("Fisica");
        fisica1.setCodigo(1);
        fisica1.setArea(AreaConhecimento.MAT);

        disciplinaRepo.adicionar(fisica1);

        Semestre semestre = new Semestre() {
            @Override
            public boolean terminou() {
                return true;
            }

            @Override
            public boolean foiAprovado(Disciplina d) {
                return false;
            }
        };
        semestre.setDisciplinas(new ArrayList<>(List.of(fisica1)));

        Personagem personagem = new Personagem();
        personagem.setPersonagemId(20);

        Semestre novoSemestre = service.encerrarSemestre(personagem, semestre);

        assertNotNull(novoSemestre);
        assertEquals(1, novoSemestre.getDisciplinas().size());
        assertEquals(fisica1, novoSemestre.getDisciplinas().get(0));

        assertEquals(2, semestreRepo.getSemestresPorJogador(20).size());
    }

    @Test
    void naoDeveAdicionarProximaDisciplinaQuandoElaNaoExiste() {
        Disciplina historia1 = new Disciplina();
        historia1.setNome("Historia");
        historia1.setCodigo(1);
        historia1.setArea(AreaConhecimento.HUM);

        disciplinaRepo.adicionar(historia1);

        Semestre semestre = new Semestre() {
            @Override
            public boolean terminou() {
                return true;
            }

            @Override
            public boolean foiAprovado(Disciplina d) {
                return true;
            }
        };
        semestre.setDisciplinas(new ArrayList<>(List.of(historia1)));

        Personagem personagem = new Personagem();
        personagem.setPersonagemId(30);

        Semestre novoSemestre = service.encerrarSemestre(personagem, semestre);

        assertNotNull(novoSemestre);
        assertTrue(novoSemestre.getDisciplinas().isEmpty());

        assertEquals(2, semestreRepo.getSemestresPorJogador(30).size());
    }
}
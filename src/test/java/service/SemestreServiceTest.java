package service;

import exception.Disciplina.DisciplinaInvalidaException;
import exception.Semestre.SemestreInvalidoException;
import model.Disciplina.AreaConhecimento;
import model.Disciplina.Disciplina;
import model.Personagem;
import model.Tempo.Dia;
import model.Tempo.Semestre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.DisciplinaRepository;
import repository.SemestreRepository;

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

    private Disciplina criarDisciplina(String nome, float codigo, AreaConhecimento area) {
        Disciplina d = new Disciplina();
        d.setNome(nome);
        d.setCodigo(codigo);
        d.setArea(area);
        return d;
    }

    // criarSemestre

    @Test
    void deveCriarSemestreComNumeroBaseadoNoHistoricoDoPersonagem() {
        Personagem personagem = new Personagem();

        Semestre semestre = service.criarSemestre(personagem);

        assertNotNull(semestre);
        assertEquals(1, semestre.getNumeroSemestre());
    }

    // iniciarSemestreComEscolha

    @Test
    void naoDeveIniciarSemestreComListaVazia() {
        Personagem personagem = new Personagem();
        personagem.setPersonagemId(1);

        assertThrows(SemestreInvalidoException.class, () ->
                service.iniciarSemestreComEscolha(1, new ArrayList<>(), personagem));
    }

    @Test
    void naoDeveIniciarSemestreComMaisDeTresDisciplinas() {
        Disciplina d1 = criarDisciplina("Matematica", 1, AreaConhecimento.MAT);
        Disciplina d2 = criarDisciplina("Naturezas", 1, AreaConhecimento.NAT);
        Disciplina d3 = criarDisciplina("Software", 1, AreaConhecimento.SOF);
        Disciplina d4 = criarDisciplina("Hardware", 1, AreaConhecimento.HAR);

        disciplinaRepo.adicionar(d1);
        disciplinaRepo.adicionar(d2);
        disciplinaRepo.adicionar(d3);
        disciplinaRepo.adicionar(d4);

        Personagem personagem = new Personagem();
        personagem.setPersonagemId(2);

        List<Disciplina> escolhidas = List.of(d1, d2, d3, d4);

        assertThrows(SemestreInvalidoException.class, () ->
                service.iniciarSemestreComEscolha(2, escolhidas, personagem));
    }

    @Test
    void naoDeveIniciarSemestreComDisciplinaIndisponivel() {
        Disciplina matematica2 = criarDisciplina("Matematica", 2, AreaConhecimento.MAT);
        disciplinaRepo.adicionar(matematica2);

        Personagem personagem = new Personagem();
        personagem.setPersonagemId(3);

        List<Disciplina> escolhidas = List.of(matematica2);

        assertThrows(DisciplinaInvalidaException.class, () ->
                service.iniciarSemestreComEscolha(3, escolhidas, personagem));
    }

    @Test
    void deveIniciarSemestreComDisciplinasDisponiveis() {
        Disciplina matematica1 = criarDisciplina("Matematica", 1, AreaConhecimento.MAT);
        Disciplina software1 = criarDisciplina("Software", 1, AreaConhecimento.SOF);

        disciplinaRepo.adicionar(matematica1);
        disciplinaRepo.adicionar(software1);

        Personagem personagem = new Personagem();
        personagem.setPersonagemId(4);

        List<Disciplina> escolhidas = List.of(matematica1, software1);

        Semestre semestre = service.iniciarSemestreComEscolha(4, escolhidas, personagem);

        assertNotNull(semestre);
        assertEquals(1, semestre.getNumeroSemestre());
        assertEquals(2, semestre.getDisciplinas().size());
        assertTrue(semestre.getDisciplinas().containsAll(escolhidas));
    }

    // getDisciplinasDisponiveis

    @Test
    void deveOfertarPrimeiroNivelParaJogadorSemHistorico() {
        Disciplina matematica1 = criarDisciplina("Matematica", 1, AreaConhecimento.MAT);
        Disciplina matematica2 = criarDisciplina("Matematica", 2, AreaConhecimento.MAT);

        disciplinaRepo.adicionar(matematica1);
        disciplinaRepo.adicionar(matematica2);

        List<Disciplina> disponiveis = service.getDisciplinasDisponiveis(5);

        assertEquals(1, disponiveis.size());
        assertEquals(matematica1, disponiveis.get(0));
    }

    // avancarDia

    @Test
    void deveAvancarDiaQuandoSemestreNaoTerminou() {
        Semestre semestre = new Semestre();

        Dia dia = service.avancarDia(semestre);

        assertNotNull(dia);
        assertEquals(1, semestre.getDias().size());
    }

    @Test
    void deveLancarExcecaoAoAvancarDiaQuandoSemestreTerminou() {
        Semestre semestre = new Semestre() {
            @Override
            public boolean terminou() {
                return true;
            }
        };

        assertThrows(SemestreInvalidoException.class, () -> service.avancarDia(semestre));
    }

    // adicionarDisciplina

    @Test
    void deveAdicionarDisciplinaAoSemestre() {
        Semestre semestre = new Semestre();

        Disciplina disciplina = criarDisciplina("Quimica", 1, AreaConhecimento.MAT);

        service.adicionarDisciplina(semestre, disciplina);

        assertEquals(1, semestre.getDisciplinas().size());
        assertTrue(semestre.getDisciplinas().contains(disciplina));
    }

    @Test
    void naoDeveAdicionarDisciplinaDuplicadaAoSemestre() {
        Semestre semestre = new Semestre();

        Disciplina disciplina = criarDisciplina("Quimica", 1, AreaConhecimento.MAT);

        semestre.adicionarDisciplinas(disciplina);

        assertThrows(SemestreInvalidoException.class, () ->
                service.adicionarDisciplina(semestre, disciplina));
    }

    // terminouSemestre

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

    // encerrarSemestre

    @Test
    void deveLancarExcecaoAoEncerrarSemestreNulo() {
        Personagem personagem = new Personagem();

        assertThrows(SemestreInvalidoException.class, () ->
                service.encerrarSemestre(personagem, null));
    }

    @Test
    void naoDeveFazerNadaSeSemestreAindaNaoTerminou() throws Exception {
        Personagem personagem = new Personagem();
        personagem.setPersonagemId(6);

        Semestre semestre = new Semestre() {
            @Override
            public boolean terminou() {
                return false;
            }
        };

        service.encerrarSemestre(personagem, semestre);

        assertTrue(semestreRepo.getSemestresPorJogador(6).isEmpty());
    }

    @Test
    void deveEncerrarSemestreEPersistirNoHistorico() throws Exception {
        Semestre semestre = new Semestre() {
            @Override
            public boolean terminou() {
                return true;
            }
        };

        Personagem personagem = new Personagem();
        personagem.setPersonagemId(7);

        service.encerrarSemestre(personagem, semestre);

        List<Semestre> semestres = semestreRepo.getSemestresPorJogador(7);
        assertEquals(1, semestres.size());
        assertTrue(semestres.contains(semestre));
    }

    // definirResultadoDisciplina

    @Test
    void deveLancarExcecaoAoDefinirResultadoComSemestreNulo() {
        Disciplina disciplina = criarDisciplina("Historia", 1, AreaConhecimento.SOF);

        assertThrows(SemestreInvalidoException.class, () ->
                service.definirResultadoDisciplina(null, disciplina, true));
    }

    @Test
    void deveLancarExcecaoAoDefinirResultadoComDisciplinaNula() {
        Semestre semestre = new Semestre();

        assertThrows(SemestreInvalidoException.class, () ->
                service.definirResultadoDisciplina(semestre, null, true));
    }

    @Test
    void deveRegistrarResultadoDaDisciplinaNoSemestre() {
        Semestre semestre = new Semestre();
        Disciplina disciplina = criarDisciplina("Historia", 1, AreaConhecimento.SOF);
        semestre.adicionarDisciplinas(disciplina);

        service.definirResultadoDisciplina(semestre, disciplina, true);

        assertTrue(semestre.foiAprovado(disciplina));
    }
}
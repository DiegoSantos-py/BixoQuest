package service;

import exception.Personagem.PersonagemInvalidoException;
import model.Disciplina.Disciplina;
import model.Local.Area;
import model.Local.Local;
import model.Local.TipoLocal;
import model.Personagem;
import model.Tempo.Semestre;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.EventoRepository;
import repository.PersonagemRepository;
import repository.SemestreRepository;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class PersonagemServiceTest {

    private PersonagemService personagemService;
    private EventoService eventoService;

    private Local localInicial;

    @BeforeEach
    void setUp() {
        eventoService = new EventoService(new EventoRepository());
        personagemService = new PersonagemService(
                new PersonagemRepository(), eventoService, new SemestreRepository());

        localInicial = new Local(
                "Ponto de ônibus",
                new Area(10, -10, 10, -10),
                TipoLocal.PONTO_ONIBUS,
                null
        );
    }

    @AfterEach
    void limpar() {
        new File("gameFiles/personagens.json").delete();
        new File("gameFiles/eventos.json").delete();
        new File("gameFiles/semestres.json").delete();
    }

    // criarPersonagem

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
                0, 1
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
        assertThrows(PersonagemInvalidoException.class, () ->
                personagemService.criarPersonagem(
                        null, 100, 80, 90, 50,
                        "sprite.png", localInicial, 0, 0, 1
                )
        );
    }

    @Test
    void deveLancarExcecaoAoCriarPersonagemComNomeEmBranco() {
        assertThrows(PersonagemInvalidoException.class, () ->
                personagemService.criarPersonagem(
                        "   ", 100, 80, 90, 50,
                        "sprite.png", localInicial, 0, 0, 1
                )
        );
    }

    @Test
    void deveLancarExcecaoAoCriarPersonagemComAtributosNegativos() {
        assertThrows(PersonagemInvalidoException.class, () ->
                personagemService.criarPersonagem(
                        "Fulano", -1, 80, 90, 50,
                        "sprite.png", localInicial, 0, 0, 1
                )
        );

        assertThrows(PersonagemInvalidoException.class, () ->
                personagemService.criarPersonagem(
                        "Fulano", 100, -1, 90, 50,
                        "sprite.png", localInicial, 0, 0, 1
                )
        );

        assertThrows(PersonagemInvalidoException.class, () ->
                personagemService.criarPersonagem(
                        "Fulano", 100, 80, -1, 50,
                        "sprite.png", localInicial, 0, 0, 1
                )
        );

        assertThrows(PersonagemInvalidoException.class, () ->
                personagemService.criarPersonagem(
                        "Fulano", 100, 80, 90, -1,
                        "sprite.png", localInicial, 0, 0, 1
                )
        );
    }

    // calcularDesempenhoGeral

    @Test
    void calcularDesempenhoGeralDeveRetornarZeroSemSemestres() {
        Personagem personagem = personagemService.criarPersonagem(
                "Fulano", 100, 80, 90, 50,
                "sprite.png", localInicial, 0, 0, 1
        );

        double desempenho = personagemService.calcularDesempenhoGeral(personagem);

        assertEquals(0.0, desempenho);
    }

    @Test
    void calcularDesempenhoGeralDeveRetornarUmQuandoTodasForemAprovadas() {
        Personagem personagem = personagemService.criarPersonagem(
                "Fulano", 100, 80, 90, 50,
                "sprite.png", localInicial, 0, 0, 1
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
                "sprite.png", localInicial, 0, 0, 1
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
                "sprite.png", localInicial, 0, 0, 1
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

    // getAtributos / getConhecimentos

    @Test
    void deveRetornarAtributosDoPersonagemCriado() throws Exception {
        Personagem personagem = personagemService.criarPersonagem(
                "Fulano", 100, 80, 90, 50,
                "sprite.png", localInicial, 0, 0, 1
        );
        personagemService.criarESalvarPersonagem(
                "Fulano", 100, 80, 90, 50, "sprite.png", localInicial, 0, 0, 1
        );

        var atributos = personagemService.getAtributos(1);

        assertEquals(100.0, atributos.get("energia"));
        assertEquals(80.0, atributos.get("motivação"));
        assertEquals(90.0, atributos.get("saúde"));
        assertEquals(50.0, atributos.get("dinheiro"));
    }
}
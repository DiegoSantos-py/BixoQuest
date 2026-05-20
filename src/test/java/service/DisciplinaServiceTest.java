package service;

import exception.Disciplina.DisciplinaDuplicadaException;
import exception.Disciplina.DisciplinaInvalidaException;
import exception.Disciplina.DisciplinaNaoEncontradaException;
import model.Disciplina.AreaConhecimento;
import model.Disciplina.Disciplina;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.DisciplinaRepository;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DisciplinaServiceTest {

    private DisciplinaRepository repo;
    private DisciplinaService service;

    @BeforeEach
    void setUp() {
        repo = new DisciplinaRepository();
        service = new DisciplinaService(repo);
    }

    @AfterEach
    void limpar() {
        new File("disciplinas.json").delete();
    }

    @Test
    void deveCriarDisciplinasPorNivel() throws Exception {
        service.criarDisciplinasPorNivel("Matematica", 3, AreaConhecimento.MAT);

        List<Disciplina> disciplinas = service.buscarPorNome("Matematica");

        assertEquals(3, disciplinas.size());
        assertEquals(1, disciplinas.get(0).getCodigo());
        assertEquals(2, disciplinas.get(1).getCodigo());
        assertEquals(3, disciplinas.get(2).getCodigo());
    }

    @Test
    void deveLancarExcecaoAoCriarDisciplinasDuplicadas() throws Exception {
        service.criarDisciplinasPorNivel("Matematica", 2, AreaConhecimento.MAT);

        assertThrows(DisciplinaDuplicadaException.class, () ->
                service.criarDisciplinasPorNivel("Matematica", 2, AreaConhecimento.MAT));
    }

    @Test
    void deveLancarExcecaoQuandoNomeForNulo() {
        assertThrows(DisciplinaInvalidaException.class, () ->
                service.criarDisciplinasPorNivel(null, 2, AreaConhecimento.MAT));
    }

    @Test
    void deveLancarExcecaoQuandoQuantidadeNiveisForInvalida() {
        assertThrows(DisciplinaInvalidaException.class, () ->
                service.criarDisciplinasPorNivel("Matematica", 0, AreaConhecimento.MAT));
    }

    @Test
    void deveLancarExcecaoQuandoAreaForNula() {
        assertThrows(DisciplinaInvalidaException.class, () ->
                service.criarDisciplinasPorNivel("Matematica", 2, null));
    }

    @Test
    void deveBuscarPorNome() throws Exception {
        service.criarDisciplinasPorNivel("Software", 2, AreaConhecimento.MAT);

        List<Disciplina> resultado = service.buscarPorNome("Software");

        assertEquals(2, resultado.size());
    }

    @Test
    void deveBuscarDisciplinaPorNomeECodigo() throws Exception {
        service.criarDisciplinasPorNivel("Quimica", 3, AreaConhecimento.MAT);

        Disciplina disciplina = service.buscar("Quimica", 2);

        assertNotNull(disciplina);
        assertEquals("Quimica", disciplina.getNome());
        assertEquals(2, disciplina.getCodigo());
    }

    @Test
    void deveLancarExcecaoAoBuscarDisciplinaInexistente() {
        assertThrows(DisciplinaNaoEncontradaException.class, () ->
                service.buscar("Hardware", 1));
    }

    @Test
    void deveCarregarDisciplinas() throws Exception {
        service.criarDisciplinasPorNivel("Quimica", 2, AreaConhecimento.NAT);

        Map<String, List<Disciplina>> mapa = service.carregarDisciplinas();

        assertTrue(mapa.containsKey("Quimica"));
        assertEquals(2, mapa.get("Quimica").size());
    }
}
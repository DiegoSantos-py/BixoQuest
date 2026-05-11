package repository;

import model.Disciplina.AreaConhecimento;
import model.Disciplina.Disciplina;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DisciplinaRepositoryTest {

    private static final File ARQUIVO = new File("disciplinas.json");
    private DisciplinaRepository repository;

    private Disciplina criarDisciplina(String nome, float codigo, AreaConhecimento area) {
        Disciplina d = new Disciplina();
        d.setNome(nome);
        d.setCodigo(codigo);
        d.setArea(area);
        return d;
    }

    @BeforeEach
    void setUp() {
        repository = new DisciplinaRepository();
        if (ARQUIVO.exists()) ARQUIVO.delete();
    }

    @AfterAll
    static void limpar() {
        if (ARQUIVO.exists()) ARQUIVO.delete();
    }

    // -------------------------------------------------------------------------
    // adicionar
    // -------------------------------------------------------------------------

    @Test
    @Order(1)
    @DisplayName("Deve adicionar disciplina válida")
    void deveAdicionarDisciplinaValida() {
        Disciplina d = criarDisciplina("Cálculo", 1.0f, AreaConhecimento.MAT);

        repository.adicionar(d);

        assertTrue(repository.existe(d));
    }

    @Test
    @Order(2)
    @DisplayName("Não deve adicionar disciplina nula")
    void naoDeveAdicionarDisciplinaNula() {
        assertThrows(IllegalArgumentException.class, () -> repository.adicionar(null));
    }

    @Test
    @Order(3)
    @DisplayName("Não deve adicionar disciplina com nome nulo")
    void naoDeveAdicionarDisciplinaComNomeNulo() {
        Disciplina d = new Disciplina();
        d.setCodigo(1.0f);

        assertThrows(IllegalArgumentException.class, () -> repository.adicionar(d));
    }

    @Test
    @Order(4)
    @DisplayName("Não deve adicionar disciplina duplicada")
    void naoDeveAdicionarDisciplinaDuplicada() {
        Disciplina d = criarDisciplina("Cálculo", 1.0f, AreaConhecimento.MAT);

        repository.adicionar(d);
        repository.adicionar(d);

        assertEquals(1, repository.buscarPorNome("Cálculo").size());
    }

    @Test
    @Order(5)
    @DisplayName("Deve adicionar disciplinas com mesmo nome e códigos diferentes")
    void deveAdicionarDisciplinasComMesmoNomeECodigosDiferentes() {
        repository.adicionar(criarDisciplina("Cálculo", 1.0f, AreaConhecimento.MAT));
        repository.adicionar(criarDisciplina("Cálculo", 2.0f, AreaConhecimento.MAT));
        repository.adicionar(criarDisciplina("Cálculo", 3.0f, AreaConhecimento.MAT));

        assertEquals(3, repository.buscarPorNome("Cálculo").size());
    }

    @Test
    @Order(6)
    @DisplayName("Deve ordenar disciplinas por código ao adicionar")
    void deveOrdenarDisciplinasPorCodigo() {
        repository.adicionar(criarDisciplina("Cálculo", 3.0f, AreaConhecimento.MAT));
        repository.adicionar(criarDisciplina("Cálculo", 1.0f, AreaConhecimento.MAT));
        repository.adicionar(criarDisciplina("Cálculo", 2.0f, AreaConhecimento.MAT));

        List<Disciplina> lista = repository.buscarPorNome("Cálculo");
        assertEquals(1.0f, lista.get(0).getCodigo());
        assertEquals(2.0f, lista.get(1).getCodigo());
        assertEquals(3.0f, lista.get(2).getCodigo());
    }

    // -------------------------------------------------------------------------
    // buscarPorNome
    // -------------------------------------------------------------------------

    @Test
    @Order(7)
    @DisplayName("Deve retornar lista vazia para nome inexistente")
    void deveRetornarListaVaziaParaNomeInexistente() {
        List<Disciplina> lista = repository.buscarPorNome("Inexistente");

        assertNotNull(lista);
        assertTrue(lista.isEmpty());
    }

    // -------------------------------------------------------------------------
    // buscarPorArea
    // -------------------------------------------------------------------------

    @Test
    @Order(8)
    @DisplayName("Deve buscar disciplinas por área")
    void deveBuscarDisciplinasPorArea() {
        repository.adicionar(criarDisciplina("Cálculo", 1.0f, AreaConhecimento.MAT));
        repository.adicionar(criarDisciplina("Álgebra", 1.0f, AreaConhecimento.MAT));
        repository.adicionar(criarDisciplina("Programação", 1.0f, AreaConhecimento.SOF));

        List<Disciplina> resultado = repository.buscarPorArea(AreaConhecimento.MAT);

        assertEquals(2, resultado.size());
    }

    @Test
    @Order(9)
    @DisplayName("Deve retornar lista vazia para área inexistente")
    void deveRetornarListaVaziaParaAreaInexistente() {
        repository.adicionar(criarDisciplina("Cálculo", 1.0f, AreaConhecimento.MAT));

        List<Disciplina> resultado = repository.buscarPorArea(AreaConhecimento.SOF);

        assertTrue(resultado.isEmpty());
    }

    // -------------------------------------------------------------------------
    // buscar
    // -------------------------------------------------------------------------

    @Test
    @Order(10)
    @DisplayName("Deve buscar disciplina por nome e código")
    void deveBuscarDisciplinaPorNomeECodigo() {
        repository.adicionar(criarDisciplina("Cálculo", 1.0f, AreaConhecimento.MAT));
        repository.adicionar(criarDisciplina("Cálculo", 2.0f, AreaConhecimento.MAT));

        Disciplina encontrada = repository.buscar("Cálculo", 2.0f);

        assertNotNull(encontrada);
        assertEquals(2.0f, encontrada.getCodigo());
    }

    @Test
    @Order(11)
    @DisplayName("Deve retornar null para disciplina inexistente")
    void deveRetornarNullParaDisciplinaInexistente() {
        assertNull(repository.buscar("Inexistente", 1.0f));
    }

    // -------------------------------------------------------------------------
    // proximaDisciplina
    // -------------------------------------------------------------------------

    @Test
    @Order(12)
    @DisplayName("Deve retornar próxima disciplina pelo código")
    void deveRetornarProximaDisciplina() {
        repository.adicionar(criarDisciplina("Cálculo", 1.0f, AreaConhecimento.MAT));
        repository.adicionar(criarDisciplina("Cálculo", 2.0f, AreaConhecimento.MAT));

        Disciplina proxima = repository.proximaDisciplina("Cálculo", 1.0f);

        assertNotNull(proxima);
        assertEquals(2.0f, proxima.getCodigo());
    }

    @Test
    @Order(13)
    @DisplayName("Deve retornar null quando não há próxima disciplina")
    void deveRetornarNullQuandoNaoHaProximaDisciplina() {
        repository.adicionar(criarDisciplina("Cálculo", 1.0f, AreaConhecimento.MAT));

        assertNull(repository.proximaDisciplina("Cálculo", 1.0f));
    }

    // -------------------------------------------------------------------------
    // buscarDisciplinasIniciais
    // -------------------------------------------------------------------------

    @Test
    @Order(14)
    @DisplayName("Deve retornar disciplinas com código 1")
    void deveRetornarDisciplinasIniciais() {
        repository.adicionar(criarDisciplina("Cálculo", 1.0f, AreaConhecimento.MAT));
        repository.adicionar(criarDisciplina("Cálculo", 2.0f, AreaConhecimento.MAT));
        repository.adicionar(criarDisciplina("Programação", 1.0f, AreaConhecimento.SOF));

        List<Disciplina> iniciais = repository.buscarDisciplinasIniciais();

        assertEquals(2, iniciais.size());
    }

    // -------------------------------------------------------------------------
    // salvar e carregar
    // -------------------------------------------------------------------------

    @Test
    @Order(15)
    @DisplayName("Deve salvar e carregar disciplinas corretamente")
    void deveSalvarECarregarDisciplinas() throws Exception {
        repository.adicionar(criarDisciplina("Cálculo", 1.0f, AreaConhecimento.MAT));
        repository.adicionar(criarDisciplina("Cálculo", 2.0f, AreaConhecimento.MAT));
        repository.adicionar(criarDisciplina("Programação", 1.0f, AreaConhecimento.SOF));
        repository.salvar();

        DisciplinaRepository novoRepository = new DisciplinaRepository();
        novoRepository.carregar();

        assertEquals(2, novoRepository.buscarPorNome("Cálculo").size());
        assertEquals(1, novoRepository.buscarPorNome("Programação").size());
        assertEquals(AreaConhecimento.SOF, novoRepository.buscar("Programação", 1.0f).getArea());
    }

    @Test
    @Order(16)
    @DisplayName("Deve preservar ordenação após carregar")
    void devePreservarOrdenacaoAposCarregar() throws Exception {
        repository.adicionar(criarDisciplina("Cálculo", 3.0f, AreaConhecimento.MAT));
        repository.adicionar(criarDisciplina("Cálculo", 1.0f, AreaConhecimento.MAT));
        repository.adicionar(criarDisciplina("Cálculo", 2.0f, AreaConhecimento.MAT));
        repository.salvar();

        DisciplinaRepository novoRepository = new DisciplinaRepository();
        novoRepository.carregar();

        List<Disciplina> lista = novoRepository.buscarPorNome("Cálculo");
        assertEquals(1.0f, lista.get(0).getCodigo());
        assertEquals(2.0f, lista.get(1).getCodigo());
        assertEquals(3.0f, lista.get(2).getCodigo());
    }

    @Test
    @Order(17)
    @DisplayName("Carregar sem arquivo não deve lançar exceção")
    void carregarSemArquivoNaoDeveLancarExcecao() {
        assertDoesNotThrow(() -> repository.carregar());
        assertTrue(repository.carregarDisciplinas().isEmpty());
    }

    @Test
    @Order(18)
    @DisplayName("Gerar JSON para inspeção manual")
    void gerarJsonParaInspecaoManual() throws Exception {
        repository.adicionar(criarDisciplina("Cálculo", 1.0f, AreaConhecimento.MAT));
        repository.adicionar(criarDisciplina("Cálculo", 2.0f, AreaConhecimento.MAT));
        repository.adicionar(criarDisciplina("Programação", 1.0f, AreaConhecimento.SOF));
        repository.salvar();

        String json = new String(java.nio.file.Files.readAllBytes(ARQUIVO.toPath()));
        System.out.println(json);

        assertTrue(ARQUIVO.exists());
    }
}
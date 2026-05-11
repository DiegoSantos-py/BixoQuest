package repository;

import model.Personagem;
import model.Local.Area;
import model.Local.Local;
import model.Local.TipoLocal;
import model.Disciplina.AreaConhecimento;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonagemRepositoryTest {

    private static final File ARQUIVO = new File("personagens.json");
    private PersonagemRepository repository;

    private Personagem criarPersonagem(String nome) {
        return new Personagem(nome, 80.0, 70.0, 90.0, 50.0, "sprites/jogador.png");
    }

    @BeforeEach
    void setUp() {
        repository = new PersonagemRepository();
        if (ARQUIVO.exists()) ARQUIVO.delete();
    }

    @AfterAll
    static void limpar() {
        if (ARQUIVO.exists()) ARQUIVO.delete();
    }

    // -------------------------------------------------------------------------
    // adicionarPersonagem
    // -------------------------------------------------------------------------

    @Test
    @Order(1)
    @DisplayName("Deve adicionar personagem válido")
    void deveAdicionarPersonagemValido() {
        Personagem p = criarPersonagem("Ana");

        repository.adicionarPersonagem(p);

        assertTrue(repository.carregarPersonagens().containsKey(p.getPersonagemId()));
    }

    @Test
    @Order(2)
    @DisplayName("Não deve adicionar personagem nulo")
    void naoDeveAdicionarPersonagemNulo() {
        repository.adicionarPersonagem(null);

        assertTrue(repository.carregarPersonagens().isEmpty());
    }

    @Test
    @Order(3)
    @DisplayName("Não deve adicionar personagem duplicado")
    void naoDeveAdicionarPersonagemDuplicado() {
        Personagem p = criarPersonagem("Ana");

        repository.adicionarPersonagem(p);
        repository.adicionarPersonagem(p);

        assertEquals(1, repository.carregarPersonagens().size());
    }

    // -------------------------------------------------------------------------
    // existePersonagem
    // -------------------------------------------------------------------------

    @Test
    @Order(4)
    @DisplayName("Deve retornar true para personagem existente")
    void deveRetornarTrueParaPersonagemExistente() {
        Personagem p = criarPersonagem("Ana");
        repository.adicionarPersonagem(p);

        assertTrue(repository.existePersonagem(p));
    }

    @Test
    @Order(5)
    @DisplayName("Deve retornar false para personagem inexistente")
    void deveRetornarFalseParaPersonagemInexistente() {
        Personagem p = criarPersonagem("Ana");

        assertFalse(repository.existePersonagem(p));
    }

    @Test
    @Order(6)
    @DisplayName("Deve retornar false para personagem nulo")
    void deveRetornarFalseParaPersonagemNulo() {
        assertFalse(repository.existePersonagem(null));
    }

    // -------------------------------------------------------------------------
    // salvar e carregar
    // -------------------------------------------------------------------------

    @Test
    @Order(7)
    @DisplayName("Deve salvar e carregar personagem corretamente")
    void deveSalvarECarregarPersonagem() throws Exception {
        Personagem p = criarPersonagem("Ana");
        repository.adicionarPersonagem(p);
        repository.salvar();

        PersonagemRepository novoRepository = new PersonagemRepository();
        novoRepository.carregar();

        Map<Integer, Personagem> personagens = novoRepository.carregarPersonagens();
        assertTrue(personagens.containsKey(p.getPersonagemId()));

        Personagem carregado = personagens.get(p.getPersonagemId());
        assertEquals("Ana", carregado.getNome());
        assertEquals(80.0, carregado.getEnergia());
        assertEquals(70.0, carregado.getMotivacao());
        assertEquals(90.0, carregado.getSaude());
        assertEquals(50.0, carregado.getDinheiro());
    }

    @Test
    @Order(8)
    @DisplayName("Deve salvar e carregar conhecimentos do personagem")
    void deveSalvarECarregarConhecimentos() throws Exception {
        Personagem p = criarPersonagem("Ana");
        p.adicionarConhecimento(AreaConhecimento.MAT, 25.0);
        repository.adicionarPersonagem(p);
        repository.salvar();

        PersonagemRepository novoRepository = new PersonagemRepository();
        novoRepository.carregar();

        Personagem carregado = novoRepository.carregarPersonagens().get(p.getPersonagemId());
        assertEquals(35.0, carregado.getConhecimento(AreaConhecimento.MAT)); // 10.0 inicial + 25.0
    }

    @Test
    @Order(9)
    @DisplayName("Deve salvar e carregar localAtualNome do personagem")
    void deveSalvarECarregarLocalAtualNome() throws Exception {
        Personagem p = criarPersonagem("Ana");

        Area area = new Area(100, 0, 100, 0);
        Local local = new Local("Vila", area, TipoLocal.CANTINA);
        p.setLocalAtual(local);

        repository.adicionarPersonagem(p);
        repository.salvar();

        PersonagemRepository novoRepository = new PersonagemRepository();
        novoRepository.carregar();

        Personagem carregado = novoRepository.carregarPersonagens().get(p.getPersonagemId());
        assertEquals("Vila", carregado.getLocalAtualNome());
        // localAtual em si é reconstruído no service — aqui só verificamos o nome
        assertNull(carregado.getLocalAtual());
    }

    @Test
    @Order(10)
    @DisplayName("Deve salvar e carregar posição do personagem")
    void deveSalvarECarregarPosicao() throws Exception {
        Personagem p = criarPersonagem("Ana");
        p.setcX(15);
        p.setcY(30);
        repository.adicionarPersonagem(p);
        repository.salvar();

        PersonagemRepository novoRepository = new PersonagemRepository();
        novoRepository.carregar();

        Personagem carregado = novoRepository.carregarPersonagens().get(p.getPersonagemId());
        assertEquals(15, carregado.getcX());
        assertEquals(30, carregado.getcY());
    }

    @Test
    @Order(11)
    @DisplayName("Carregar sem arquivo não deve lançar exceção")
    void carregarSemArquivoNaoDeveLancarExcecao() {
        assertDoesNotThrow(() -> repository.carregar());
        assertTrue(repository.carregarPersonagens().isEmpty());
    }

    @Test
    @Order(12)
    @DisplayName("Deve gerar JSON com estrutura esperada")
    void deveGerarJsonComEstruturaEsperada() throws Exception {
        Personagem p = new Personagem("Ana", 80.0, 70.0, 90.0, 50.0, "sprites/ana.png");
        p.adicionarConhecimento(AreaConhecimento.MAT, 25.0);
        p.setcX(15);
        p.setcY(30);

        repository.adicionarPersonagem(p);
        repository.salvar();

        String json = new String(java.nio.file.Files.readAllBytes(ARQUIVO.toPath()));

        // Verifica campos presentes
        assertTrue(json.contains("\"nome\" : \"Ana\""));
        assertTrue(json.contains("\"energia\" : 80.0"));
        assertTrue(json.contains("\"motivacao\" : 70.0"));
        assertTrue(json.contains("\"saude\" : 90.0"));
        assertTrue(json.contains("\"dinheiro\" : 50.0"));
        assertTrue(json.contains("\"cX\" : 15"));
        assertTrue(json.contains("\"cY\" : 30"));
        assertTrue(json.contains("\"MAT\" : 35.0"));
        assertTrue(json.contains("\"spriteDir\" : \"sprites/ana.png\""));
        assertTrue(json.contains("\"localAtualNome\" :"));
        assertTrue(json.contains("\"conhecimentosNomes\" :"));

        // Verifica campos ignorados não aparecem
        assertFalse(json.contains("\"localAtual\" :"));
        assertFalse(json.contains("\"semestres\" :"));
        assertFalse(json.contains("\"conhecimentos\" :"));
    }
}
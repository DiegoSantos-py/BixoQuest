package repository;

import model.Local.Area;
import model.Local.Direcao;
import model.Local.Local;
import model.Local.TipoLocal;
import model.Local.ZonaInterativa;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LocalRepositoryTest {

    private static final File ARQUIVO = new File("locais.json");
    private LocalRepository repository;

    // Dados reutilizáveis nos testes
    private Local criarLocal(String nome, TipoLocal tipo) {
        Area area = new Area(100, 0, 100, 0);
        Local local = new Local(nome, area, tipo);
        return local;
    }

    @BeforeEach
    void setUp() {
        repository = new LocalRepository();
        if (ARQUIVO.exists()) ARQUIVO.delete(); // garante ambiente limpo
    }

    @AfterAll
    static void limpar() {
        if (ARQUIVO.exists()) ARQUIVO.delete();
    }

    // -------------------------------------------------------------------------
    // adicionarLocal
    // -------------------------------------------------------------------------

    @Test
    @Order(1)
    @DisplayName("Deve adicionar um local válido")
    void deveAdicionarLocalValido() {
        Local colegiado = criarLocal("Colegiado", TipoLocal.COLEGIADO);

        repository.adicionarLocal(colegiado);

        Map<String, Local> locais = repository.carregarLocal();
        assertTrue(locais.containsKey("Colegiado"));
    }

    @Test
    @Order(2)
    @DisplayName("Não deve adicionar local nulo")
    void naoDeveAdicionarLocalNulo() {
        repository.adicionarLocal(null);

        assertTrue(repository.carregarLocal().isEmpty());
    }

    @Test
    @Order(3)
    @DisplayName("Não deve adicionar local com nome nulo")
    void naoDeveAdicionarLocalComNomeNulo() {
        Area area = new Area(100, 0, 100, 0);
        Local local = new Local(null, area);

        repository.adicionarLocal(local);

        assertTrue(repository.carregarLocal().isEmpty());
    }

    @Test
    @Order(4)
    @DisplayName("Não deve adicionar local duplicado")
    void naoDeveAdicionarLocalDuplicado() {
        Local colegiado1 = criarLocal("Colegiado", TipoLocal.COLEGIADO);
        Local colegiado2 = criarLocal("Colegiado", TipoLocal.COLEGIADO);

        repository.adicionarLocal(colegiado1);
        repository.adicionarLocal(colegiado2);

        assertEquals(1, repository.carregarLocal().size());
        assertEquals(TipoLocal.COLEGIADO, repository.carregarLocal().get("Colegiado").getTipo());
    }

    // -------------------------------------------------------------------------
    // buscarPorTipo
    // -------------------------------------------------------------------------

    @Test
    @Order(5)
    @DisplayName("Deve encontrar local pelo tipo")
    void deveEncontrarLocalPorTipo() {
        repository.adicionarLocal(criarLocal("Cantina", TipoLocal.CANTINA));
        repository.adicionarLocal(criarLocal("Colegiado", TipoLocal.COLEGIADO));

        Local encontrado = repository.buscarPorTipo(TipoLocal.CANTINA);

        assertNotNull(encontrado);
        assertEquals("Cantina", encontrado.getNome());
    }

    @Test
    @Order(6)
    @DisplayName("Deve retornar null quando tipo não existe")
    void deveRetornarNullQuandoTipoNaoExiste() {
        repository.adicionarLocal(criarLocal("Cantina", TipoLocal.CANTINA));

        Local encontrado = repository.buscarPorTipo(TipoLocal.COLEGIADO);

        assertNull(encontrado);
    }

    // -------------------------------------------------------------------------
    // salvar e carregar
    // -------------------------------------------------------------------------

    @Test
    @Order(7)
    @DisplayName("Deve salvar e carregar locais corretamente")
    void deveSalvarECarregarLocais() throws Exception {
        repository.adicionarLocal(criarLocal("Sala", TipoLocal.SALA));
        repository.adicionarLocal(criarLocal("Colegiado", TipoLocal.COLEGIADO));

        repository.salvar();

        LocalRepository novoRepository = new LocalRepository();
        novoRepository.carregar();

        Map<String, Local> locais = novoRepository.carregarLocal();
        assertEquals(2, locais.size());
        assertTrue(locais.containsKey("Sala"));
        assertTrue(locais.containsKey("Colegiado"));
        assertEquals(TipoLocal.SALA, locais.get("Sala").getTipo());
    }

    @Test
    @Order(8)
    @DisplayName("Deve salvar e carregar local com ZonaInterativa")
    void deveSalvarLocalComZonaInterativa() throws Exception {
        Area areaLocal = new Area(200, 0, 200, 0);
        Local cantina = new Local("Cantina", areaLocal, TipoLocal.CANTINA);

        Area areaZona = new Area(50, 10, 50, 10);
        ZonaInterativa zona = new ZonaInterativa(areaZona, "Mercado");
        cantina.getZonaInterativasDisponiveis().add(zona);

        repository.adicionarLocal(cantina);
        repository.salvar();

        LocalRepository novoRepository = new LocalRepository();
        novoRepository.carregar();

        Local carregado = novoRepository.carregarLocal().get("Cantina");
        assertNotNull(carregado);
        assertEquals(1, carregado.getZonaInterativasDisponiveis().size());
        assertEquals("Mercado", carregado.getZonaInterativasDisponiveis().get(0).getNome());
    }

    @Test
    @Order(9)
    @DisplayName("Deve reconstruir vizinhos após carregar")
    void deveReconstruirVizinhosAposCarregar() throws Exception {
        Local colegiado = criarLocal("Colegiado", TipoLocal.COLEGIADO);
        Local sala  = criarLocal("Sala",  TipoLocal.SALA);

        colegiado.adicionarVizinho(Direcao.CIMA, sala);

        repository.adicionarLocal(colegiado);
        repository.adicionarLocal(sala);
        repository.salvar();

        LocalRepository novoRepository = new LocalRepository();
        novoRepository.carregar();

        Local colegiadoCar = novoRepository.carregarLocal().get("Colegiado");
        Local vizinho = colegiadoCar.getVizinho(Direcao.CIMA);

        assertNotNull(vizinho);
        assertEquals("Sala", vizinho.getNome());
    }

    @Test
    @Order(10)
    @DisplayName("Carregar sem arquivo não deve lançar exceção")
    void carregarSemArquivoNaoDeveLancarExcecao() {
        assertDoesNotThrow(() -> repository.carregar());
        assertTrue(repository.carregarLocal().isEmpty());
    }


    @Test
    @Order(11)
    @DisplayName("Gerar arquivo para inspeção manual")
    void gerarArquivoParaInspecao() throws Exception {
        Local sala = criarLocal("Sala", TipoLocal.SALA);
        Local cantina  = criarLocal("cantina",  TipoLocal.CANTINA);
        sala.adicionarVizinho(Direcao.CIMA, cantina);

        repository.adicionarLocal(sala);
        repository.adicionarLocal(cantina);
        repository.salvar();

        String json = new String(java.nio.file.Files.readAllBytes(ARQUIVO.toPath()));
        System.out.println(json);
        // sem assert — só para gerar o arquivo
        assertTrue(ARQUIVO.exists());
    }
}

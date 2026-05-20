package repository;

import exception.Evento.EventoDuplicadoException;
import exception.Evento.EventoInvalidoException;
import exception.Evento.EventoNaoEncontradoException;
import model.Disciplina.AreaConhecimento;
import model.Evento.Evento;
import model.Evento.EventoAleatorio;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EventoRepositoryTest {

    private static final File ARQUIVO = new File("eventos.json");
    private EventoRepository repository;

    @BeforeEach
    void setUp() {
        repository = new EventoRepository();
        if (ARQUIVO.exists()) ARQUIVO.delete();
    }

    @AfterAll
    static void limpar() {
        if (ARQUIVO.exists()) ARQUIVO.delete();
    }

    // adicionarEvento

    @Test
    @Order(1)
    @DisplayName("Deve adicionar evento válido")
    void deveAdicionarEventoValido() {
        Evento e = new Evento("Prova de Cálculo", "Uma prova difícil");

        repository.adicionarEvento(e);

        assertTrue(repository.carregarEventos().containsKey("Prova de Cálculo"));
    }

    @Test
    @Order(2)
    @DisplayName("Não deve adicionar evento nulo")
    void naoDeveAdicionarEventoNulo() {
        assertThrows(EventoInvalidoException.class, () -> repository.adicionarEvento(null));
    }

    @Test
    @Order(3)
    @DisplayName("Não deve adicionar evento com nome nulo")
    void naoDeveAdicionarEventoComNomeNulo() {
        Evento e = new Evento();

        assertThrows(EventoInvalidoException.class, () -> repository.adicionarEvento(e));
    }

    @Test
    @Order(4)
    @DisplayName("Não deve adicionar evento duplicado")
    void naoDeveAdicionarEventoDuplicado() {
        Evento e1 = new Evento("Prova de Cálculo", "Uma prova difícil");
        Evento e2 = new Evento("Prova de Cálculo", "Descrição diferente");

        repository.adicionarEvento(e1);

        assertThrows(EventoDuplicadoException.class, () -> repository.adicionarEvento(e2));
    }

    // buscarPorNome

    @Test
    @Order(5)
    @DisplayName("Deve encontrar evento pelo nome")
    void deveEncontrarEventoPeloNome() {
        repository.adicionarEvento(new Evento("Almoço no RU", "Refeição no restaurante"));

        Evento encontrado = repository.buscarPorNome("Almoço no RU");

        assertNotNull(encontrado);
        assertEquals("Almoço no RU", encontrado.getNome());
    }

    @Test
    @Order(6)
    @DisplayName("Deve lançar exceção para evento inexistente")
    void deveLancarExcecaoParaEventoInexistente() {
        assertThrows(EventoNaoEncontradoException.class,
                () -> repository.buscarPorNome("Evento Inexistente"));
    }

    // salvar e carregar

    @Test
    @Order(7)
    @DisplayName("Deve salvar e carregar evento simples")
    void deveSalvarECarregarEventoSimples() throws Exception {
        Evento e = new Evento("Almoço no RU", "Refeição no restaurante");
        e.setEfeitoEnergia(20.0);
        e.setEfeitoDinheiro(-15.0);

        repository.adicionarEvento(e);
        repository.salvar();

        EventoRepository novoRepository = new EventoRepository();
        novoRepository.carregar();

        Evento carregado = novoRepository.buscarPorNome("Almoço no RU");
        assertNotNull(carregado);
        assertEquals("Almoço no RU", carregado.getNome());
        assertEquals(20.0, carregado.getEfeitoEnergia());
        assertEquals(-15.0, carregado.getEfeitoDinheiro());
    }

    @Test
    @Order(8)
    @DisplayName("Deve salvar e carregar EventoAleatorio")
    void deveSalvarECarregarEventoAleatorio() throws Exception {
        EventoAleatorio e = new EventoAleatorio("Chuva Surpresa", "Começou a chover", 0.3);

        repository.adicionarEvento(e);
        repository.salvar();

        EventoRepository novoRepository = new EventoRepository();
        novoRepository.carregar();

        Evento carregado = novoRepository.buscarPorNome("Chuva Surpresa");
        assertNotNull(carregado);
        assertInstanceOf(EventoAleatorio.class, carregado);
        assertEquals(0.3, ((EventoAleatorio) carregado).getChanceAtivacao());
    }

    @Test
    @Order(9)
    @DisplayName("Deve salvar e carregar efeitoConhecimento")
    void deveSalvarECarregarEfeitoConhecimento() throws Exception {
        Evento e = new Evento("Aula de Matemática", "Uma aula produtiva");
        Map<AreaConhecimento, Double> efeitos = new HashMap<>();
        efeitos.put(AreaConhecimento.MAT, 15.0);
        efeitos.put(AreaConhecimento.SOF, 5.0);
        e.setEfeitoConhecimento(efeitos);

        repository.adicionarEvento(e);
        repository.salvar();

        EventoRepository novoRepository = new EventoRepository();
        novoRepository.carregar();

        Evento carregado = novoRepository.buscarPorNome("Aula de Matemática");
        assertNotNull(carregado.getEfeitosConhecimento());
        assertEquals(15.0, carregado.getEfeitosConhecimento().get(AreaConhecimento.MAT));
        assertEquals(5.0, carregado.getEfeitosConhecimento().get(AreaConhecimento.SOF));
    }

    @Test
    @Order(10)
    @DisplayName("Deve reconstruir eventoRequisito após carregar")
    void deveReconstruirEventoRequisitoAposCarregar() throws Exception {
        Evento requisito = new Evento("Inscrição na Prova", "Pré-requisito");
        Evento prova = new Evento("Prova Final", "A prova final");
        prova.setEventoRequisito(requisito);

        repository.adicionarEvento(requisito);
        repository.adicionarEvento(prova);
        repository.salvar();

        EventoRepository novoRepository = new EventoRepository();
        novoRepository.carregar();

        Evento carregado = novoRepository.buscarPorNome("Prova Final");
        assertNotNull(carregado.getEventoRequisito());
        assertEquals("Inscrição na Prova", carregado.getEventoRequisito().getNome());
    }

    @Test
    @Order(11)
    @DisplayName("Carregar sem arquivo não deve lançar exceção")
    void carregarSemArquivoNaoDeveLancarExcecao() {
        assertDoesNotThrow(() -> repository.carregar());
        assertTrue(repository.carregarEventos().isEmpty());
    }

    @Test
    @Order(12)
    @DisplayName("Gerar JSON para inspeção manual")
    void gerarJsonParaInspecaoManual() throws Exception {
        Evento e1 = new Evento("Almoço no RU", "Refeição no restaurante");
        e1.setEfeitoEnergia(20.0);
        e1.setEfeitoDinheiro(-15.0);

        EventoAleatorio e2 = new EventoAleatorio("Chuva Surpresa", "Começou a chover", 0.3);

        Map<AreaConhecimento, Double> efeitos = new HashMap<>();
        efeitos.put(AreaConhecimento.MAT, 15.0);
        Evento e3 = new Evento("Aula de Matemática", "Uma aula produtiva");
        e3.setEfeitoConhecimento(efeitos);
        e3.setEventoRequisito(e1);

        repository.adicionarEvento(e1);
        repository.adicionarEvento(e2);
        repository.adicionarEvento(e3);
        repository.salvar();

        String json = new String(java.nio.file.Files.readAllBytes(ARQUIVO.toPath()));
        System.out.println(json);

        assertTrue(ARQUIVO.exists());
    }
}
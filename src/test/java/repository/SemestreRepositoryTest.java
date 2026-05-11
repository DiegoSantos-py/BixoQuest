package repository;

import model.Disciplina.AreaConhecimento;
import model.Disciplina.Disciplina;
import model.Evento.Evento;
import model.Evento.EventoAleatorio;
import model.Tempo.Dia;
import model.Tempo.Semestre;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SemestreRepositoryTest {

    private static final File ARQUIVO = new File("semestres.json");
    private SemestreRepository repository;

    private Disciplina criarDisciplina(String nome, float codigo) {
        Disciplina d = new Disciplina();
        d.setNome(nome);
        d.setCodigo(codigo);
        d.setArea(AreaConhecimento.MAT);
        return d;
    }

    @BeforeEach
    void setUp() {
        repository = new SemestreRepository();
        if (ARQUIVO.exists()) ARQUIVO.delete();
    }

    @AfterAll
    static void limpar() {
        if (ARQUIVO.exists()) ARQUIVO.delete();
    }

    // -------------------------------------------------------------------------
    // adicionarSemestre
    // -------------------------------------------------------------------------

    @Test
    @Order(1)
    @DisplayName("Deve adicionar semestre válido")
    void deveAdicionarSemestreValido() {
        Semestre s = new Semestre();

        repository.adicionarSemestre(1, s);

        assertEquals(1, repository.getSemestresPorJogador(1).size());
    }

    @Test
    @Order(2)
    @DisplayName("Não deve adicionar semestre nulo")
    void naoDeveAdicionarSemestreNulo() {
        assertThrows(IllegalArgumentException.class, () -> repository.adicionarSemestre(1, null));
    }

    @Test
    @Order(3)
    @DisplayName("Deve adicionar múltiplos semestres para o mesmo jogador")
    void deveAdicionarMultiplosSemestres() {
        repository.adicionarSemestre(1, new Semestre());
        repository.adicionarSemestre(1, new Semestre());
        repository.adicionarSemestre(1, new Semestre());

        assertEquals(3, repository.getSemestresPorJogador(1).size());
    }

    @Test
    @Order(4)
    @DisplayName("Deve manter semestres separados por jogador")
    void deveManterSemestresSeparadosPorJogador() {
        repository.adicionarSemestre(1, new Semestre());
        repository.adicionarSemestre(1, new Semestre());
        repository.adicionarSemestre(2, new Semestre());

        assertEquals(2, repository.getSemestresPorJogador(1).size());
        assertEquals(1, repository.getSemestresPorJogador(2).size());
    }

    // -------------------------------------------------------------------------
    // getSemestresPorJogador
    // -------------------------------------------------------------------------

    @Test
    @Order(5)
    @DisplayName("Deve retornar lista vazia para jogador inexistente")
    void deveRetornarListaVaziaParaJogadorInexistente() {
        List<Semestre> lista = repository.getSemestresPorJogador(99);

        assertNotNull(lista);
        assertTrue(lista.isEmpty());
    }

    // -------------------------------------------------------------------------
    // salvar e carregar
    // -------------------------------------------------------------------------

    @Test
    @Order(6)
    @DisplayName("Deve salvar e carregar semestres corretamente")
    void deveSalvarECarregarSemestres() throws Exception {
        Semestre s1 = new Semestre();
        Semestre s2 = new Semestre();

        repository.adicionarSemestre(1, s1);
        repository.adicionarSemestre(1, s2);
        repository.adicionarSemestre(2, new Semestre());
        repository.salvar();

        SemestreRepository novoRepository = new SemestreRepository();
        novoRepository.carregar();

        assertEquals(2, novoRepository.getSemestresPorJogador(1).size());
        assertEquals(1, novoRepository.getSemestresPorJogador(2).size());
    }

    @Test
    @Order(7)
    @DisplayName("Deve salvar e carregar disciplinas do semestre")
    void deveSalvarECarregarDisciplinas() throws Exception {
        Semestre s = new Semestre();
        s.adicionarDisciplinas(criarDisciplina("Cálculo", 1.0f));
        s.adicionarDisciplinas(criarDisciplina("Programação", 2.0f));

        repository.adicionarSemestre(1, s);
        repository.salvar();

        SemestreRepository novoRepository = new SemestreRepository();
        novoRepository.carregar();

        Semestre carregado = novoRepository.getSemestresPorJogador(1).get(0);
        assertEquals(2, carregado.getDisciplinas().size());
        assertEquals("Cálculo", carregado.getDisciplinas().get(0).getNome());
    }

    @Test
    @Order(8)
    @DisplayName("Deve salvar e carregar resultadosNomes")
    void deveSalvarECarregarResultadosNomes() throws Exception {
        Semestre s = new Semestre();
        Disciplina d = criarDisciplina("Cálculo", 1.0f);
        s.adicionarDisciplinas(d);
        s.registrarResultado(d, true);

        repository.adicionarSemestre(1, s);
        repository.salvar();

        SemestreRepository novoRepository = new SemestreRepository();
        novoRepository.carregar();

        Semestre carregado = novoRepository.getSemestresPorJogador(1).get(0);
        assertTrue(carregado.getResultadosNomes().containsKey("Cálculo:1.0"));
        assertTrue(carregado.getResultadosNomes().get("Cálculo:1.0"));
    }

    @Test
    @Order(9)
    @DisplayName("Deve salvar e carregar dias do semestre")
    void deveSalvarECarregarDias() throws Exception {
        Semestre s = new Semestre();
        s.adicionarDia(new Dia());
        s.adicionarDia(new Dia());

        repository.adicionarSemestre(1, s);
        repository.salvar();

        SemestreRepository novoRepository = new SemestreRepository();
        novoRepository.carregar();

        Semestre carregado = novoRepository.getSemestresPorJogador(1).get(0);
        assertEquals(2, carregado.getDias().size());
    }

    @Test
    @Order(10)
    @DisplayName("Deve salvar e carregar nomes de eventos do dia")
    void deveSalvarECarregarNomesDeEventosDoDia() throws Exception {
        Semestre s = new Semestre();
        Dia dia = new Dia();

        Evento obrigatorio = new Evento("Prova de Cálculo", "Uma prova");
        EventoAleatorio aleatorio = new EventoAleatorio("Chuva Surpresa", "Choveu", 0.3);

        dia.addEventosObrigatorios(java.util.Map.of("Prova de Cálculo", obrigatorio));
        dia.addEventosAleatorios(java.util.Map.of("Chuva Surpresa", aleatorio));
        s.adicionarDia(dia);

        repository.adicionarSemestre(1, s);
        repository.salvar();

        SemestreRepository novoRepository = new SemestreRepository();
        novoRepository.carregar();

        Dia carregado = novoRepository.getSemestresPorJogador(1).get(0).getDias().get(0);
        assertTrue(carregado.getEventosObrigatoriosNomes().contains("Prova de Cálculo"));
        assertTrue(carregado.getEventosAleatoriosNomes().contains("Chuva Surpresa"));
        // objetos Evento são reconstruídos no service — aqui só verificamos os nomes
        assertTrue(carregado.getEventosObrigatorios().isEmpty());
        assertTrue(carregado.getEventosAleatorios().isEmpty());
    }

    @Test
    @Order(11)
    @DisplayName("Carregar sem arquivo não deve lançar exceção")
    void carregarSemArquivoNaoDeveLancarExcecao() {
        assertDoesNotThrow(() -> repository.carregar());
        assertTrue(repository.carregarSemestres().isEmpty());
    }

    @Test
    @Order(12)
    @DisplayName("Gerar JSON para inspeção manual")
    void gerarJsonParaInspecaoManual() throws Exception {
        Semestre s = new Semestre();
        Disciplina d = criarDisciplina("Cálculo", 1.0f);
        s.adicionarDisciplinas(d);
        s.registrarResultado(d, true);

        Dia dia = new Dia();
        Evento obrigatorio = new Evento("Prova de Cálculo", "Uma prova");
        dia.addEventosObrigatorios(java.util.Map.of("Prova de Cálculo", obrigatorio));
        s.adicionarDia(dia);

        repository.adicionarSemestre(1, s);
        repository.salvar();

        String json = new String(java.nio.file.Files.readAllBytes(ARQUIVO.toPath()));
        System.out.println(json);

        assertTrue(ARQUIVO.exists());
    }
}
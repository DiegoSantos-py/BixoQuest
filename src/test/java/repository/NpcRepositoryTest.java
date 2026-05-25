package repository;

import exception.Npc.NpcDuplicadoException;
import exception.Npc.NpcInvalidoException;
import exception.Npc.NpcNaoEncontradoException;
import model.Disciplina.AreaConhecimento;
import model.Npc.Animal;
import model.Npc.Colega;
import model.Npc.Especie;
import model.Npc.Npc;
import model.Npc.Professor;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NpcRepositoryTest {

    private static final File ARQUIVO = new File("npcs.json");
    private NpcRepository repository;

    private Professor criarProfessor(String nome) {
        return new Professor(nome, "sprites/professor.png", 0, 0,
                new ArrayList<>(List.of("Bom dia!", "Estudem muito!")),
                AreaConhecimento.MAT);
    }

    private Colega criarColega(String nome) {
        return new Colega(nome, "sprites/colega.png", 0, 0,
                new ArrayList<>(List.of("E aí!", "Vamos estudar?")),
                AreaConhecimento.SOF, 5.0);
    }

    private Animal criarAnimal(String nome) {
        return new Animal(nome, "sprites/animal.png", 0, 0,
                new ArrayList<>(List.of("*rosna*", "*abana o rabo*")),
                Especie.CACHORRO, 8);
    }

    @BeforeEach
    void setUp() {
        repository = new NpcRepository();
        if (ARQUIVO.exists()) ARQUIVO.delete();
    }

    @AfterAll
    static void limpar() {
        if (ARQUIVO.exists()) ARQUIVO.delete();
    }

    // adicionarNpc

    @Test
    @Order(1)
    @DisplayName("Deve adicionar professor válido")
    void deveAdicionarProfessorValido() {
        Professor p = criarProfessor("Prof. Silva");

        repository.adicionarNpc(p);

        assertTrue(repository.carregarNpcs().containsKey("Prof. Silva"));
    }

    @Test
    @Order(2)
    @DisplayName("Deve adicionar colega válido")
    void deveAdicionarColegaValido() {
        Colega c = criarColega("João");

        repository.adicionarNpc(c);

        assertTrue(repository.carregarNpcs().containsKey("João"));
    }

    @Test
    @Order(3)
    @DisplayName("Deve adicionar animal válido")
    void deveAdicionarAnimalValido() {
        Animal a = criarAnimal("Bidu");

        repository.adicionarNpc(a);

        assertTrue(repository.carregarNpcs().containsKey("Bidu"));
    }

    @Test
    @Order(4)
    @DisplayName("Não deve adicionar npc nulo")
    void naoDeveAdicionarNpcNulo() {
        assertThrows(NpcInvalidoException.class, () -> repository.adicionarNpc(null));
    }

    @Test
    @Order(5)
    @DisplayName("Não deve adicionar npc com nome nulo")
    void naoDeveAdicionarNpcComNomeNulo() {
        Professor p = criarProfessor(null);

        assertThrows(NpcInvalidoException.class, () -> repository.adicionarNpc(p));
    }

    @Test
    @Order(6)
    @DisplayName("Não deve adicionar npc duplicado")
    void naoDeveAdicionarNpcDuplicado() {
        Professor p1 = criarProfessor("Prof. Silva");
        Professor p2 = criarProfessor("Prof. Silva");

        repository.adicionarNpc(p1);

        assertThrows(NpcDuplicadoException.class, () -> repository.adicionarNpc(p2));
    }

    // buscarPorNome

    @Test
    @Order(7)
    @DisplayName("Deve buscar npc pelo nome")
    void deveBuscarNpcPeloNome() {
        repository.adicionarNpc(criarProfessor("Prof. Silva"));

        Npc encontrado = repository.buscarPorNome("Prof. Silva");

        assertNotNull(encontrado);
        assertEquals("Prof. Silva", encontrado.getNome());
    }

    @Test
    @Order(8)
    @DisplayName("Deve lançar exceção para npc inexistente")
    void deveLancarExcecaoParaNpcInexistente() {
        assertThrows(NpcNaoEncontradoException.class,
                () -> repository.buscarPorNome("Inexistente"));
    }

    // buscarPorSubtipo

    @Test
    @Order(9)
    @DisplayName("Deve retornar apenas professores")
    void deveRetornarApenasProfessores() {
        repository.adicionarNpc(criarProfessor("Prof. Silva"));
        repository.adicionarNpc(criarProfessor("Prof. Costa"));
        repository.adicionarNpc(criarColega("João"));
        repository.adicionarNpc(criarAnimal("Bidu"));

        List<Professor> professores = repository.buscarProfessores();

        assertEquals(2, professores.size());
        assertTrue(professores.stream().allMatch(p -> p instanceof Professor));
    }

    @Test
    @Order(10)
    @DisplayName("Deve retornar apenas colegas")
    void deveRetornarApenasColegas() {
        repository.adicionarNpc(criarColega("João"));
        repository.adicionarNpc(criarColega("Maria"));
        repository.adicionarNpc(criarProfessor("Prof. Silva"));

        List<Colega> colegas = repository.buscarColegas();

        assertEquals(2, colegas.size());
        assertTrue(colegas.stream().allMatch(c -> c instanceof Colega));
    }

    @Test
    @Order(11)
    @DisplayName("Deve retornar apenas animais")
    void deveRetornarApenasAnimais() {
        repository.adicionarNpc(criarAnimal("Bidu"));
        repository.adicionarNpc(criarAnimal("Miau"));
        repository.adicionarNpc(criarColega("João"));

        List<Animal> animais = repository.buscarAnimais();

        assertEquals(2, animais.size());
        assertTrue(animais.stream().allMatch(a -> a instanceof Animal));
    }

    // salvar e carregar

    @Test
    @Order(12)
    @DisplayName("Deve salvar e carregar professor com subtipo correto")
    void deveSalvarECarregarProfessor() throws Exception {
        repository.adicionarNpc(criarProfessor("Prof. Silva"));
        repository.salvar();

        NpcRepository novoRepository = new NpcRepository();
        novoRepository.carregar();

        Npc carregado = novoRepository.buscarPorNome("Prof. Silva");
        assertInstanceOf(Professor.class, carregado);
        assertEquals(AreaConhecimento.MAT, ((Professor) carregado).getAreaConhecimento());
    }

    @Test
    @Order(13)
    @DisplayName("Deve salvar e carregar colega com subtipo correto")
    void deveSalvarECarregarColega() throws Exception {
        repository.adicionarNpc(criarColega("João"));
        repository.salvar();

        NpcRepository novoRepository = new NpcRepository();
        novoRepository.carregar();

        Npc carregado = novoRepository.buscarPorNome("João");
        assertInstanceOf(Colega.class, carregado);
        assertEquals(5.0, ((Colega) carregado).getConhecimentoNpc());
        assertEquals(AreaConhecimento.SOF, ((Colega) carregado).getArea());
    }

    @Test
    @Order(14)
    @DisplayName("Deve salvar e carregar animal com subtipo correto")
    void deveSalvarECarregarAnimal() throws Exception {
        repository.adicionarNpc(criarAnimal("Bidu"));
        repository.salvar();

        NpcRepository novoRepository = new NpcRepository();
        novoRepository.carregar();

        Npc carregado = novoRepository.buscarPorNome("Bidu");
        assertInstanceOf(Animal.class, carregado);
        assertEquals(Especie.CACHORRO, ((Animal) carregado).getEspecie());
        assertEquals(8, ((Animal) carregado).getIndole());
    }

    @Test
    @Order(15)
    @DisplayName("Deve salvar e carregar múltiplos subtipos juntos")
    void deveSalvarECarregarMultiplosSubtipos() throws Exception {
        repository.adicionarNpc(criarProfessor("Prof. Silva"));
        repository.adicionarNpc(criarColega("João"));
        repository.adicionarNpc(criarAnimal("Bidu"));
        repository.salvar();

        NpcRepository novoRepository = new NpcRepository();
        novoRepository.carregar();

        assertEquals(1, novoRepository.buscarProfessores().size());
        assertEquals(1, novoRepository.buscarColegas().size());
        assertEquals(1, novoRepository.buscarAnimais().size());
    }

    @Test
    @Order(16)
    @DisplayName("Deve preservar falas após carregar")
    void devePreservarFalasAposCarregar() throws Exception {
        repository.adicionarNpc(criarProfessor("Prof. Silva"));
        repository.salvar();

        NpcRepository novoRepository = new NpcRepository();
        novoRepository.carregar();

        Npc carregado = novoRepository.buscarPorNome("Prof. Silva");
        assertNotNull(carregado.getFalas());
        assertEquals(2, carregado.getFalas().size());
        assertTrue(carregado.getFalas().contains("Bom dia!"));
    }

    @Test
    @Order(17)
    @DisplayName("Carregar sem arquivo não deve lançar exceção")
    void carregarSemArquivoNaoDeveLancarExcecao() {
        assertDoesNotThrow(() -> repository.carregar());
        assertTrue(repository.carregarNpcs().isEmpty());
    }

    @Test
    @Order(18)
    @DisplayName("Gerar JSON para inspeção manual")
    void gerarJsonParaInspecaoManual() throws Exception {
        repository.adicionarNpc(criarProfessor("Prof. Silva"));
        repository.adicionarNpc(criarColega("João"));
        repository.adicionarNpc(criarAnimal("Bidu"));
        repository.salvar();

        String json = new String(java.nio.file.Files.readAllBytes(ARQUIVO.toPath()));
        System.out.println(json);

        assertTrue(ARQUIVO.exists());
    }

    // atualizarNpc

    @Test
    @Order(19)
    @DisplayName("Deve atualizar npc existente")
    void deveAtualizarNpcExistente() {
        Animal animal = criarAnimal("Bidu");
        repository.adicionarNpc(animal);

        // Altera o estado do animal (e.g. domado, indole)
        animal.setDomado(true);
        animal.setIndole(15);

        repository.atualizarNpc(animal);

        Animal animalAtualizado = (Animal) repository.buscarPorNome("Bidu");
        assertTrue(animalAtualizado.isDomado());
        assertEquals(15, animalAtualizado.getIndole());
    }

    @Test
    @Order(20)
    @DisplayName("Não deve atualizar npc inexistente")
    void naoDeveAtualizarNpcInexistente() {
        Animal animal = criarAnimal("Desconhecido");

        assertThrows(NpcNaoEncontradoException.class, () -> repository.atualizarNpc(animal));
    }

    @Test
    @Order(21)
    @DisplayName("Não deve atualizar npc nulo")
    void naoDeveAtualizarNpcNulo() {
        assertThrows(NpcInvalidoException.class, () -> repository.atualizarNpc(null));
    }
}
package repository;

import model.Evento.Prova.ResultadoProva;
import model.Personagem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ResultadoProvaRepositoryTest {

    private ResultadoProvaRepository repository;
    private static final File arquivoJson = new File("resultadoProvas.json");

    @BeforeEach
    public void setUp() {
        repository = new ResultadoProvaRepository();
        // limpa o arquivo de salvamento antes de cada teste
        if (arquivoJson.exists()) {
            arquivoJson.delete();
        }
    }

    @AfterEach
    public void tearDown() {
        // limpa o arquivo no final
        if (arquivoJson.exists()) {
            arquivoJson.delete();
        }
    }

    @Test
    public void testAdicionarEBuscarResultadoProva() {
        Personagem personagem = new Personagem("Teste", 100, 100, 100, 100, "sprite.png", 1);
        ResultadoProva resultado1 = new ResultadoProva(personagem, null, 8.5f, 5, true, false, false);
        ResultadoProva resultado2 = new ResultadoProva(personagem, null, 9.0f, 3, false, true, false);

        int jogadorId = personagem.getPersonagemId();
        int semestreNumero = 1;

        // um jogador que nao fez provas não deve conter provas...
        assertTrue(repository.getResultadosProvaPorJogador(jogadorId).isEmpty());

        // adiciona ao repositório passando o semestre
        repository.adicionarResultadoProva(jogadorId, semestreNumero, resultado1);
        repository.adicionarResultadoProva(jogadorId, semestreNumero, resultado2);

        // puxa os semestres do jogador
        Map<Integer, List<ResultadoProva>> semestres = repository.getResultadosProvaPorJogador(jogadorId);

        // puxa a lista específica do semestre 1
        List<ResultadoProva> resultadosSemestre1 = semestres.get(semestreNumero);

        // Verifica a persistência
        assertNotNull(resultadosSemestre1, "A lista do semestre 1 não deveria ser nula");
        assertEquals(2, resultadosSemestre1.size());
        assertEquals(8.5f, resultadosSemestre1.get(0).getNotaFinal());
        assertEquals(9f, resultadosSemestre1.get(1).getNotaFinal());
    }

    //  Valida se a estrutura realmente separa por semestres
    @Test
    public void testAdicionarEmMultiplosSemestres() {
        Personagem personagem = new Personagem("TesteMultiplo", 100, 100, 100, 100, "sprite.png", 2);
        ResultadoProva resultadoSem1 = new ResultadoProva(personagem, "NAT-02", 7.0f, 2, true, false, false);
        ResultadoProva resultadoSem2 = new ResultadoProva(personagem, "HUM-01", 9.5f, 1, false, false, true);

        int jogadorId = personagem.getPersonagemId();

        // Adiciona em semestres diferentes
        repository.adicionarResultadoProva(jogadorId, 1, resultadoSem1);
        repository.adicionarResultadoProva(jogadorId, 2, resultadoSem2);

        Map<Integer, List<ResultadoProva>> semestres = repository.getResultadosProvaPorJogador(jogadorId);

        // O jogador deve ter provas registradas em 2 semestres distintos
        assertEquals(2, semestres.size());

        //verica se o semestre esta indo para o local certo com a nota corrreta
        assertEquals(7.0f, semestres.get(1).get(0).getNotaFinal());
        assertEquals(9.5f, semestres.get(2).get(0).getNotaFinal());
    }

    @Test
    public void testAdicionarResultadoNuloLancaExcecao() {
        int jogadorId = 1;
        int semestreNumero = 1;

        Exception exception = assertThrows(exception.Evento.Prova.ResultadoProvaInvalidoException.class, () -> {
            repository.adicionarResultadoProva(jogadorId, semestreNumero, null);
        });

        assertEquals("Resultado Prova Inválido — campo 'resultadoProva': não pode ser nulo", exception.getMessage());
    }

    @Test
    public void testSalvarECarregar() throws Exception {
        Personagem personagem = new Personagem("SalvarTeste", 100, 100, 100, 100, "sprite.png", 3);
        ResultadoProva resultado = new ResultadoProva(personagem, null, 10.0f, 2, true, false, false);

        int jogadorId = 3;
        int semestreNumero = 3;

        repository.adicionarResultadoProva(jogadorId, semestreNumero, resultado);

        // Salva os dados no arquivo JSON
        repository.salvar();
        assertTrue(arquivoJson.exists(), "O arquivo JSON deveria ter sido criado.");

        // Simula reiniciar o jogo: cria um NOVO repositório e carrega os dados
        ResultadoProvaRepository novoRepository = new ResultadoProvaRepository();
        novoRepository.carregar();

        Map<Integer, List<ResultadoProva>> semestresCarregados = novoRepository.getResultadosProvaPorJogador(jogadorId);
        List<ResultadoProva> provasSemestre3 = semestresCarregados.get(semestreNumero);

        // Verifica se carregou corretamente do arquivo
        assertNotNull(provasSemestre3, "A lista de provas carregada não deveria ser nula.");
        assertFalse(provasSemestre3.isEmpty(), "A lista não deveria estar vazia após o carregamento.");
        assertEquals(1, provasSemestre3.size());
        assertEquals(10.0f, provasSemestre3.get(0).getNotaFinal());
    }
}
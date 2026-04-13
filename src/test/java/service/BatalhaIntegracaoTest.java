package service;

import model.EstadoBatalha;
import model.Oponente;
import model.Personagem;
import model.Evento.ProvaBatalha;
import model.Evento.ResultadoProva;
import repository.ResultadoProvaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BatalhaIntegracaoTest {

    private BatalhaService batalhaService;
    private ResultadoProvaRepository resultadoRepo;
    private Personagem estudante;
    private ProvaBatalha matrizFinal;

    @BeforeEach
    void setUp() {
        batalhaService = new BatalhaService();
        resultadoRepo = new ResultadoProvaRepository();

        // Estudante experiente com bons atributos
        estudante = new Personagem("Veterano de Computação", 100, 100, 100, 100, "sprite.png");
        estudante.adicionarConhecimento(model.Disciplina.AreaConhecimento.MAT, 80.0);

        // Prova com 3 questões e dificuldade 3
        matrizFinal = new ProvaBatalha("Matemática-3: Integrais e Integrais", "bla bla", 3, 3);
    }

    @Test
    void testSimulacaoCompletaUndertaleLoop() {

        // --- INÍCIO DA BATALHA ---
        EstadoBatalha cena = batalhaService.iniciarBatalha(estudante, matrizFinal);

        assertNotNull(cena.getPlayer(),
            "O boneco do player (coração) não foi criado ao iniciar a batalha. Verifique o BatalhaService.");
        assertEquals(3, cena.getFilaOponentes().size() + (cena.getOponenteAtual() != null ? 1 : 0),
            "Eram esperadas 3 questões na fila de oponentes, mas a quantidade está errada.");
        assertFalse(cena.isBatalhaAnimal(),
            "Uma prova foi reconhecida como batalha de animal. Verifique o instanceof no BatalhaService.");
        System.out.println("Batalha iniciada! Estudante entrou na luta contra " + matrizFinal.getNome() + " com " + (cena.getFilaOponentes().size() + 1) + " questões na fila.");

        // --- ABATENDO A PRIMEIRA QUESTÃO ---
        Oponente primeiraQuestao = cena.getOponenteAtual();
        while (!primeiraQuestao.isDerrotado() && !cena.isFinalizado()) {
            batalhaService.atacarOponenteAtual(cena, 1.0f); // acerto perfeito no slider
            batalhaService.atualizar(cena, 0.16f);
        }

        assertNotEquals(primeiraQuestao, cena.getOponenteAtual(),
            "A primeira questão foi derrotada mas a fila não avançou para a próxima.");
        System.out.println("Primeira questão eliminada! A fila avançou para o próximo oponente.");

        // --- LEVANDO DANO (tiro não desviado) ---
        int shieldAntes = cena.getPlayer().getShieldAtual();
        cena.getPlayer().ReceberDano(3, 1.0f);

        assertTrue(cena.getPlayer().getShieldAtual() < shieldAntes || cena.getPlayer().isPerdeuNota(),
            "O player tomou dano mas nem o shield reduziu nem a nota foi afetada. O sistema de dano está ignorando o impacto.");
        System.out.println("Player levou um tiro! Shield ou nota foram afetados conforme esperado.");

        // --- FINALIZANDO A BATALHA ---
        while (!cena.isFinalizado()) {
            batalhaService.atacarOponenteAtual(cena, 1.0f);
            batalhaService.atualizar(cena, 0.16f);
        }

        assertTrue(cena.isVitoria(),
            "Todas as questões foram eliminadas mas a batalha não registrou vitória.");
        System.out.println("Todas as questões foram derrotadas! Vitória registrada.");

        // --- SALVANDO O BOLETIM ---
        ResultadoProva boletim = new ResultadoProva(
            cena.getPersonagemOriginal(),
            matrizFinal,
            7.5f,
            3, 3,
            cena.getPlayer().isPerdeuNota() ? 10 : 1,
            true,
            cena.getPlayer().isPerdeuNota()
        );
        resultadoRepo.salvar(boletim);

        assertEquals(1, resultadoRepo.buscarTodos().size(),
            "O boletim não foi salvo no repositório após o fim da batalha.");
        assertEquals("Veterano de Computação", resultadoRepo.buscarTodos().get(0).getPersonagem().getNome(),
            "O nome do estudante no boletim salvo está diferente do esperado. Os dados foram corrompidos.");
        System.out.println("Boletim salvo com sucesso! Estudante: " + resultadoRepo.buscarTodos().get(0).getPersonagem().getNome() + " — Nota: 7.5");
        }
}

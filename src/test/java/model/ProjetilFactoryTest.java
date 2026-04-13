package model;

import model.EntidadeBatalha;
import model.Player.PlayerProva;
import model.Projetil.Projetil;
import model.Projetil.ProjetilFactory;
import model.Projetil.ProjetilID;
import model.util.Hitbox;
import model.util.Vector2D;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProjetilFactoryTest {

    private PlayerProva mockPlayer;
    private EntidadeBatalha mockOwner;

    @BeforeEach
    void setUp() {
        mockPlayer = new PlayerProva(
            new Hitbox(new Vector2D(0, 0), new Vector2D(20, 20), 0),
            new Vector2D(0, 0),
            10.0f
        );

        mockOwner = new Oponente(
            new Hitbox(new Vector2D(50, 50), new Vector2D(10, 10), 0),
            new Vector2D(0, 0), "Boss", 100f
        ) {
            @Override
            public model.Ataque.Ataque criarAtaque(PlayerProva alvo, EntidadeBatalha owner) {
                return null;
            }
        };
    }

    @Test
    void testPoolCiclicaNaoEstouraGC() {
        // Pool isolada: 5 básicos, sem outros tipos
        ProjetilFactory factory = new ProjetilFactory(mockPlayer, mockOwner, 5, 0, 0);

        // Spawnar 8 vezes numa pool de 5 → deve reutilizar os mesmos 5 objetos em ciclo
        for (int i = 0; i < 8; i++) {
            factory.spawn(0, 0, 10, 10, 10f, 0f, 0f, ProjetilID.BASICO, 5, 1.0f, 1000f);
        }

        List<Projetil> ativos = factory.getAtivos();
        assertEquals(5, ativos.size(),
            "Pool de 5 deve ter max 5 ativos, mesmo com 8 spawns — GC circular validado!");
        System.out.println("✅ [ProjetilFactoryTest] testPoolCiclicaNaoEstouraGC: " + ativos.size() + " ativos. Buffer respeitado!");
    }

    @Test
    void testTiposComPoolsIndependentes() {
        // Pool isolada criada DENTRO do teste — garante que não carrega estado do setUp
        ProjetilFactory multiFactory = new ProjetilFactory(mockPlayer, mockOwner, 2, 2, 2);

        // Spawna 1 de cada tipo → 3 ativos esperados
        multiFactory.spawn(10, 10, 5, 5, 10f, 0f, 0f, ProjetilID.BASICO,    5, 1.0f, 1000f);
        multiFactory.spawn(10, 10, 5, 5, 10f, 0f, 0f, ProjetilID.HOMING,    5, 1.0f, 1000f);
        multiFactory.spawn(10, 10, 5, 5, 10f, 0f, 0f, ProjetilID.EXPLOSIVE, 5, 1.0f, 1000f);

        List<Projetil> ativos = multiFactory.getAtivos();
        assertEquals(3, ativos.size(),
            "Apenas 1 de cada tipo foi spawnado — 3 ativos, pools independentes!");
        System.out.println("✅ [ProjetilFactoryTest] testTiposComPoolsIndependentes: " + ativos.size() + " projéteis de tipos diferentes ativos sem conflito!");
    }

    @Test
    void testProjetilExpiradoSomeNaProximaAtualizacao() {
        ProjetilFactory factory = new ProjetilFactory(mockPlayer, mockOwner, 3, 0, 0);

        // Duração curtíssima
        factory.spawn(0, 0, 5, 5, 10f, 0f, 0f, ProjetilID.BASICO, 1, 0f, 0.001f);

        // Primeira atualização: tempoDeVida vai de 0→1s, mas o check é ANTES do incremento
        // Precisamos de 2 chamadas para garantir que o projétil morra
        factory.atualizar(1.0f); // tempoDeVida = 1.0 > 0.001 na segunda checagem
        factory.atualizar(0.1f); // confirma expiração

        List<Projetil> ativos = factory.getAtivos();
        assertEquals(0, ativos.size(),
            "Projétil com duração expirada deve sumir dos ativos após atualização!");
        System.out.println("✅ [ProjetilFactoryTest] testProjetilExpiradoSomeNaProximaAtualizacao: projétil com tempo esgotado desativado com sucesso!");
    }
}

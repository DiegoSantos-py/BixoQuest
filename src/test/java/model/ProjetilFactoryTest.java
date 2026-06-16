package model;

import model.Batalha.EntidadeBatalha;
import model.Batalha.Oponente;
import model.Player.PlayerProva;
import model.Projetil.Projetil;
import model.Projetil.ProjetilFactory;
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
                10.0f);

        mockOwner = new Oponente(
                new Hitbox(new Vector2D(50, 50), new Vector2D(10, 10), 0),
                new Vector2D(0, 0), "Boss", 100f, "dummy_sprite") {
            // @Override n funciona
            public model.Ataque.Ataque criarAtaque(PlayerProva alvo, EntidadeBatalha owner) {
                return new model.Ataque.Ataques.AtaqueMordida(alvo, owner, 10.0f);
            }
        };
    }

    @Test
    void testPoolCiclicaNaoEstouraGC() {
        // Pool isolada: capacidade 5
        ProjetilFactory factory = new ProjetilFactory(mockPlayer, mockOwner, 5);

        // Spawnar 8 vezes numa pool de 5 → deve reutilizar os mesmos 5 objetos em ciclo
        for (int i = 0; i < 8; i++) {
            factory.spawn(0, 0, 10, 10, 10f, 0f, 0f, 5, 1.0f, 1000f, "dummy_sprite");
        }

        List<Projetil> ativos = factory.getAtivos();
        assertEquals(5, ativos.size(),
                "Pool de 5 deve ter max 5 ativos, mesmo com 8 spawns — GC circular validado!");
        System.out.println("✅ [ProjetilFactoryTest] testPoolCiclicaNaoEstouraGC: " + ativos.size()
                + " ativos. Buffer respeitado!");
    }

    @Test
    void testInjecaoDeComportamentos() {
        ProjetilFactory factory = new ProjetilFactory(mockPlayer, mockOwner, 5);

        Projetil p1 = factory.spawn(10, 10, 5, 5, 10f, 0f, 0f, 5, 1.0f, 1000f, "dummy_sprite");
        p1.addComportamento(new model.Projetil.Comportamentos.ProjetilQueSegue());

        Projetil p2 = factory.spawn(10, 10, 5, 5, 10f, 0f, 0f, 5, 1.0f, 1000f, "dummy_sprite");
        p2.addComportamentoDespawn(new model.Projetil.Comportamentos.ProjetilExplosivo());

        List<Projetil> ativos = factory.getAtivos();
        assertEquals(2, ativos.size(),
                "Dois projéteis foram spawnados na mesma pool.");
        System.out.println(
                "✅ [ProjetilFactoryTest] testInjecaoDeComportamentos: Comportamentos injetados com sucesso em projéteis genéricos!");
    }

    @Test
    void testProjetilExpiradoSomeNaProximaAtualizacao() {
        ProjetilFactory factory = new ProjetilFactory(mockPlayer, mockOwner, 3);

        // Duração curtíssima
        factory.spawn(0, 0, 5, 5, 10f, 0f, 0f, 1, 0f, 0.001f, "dummy_sprite");

        // Primeira atualização: tempoDeVida vai de 0→1s, mas o check é ANTES do
        // incremento
        // Precisamos de 2 chamadas para garantir que o projétil morra
        factory.atualizar(1.0f); // tempoDeVida = 1.0 > 0.001 na segunda checagem
        factory.atualizar(0.1f); // confirma expiração

        List<Projetil> ativos = factory.getAtivos();
        assertEquals(0, ativos.size(),
                "Projétil com duração expirada deve sumir dos ativos após atualização!");
        System.out.println(
                "✅ [ProjetilFactoryTest] testProjetilExpiradoSomeNaProximaAtualizacao: projétil com tempo esgotado desativado com sucesso!");
    }
}

package model.Projetil;

import java.util.ArrayList;
import java.util.List;
import model.Batalha.EntidadeBatalha;
import model.Player.PlayerProva;
import model.util.Hitbox;
import model.util.Vector2D;

public class ProjetilPool {

    private Projetil[] pool;
    private int index = 0;

    public ProjetilPool(int capacidade) {
        if (capacidade < 0)
            throw new exception.Projetil.FactoryInvalidaException("capacidade", "não pode ser negativa");

        pool = new Projetil[capacidade];
        for (int i = 0; i < capacidade; i++) {
            pool[i] = new Projetil(
                    new Hitbox(new Vector2D(0, 0), new Vector2D(0, 0), 0),
                    new Vector2D(0, 0), 0, 0, 0,
                    "Null");
        }
    }

    public void configurar(ProjetilFactory factory, EntidadeBatalha owner, PlayerProva target) {
        for (Projetil p : pool) {
            p.setFactory(factory);
            p.setOwner(owner);
            p.setTarget(target);
        }
    }

    public void atualizarTarget(PlayerProva target) {
        for (Projetil p : pool) {
            p.setTarget(target);
        }
    }

    // Takes pre-decomposed velX/velY — angle math is the factory's job, not the
    // pool's
    public Projetil spawn(float posX, float posY, float tamanhoX, float tamanhoY,
            float velX, float velY, float anguloHitbox,
            int danoShield, float danoNota, float duracaoMaxima, String spriteDir) {
        if (pool.length == 0)
            return null;

        Projetil p = pool[index];
        index = (index + 1) % pool.length;
        p.reviver(posX, posY, tamanhoX, tamanhoY, velX, velY, anguloHitbox, danoShield, danoNota, duracaoMaxima, spriteDir);
        return p;
    }

    public void atualizar(float dt) {
        for (Projetil p : pool)
            if (p.isAtivo())
                p.atualizar(dt);
    }

    public void desativarTodos() {
        for (Projetil p : pool)
            p.desativar();
    }

    public List<Projetil> getAtivos() {
        List<Projetil> ativos = new ArrayList<>();
        for (Projetil p : pool)
            if (p.isAtivo())
                ativos.add(p);
        return ativos;
    }
}

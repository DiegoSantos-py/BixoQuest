package model.Projetil.Comportamentos;

import java.util.HashMap;
import java.util.Map;
import model.Projetil.ComportamentoAoDespawnar;
import model.Projetil.ComportamentoProjetil;

//flyweight factory pros comportamento
public class ComportamentoFactory {

    private static final Map<String, ComportamentoProjetil> comportamentoCache = new HashMap<>();
    private static final Map<String, ComportamentoAoDespawnar> despawnCache = new HashMap<>();

    static {
        comportamentoCache.put("HOMING", new ProjetilQueSegue());
        despawnCache.put("EXPLOSIVO", new ProjetilExplosivo());
        despawnCache.put("SPAWN_ARRANHAO", new ProjetilSpawnAoMorrer(
                "/assets/batalha/projeteis/arranhao.png",
                3, 650, // Tamanho igual ao da prévia (3x600)
                1, 0.2f, // Dá 1 de dano no shield, 0.2 na nota
                0.2f     // Dura apenas 0.2 segundos
        ));
        despawnCache.put("SPAWN_FUNCAO", new ProjetilSpawnAoMorrer(
                "/assets/batalha/projeteis/arranhao.png",
                3, 650, // Tamanho igual ao da prévia (3x600)
                1, 0.5f, // Dá 1 de dano no shield, 0.2 na nota
                0.2f     // Dura apenas 0.2 segundos
        ));
    }

    public static ComportamentoProjetil getAI(String key) {
        return comportamentoCache.get(key.toUpperCase());
    }

    public static ComportamentoAoDespawnar getDespawn(String key) {
        return despawnCache.get(key.toUpperCase());
    }
}

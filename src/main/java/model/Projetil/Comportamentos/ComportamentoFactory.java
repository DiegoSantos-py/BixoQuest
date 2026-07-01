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
        comportamentoCache.put("PREDITIVO", new ProjetilQuePreve());
        comportamentoCache.put("SENOIDAL_X_UP", new ProjetilSenoidal(200f, (float) Math.PI * 1.5f, true));
        comportamentoCache.put("SENOIDAL_X_DOWN", new ProjetilSenoidal(-200f, (float) Math.PI * 1.5f, true));
        comportamentoCache.put("SENOIDAL_Y_UP", new ProjetilSenoidal(200f, (float) Math.PI * 1.5f, false));
        comportamentoCache.put("SENOIDAL_Y_DOWN", new ProjetilSenoidal(-200f, (float) Math.PI * 1.5f, false));
        despawnCache.put("EXPLOSIVO", new ProjetilExplosivo());
        despawnCache.put("SPAWN_ARRANHAO", new ProjetilSpawnAoMorrer(
                "arranhao.png",
                3, 650,
                1, 0.75f,
                0.2f
        ));
        despawnCache.put("SPAWN_FUNCAO", new ProjetilSpawnAoMorrer(
                "arranhao.png",
                3, 650,
                1, 0.75f,
                0.2f
        ));
    }

    public static ComportamentoProjetil getAI(String key) {
        return comportamentoCache.get(key.toUpperCase());
    }

    public static ComportamentoAoDespawnar getDespawn(String key) {
        return despawnCache.get(key.toUpperCase());
    }
}

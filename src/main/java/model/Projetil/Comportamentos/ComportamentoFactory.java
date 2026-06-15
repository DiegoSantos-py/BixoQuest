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
    }

    public static ComportamentoProjetil getAI(String key) {
        return comportamentoCache.get(key.toUpperCase());
    }

    public static ComportamentoAoDespawnar getDespawn(String key) {
        return despawnCache.get(key.toUpperCase());
    }
}

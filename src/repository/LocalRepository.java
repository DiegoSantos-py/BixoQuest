package repository;

import model.Local.Local;

import java.util.HashMap;
import java.util.Map;

public class LocalRepository {
    private Map<String, Local> locais;

    public LocalRepository(){
        this.locais = new HashMap<>();
    }

    public void adicionarLocal(Local local){
        if (local == null || local.getNome() == null) {
            return;
        }

        if (this.locais.containsKey(local.getNome())){
            return;
        }

        this.locais.put(local.getNome(), local);
    }

    public Map<String, Local> carregarLocal(){
        return this.locais;
    }
}


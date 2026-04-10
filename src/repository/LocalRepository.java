package repository;

import model.Local;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalRepository {
    private Map<String, Local> locais;

    public LocalRepository(){
        this.locais = new HashMap<>();
    }

    public void adicionarLocal(Local local){
            if (this.locais.containsKey(local.getNome())){
                this.locais.put(local.getNome(), local);
            }

    }

    public Map<String, Local> carregarLocal(){
        return this.locais;
    }
}

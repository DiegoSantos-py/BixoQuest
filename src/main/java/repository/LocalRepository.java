package repository;

import model.Local.Local;
import model.Local.TipoLocal;

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

        //verifica se local ja existe no repositório
        if (this.locais.containsKey(local.getNome())){
            return;
        }

        // adiciona local
        this.locais.put(local.getNome(), local);
    }

    public Map<String, Local> carregarLocal(){
        return this.locais;
    }

    public Local buscarPorTipo(TipoLocal tipo){
        for (String l: this.locais.keySet()){
                if (locais.get(l).getTipo() == tipo){
                    return locais.get(l);
                }
        }
        return null;
    }
}


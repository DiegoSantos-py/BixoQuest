package repository;

import model.Local;

import java.util.ArrayList;
import java.util.List;

public class LocalRepository {
    private List<Local> locais;

    public LocalRepository(){
        this.locais = new ArrayList<>();
    }

    public void adicionarLocal(Local local){
        if (!this.locais.contains(local)){
            this.locais.add(local);
        }
    }

    public List<Local> carregarLocal(){
        return this.locais;
    }
}

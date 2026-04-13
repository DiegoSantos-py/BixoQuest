package model.Local;

import java.util.*;

public class Mapa {
    private Map<String, Local> locais;

    public Mapa(){
        locais = new HashMap<String, Local>();
    }

    // adiciona local ao map tendo nome como chave
    public void adicionarLocal(String nome, Local outro){
        this.locais.put(nome, outro);
    }

    public Map<String, Local> getLocais() {
        return locais;
    }

    public void setLocais(Map<String, Local> locais) {
        this.locais = locais;
    }
}

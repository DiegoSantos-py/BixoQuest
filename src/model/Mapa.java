package model;
import java.util.*;

public class Mapa {
    private Map<String, Local> locais;

    public Mapa(){
        locais = new HashMap<String, Local>();
    }

    public void adicionarLocal(String nome, Local outro){
        this.locais.put(nome, outro);
    }
}

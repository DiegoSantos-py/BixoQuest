package model;
import java.util.*;

public class Mapa {
    private List<Local> locais;

    public Mapa(){
        locais = new ArrayList<Local>();
    }

    public void adicionarLocal(Local outro){
        this.locais.add(outro);
    }
}

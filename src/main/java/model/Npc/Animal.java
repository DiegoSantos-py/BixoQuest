package model.Npc;

import java.util.ArrayList;
import model.Batalhavel;

public class Animal extends Npc implements Batalhavel {
    private int indole;

    public Animal(String nome, int cX, int cY, ArrayList<String> falas, int indole){
        super(nome, cX, cY, falas);
        this.indole = indole;
    }

    @Override
    public String getNomeBatalha() {
        return getNome();
    }
    
    public int getIndole() {
        return indole;
    }
}

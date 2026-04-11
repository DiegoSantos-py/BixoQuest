package model.Npc;

import java.util.ArrayList;

public class Animal extends Npc {
    private int indole;

    public Animal(String nome, int cX, int cY, ArrayList<String> falas, int indole){
        super(nome, cX, cY, falas);
        this.indole = indole;
    }
}

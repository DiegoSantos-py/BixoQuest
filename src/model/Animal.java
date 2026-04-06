package model;

import java.util.ArrayList;

public class Animal extends Npc {
    private int indole;

    public Animal(String nome, int cX, int cY, Evento evento, ArrayList<String> falas, int indole){
        super(nome, cX, cY, evento, falas);
        this.indole = indole;
    }
}

package model.Npc;

import model.Personagem;

import java.util.ArrayList;

public class Animal extends Npc {

    private int indole;
    private boolean domado;
    private Especie especie;

    public Animal() {}

    public Animal(String nome, String spriteDir, int cX, int cY,
                  ArrayList<String> falas, Especie especie, int indole) {
        super(nome, spriteDir, cX, cY, falas);
        this.indole = indole;
        this.especie = especie;
    }

    public Especie getEspecie() { return especie; }
    public void setEspecie(Especie especie) { this.especie = especie; }
    public int getIndole() { return indole; }
    public void setIndole(int indole) { this.indole = indole; }
    public boolean isDomado() { return domado; }
    public void setDomado(boolean domado) { this.domado = domado; }

    @Override
    public String aoInteragir(Personagem player) {
        if (domado) {
            player.addMotivacao(indole / 5);
            return this.getFalas().get(1);
        }
        return this.getFalas().get(0);
    }
}
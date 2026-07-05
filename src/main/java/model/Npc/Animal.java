package model.Npc;

import model.Personagem;

import java.util.ArrayList;

public class Animal extends Npc {

    private int indole;
    private Especie especie;
    // campo "domado" removido

    public Animal() {}

    public Animal(String nome, String spriteDir, int cX, int cY, ArrayList<String> falas, Especie especie, int indole) {
        super(nome, spriteDir, cX, cY, falas);
        this.indole = indole;
        this.especie = especie;
    }

    public Especie getEspecie() { return especie; }
    public int getIndole() { return indole; }
    public void setIndole(int indole) { this.indole = indole; }

    // isDomado()/setDomado() removidos — a informação agora vive em Personagem

    @Override
    public String aoInteragir(Personagem player) {
        if (player.domouAnimal(getNome())) {
            player.addMotivacao(Math.round(indole / 2.0f));
            return this.getFalas().get(1);
        }
        return this.getFalas().get(0);
    }
}
package model.Npc;

import java.util.ArrayList;

import model.Personagem;

public class Animal extends Npc {
    private int indole;
    private boolean domado;
    private Especie especie;
    public Animal(String nome,String spriteDir,int cX, int cY, ArrayList<String> falas,Especie especie, int indole){
        super(nome, spriteDir, cX, cY, falas);
        this.indole = indole;
        this.especie = especie;
    }


    public void setDomado(boolean domado) {
        this.domado = domado;
    }

    @Override
    public String aoInteragir(Personagem player) {
        if (domado) {
            player.addMotivacao(indole / 5);
            return this.getFalas().get(1);
        }
        //fala 1 = boa, fala 0 = ruim

        return this.getFalas().get(0);
        //o player (target (playerProva) é definido no Servico de batalha]

    }

    // Adicione no final do arquivo Animal.java
    public Especie getEspecie() {
        return especie;
    }

    public void setEspecie(Especie especie) {
        this.especie = especie;
    }

    public int getIndole() {
        return indole;
    }
}

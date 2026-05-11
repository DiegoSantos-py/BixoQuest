package model.Npc;

import model.Personagem;

import java.util.ArrayList;

public class Colega extends Npc {

    public Colega(String nome, int cX, int cY, ArrayList<String> falas){
        super(nome, cX, cY, falas);

    }

    //TODO: CONSERTAR ISSO AQUI
    /*public String aoInteragir(Personagem player) {
        if(!getInteragido()) {
            return this.getFalaAleatoria();
        }
        return "nao sei";

        //TODO: FAZER COLEGA
        //IDEIA: TROCAR CONHECIMENTO MAT-> HUM E ETC
        //cada colega tem uma area de disciplina(spawna aleatoriamente pelo mapa
        //vc interage, perde tipo 0.5 e ganha 1
        //genial eu sei
    }*/
}

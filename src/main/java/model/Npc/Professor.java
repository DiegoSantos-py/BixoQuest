package model.Npc;

import model.Disciplina.AreaConhecimento;
import model.Disciplina.Disciplina;
import model.Personagem;

import java.util.ArrayList;

public class Professor extends Npc {

    AreaConhecimento areaConhecimento;
    public Professor(String nome, int cX, int cY, ArrayList<String> falas) {
        super(nome, cX, cY, falas);
    }
    @Override
    public String aoInteragir(Personagem player) {
        if(!getInteragido()) {
            player.adicionarConhecimento(areaConhecimento, Math.random()); //da entre 0 e 1 de conhecimento so por falar
            this.setInteragido(true);
            return this.getFalaAleatoria();
        }
        return "nao sei";
    }


}

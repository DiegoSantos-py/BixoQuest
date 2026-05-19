package model.Npc;

import model.Disciplina.AreaConhecimento;
import model.Disciplina.Disciplina;
import model.Personagem;

import java.util.ArrayList;

public class Professor extends Npc {

    AreaConhecimento areaConhecimento;
    public Professor(String nome,String spriteDir,  int cX, int cY, ArrayList<String> falas, AreaConhecimento areaConhecimento) {

        super(nome, spriteDir, cX, cY, falas);
        this.areaConhecimento = areaConhecimento;
    }

    public String aoInteragir(Personagem player) {
        if(isInteragido()) {
            player.atualizarConhecimento(areaConhecimento, Math.random()); //da entre 0 e 1 de conhecimento so por falar
            this.setInteragido(true);
            return this.getFalaAleatoria();
        }
        return "";
    }


}

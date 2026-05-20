package model.Npc;

import model.Disciplina.AreaConhecimento;
import model.Personagem;

import java.util.ArrayList;

public class Professor extends Npc {

    private AreaConhecimento areaConhecimento;

    public Professor() {}

    public Professor(String nome, String spriteDir, int cX, int cY,
                     ArrayList<String> falas, AreaConhecimento areaConhecimento) {
        super(nome, spriteDir, cX, cY, falas);
        this.areaConhecimento = areaConhecimento;
    }

    public AreaConhecimento getAreaConhecimento() { return areaConhecimento; }
    public void setAreaConhecimento(AreaConhecimento areaConhecimento) { this.areaConhecimento = areaConhecimento; }

    @Override
    public String aoInteragir(Personagem player) {
        if (!isInteragido()) {
            player.atualizarConhecimento(areaConhecimento, Math.random());
            this.setInteragido(true);
            return this.getFalaAleatoria();
        }
        return "";
    }
}
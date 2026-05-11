package model.Npc;

import model.Disciplina.AreaConhecimento;
import model.Personagem;

import java.util.ArrayList;

public class Colega extends Npc {
    private AreaConhecimento area;
    private double conhecimentoNpc;

    public Colega(String nome, int cX, int cY, ArrayList<String> falas){
        super(nome, cX, cY, falas);

    }

    public String aoInteragir(Personagem player) {
        player.atualizarConhecimento(area, conhecimentoNpc);

        if(isInteragido()) {
            return this.getFalaAleatoria();
        }
        return "";
    }

    public double getConhecimentoNpc() {
        return conhecimentoNpc;
    }

    public void setConhecimentoNpc(double conhecimentoNpc) {
        this.conhecimentoNpc = conhecimentoNpc;
    }

    public AreaConhecimento getArea() {
        return area;
    }

    public void setArea(AreaConhecimento area) {
        this.area = area;
    }
}

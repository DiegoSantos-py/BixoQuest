package model.Npc;
import model.Personagem;

import java.util.ArrayList;
import java.util.Random;

public abstract class Npc {
    private String nome;
    private int cX;
    private int cY;
    private ArrayList<String> falas; //tlvz faça mais sentido substituir por uma classe Dialogo
    private String spriteDir ;
    private boolean isInteragido;

    public Npc(String nome,String spriteDir ,int cX, int cY, ArrayList<String> falas) {
        this.nome = nome;
        this.cX = cX;
        this.cY = cY;
        this.falas = falas;
    }

    public Npc(String nome, int cX, int cY, ArrayList<String> falas, String spriteDir) {
        this.nome = nome;
        this.spriteDir = spriteDir;
        this.cX = cX;
        this.cY = cY;
        this.falas = falas;
        this.spriteDir = spriteDir;
    }

    public ArrayList<String> getFalas() {
        return falas;
    }

    public void setFalas(ArrayList<String> falas) {
        this.falas = falas;
    }

    public int getcY() {
        return cY;
    }

    public void setcY(int cY) {
        this.cY = cY;
    }

    public int getcX() {
        return cX;
    }

    public void setcX(int cX) {
        this.cX = cX;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }



    public boolean isInteragido() {
        return isInteragido;
    }

    public void setInteragido(boolean interagido) {
        isInteragido = interagido;
    }

    public String getFalaAleatoria() {
        if (falas == null || falas.isEmpty()) return "";
        Random random = new Random();
        return falas.get(random.nextInt(falas.size()));
    }

    public abstract String aoInteragir(Personagem player);
}

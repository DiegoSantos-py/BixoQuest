package model.Npc;
import java.util.ArrayList;

public class Npc {
    private String nome;
    private int cX;
    private int cY;
    private ArrayList<String> falas; //tlvz faça mais sentido substituir por uma classe Dialogo
    private String spriteDir ;

    public Npc(String nome, int cX, int cY, ArrayList<String> falas) {
        this.nome = nome;
        this.cX = cX;
        this.cY = cY;
        this.falas = falas;
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

    //TODO melhorar npcs

}

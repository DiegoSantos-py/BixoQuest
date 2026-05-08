package model.Npc;
import java.util.ArrayList;
import model.Personagem;

public abstract class Npc {
    private String nome;
    private int cX;
    private int cY;
    private ArrayList<String> falas; //tlvz faça mais sentido substituir por uma classe Dialogo
    private String spriteDir ;
    protected static float posXspritebatalha = 960;
    protected static float posYspritebatalha = 540;//meio da tela em 1920x1080

    private boolean interagido;
    public Npc(String nome, int cX, int cY, ArrayList<String> falas) {
        this.nome = nome;
        this.cX = cX;
        this.cY = cY;
        this.falas = falas;
    }

    public ArrayList<String> getFalas() {
        return falas;
    }

    public String getFalaAleatoria(){
        return falas.get( (int)Math.floor ( Math.random() * falas.size())); // Math.random nunca vai ser 1 ent tem q ser .size msm;
    }

    public void setInteragido(boolean interagido) {
        this.interagido = interagido;
    }
    public boolean getInteragido() {return interagido;}
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

    public String getSprite() {
        return spriteDir;
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

    public abstract String aoInteragir(Personagem player);


}

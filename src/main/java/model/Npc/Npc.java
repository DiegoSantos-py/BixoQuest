package model.Npc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import model.Personagem;

import java.util.ArrayList;
import java.util.Random;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "tipo",
        defaultImpl = Npc.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Professor.class, name = "professor"),
        @JsonSubTypes.Type(value = Colega.class,    name = "colega"),
        @JsonSubTypes.Type(value = Animal.class,    name = "animal")
})
public abstract class Npc {
    private String nome;
    private int cX;
    private int cY;
    private ArrayList<String> falas;
    private String spriteDir;
    private boolean isInteragido;

    public Npc() {}

    public Npc(String nome, String spriteDir, int cX, int cY, ArrayList<String> falas) {
        this.nome = nome;
        this.spriteDir = spriteDir;
        this.cX = cX;
        this.cY = cY;
        this.falas = falas;
    }

    public ArrayList<String> getFalas() { return falas; }
    public void setFalas(ArrayList<String> falas) { this.falas = falas; }
    public int getcY() { return cY; }
    public void setcY(int cY) { this.cY = cY; }
    public int getcX() { return cX; }
    public void setcX(int cX) { this.cX = cX; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getSpriteDir() { return spriteDir; }
    public void setSpriteDir(String spriteDir) { this.spriteDir = spriteDir; }
    public boolean isInteragido() { return isInteragido; }
    public void setInteragido(boolean interagido) { isInteragido = interagido; }

    @JsonIgnore
    public String getFalaAleatoria() {
        if (falas == null || falas.isEmpty()) return "";
        return falas.get(new Random().nextInt(falas.size()));
    }

    public abstract String aoInteragir(Personagem player);
}
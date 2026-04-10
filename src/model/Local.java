package model;

import java.util.ArrayList;
import java.util.List;

public class Local {
    private String nome;
    private List<Local> vizinhos;
    private List<ZonaInterativa> zonas;
    private String spriteDir;
    private Area area;

    public Local(String nome, Area area){
        this.nome = nome;
        this.area = area;
        this.vizinhos = new ArrayList<>();
    }

    public Local(String nome, List<ZonaInterativa> zonas){
        this.nome = nome;
        this.zonas = zonas;
        this.vizinhos = new ArrayList<>();
        this.zonas = new ArrayList<>();
    }

    public Local(String nome, List<ZonaInterativa> zonas, String spriteDir){
        this.nome = nome;
        this.zonas = zonas;
        this.vizinhos = new ArrayList<>();
        this.spriteDir = spriteDir;
    }

    public List<Local> getVizinhos() {
        return vizinhos;
    }

    public String getNome() {
        return nome;
    }

    public String getSpriteDir() {
        return spriteDir;
    }

    public Area getArea() {
        return area;
    }

    public List<ZonaInterativa> getZonaInterativasDisponiveis(){
        return this.zonas;
    }

    public void conectar(Local outro) {

        if (this == outro) {
            throw new IllegalArgumentException("Um local não pode se conectar a si mesmo.");
        }

        if (this.vizinhos.contains(outro)) {
            return; // já conectado, não faz nada
        }

        this.vizinhos.add(outro);

        if (!outro.vizinhos.contains(this)) {
            outro.vizinhos.add(this);
        }
    }
}

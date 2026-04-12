package model.Local;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Local {
    private String nome;
    private Map<Direcao, Local> vizinhos;
    private List<ZonaInterativa> zonas;
    private String spriteDir;
    private Area area;
    private TipoLocal tipo;

    public Local(String nome, Area area){
        this.nome = nome;
        this.area = area;
        this.vizinhos = new HashMap<>();
    }

    public Local(String nome, Area area, TipoLocal tipo) {
        this.nome = nome;
        this.area = area;
        this.tipo = tipo;
        this.vizinhos = new HashMap<>();
    }

    public Local(String nome, List<ZonaInterativa> zonas){
        this.nome = nome;
        this.zonas = zonas;
        this.vizinhos = new HashMap<>();
    }

    public Local(String nome, List<ZonaInterativa> zonas, String spriteDir){
        this.nome = nome;
        this.zonas = zonas;
        this.vizinhos = new HashMap<>();
        this.spriteDir = spriteDir;
    }

    public Map<Direcao, Local> getVizinhos() {
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

    public Local getVizinho(Direcao direcao) {
        return vizinhos.get(direcao);
    }

    public void adicionarVizinho(Direcao direcao, Local local) {
        this.vizinhos.put(direcao, local);
    }

    public TipoLocal getTipo() {
        return tipo;
    }
}

package model.Local;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Local {
    private String nome;
    // Ignora o mapa de objetos na serialização
    @JsonIgnore
    private Map<Direcao, Local> vizinhos; // locais que fazem fronteira com o local atual
    // Jackson usa esse campo no lugar
    private Map<String, String> vizinhosNomes = new HashMap<>();
    private List<ZonaInterativa> zonas; // zonas de interação desse local
    private String spriteDir;
    private Area area;
    private TipoLocal tipo;

    public Local() {
        this.vizinhos = new HashMap<>();
        this.zonas = new ArrayList<>();
        this.vizinhosNomes = new HashMap<>();
    }

    public Local(String nome, Area area){
        this.nome = nome;
        this.area = area;
        this.vizinhos = new HashMap<>();
        this.zonas = new ArrayList<>();
    }

    public Local(String nome, Area area, TipoLocal tipo) {
        this.nome = nome;
        this.area = area;
        this.tipo = tipo;
        this.vizinhos = new HashMap<>();
        this.zonas = new ArrayList<>();
    }

    public Local(String nome, List<ZonaInterativa> zonas){
        this.nome = nome;
        this.zonas = zonas;
        this.vizinhos = new HashMap<>();
        this.zonas = new ArrayList<>();
    }

    public Local(String nome, List<ZonaInterativa> zonas, String spriteDir){
        this.nome = nome;
        this.zonas = zonas;
        this.vizinhos = new HashMap<>();
        this.spriteDir = spriteDir;
        this.zonas = new ArrayList<>();
    }

    public Map<Direcao, Local> getVizinhos() {
        return vizinhos;
    }

    // Getter/setter para o campo auxiliar
    public Map<String, String> getVizinhosNomes() { return vizinhosNomes; }
    public void setVizinhosNomes(Map<String, String> vizinhosNomes) { this.vizinhosNomes = vizinhosNomes; }

    // Ao adicionar vizinho, atualiza os dois campos
    public void adicionarVizinho(Direcao direcao, Local local) {
        this.vizinhos.put(direcao, local);
        this.vizinhosNomes.put(direcao.name(), local.getNome()); // sincroniza
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

    public TipoLocal getTipo() {
        return tipo;
    }

    public void setTipo(TipoLocal tipoLocal) {
        this.tipo = tipoLocal;
    }

    public void setNome(String nome) { this.nome = nome; }

    public void setSpriteDir(String spriteDir) { this.spriteDir = spriteDir; }

    public void setArea(Area area) { this.area = area; }

    public void setZonaInterativasDisponiveis(List<ZonaInterativa> zonas) {
        this.zonas = zonas;
    }

}

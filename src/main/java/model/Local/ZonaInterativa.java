package model.Local;

public class ZonaInterativa {
    protected Area area;
    private String nome;

    public ZonaInterativa() {
    }

    public ZonaInterativa(Area area, String nome){
        this.area = area;
        this.nome = nome;
    }

    public boolean contemCoordenada(int x, int y){
        return area.contemCoordenada(x, y);
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {this.area = area;}

    public String getNome() {
        return nome;
    }

    public void setNome(String s) {
        this.nome = s;
    }
}

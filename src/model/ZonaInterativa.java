package model;

public class ZonaInterativa {
    protected Area area;
    private String nome;

    public ZonaInterativa(Area area, String nome){
        this.area = area;
        this.nome = nome;
    }

    public boolean contem(int x, int y){
        return area.contem(x, y);
    }

    public Area getArea() {
        return area;
    }

    public String getNome() {
        return nome;
    }
}

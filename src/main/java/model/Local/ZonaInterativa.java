package model.Local;

public class ZonaInterativa {
    protected Area area;
    private String nome;
    private String sprite;

    public ZonaInterativa() {
    }

    public ZonaInterativa(Area area, String nome){
        this.area = area;
        this.nome = nome;
    }

    public ZonaInterativa(Area area, String nome, String sprite) {
        this.area = area;
        this.nome = nome;
        this.sprite = sprite;
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

    public String getSprite() { return sprite; }

    public void setSprite(String sprite) { this.sprite = sprite; }

    public int getX() { return area.getMinX(); }

    public int getY() { return area.getMinY(); }

    public int getLargura() { return area.getMaxX() - area.getMinX(); }

    public int getAltura() { return area.getMaxY() - area.getMinY(); }
}

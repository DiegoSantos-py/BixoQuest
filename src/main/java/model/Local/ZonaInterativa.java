package model.Local;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ZonaInterativa {
    protected Area area;
    private String nome;
    private String sprite;

    private int spriteLargura;
    private int spriteAltura;
    private int spriteX;
    private int spriteY;

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

    @JsonIgnore
    public int getX() { return area.getMinX(); }

    @JsonIgnore
    public int getY() { return area.getMinY(); }

    @JsonIgnore
    public int getLargura() { return area.getMaxX() - area.getMinX(); }

    @JsonIgnore
    public int getAltura() { return area.getMaxY() - area.getMinY(); }

    public int getSpriteLargura() { return spriteLargura; }
    public void setSpriteLargura(int spriteLargura) { this.spriteLargura = spriteLargura; }

    public int getSpriteAltura() { return spriteAltura; }
    public void setSpriteAltura(int spriteAltura) { this.spriteAltura = spriteAltura; }

    public int getSpriteX() { return spriteX; }
    public void setSpriteX(int spriteX) { this.spriteX = spriteX; }

    public int getSpriteY() { return spriteY; }
    public void setSpriteY(int spriteY) { this.spriteY = spriteY; }

}

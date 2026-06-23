package model.Local;

public class ElementoLocal {
    private String sprite;
    private int largura;
    private int x;
    private int y;
    private int hitboxOffsetX;
    private int hitboxOffsetY;
    private int hitboxLargura;
    private int hitboxAltura;

    public ElementoLocal() {}

    public ElementoLocal(String sprite, int largura, int x, int y,
                        int hitboxOffsetX, int hitboxOffsetY,
                        int hitboxLargura, int hitboxAltura) {
        this.sprite = sprite;
        this.largura = largura;
        this.x = x;
        this.y = y;
        this.hitboxOffsetX = hitboxOffsetX;
        this.hitboxOffsetY = hitboxOffsetY;
        this.hitboxLargura = hitboxLargura;
        this.hitboxAltura = hitboxAltura;
    }

    public int getLargura() {
        return largura;
    }

    public void setLargura(int largura) {
        this.largura = largura;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHitboxOffsetX() {
        return hitboxOffsetX;
    }

    public void setHitboxOffsetX(int hitboxOffsetX) {
        this.hitboxOffsetX = hitboxOffsetX;
    }

    public int getHitboxOffsetY() {
        return hitboxOffsetY;
    }

    public void setHitboxOffsetY(int hitboxOffsetY) {
        this.hitboxOffsetY = hitboxOffsetY;
    }

    public int getHitboxLargura() {
        return hitboxLargura;
    }

    public void setHitboxLargura(int hitboxLargura) {
        this.hitboxLargura = hitboxLargura;
    }

    public int getHitboxAltura() {
        return hitboxAltura;
    }

    public void setHitboxAltura(int hitboxAltura) {
        this.hitboxAltura = hitboxAltura;
    }

    public String getSprite() {
        return sprite;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }
}
package model;

public class Area {
    private final int maxX;
    private final int minX;
    private final int maxY;
    private final int minY;

    public Area(int maxX, int minX, int maxY, int minY){
        this.maxX = maxX;
        this.minX = minX;
        this.maxY = maxY;
        this.minY = minY;
    }

    public boolean contemArea(Area outra) {
        return this.minX <= outra.minX &&
                this.maxX >= outra.maxX &&
                this.minY <= outra.minY &&
                this.maxY >= outra.maxY;
    }

    public boolean intersecta(Area outra) {
        return this.minX < outra.maxX &&
                this.maxX > outra.minX &&
                this.minY < outra.maxY &&
                this.maxY > outra.minY;
    }

}

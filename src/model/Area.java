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

    public boolean contem(int x, int y){
        return (this.minX <= x) && (x <= this.maxX) &&
                (this.minY <= y) && (y <= this.maxY);
    }


}

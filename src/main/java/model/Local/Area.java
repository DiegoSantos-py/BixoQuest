package model.Local;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Area {
    private final int maxX;
    private final int minX;
    private final int maxY;
    private final int minY;

    @JsonCreator
    public Area(
            @JsonProperty("maxX") int maxX,
            @JsonProperty("minX") int minX,
            @JsonProperty("maxY") int maxY,
            @JsonProperty("minY") int minY) {
        this.maxX = maxX;
        this.minX = minX;
        this.maxY = maxY;
        this.minY = minY;
    }

    public int getMinX() {
        return minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public boolean contemCoordenada(int x, int y){
        return (this.minX <= x) && (x <= this.maxX) &&
                (this.minY <= y) && (y <= this.maxY);
    }

    public boolean contemArea(Area outra) {
        return this.minX <= outra.minX &&
                this.maxX >= outra.maxX &&
                this.minY <= outra.minY &&
                this.maxY >= outra.maxY;
    }

    // verifica se as áreas colidem
    public boolean intersecta(Area outra) {
        return this.minX < outra.maxX &&
                this.maxX > outra.minX &&
                this.minY < outra.maxY &&
                this.maxY > outra.minY;
    }

}

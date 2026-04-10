package model.util;

public class Vector2D {
    private float x;
    private float y;

    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }
    

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Vector2D add(Vector2D other) {
        return new Vector2D(this.x + other.x, this.y + other.y);
    }

    public Vector2D subtrair(Vector2D other) {
        return new Vector2D(this.x - other.x, this.y - other.y);
    }
     
    public Vector2D mult(float escalar) {
        return new Vector2D(this.x * escalar, this.y * escalar);
    }
    public Vector2D escalar(float scalar) {
        return new Vector2D(this.x * scalar, this.y * scalar);
    }   
    
    public float magnitude() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public float produtoEscalar(Vector2D other) {
        return this.x * other.x + this.y * other.y;
    }

    public Vector2D normalize() {
        float mag = magnitude();
        if (mag == 0) {
            return new Vector2D(0, 0);
        }
        return new Vector2D(x / mag, y / mag);
    }

}
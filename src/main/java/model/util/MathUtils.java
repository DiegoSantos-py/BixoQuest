package model.util;


public class MathUtils {
    public static int randomIntInRange(int min, int max){
        return (int) (Math.floor(Math.random() * (max - min) + min));
    }

    public static float randomFloatInRange(float min, float max){
        return (float) ((Math.random() * (max - min) + min));
    }

    public static boolean randomBoolean(){
        return (Math.random() < 0.5);
    }
    
    public static void rotacionarPonto(float x, float y, float cos, float sin, float cx, float cy, Vector2D out) {
        float rotX = x * cos - y * sin;
        float rotY = x * sin + y * cos;
        out.set(cx + rotX, cy + rotY);
    }
}

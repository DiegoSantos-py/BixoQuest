package model.util;


public class MathUtils {
    public static int randomIntInRange(int min, int max){
        return (int) (Math.floor(Math.random() * (max - min) + min));
    }

    public static float randomFloatInRange(float min, float max){
        return (float) ((Math.random() * (max - min) + min));
    }
}

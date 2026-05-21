package exception.Ataque;


public class NullTargetException extends RuntimeException {


    public NullTargetException() {
        super("Ataque Inválido — o target não foi incializado.");
    }


}

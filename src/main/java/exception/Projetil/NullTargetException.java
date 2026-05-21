package exception.Projetil;

public class NullTargetException extends RuntimeException {


    public NullTargetException() {
        super("Projetil Inválido — o target não foi incializado.");
    }

}

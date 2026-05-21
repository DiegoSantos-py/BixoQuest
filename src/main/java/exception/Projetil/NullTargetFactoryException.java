package exception.Projetil;

public class NullTargetFactoryException extends RuntimeException {

    public NullTargetFactoryException() {
        super("Factory Inválida — o target não foi inicializado.");
    }

}

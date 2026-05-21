package exception.Projetil;

public class FactoryInvalidaException extends RuntimeException {

    private final String campoCausador;

    public FactoryInvalidaException(String campoCausador, String detalhe) {
        super("Factory inválida — campo '" + campoCausador + "': " + detalhe);
        this.campoCausador = campoCausador;
    }

    public String getCampoCausador() {
        return campoCausador;
    }
}

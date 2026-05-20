package exception.Local;

public class LocalInvalidoException extends RuntimeException {

    private final String campoCausador;

    public LocalInvalidoException(String campoCausador, String detalhe) {
        super("Local inválido — campo '" + campoCausador + "': " + detalhe);
        this.campoCausador = campoCausador;
    }

    public String getCampoCausador() {
        return campoCausador;
    }
}
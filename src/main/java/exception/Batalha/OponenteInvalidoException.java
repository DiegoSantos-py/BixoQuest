package exception.Batalha;


public class OponenteInvalidoException extends RuntimeException {

    private final String campoCausador;

    public OponenteInvalidoException(String campoCausador, String detalhe) {
        super("Oponente Invalido — campo '" + campoCausador + "': " + detalhe);
        this.campoCausador = campoCausador;
    }

    public String getCampoCausador() {
        return campoCausador;
    }
}

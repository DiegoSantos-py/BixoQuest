package exception.Batalha;

public class EstadoBatalhaInvalidoException extends RuntimeException {

    private final String campoCausador;

    public EstadoBatalhaInvalidoException(String campoCausador, String detalhe) {
        super("Estado de Batalha inválido — campo '" + campoCausador + "': " + detalhe);
        this.campoCausador = campoCausador;
    }

    public String getCampoCausador() {
        return campoCausador;
    }
}

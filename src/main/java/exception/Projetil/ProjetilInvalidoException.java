package exception.Projetil;

public class ProjetilInvalidoException extends RuntimeException {

    private final String campoCausador;

    public ProjetilInvalidoException(String campoCausador, String detalhe) {
        super("Projetil inválido — campo '" + campoCausador + "': " + detalhe);
        this.campoCausador = campoCausador;
    }

    public String getCampoCausador() {
        return campoCausador;
    }
}

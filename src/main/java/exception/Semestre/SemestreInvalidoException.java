package exception.Semestre;

public class SemestreInvalidoException extends RuntimeException {

    private final String campoCausador;

    public SemestreInvalidoException(String campoCausador, String detalhe) {
        super("Semestre inválido — campo '" + campoCausador + "': " + detalhe);
        this.campoCausador = campoCausador;
    }

    public String getCampoCausador() {
        return campoCausador;
    }
}
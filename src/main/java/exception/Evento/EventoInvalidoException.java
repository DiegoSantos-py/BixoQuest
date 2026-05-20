package exception.Evento;

public class EventoInvalidoException extends RuntimeException {

    private final String campoCausador;

    public EventoInvalidoException(String campoCausador, String detalhe) {
        super("Evento inválido — campo '" + campoCausador + "': " + detalhe);
        this.campoCausador = campoCausador;
    }

    public String getCampoCausador() {
        return campoCausador;
    }
}
package exception.Local;

public class LocalNaoEncontradoException extends RuntimeException {

    private final String identificador;

    public LocalNaoEncontradoException(String identificador) {
        super("Local não encontrado: " + identificador);
        this.identificador = identificador;
    }

    public String getIdentificador() {
        return identificador;
    }
}

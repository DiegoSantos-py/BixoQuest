package exception.Batalha;

public class OponenteAnimalNaoEncontradoException extends RuntimeException {

    private final String identificador;

    public OponenteAnimalNaoEncontradoException(String identificador) {
        super("Oponente não encontrado: " + identificador + "\\n verifique oponenteDados.json.") ;
        this.identificador = identificador;
    }

    public String getIdentificador() {
        return identificador;
    }
}

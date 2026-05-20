package exception.Local;

public class LocalDuplicadoException extends RuntimeException {

    private final String nome;

    public LocalDuplicadoException(String nome) {
        super("Local já cadastrado: " + nome);
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}

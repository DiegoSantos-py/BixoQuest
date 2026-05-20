package exception.Evento;

public class EventoDuplicadoException extends RuntimeException {

    private final String nome;

    public EventoDuplicadoException(String nome) {
        super("Evento já cadastrado: " + nome);
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}

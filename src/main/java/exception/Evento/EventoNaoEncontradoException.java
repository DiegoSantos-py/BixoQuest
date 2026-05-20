package exception.Evento;

public class EventoNaoEncontradoException extends RuntimeException {

    private final String nome;

    public EventoNaoEncontradoException(String nome) {
        super("Evento não encontrado: " + nome);
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}

package exception.Npc;

public class NpcNaoEncontradoException extends RuntimeException {

    private final String nome;

    public NpcNaoEncontradoException(String nome) {
        super("Npc não encontrado: " + nome);
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}

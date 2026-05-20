package exception.Npc;

public class NpcDuplicadoException extends RuntimeException {

    private final String nome;

    public NpcDuplicadoException(String nome) {
        super("Npc já cadastrado: " + nome);
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}

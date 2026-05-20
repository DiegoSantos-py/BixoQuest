package exception.Semestre;

public class SemestreNaoEncontradoException extends RuntimeException {

    private final int jogadorId;

    public SemestreNaoEncontradoException(int jogadorId) {
        super("Nenhum semestre encontrado para o jogador com id: " + jogadorId);
        this.jogadorId = jogadorId;
    }

    public int getJogadorId() {
        return jogadorId;
    }
}

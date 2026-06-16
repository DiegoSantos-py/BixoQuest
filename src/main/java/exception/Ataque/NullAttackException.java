package exception.Ataque;

public class NullAttackException extends RuntimeException {

    public NullAttackException() {
        super("Ataque Inválido — tentou executar um ataque que não foi inicializado (nulo).");
    }

}

package exception.Utils;

/**
 Lançada quando uma Disciplina possui dados inválidos ou nulos
 ao ser inserida no repositório.

 */
public class HitboxInvalidaException extends RuntimeException {

    private final String campoCausador;

    public HitboxInvalidaException(String campoCausador, String detalhe) {
        super("Hitbox Inválida — campo '" + campoCausador + "': " + detalhe);
        this.campoCausador = campoCausador;
    }

    public String getCampoCausador() {
        return campoCausador;
    }
}
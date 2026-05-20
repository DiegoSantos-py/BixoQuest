package exception.Disciplina;

/**
  Lançada quando uma Disciplina possui dados inválidos ou nulos
  ao ser inserida no repositório.

 */
public class DisciplinaInvalidaException extends RuntimeException {

    private final String campoCausador;

    public DisciplinaInvalidaException(String campoCausador, String detalhe) {
        super("Disciplina inválida — campo '" + campoCausador + "': " + detalhe);
        this.campoCausador = campoCausador;
    }

    public String getCampoCausador() {
        return campoCausador;
    }
}
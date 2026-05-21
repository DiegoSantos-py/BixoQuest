package exception.Evento.Prova;

/**
 Lançada quando uma Disciplina possui dados inválidos ou nulos
 ao ser inserida no repositório.

 */
public class ResultadoProvaInvalidoException extends RuntimeException {

    private final String campoCausador;

    public ResultadoProvaInvalidoException(String campoCausador, String detalhe) {
        super("Resultado Prova Inválido — campo '" + campoCausador + "': " + detalhe);
        this.campoCausador = campoCausador;
    }

    public String getCampoCausador() {
        return campoCausador;
    }
}
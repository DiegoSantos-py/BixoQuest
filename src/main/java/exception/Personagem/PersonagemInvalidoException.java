package exception.Personagem;

public class PersonagemInvalidoException extends RuntimeException {

    private final String campoCausador;

    public PersonagemInvalidoException(String campoCausador, String detalhe) {
        super("Personagem inválido — campo '" + campoCausador + "': " + detalhe);
        this.campoCausador = campoCausador;
    }

    public String getCampoCausador() {
        return campoCausador;
    }
}

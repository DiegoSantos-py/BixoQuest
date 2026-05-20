package exception.Npc;

public class NpcInvalidoException extends RuntimeException {

    private final String campoCausador;

    public NpcInvalidoException(String campoCausador, String detalhe) {
        super("Npc inválido — campo '" + campoCausador + "': " + detalhe);
        this.campoCausador = campoCausador;
    }

    public String getCampoCausador() {
        return campoCausador;
    }
}

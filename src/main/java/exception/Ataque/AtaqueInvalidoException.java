package exception.Ataque;


public class AtaqueInvalidoException extends RuntimeException {

    private final String campoCausador;

    public AtaqueInvalidoException(String campoCausador, String detalhe) {
        super("Ataque Inválido — campo '" + campoCausador + "': " + detalhe);
        this.campoCausador = campoCausador;
    }

    public String getCampoCausador() {
        return campoCausador;
    }
}

/*        this.target = target;
        this.owner = owner;
        this.dificuldade = dificuldade;
        this.maxX = 1160f;
        this.maxY = 1000f;
        this.minX = 760f;
        this.minY = 600f;*/
package exception.Personagem;

public class PersonagemNaoEncontradoException extends RuntimeException {

    private final int personagemId;

    public PersonagemNaoEncontradoException(int personagemId) {
        super("Personagem não encontrado com id: " + personagemId);
        this.personagemId = personagemId;
    }

    public int getPersonagemId() {
        return personagemId;
    }
}

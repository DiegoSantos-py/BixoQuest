package exception.Personagem;

public class PersonagemDuplicadoException extends RuntimeException {

    private final int personagemId;

    public PersonagemDuplicadoException(int personagemId) {
        super("Personagem já cadastrado com id: " + personagemId);
        this.personagemId = personagemId;
    }

    public int getPersonagemId() {
        return personagemId;
    }
}

package exception.Evento.Prova;

public class ResultadoProvaDuplicadoException  extends RuntimeException  {

    private final String nome;

    public ResultadoProvaDuplicadoException(String nome) {
        super("Resultado de Prova já cadastrado: " + nome);
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}

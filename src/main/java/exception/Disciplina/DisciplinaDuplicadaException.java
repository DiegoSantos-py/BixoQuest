package exception.Disciplina;

/**
  Lançada quando se tenta adicionar uma Disciplina que já existe
  no repositório (mesmo nome e código).
  É uma exceção não verificada, pois representa uma violação de
  regra de negócio detectável antes da chamada.
 */
public class DisciplinaDuplicadaException extends RuntimeException {

    private final String nome;
    private final float  codigo;

    public DisciplinaDuplicadaException(String nome, float codigo) {
        super("Disciplina já cadastrada — nome: '" + nome + "', código: " + codigo);
        this.nome   = nome;
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public float getCodigo() {
        return codigo;
    }
}
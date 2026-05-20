package exception.Disciplina;

/**
  Lançada quando uma Disciplina buscada não é encontrada no repositório.
  É uma exceção não verificada (RuntimeException) pois "não encontrado"
  é uma condição de negócio que o Controller pode tratar opcionalmente.


 */
public class DisciplinaNaoEncontradaException extends RuntimeException {

    private final String nome;
    private final Float codigo;

    /** Busca apenas por nome (ex: buscarPorNome, buscarPorArea) */
    public DisciplinaNaoEncontradaException(String nome) {
        super("Nenhuma disciplina encontrada com o nome: " + nome);
        this.nome   = nome;
        this.codigo = null;
    }

    /** Busca por nome + código (ex: buscar, proximaDisciplina) */
    public DisciplinaNaoEncontradaException(String nome, float codigo) {
        super("Disciplina não encontrada — nome: '" + nome + "', código: " + codigo);
        this.nome   = nome;
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public Float getCodigo() {
        return codigo;
    }
}
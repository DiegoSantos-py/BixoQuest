package exception;

/**
  Exceção base para todas as falhas de repositório.
  Envolve erros de infraestrutura (I/O, serialização) em uma
  abstração de domínio, desacoplando o Controller dos detalhes técnicos.
 */
public class RepositoryException extends Exception {

    public RepositoryException(String mensagem) {
        super(mensagem);
    }

    public RepositoryException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}

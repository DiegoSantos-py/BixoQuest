package exception;

/**
  Lançada quando ocorre falha ao persistir ou carregar dados do arquivo JSON.
  Cobre erros de I/O e de serialização/desserialização do Jackson.

  Exemplos de uso:
    - Falha ao escrever em disciplinas.json (sem permissão, disco cheio)
    - JSON malformado ou incompatível com Map<String, List<Disciplina>>
 */
public class PersistenciaException extends RepositoryException {

    private final OperacaoPersistencia operacao;

    public PersistenciaException(OperacaoPersistencia operacao, Throwable causa) {
        super(operacao.getMensagem(), causa);
        this.operacao = operacao;
    }

    public PersistenciaException(OperacaoPersistencia operacao, String detalhe, Throwable causa) {
        super(operacao.getMensagem() + ": " + detalhe, causa);
        this.operacao = operacao;
    }

    public OperacaoPersistencia getOperacao() {
        return operacao;
    }
}
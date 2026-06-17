package exception;

/**
  Representa a operação de persistência que originou uma falha.
  Utilizado por {PersistenciaException} para contextualizar o erro.
 */
public enum OperacaoPersistencia {

    SALVAR("Erro ao salvar dados no arquivo"),
    CARREGAR("Erro ao carregar dados do arquivo");

    private final String mensagem;

    OperacaoPersistencia(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}
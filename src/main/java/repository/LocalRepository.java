package repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import exception.Local.LocalDuplicadoException;
import exception.Local.LocalInvalidoException;
import exception.Local.LocalNaoEncontradoException;
import exception.OperacaoPersistencia;
import exception.PersistenciaException;
import model.Local.Direcao;
import model.Local.Local;
import model.Local.TipoLocal;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
  Repositório responsável por armazenar, buscar e persistir os locais do jogo.

  Os locais são mantidos em memória em um Map, usando o nome do local como chave.
  Também é responsável por salvar e carregar esses dados em um arquivo JSON.
 */
public class LocalRepository {

    // Estrutura principal de armazenamento:
    // chave -> nome do local
    // valor -> objeto Local correspondente
    private Map<String, Local> locais;

    // ObjectMapper utilizado para converter objetos Java em JSON e JSON em objetos Java
    private static final ObjectMapper mapper = criarMapper();

    // Arquivo onde os locais serão salvos/carregados
    private static final File ARQUIVO = new File("locais.json");

    /**
      Cria o repositório inicializando o mapa de locais vazio.
     */
    public LocalRepository() {
        this.locais = new HashMap<>();
    }

    private static ObjectMapper criarMapper() {
        ObjectMapper m = new ObjectMapper();

        return m;
    }


    /**
      Salva o mapa de locais no arquivo JSON.

      lança PersistenciaException se ocorrer falha ao salvar o arquivo
     */
    public void salvar() throws PersistenciaException {
        try {
            // Converte o mapa "locais" para JSON formatado
            // e grava no arquivo definido por ARQUIVO.
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(ARQUIVO, locais);

        } catch (Exception e) {
            // Encapsula qualquer erro em uma exceção própria da camada de persistência.
            throw new PersistenciaException(OperacaoPersistencia.SALVAR, e);
        }
    }

    /**
      Carrega os locais do arquivo JSON para o mapa em memória.

      Após carregar os objetos Local, reconstrói as referências entre locais vizinhos.

      lança PersistenciaException se ocorrer falha ao carregar o arquivo
     */
    public void carregar() throws PersistenciaException {
        // Se o arquivo ainda não existir, não há nada a carregar.
        if (!ARQUIVO.exists()) return;

        try {
            // Lê o arquivo JSON e converte para:
            // HashMap<String, Local>
            this.locais = mapper.readValue(
                    ARQUIVO,
                    mapper.getTypeFactory()
                            .constructMapType(
                                    HashMap.class,
                                    String.class,
                                    Local.class
                            )
            );

            /*
              Reconstrói os vizinhos reais de cada local.

              Durante a persistência, geralmente não se salva diretamente
              a referência completa para outro Local, pois isso poderia gerar
              ciclos no JSON.

              Por isso, salva-se como:

              "vizinhosNomes": {
                  "CIMA": "Sala 1",
                  "DIREITA": "Cantina 1"
              }

              Depois do carregamento, esses nomes são convertidos novamente
              para referências reais dentro do mapa de vizinhos.
             */
            for (Local local : locais.values()) {
                for (Map.Entry<String, String> entry : local.getVizinhosNomes().entrySet()) {

                    // Converte a String salva no JSON para o enum Direcao.
                    Direcao direcao = Direcao.valueOf(entry.getKey());

                    // Busca o objeto Local correspondente ao nome salvo.
                    Local vizinho = locais.get(entry.getValue());

                    // Se o vizinho existir no mapa, adiciona a referência real.
                    if (vizinho != null) {
                        local.getVizinhos().put(direcao, vizinho);
                    }
                }
            }

        } catch (Exception e) {
            throw new PersistenciaException(OperacaoPersistencia.CARREGAR, e);
        }
    }


    /**
      Adiciona um novo local ao repositório.

      Valida:
      - se o local não é nulo;
      - se o nome do local não é nulo ou vazio;
      - se já não existe outro local com o mesmo nome.

      lança LocalInvalidoException se o local ou seu nome forem nulos/vazios
      lança LocalDuplicadoException se já existir local com o mesmo nome
     */
    public void adicionarLocal(Local local) {
        if (local == null) {
            throw new LocalInvalidoException("local", "não pode ser nulo");
        }

        if (local.getNome() == null || local.getNome().isBlank()) {
            throw new LocalInvalidoException("nome", "não pode ser nulo ou vazio");
        }

        if (locais.containsKey(local.getNome())) {
            throw new LocalDuplicadoException(local.getNome());
        }

        // Insere o local no mapa usando seu nome como chave.
        locais.put(local.getNome(), local);
    }


    /**
     lança LocalNaoEncontradoException se não existir local com o nome informado
     */
    public Local buscarPorNome(String nome) {
        Local local = locais.get(nome);

        if (local == null) {
            throw new LocalNaoEncontradoException(nome);
        }

        return local;
    }

    /**
      Busca o primeiro local encontrado com determinado tipo.

      Exemplo: buscar o primeiro local do tipo PONTO_ONIBUS, SALA, CANTINA etc.
      lança LocalNaoEncontradoException se não existir local com o tipo informado
     */
    public Local buscarPorTipo(TipoLocal tipo) {
        for (Local local : locais.values()) {
            if (local.getTipo() == tipo) {
                return local;
            }
        }

        throw new LocalNaoEncontradoException(tipo.name());
    }

    /**
      Retorna uma visão somente leitura do mapa de locais.

      Isso impede que outras classes modifiquem diretamente
      a estrutura interna do repositório.
     */
    public Map<String, Local> carregarLocais() {
        return Collections.unmodifiableMap(locais);
    }
}
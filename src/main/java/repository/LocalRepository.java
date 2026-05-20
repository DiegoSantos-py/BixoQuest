package repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

public class LocalRepository {

    private Map<String, Local> locais;

    private static final ObjectMapper mapper = criarMapper();
    private static final File ARQUIVO = new File("locais.json");

    public LocalRepository() {
        this.locais = new HashMap<>();
    }

    private static ObjectMapper criarMapper() {
        ObjectMapper m = new ObjectMapper();
        m.registerModule(new JavaTimeModule());
        m.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return m;
    }

    // Persistência
    /**@throws PersistenciaException se ocorrer falha ao salvar o arquivo*/
    public void salvar() throws PersistenciaException {
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(ARQUIVO, locais);
        } catch (Exception e) {
            throw new PersistenciaException(OperacaoPersistencia.SALVAR, e);
        }
    }

    /**@throws PersistenciaException se ocorrer falha ao carregar o arquivo*/
    public void carregar() throws PersistenciaException {
        if (!ARQUIVO.exists()) return;

        try {
            this.locais = mapper.readValue(ARQUIVO,
                    mapper.getTypeFactory()
                            .constructMapType(HashMap.class, String.class, Local.class));

            for (Local local : locais.values()) {
                for (Map.Entry<String, String> entry : local.getVizinhosNomes().entrySet()) {
                    Direcao direcao = Direcao.valueOf(entry.getKey());
                    Local vizinho = locais.get(entry.getValue());
                    if (vizinho != null) {
                        local.getVizinhos().put(direcao, vizinho);
                    }
                }
            }
        } catch (Exception e) {
            throw new PersistenciaException(OperacaoPersistencia.CARREGAR, e);
        }
    }

    // Escrita
    /**@throws LocalInvalidoException  se o local ou seu nome forem nulos/vazios
     @throws LocalDuplicadoException se já existir local com o mesmo nome*/
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

        locais.put(local.getNome(), local);
    }

    // Leitura
    /**@throws LocalNaoEncontradoException se não existir local com o nome informado*/
    public Local buscarPorNome(String nome) {
        Local local = locais.get(nome);

        if (local == null) {
            throw new LocalNaoEncontradoException(nome);
        }

        return local;
    }

    /**@throws LocalNaoEncontradoException se não existir local com o tipo informado*/
    public Local buscarPorTipo(TipoLocal tipo) {
        for (Local local : locais.values()) {
            if (local.getTipo() == tipo) {
                return local;
            }
        }

        throw new LocalNaoEncontradoException(tipo.name());
    }

    public Map<String, Local> carregarLocais() {
        return Collections.unmodifiableMap(locais);
    }
}
package repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import model.Local.Direcao;
import model.Local.Local;
import model.Local.TipoLocal;

import java.io.File;
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

    public void salvar() throws Exception {
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(ARQUIVO, locais);
    }

    public void carregar() throws Exception {
        if (!ARQUIVO.exists()) return;

        // 1. Carrega todos os locais sem vizinhos
        Map<String, Local> carregados = mapper.readValue(ARQUIVO,
                mapper.getTypeFactory()
                        .constructMapType(HashMap.class, String.class, Local.class));

        this.locais = carregados;

        // 2. Reconstrói as referências de vizinhos pelos nomes
        for (Local local : locais.values()) {
            for (Map.Entry<String, String> entry : local.getVizinhosNomes().entrySet()) {
                Direcao direcao = Direcao.valueOf(entry.getKey());
                Local vizinho = locais.get(entry.getValue());
                if (vizinho != null) {
                    local.getVizinhos().put(direcao, vizinho);
                }
            }
        }
    }

    public void adicionarLocal(Local local){
        if (local == null || local.getNome() == null) {
            return;
        }

        //verifica se local ja existe no repositório
        if (this.locais.containsKey(local.getNome())){
            return;
        }

        // adiciona local
        this.locais.put(local.getNome(), local);
    }

    public Map<String, Local> carregarLocal(){
        return this.locais;
    }

    public Local buscarPorTipo(TipoLocal tipo){
        for (String l: this.locais.keySet()){
                if (locais.get(l).getTipo() == tipo){
                    return locais.get(l);
                }
        }
        return null;
    }
}


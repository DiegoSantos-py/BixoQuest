package repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import exception.Npc.NpcDuplicadoException;
import exception.Npc.NpcInvalidoException;
import exception.Npc.NpcNaoEncontradoException;
import exception.OperacaoPersistencia;
import exception.PersistenciaException;
import model.Npc.Animal;
import model.Npc.Colega;
import model.Npc.Npc;
import model.Npc.Professor;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NpcRepository {

    private Map<String, Npc> npcs;

    private static final ObjectMapper mapper = criarMapper();
    private static final File ARQUIVO = new File("npcs.json");

    public NpcRepository() {
        this.npcs = new HashMap<>();
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
            var type = mapper.getTypeFactory()
                    .constructMapType(HashMap.class, String.class, Npc.class);

            mapper.writerFor(type)
                    .withDefaultPrettyPrinter()
                    .writeValue(ARQUIVO, npcs);
        } catch (Exception e) {
            throw new PersistenciaException(OperacaoPersistencia.SALVAR, e);
        }
    }

    /**@throws PersistenciaException se ocorrer falha ao carregar o arquivo*/
    public void carregar() throws PersistenciaException {
        if (!ARQUIVO.exists()) return;

        try {
            this.npcs = mapper.readValue(ARQUIVO,
                    mapper.getTypeFactory()
                            .constructMapType(HashMap.class, String.class, Npc.class));
        } catch (Exception e) {
            throw new PersistenciaException(OperacaoPersistencia.CARREGAR, e);
        }
    }

    // Escrita
    /**@throws NpcInvalidoException  se o npc ou seu nome forem nulos/vazios
     @throws NpcDuplicadoException se já existir npc com o mesmo nome*/
    public void adicionarNpc(Npc npc) {
        if (npc == null) {
            throw new NpcInvalidoException("npc", "não pode ser nulo");
        }
        if (npc.getNome() == null || npc.getNome().isBlank()) {
            throw new NpcInvalidoException("nome", "não pode ser nulo ou vazio");
        }
        if (npcs.containsKey(npc.getNome())) {
            throw new NpcDuplicadoException(npc.getNome());
        }

        npcs.put(npc.getNome(), npc);
    }

    /**@throws NpcInvalidoException se o npc ou seu nome forem nulos/vazios
     @throws NpcNaoEncontradoException se não existir npc com o mesmo nome para atualizar*/
    public void atualizarNpc(Npc npc) {
        if (npc == null) {
            throw new NpcInvalidoException("npc", "não pode ser nulo");
        }
        if (npc.getNome() == null || npc.getNome().isBlank()) {
            throw new NpcInvalidoException("nome", "não pode ser nulo ou vazio");
        }
        if (!npcs.containsKey(npc.getNome())) {
            throw new NpcNaoEncontradoException(npc.getNome());
        }

        npcs.put(npc.getNome(), npc);
    }

    // Leitura
    /**@throws NpcNaoEncontradoException se não existir npc com o nome informado*/
    public Npc buscarPorNome(String nome) {
        Npc npc = npcs.get(nome);

        if (npc == null) {
            throw new NpcNaoEncontradoException(nome);
        }

        return npc;
    }

    public List<Professor> buscarProfessores() {
        return npcs.values().stream()
                .filter(n -> n instanceof Professor)
                .map(n -> (Professor) n)
                .collect(Collectors.toList());
    }

    public List<Colega> buscarColegas() {
        return npcs.values().stream()
                .filter(n -> n instanceof Colega)
                .map(n -> (Colega) n)
                .collect(Collectors.toList());
    }

    public List<Animal> buscarAnimais() {
        return npcs.values().stream()
                .filter(n -> n instanceof Animal)
                .map(n -> (Animal) n)
                .collect(Collectors.toList());
    }

    public Map<String, Npc> carregarNpcs() {
        return Collections.unmodifiableMap(npcs);
    }
}
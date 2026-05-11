package repository;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import model.Personagem;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PersonagemRepository {

    private Map<Integer, Personagem> personagens = new HashMap<>();

    private static final ObjectMapper mapper = criarMapper();
    private static final File ARQUIVO = new File("personagens.json");

    public PersonagemRepository() {
        this.personagens = new HashMap<>();
    }

    private static ObjectMapper criarMapper() {
        ObjectMapper m = new ObjectMapper();
        m.registerModule(new JavaTimeModule());
        m.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        m.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS); // lê enums como chave
        return m;
    }

    public void salvar() throws IOException {
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(ARQUIVO, personagens);
    }

    public void carregar() throws IOException {
        if (!ARQUIVO.exists()) return;

        this.personagens = mapper.readValue(ARQUIVO,
                mapper.getTypeFactory().constructMapType(
                        HashMap.class, Integer.class, Personagem.class));
    }

    public void adicionarPersonagem(Personagem personagem) {
        if (personagem == null || this.personagens.containsKey(personagem.getPersonagemId())) {
            return;
        }
        this.personagens.put(personagem.getPersonagemId(), personagem);
    }

    public boolean existePersonagem(Personagem personagem) {
        if (personagem == null || !this.personagens.containsKey(personagem.getPersonagemId())) {
            return false;
        }
        return true;
    }

    public Map<Integer, Personagem> carregarPersonagens() {
        return this.personagens;
    }
}
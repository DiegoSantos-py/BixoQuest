package repository;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import exception.OperacaoPersistencia;
import exception.Personagem.PersonagemDuplicadoException;
import exception.Personagem.PersonagemInvalidoException;
import exception.Personagem.PersonagemNaoEncontradoException;
import exception.PersistenciaException;
import model.Personagem;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PersonagemRepository {

    private Map<Integer, Personagem> personagens;

    private static final ObjectMapper mapper = criarMapper();
    private static final File ARQUIVO = new File("personagens.json");

    public PersonagemRepository() {
        this.personagens = new HashMap<>();
    }

    private static ObjectMapper criarMapper() {
        ObjectMapper m = new ObjectMapper();
        m.registerModule(new JavaTimeModule());
        m.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        m.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        return m;
    }

    // Persistência
    /**@throws PersistenciaException se ocorrer falha ao salvar o arquivo*/
    public void salvar() throws PersistenciaException {
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(ARQUIVO, personagens);
        } catch (Exception e) {
            throw new PersistenciaException(OperacaoPersistencia.SALVAR, e);
        }
    }

    /**@throws PersistenciaException se ocorrer falha ao carregar o arquivo*/
    public void carregar() throws PersistenciaException {
        if (!ARQUIVO.exists()) return;

        try {
            this.personagens = mapper.readValue(ARQUIVO,
                    mapper.getTypeFactory().constructMapType(
                            HashMap.class, Integer.class, Personagem.class));
        } catch (Exception e) {
            throw new PersistenciaException(OperacaoPersistencia.CARREGAR, e);
        }
    }

    // Escrita
    /**@throws PersonagemInvalidoException  se o personagem for nulo ou com nome nulo/vazio
     @throws PersonagemDuplicadoException se já existir personagem com o mesmo id*/
    public void adicionarPersonagem(Personagem personagem) {
        if (personagem == null) {
            throw new PersonagemInvalidoException("personagem", "não pode ser nulo");
        }
        if (personagem.getNome() == null || personagem.getNome().isBlank()) {
            throw new PersonagemInvalidoException("nome", "não pode ser nulo ou vazio");
        }
        if (personagens.containsKey(personagem.getPersonagemId())) {
            throw new PersonagemDuplicadoException(personagem.getPersonagemId());
        }

        personagens.put(personagem.getPersonagemId(), personagem);
    }

    // Leitura
    /**@throws PersonagemNaoEncontradoException se não existir personagem com o id informado*/
    public Personagem buscarPorId(int id) {
        Personagem personagem = personagens.get(id);

        if (personagem == null) {
            throw new PersonagemNaoEncontradoException(id);
        }

        return personagem;
    }

    public boolean existePersonagem(Personagem personagem) {
        if (personagem == null) return false;
        return personagens.containsKey(personagem.getPersonagemId());
    }

    public Map<Integer, Personagem> carregarPersonagens() {
        return Collections.unmodifiableMap(personagens);
    }
}
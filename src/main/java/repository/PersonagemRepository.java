package repository;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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

/**
 Repositório responsável por armazenar, buscar e persistir os personagens do jogo.

 Os personagens são mantidos em memória em um Map, usando o identificador do
 personagem como chave. Também é responsável por salvar e carregar esses dados
 em um arquivo JSON.
 */
public class PersonagemRepository {

    // Estrutura principal de armazenamento:
    // chave -> id do personagem
    // valor -> objeto Personagem correspondente
    private Map<Integer, Personagem> personagens;

    // ObjectMapper utilizado para converter objetos Java em JSON e JSON em objetos Java
    private static final ObjectMapper mapper = criarMapper();

    // Arquivo onde os personagens serão salvos/carregados
    private static final File ARQUIVO = new File("personagens.json");

    /**
     Cria o repositório inicializando o mapa de personagens vazio.
     */
    public PersonagemRepository() {
        this.personagens = new HashMap<>();
    }

    /**
     Cria e configura o ObjectMapper usado na persistência JSON.
     A configuração ACCEPT_CASE_INSENSITIVE_ENUMS permite que enums sejam
     carregados mesmo quando houver diferença entre letras maiúsculas e minúsculas.
     */
    private static ObjectMapper criarMapper() {
        ObjectMapper m = new ObjectMapper();

        // Permite desserializar enums sem diferenciar maiúsculas e minúsculas.
        m.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);

        return m;
    }

    /**
     Salva o mapa de personagens no arquivo JSON.
     lança PersistenciaException se ocorrer falha ao salvar o arquivo
     */
    public void salvar() throws PersistenciaException {
        try {
            // Converte o mapa "personagens" para JSON formatado
            // e grava no arquivo definido por ARQUIVO.
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(ARQUIVO, personagens);

        } catch (Exception e) {
            // Encapsula qualquer erro em uma exceção própria da camada de persistência.
            throw new PersistenciaException(OperacaoPersistencia.SALVAR, e);
        }
    }

    /**
     Carrega os personagens do arquivo JSON para o mapa em memória.
     lança PersistenciaException se ocorrer falha ao carregar o arquivo
     */
    public void carregar() throws PersistenciaException {
        // Se o arquivo ainda não existir, não há nada a carregar.
        if (!ARQUIVO.exists()) return;

        try {
            // Lê o arquivo JSON e converte para:
            // HashMap<Integer, Personagem>
            this.personagens = mapper.readValue(
                    ARQUIVO,
                    mapper.getTypeFactory()
                            .constructMapType(
                                    HashMap.class,
                                    Integer.class,
                                    Personagem.class
                            )
            );

        } catch (Exception e) {
            throw new PersistenciaException(OperacaoPersistencia.CARREGAR, e);
        }
    }

    /**
     Adiciona um novo personagem ao repositório.

     Valida:
     - se o personagem não é nulo;
     - se o nome do personagem não é nulo ou vazio;
     - se já não existe outro personagem com o mesmo id.

     lança PersonagemInvalidoException se o personagem for nulo ou com nome nulo/vazio
     lança PersonagemDuplicadoException se já existir personagem com o mesmo id
     */
    public void adicionarPersonagem(Personagem personagem) {
        if (personagem == null) {
            throw new PersonagemInvalidoException("personagem", "não pode ser nulo");}

        if (personagem.getNome() == null || personagem.getNome().isBlank()) {
            throw new PersonagemInvalidoException("nome", "não pode ser nulo ou vazio");}

        if (personagens.containsKey(personagem.getPersonagemId())) {
            throw new PersonagemDuplicadoException(personagem.getPersonagemId());}

        // Insere o personagem no mapa usando seu id como chave.
        personagens.put(personagem.getPersonagemId(), personagem);
    }

    /**
     Busca um personagem pelo id informado.

     lança PersonagemNaoEncontradoException se não existir personagem com o id informado
     */
    public Personagem buscarPorId(int id) {
        Personagem personagem = personagens.get(id);

        if (personagem == null) {
            throw new PersonagemNaoEncontradoException(id);}

        return personagem;
    }

    /**
     Verifica se um personagem já existe no repositório.

     Retorna false caso o personagem informado seja nulo.
     Caso contrário, verifica a existência pelo id do personagem.
     */
    public boolean existePersonagem(Personagem personagem) {
        if (personagem == null) return false;

        return personagens.containsKey(personagem.getPersonagemId());
    }

    /**
     Retorna uma visão somente leitura do mapa de personagens.

     Isso impede que outras classes modifiquem diretamente
     a estrutura interna do repositório.
     */
    public Map<Integer, Personagem> carregarPersonagens() {
        return Collections.unmodifiableMap(personagens);
    }
}
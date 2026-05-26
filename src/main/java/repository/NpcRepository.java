package repository;

import com.fasterxml.jackson.databind.ObjectMapper;
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

/**
 Repositório responsável por armazenar, buscar e persistir os NPCs do jogo.

 Os NPCs são mantidos em memória em um Map utilizando o nome como chave.
 Também fornece operações para recuperar subconjuntos específicos
 de NPCs como professores, colegas e animais.
 */
public class NpcRepository {

    // Estrutura principal de armazenamento:
    // chave -> nome do NPC
    // valor -> objeto Npc correspondente
    private Map<String, Npc> npcs;

    // ObjectMapper utilizado para serialização e desserialização JSON
    private static final ObjectMapper mapper = criarMapper();

    // Arquivo responsável por armazenar os NPCs persistidos
    private static final File ARQUIVO = new File("npcs.json");

    /**
     Cria o repositório inicializando o mapa de NPCs vazio.
     */
    public NpcRepository() {
        this.npcs = new HashMap<>();
    }

    private static ObjectMapper criarMapper() {
        ObjectMapper m = new ObjectMapper();

        return m;
    }


    /**
     Salva o mapa de NPCs no arquivo JSON.

     Define explicitamente o tipo do mapa para garantir
     que o Jackson preserve corretamente a estrutura.

     lança PersistenciaException se ocorrer falha ao salvar o arquivo
     */
    public void salvar() throws PersistenciaException {
        try {

            // Define o tipo:
            // HashMap<String, Npc>
            var type = mapper.getTypeFactory()
                    .constructMapType(
                            HashMap.class,
                            String.class,
                            Npc.class
                    );

            // Serializa os NPCs para JSON formatado
            mapper.writerFor(type)
                    .withDefaultPrettyPrinter()
                    .writeValue(ARQUIVO, npcs);

        } catch (Exception e) {

            // Encapsula erros em exceção própria da persistência
            throw new PersistenciaException(
                    OperacaoPersistencia.SALVAR,
                    e
            );
        }
    }

    /**
     Carrega os NPCs do arquivo JSON para memória.

     Reconstrói o mapa contendo os objetos carregados.

     lança PersistenciaException se ocorrer falha ao carregar o arquivo
     */
    public void carregar() throws PersistenciaException {

        // Se o arquivo não existir,
        // não existe estado salvo para recuperar.
        if (!ARQUIVO.exists()) return;

        try {

            // Converte JSON para:
            // HashMap<String, Npc>
            this.npcs = mapper.readValue(
                    ARQUIVO,

                    mapper.getTypeFactory()
                            .constructMapType(
                                    HashMap.class,
                                    String.class,
                                    Npc.class
                            )
            );

        } catch (Exception e) {

            throw new PersistenciaException(
                    OperacaoPersistencia.CARREGAR,
                    e
            );
        }
    }

    /**
     Adiciona um novo NPC ao repositório.

     Valida:
     - se o NPC não é nulo;
     - se o nome não é nulo ou vazio;
     - se ainda não existe NPC com o mesmo nome.

     lança NpcInvalidoException se o npc ou seu nome forem nulos/vazios
     lança NpcDuplicadoException se já existir npc com o mesmo nome
     */
    public void adicionarNpc(Npc npc) {

        if (npc == null) {
            throw new NpcInvalidoException(
                    "npc",
                    "não pode ser nulo"
            );
        }

        if (
                npc.getNome() == null ||
                        npc.getNome().isBlank()
        ) {
            throw new NpcInvalidoException(
                    "nome",
                    "não pode ser nulo ou vazio"
            );
        }

        if (
                npcs.containsKey(
                        npc.getNome()
                )
        ) {
            throw new NpcDuplicadoException(
                    npc.getNome()
            );
        }

        // Insere o NPC usando o nome como chave
        npcs.put(
                npc.getNome(),
                npc
        );
    }

    /**
     Atualiza um NPC já existente.

     Valida:
     - se o NPC não é nulo;
     - se o nome é válido;
     - se existe um registro correspondente.

     lança NpcInvalidoException se o npc ou seu nome forem nulos/vazios
     lança NpcNaoEncontradoException se não existir npc com o mesmo nome para atualizar
     */
    public void atualizarNpc(Npc npc) {

        if (npc == null) {
            throw new NpcInvalidoException(
                    "npc",
                    "não pode ser nulo"
            );
        }

        if (
                npc.getNome() == null ||
                        npc.getNome().isBlank()
        ) {
            throw new NpcInvalidoException(
                    "nome",
                    "não pode ser nulo ou vazio"
            );
        }

        if (
                !npcs.containsKey(
                        npc.getNome()
                )
        ) {
            throw new NpcNaoEncontradoException(
                    npc.getNome()
            );
        }

        // Substitui o NPC antigo pelo atualizado
        npcs.put(
                npc.getNome(),
                npc
        );
    }

    /**
     Busca um NPC pelo nome.
     lança NpcNaoEncontradoException se não existir npc com o nome informado
     */
    public Npc buscarPorNome(String nome) {

        Npc npc = npcs.get(nome);

        if (npc == null) {
            throw new NpcNaoEncontradoException(nome);
        }

        return npc;
    }

    /**
     Retorna todos os NPCs do tipo Professor.

     Filtra apenas instâncias de Professor
     e converte para uma lista tipada.
     */
    public List<Professor> buscarProfessores() {

        return npcs.values().stream()

                .filter(n -> n instanceof Professor)
                .map(n -> (Professor) n)
                .collect(Collectors.toList());
    }

    /**
     Retorna todos os NPCs do tipo Colega.

     Filtra apenas instâncias de Colega
     e converte para uma lista tipada.
     */
    public List<Colega> buscarColegas() {

        return npcs.values().stream()

                .filter(n -> n instanceof Colega)
                .map(n -> (Colega) n)
                .collect(Collectors.toList());
    }

    /**
     Retorna todos os NPCs do tipo Animal.

     Filtra apenas instâncias de Animal
     e converte para uma lista tipada.
     */
    public List<Animal> buscarAnimais() {

        return npcs.values().stream()

                .filter(n -> n instanceof Animal)
                .map(n -> (Animal) n)
                .collect(Collectors.toList());
    }

    /**
     Retorna uma visão somente leitura do mapa de NPCs.
     Isso impede alterações externas na estrutura interna
     do repositório.
     */
    public Map<String, Npc> carregarNpcs() {

        return Collections.unmodifiableMap(
                npcs
        );
    }
}
package service;

import exception.Npc.NpcDuplicadoException;
import exception.Npc.NpcInvalidoException;
import exception.Npc.NpcNaoEncontradoException;
import exception.PersistenciaException;
import model.Npc.Animal;
import model.Npc.Colega;
import model.Npc.Npc;
import model.Npc.Professor;
import model.Personagem;
import repository.NpcRepository;

import java.util.List;
import java.util.Map;

public class NpcService {

    private final NpcRepository npcRepo;

    public NpcService(NpcRepository npcRepo) {
        this.npcRepo = npcRepo;
    }

    // Inicialização
    /**lança PersistenciaException se ocorrer falha ao carregar o arquivo */
    public void carregar() throws PersistenciaException {
        npcRepo.carregar();
    }

    /**lança PersistenciaException se ocorrer falha ao salvar o arquivo */
    public void salvar() throws PersistenciaException {
        npcRepo.salvar();
    }

    // Escrita
    /**
     *lança NpcInvalidoException  se o npc ou seu nome forem nulos/vazios
     *lança NpcDuplicadoException se já existir npc com o mesmo nome
     *lança PersistenciaException se ocorrer falha ao salvar após adição
     */
    public void adicionarNpc(Npc npc) throws PersistenciaException {
        npcRepo.adicionarNpc(npc);
        npcRepo.salvar();
    }

    // Leitura
    /**lança NpcNaoEncontradoException se não existir npc com o nome informado */
    public Npc buscarPorNome(String nome) {
        return npcRepo.buscarPorNome(nome);
    }

    public List<Professor> buscarProfessores() {
        return npcRepo.buscarProfessores();
    }

    public List<Colega> buscarColegas() {
        return npcRepo.buscarColegas();
    }

    public List<Animal> buscarAnimais() {
        return npcRepo.buscarAnimais();
    }

    public Map<String, Npc> carregarNpcs() {
        return npcRepo.carregarNpcs();
    }

    // Lógica de negócio
    /**
     *lança NpcNaoEncontradoException se o npc não existir
     *lança NpcInvalidoException      se o personagem for nulo
     */
    public String interagir(String nomeNpc, Personagem personagem) {
        if (personagem == null) {
            throw new NpcInvalidoException("personagem", "não pode ser nulo");
        }

        Npc npc = npcRepo.buscarPorNome(nomeNpc);
        return npc.aoInteragir(personagem);
    }
}

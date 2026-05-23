package controller;

import exception.Npc.NpcDuplicadoException;
import exception.Npc.NpcInvalidoException;
import exception.Npc.NpcNaoEncontradoException;
import exception.PersistenciaException;
import model.Npc.Animal;
import model.Npc.Colega;
import model.Npc.Npc;
import model.Npc.Professor;
import model.Personagem;
import service.NpcService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class NpcController extends BaseController {

    private final NpcService service;

    public NpcController(NpcService service) {
        this.service = service;
    }

    // Inicialização
    /** Exibe erro se ocorrer falha ao carregar o arquivo */
    public void carregar() {
        try {
            service.carregar();
        } catch (PersistenciaException e) {
            tratarErroPersistencia(e);
        }
    }

    // Escrita
    /** Exibe erro se npc for inválido, duplicado ou falhar ao salvar */
    public void adicionarNpc(Npc npc) {
        try {
            service.adicionarNpc(npc);
            exibirSucesso("NPC adicionado com sucesso.");
        } catch (NpcInvalidoException e) {
            tratarInvalido(e);
        } catch (NpcDuplicadoException e) {
            tratarDuplicado(e);
        } catch (PersistenciaException e) {
            tratarErroPersistencia(e);
        }
    }

    // Leitura
    /** Retorna Optional vazio se npc não for encontrado */
    public Optional<Npc> buscarPorNome(String nome) {
        try {
            return Optional.of(service.buscarPorNome(nome));
        } catch (NpcNaoEncontradoException e) {
            tratarNaoEncontrado(e);
            return Optional.empty();
        }
    }

    public List<Professor> buscarProfessores() {
        return service.buscarProfessores();
    }

    public List<Colega> buscarColegas() {
        return service.buscarColegas();
    }

    public List<Animal> buscarAnimais() {
        return service.buscarAnimais();
    }

    public Map<String, Npc> carregarNpcs() {
        return service.carregarNpcs();
    }

    // Lógica de negócio
    /** Retorna string vazia se npc não for encontrado ou personagem for inválido */
    public String interagir(String nomeNpc, Personagem personagem) {
        try {
            return service.interagir(nomeNpc, personagem);
        } catch (NpcNaoEncontradoException e) {
            tratarNaoEncontrado(e);
            return "";
        } catch (NpcInvalidoException e) {
            tratarInvalido(e);
            return "";
        }
    }

    // Exibição
    @Override
    protected void exibirErro(String mensagem) {
        System.err.println("[ERRO] " + mensagem);
    }

    @Override
    protected void exibirSucesso(String mensagem) {
        System.out.println("[OK] " + mensagem);
    }

    // Tratamento local de exceções
    private void tratarInvalido(NpcInvalidoException e) {
        exibirErro("Dado inválido no campo '" + e.getCampoCausador() + "': " + e.getMessage());
    }

    private void tratarDuplicado(NpcDuplicadoException e) {
        exibirErro("Já cadastrado: " + e.getMessage());
    }

    private void tratarNaoEncontrado(NpcNaoEncontradoException e) {
        exibirErro("Não encontrado: " + e.getMessage());
    }
}

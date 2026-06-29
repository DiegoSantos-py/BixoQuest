package controller;

import model.Batalha.EstadoBatalha;
import model.Batalha.Oponente;
import model.Disciplina.AreaConhecimento;
import model.Evento.Prova.ProvaBatalha;
import model.Evento.Prova.ProvaFactory;
import model.Evento.Prova.ProvaIDs;
import model.Npc.Animal;
import model.Npc.Especie;
import model.Personagem;
import model.Player.AcaoBatalha;
import model.util.Hitbox;
import model.util.Vector2D;
import repository.NpcRepository;
import repository.ResultadoProvaRepository;
import service.batalha.BatalhaService;

import java.util.ArrayList;
import java.util.List;

public class BatalhaController extends BaseController {

    private final BatalhaService batalhaService;
    private final NpcRepository npcRepository;
    private EstadoBatalha estadoAtual;

    public BatalhaController(BatalhaService batalhaService, NpcRepository npcRepository) {
        this.batalhaService = batalhaService;
        this.npcRepository = npcRepository;
    }

    public void iniciarBatalhaTeste(Animal animalBase) {
        iniciarBatalhaTeste(animalBase, 0f);
    }

    public void iniciarBatalhaTeste(Animal animalBase, float bonusConhecimento) {
        Personagem personagemBase = new Personagem();
        if (bonusConhecimento != 0) {
            for (AreaConhecimento area : AreaConhecimento.values()) {
                personagemBase.atualizarConhecimento(area, bonusConhecimento);
            }
        }
        this.estadoAtual = batalhaService.iniciarBatalha(personagemBase, animalBase, this.npcRepository);
    }

    public void iniciarProvaTeste(ProvaIDs provaId, float bonusConhecimento) {
        Personagem personagemBase = new Personagem();
        if (bonusConhecimento != 0) {
            for (AreaConhecimento area : AreaConhecimento.values()) {
                personagemBase.atualizarConhecimento(area, bonusConhecimento);
            }
        }
        ProvaBatalha prova = ProvaFactory.criar(provaId, 1);
        this.estadoAtual = batalhaService.iniciarBatalha(personagemBase, prova, new ResultadoProvaRepository());
    }

    public void iniciarBatalhaAtim() {
        ArrayList<String> falas = new ArrayList<>();
        falas.add("oi");
        Animal animalBase = new Animal(
                "Atim",
                "/assets/batalha/oponentes/animais/atim.png",
                0,
                0,
                falas,
                Especie.CACHORRO,
                10);
        iniciarBatalhaTeste(animalBase);
    }

    // --- GETTERS PARA A UI ---

    public List<Animal> getAnimaisDisponiveis() {
        if (npcRepository != null) {
            return npcRepository.buscarAnimais();
        }
        return new ArrayList<>();
    }

    public EstadoBatalha getEstadoAtual() {
        return estadoAtual;
    }

    public boolean isBatalhaAnimal() {
        return estadoAtual != null && estadoAtual.isBatalhaAnimal();
    }

    public List<AcaoBatalha> getAcoes() {
        if (estadoAtual == null)
            return new ArrayList<>();

        List<AcaoBatalha> acoes = estadoAtual.getAcoesBatalha();
        return acoes != null ? acoes : new ArrayList<>();
    }

    public void executarAcao(int indexAcao) {
        if (estadoAtual != null) {
            batalhaService.executarAcaoPlayer(estadoAtual, indexAcao);
        }
    }

    public void registrarAtaquePlayer(float multiplicadorPrecisao) {
        if (estadoAtual != null) {
            batalhaService.atacarOponenteAtual(estadoAtual, multiplicadorPrecisao);
        }
    }

    public void atualizar(float dt) {
        if (estadoAtual != null) {
            batalhaService.atualizar(estadoAtual, dt);
        }
    }

    // --- FINALIZAÇÃO ---
    public void finalizarBatalha() {
        {
            batalhaService.finalizarBatalha(estadoAtual);
        }
    }

    @Override
    public void exibirErro(String mensagem) {
        System.err.println("[BatalhaController Erro]: " + mensagem);
    }

    @Override
    public void exibirSucesso(String mensagem) {
        System.out.println("[BatalhaController Sucesso]: " + mensagem);
    }
}

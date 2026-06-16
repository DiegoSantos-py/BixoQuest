package controller;

import model.Batalha.EstadoBatalha;
import model.Batalha.Oponente;
import model.Disciplina.AreaConhecimento;
import model.Npc.Animal;
import model.Npc.Especie;
import model.Personagem;
import model.Player.AcaoBatalha;
import model.util.Hitbox;
import model.util.Vector2D;
import repository.NpcRepository;
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
        Personagem personagemBase = new Personagem();
        this.estadoAtual = batalhaService.iniciarBatalha(personagemBase, animalBase, this.npcRepository);
    }

    public void iniciarBatalhaAtim() {
        ArrayList<String> falas = new ArrayList<>();
        falas.add("oi");
        Animal animalBase = new Animal(
                "Atim",
                "assets/batalha/oponentes/animais/atim.png",
                "assets/batalha/oponentes/animais/atim.png",
                0,
                0,
                falas,
                Especie.CACHORRO,
                10
        );
        iniciarBatalhaTeste(animalBase);
    }

    // --- GETTERS PARA A UI ---
    
    public EstadoBatalha getEstadoAtual() {
        return estadoAtual;
    }

    public List<AcaoBatalha> getAcoes() {
        if (estadoAtual == null || estadoAtual.getOponenteAtual() == null) return new ArrayList<>();
        
        List<AcaoBatalha> acoes = estadoAtual.getOponenteAtual().getAcoesDisponiveis();
        return acoes != null ? acoes : new ArrayList<>();
    }

    // --- ROTAS DE AÇÕES (UI -> SERVICE) ---

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

    // --- GAME LOOP ---

    public void atualizar(float dt) {
        if (estadoAtual != null) {
            batalhaService.atualizar(estadoAtual, dt);
        }
    }

    // --- FINALIZAÇÃO ---
    public void finalizarBatalha() {
        if (estadoAtual != null) {
            if (estadoAtual.isBatalhaAnimal()) {
                batalhaService.finalizarBatalha(estadoAtual, estadoAtual.getAnimal());
            } else {
                batalhaService.finalizarBatalha(estadoAtual, estadoAtual.getProvaBatalha());
            }
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

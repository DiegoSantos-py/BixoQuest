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
import service.AudioService;
import service.batalha.BatalhaService;

import java.util.ArrayList;
import java.util.List;

public class BatalhaController extends BaseController {

    private final BatalhaService batalhaService;
    private final NpcRepository npcRepository;
    private final AudioService audioService;
    private EstadoBatalha estadoAtual;
    private final EstadoController estadoController;

    public BatalhaController(BatalhaService batalhaService, NpcRepository npcRepository, AudioService audioService) {
        this.batalhaService = batalhaService;
        this.npcRepository = npcRepository;
        this.audioService = audioService;
        this.estadoController = new EstadoController(this);
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

    public EstadoController getEstadoController() {
        return estadoController;
    }

    public boolean isBatalhaAnimal() {
        return estadoAtual != null && estadoAtual.isBatalhaAnimal();
    }

    public boolean isPlayerInvulneravel() {
        if (estadoAtual != null && estadoAtual.getPlayerProva() != null) {
            return estadoAtual.getPlayerProva().isInvulneravel();
        }
        return false;
    }

    public List<AcaoBatalha> getAcoes() {
        if (estadoAtual == null)
            return new ArrayList<>();

        List<AcaoBatalha> acoes = estadoAtual.getAcoesBatalha();
        return acoes != null ? acoes : new ArrayList<>();
    }

    public boolean executarAcao(int indexAcao) {
        if (estadoAtual != null) {
            return batalhaService.executarAcaoPlayer(estadoAtual, indexAcao);
        }
        return false;
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

    // --- INPUT ---

    /** Chamado pela View quando uma tecla é pressionada. */
    public void onTeclaPressionada(javafx.scene.input.KeyCode code) {
        if (estadoAtual == null || estadoAtual.getPlayerProva() == null) return;
        model.Player.PlayerProva p = estadoAtual.getPlayerProva();
        switch (code) {
            case W: p.setMovendoCima(true); break;
            case S: p.setMovendoBaixo(true); break;
            case A: p.setMovendoEsquerda(true); break;
            case D: p.setMovendoDireita(true); break;
            default: break;
        }
    }

    /** Chamado pela View quando uma tecla é solta. */
    public void onTeclaLiberada(javafx.scene.input.KeyCode code) {
        if (estadoAtual == null || estadoAtual.getPlayerProva() == null) return;
        model.Player.PlayerProva p = estadoAtual.getPlayerProva();
        switch (code) {
            case W: p.setMovendoCima(false); break;
            case S: p.setMovendoBaixo(false); break;
            case A: p.setMovendoEsquerda(false); break;
            case D: p.setMovendoDireita(false); break;
            default: break;
        }
    }

    // --- ÁUDIO ---

    public void iniciarAudio() {
        if (estadoAtual != null) {
            String musica = estadoAtual.getMusicaDir();
            if (musica != null && !musica.isEmpty()) {
                audioService.playBGM(musica);
            }
        }
    }

    public void pararAudio() {
        audioService.stopBGM();
    }

    // --- LÓGICA DE PRECISÃO (regra de negócio, não pertence à View) ---

    /**
     * Converte a distância do cursor ao centro (0 = centro, 350 = borda)
     * em um multiplicador de precisão entre 0.0 e 1.5.
     */
    public float calcularPrecisao(double distanciaDoCentro) {
        // 6.0f radius (12px total width) guarantees exactly a 1-frame window 
        // since the cursor moves ~11.66px per frame at 700 velocity & 60fps.
        if (distanciaDoCentro <= 6.0f) {
            return 3.0f;
        }
        float precisao = (float) (1.5 - (distanciaDoCentro / 350.0) * 1.5);
        precisao = Math.max(0f, precisao);
        
        if (precisao >= 1.40f) {
            return precisao * 1.5f; // Bônus multiplicativo por acertar a zona amarela!
        }
        
        return precisao;
    }

    /**
     * Define o que é um ataque perfeito com base na precisão.
     */
    public boolean isAtaquePerfeito(float precisao) {
        return precisao >= 2.10f && precisao < 3.0f;
    }

    /**
     * Define o que é um ataque super perfeito (cravado no meio).
     */
    public boolean isAtaqueSuperPerfeito(float precisao) {
        return precisao >= 3.0f;
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

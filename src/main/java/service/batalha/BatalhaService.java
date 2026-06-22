package service.batalha;

import model.Batalha.EstadoBatalha;
import model.Evento.Prova.ProvaBatalha;
import model.Evento.Prova.ResultadoProva;
import model.Npc.Animal;
import model.Batalha.Oponente;
import model.Personagem;
import model.Player.PlayerProva;
import model.Disciplina.AreaConhecimento;
import model.Evento.Prova.Questao.Questao;
import model.util.MathUtils;
import repository.NpcRepository;
import repository.OponenteAnimalRepository;
import repository.ResultadoProvaRepository;

import java.util.LinkedList;
import java.util.Queue;
import java.util.List;
import model.Player.AcaoBatalha;

public class BatalhaService {

    private final OponenteAnimalRepository oponenteAnimalRepository = new OponenteAnimalRepository();
    private final OponenteService oponenteService = new OponenteService(oponenteAnimalRepository);
    private final BatalhaLoopService loopService = new BatalhaLoopService();
    private final BatalhaFinalizacaoService finalizacaoService = new BatalhaFinalizacaoService();
    private final PlayerProvaService playerProvaService = new PlayerProvaService();

    // inicia a batalha animal
    public EstadoBatalha iniciarBatalha(Personagem personagem, Animal animal, NpcRepository npcRepository) {
        Oponente oponenteAnimal = oponenteService.criarOponenteAnimal(animal);
        // seta o target de cada ataque dos animais como o o jogador
        PlayerProva playerProva = playerProvaService.gerarPlayerProva(personagem, AreaConhecimento.ANI);
        for (var ataque : oponenteAnimal.getAtaquesDisponiveis()) {
            ataque.setTarget(playerProva);
        }

        Queue<Oponente> oponentes = new LinkedList<>();
        // adiciona todos os oponentes da batalha(No caso 1 unico(o animal em si))
        oponentes.add(oponenteAnimal);
        String musicaDir = "/assets/audio/musicaAnimal" + MathUtils.randomIntInRange(1, 3) + ".mp3"; // escolhe uma
                                                                                                     // musica aleatoria
        List<AcaoBatalha> acoes = oponenteService.gerarAcoesAnimal(animal);
        return new EstadoBatalha(playerProva, personagem, oponentes, animal, npcRepository, musicaDir, acoes);
    }

    // mesmo fluxo pra prova
    public EstadoBatalha iniciarBatalha(Personagem personagem, ProvaBatalha provaBatalha,
            ResultadoProvaRepository resultadoProvaRepository) {
        Queue<Oponente> oponentes = new LinkedList<>();
        PlayerProva playerProva = playerProvaService.gerarPlayerProva(personagem, provaBatalha.getAreaConhecimento());
        // mas cada questao é 1 oponente difernete com 1 ATAQUE(em média)(pode ter mais)
        for (Questao questao : provaBatalha.getQuestoes()) {
            // cria os oponentes de cada questao
            Oponente oponenteQuestao = oponenteService.criarOponenteQuestao(questao, provaBatalha.getSpriteDirProva());
            for (var ataque : oponenteQuestao.getAtaquesDisponiveis()) {
                // seta o targeet dos ataques como o jogador
                ataque.setTarget(playerProva);
            }
            oponentes.add(oponenteQuestao);
        }
        return new EstadoBatalha(playerProva, personagem, oponentes, provaBatalha, resultadoProvaRepository, provaBatalha.getAcoesBatalha());
    }

    public void atualizar(EstadoBatalha estado, float dt) {
        loopService.atualizar(estado, dt);
    }

    public void executarAcaoPlayer(EstadoBatalha estado, int acaoIndex) {
        loopService.executarAcaoPlayer(estado, acaoIndex);
    }

    public void atacarOponenteAtual(EstadoBatalha estado, float multiplicadorPrecisao) {
        loopService.atacarOponenteAtual(estado, multiplicadorPrecisao);
    }

    public void salvarResultadoProvaNoRepositorio(EstadoBatalha estado, ResultadoProva resultadoProva) {
        finalizacaoService.salvarResultadoProvaNoRepositorio(estado, resultadoProva);
    }

    public void salvarAnimalNoRepositorio(EstadoBatalha estado, Animal animal) {
        finalizacaoService.salvarAnimalNoRepositorio(estado, animal);
    }

    public void finalizarBatalha(EstadoBatalha estado) {
        if (estado.getAnimal() != null) {
            finalizacaoService.finalizarBatalha(estado, estado.getAnimal());
        }
        if (estado.getProvaBatalha() != null) {
            finalizacaoService.finalizarBatalha(estado, estado.getProvaBatalha());
        }
    }
}

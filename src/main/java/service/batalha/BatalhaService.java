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
import repository.NpcRepository;
import repository.ResultadoProvaRepository;

import java.util.LinkedList;
import java.util.Queue;

public class BatalhaService {

    private final OponenteService oponenteService = new OponenteService();
    private final BatalhaLoopService loopService = new BatalhaLoopService();
    private final BatalhaFinalizacaoService finalizacaoService = new BatalhaFinalizacaoService();
    private final PlayerProvaService playerProvaService = new PlayerProvaService();

    //inicia a batalha animal
    public EstadoBatalha iniciarBatalha(Personagem personagem, Animal animal, NpcRepository npcRepository) {
        Oponente oponenteAnimal = oponenteService.criarOponenteAnimal(animal);
        //seta o target de cada ataque dos animais como o o jogador
        PlayerProva playerProva = playerProvaService.gerarPlayerProva(personagem, AreaConhecimento.ANI);
        for(var ataque : oponenteAnimal.getAtaquesDisponiveis()){
            ataque.setTarget(playerProva);
        }

        Queue<Oponente> oponentes = new LinkedList<>();
        //adiciona todos os oponentes da batalha(No caso 1 unico(o animal em si))
        oponentes.add(oponenteAnimal);

        return new EstadoBatalha(playerProva, personagem, oponentes, animal, npcRepository);
    }

    //mesmo fluxo pra prova
    public EstadoBatalha iniciarBatalha(Personagem personagem, ProvaBatalha provaBatalha, ResultadoProvaRepository resultadoProvaRepository) {
        Queue<Oponente> oponentes = new LinkedList<>();
        PlayerProva playerProva = playerProvaService.gerarPlayerProva(personagem, provaBatalha.getAreaConhecimento());
        //mas cada questao é 1 oponente difernete com 1 ATAQUE(em média)(pode ter mais)
        for(Questao questao : provaBatalha.getQuestoes()){
            //cria os oponentes de cada questao
            Oponente oponenteQuestao = oponenteService.criarOponenteQuestao(questao);
            for(var ataque : oponenteQuestao.getAtaquesDisponiveis()){
                //seta o targeet dos ataques como o jogador
                ataque.setTarget(playerProva);
            }
            oponentes.add(oponenteQuestao);
        }
        return new EstadoBatalha(playerProva, personagem, oponentes, provaBatalha, resultadoProvaRepository);
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

    public void finalizarBatalha(EstadoBatalha estado, ProvaBatalha provaBatalha) {
        finalizacaoService.finalizarBatalha(estado, provaBatalha);
    }

    public void finalizarBatalha(EstadoBatalha estado, Animal animal) {
        finalizacaoService.finalizarBatalha(estado, animal);
    }
}

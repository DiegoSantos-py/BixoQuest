package service.batalha;

import model.Ataque.Ataques.AtaqueArranhao;
import model.Ataque.Ataques.AtaqueLatido;
import model.Ataque.Ataques.AtaqueMordida;
import model.Batalha.Oponente;
import model.Batalha.OponenteDados;
import model.Disciplina.AreaConhecimento;
import model.Evento.Prova.Questao.Questao;
import model.Npc.Animal;
import model.Player.AcaoBatalha;
import model.util.Hitbox;
import model.util.Vector2D;
import repository.OponenteAnimalRepository;
import model.Ataque.Ataque;

import java.util.ArrayList;
import java.util.List;

public class OponenteService {

    OponenteAnimalRepository oponenteAnimalRepository;

    public OponenteService(OponenteAnimalRepository oponenteAnimalRepository) {
        this.oponenteAnimalRepository = oponenteAnimalRepository;
    }

    public Oponente criarOponenteAnimal(Animal animal) {
        float hpCalculado = animal.getIndole();
        // Spawna o inimigo na parte superior da tela
        Hitbox hitboxAnimal = new Hitbox(new Vector2D(960, 300), new Vector2D(50, 50), 0.0f);
        // gera o oponente a partir do animal(npc do mapa)
        OponenteDados dadosDoAnimal;
        try {
            dadosDoAnimal = oponenteAnimalRepository.buscarPorNome(animal.getNome());
        } catch (Exception e) {

            dadosDoAnimal = new OponenteDados(
                    "/assets/batalha/oponentes/animais/default.png",
                    "Ele não estava no json de oponentes",
                    "NADA AQUI FOI PLANEJADO");
        }
        Oponente animalOponente = new Oponente(hitboxAnimal, new Vector2D(0, 0), animal.getNome(), hpCalculado,
                AreaConhecimento.ANI,
                dadosDoAnimal.getSpriteDir(), dadosDoAnimal.getDescricao(), dadosDoAnimal.getTextoCaixa());
        animalOponente.setMaxTurnos(15);
        // ataque mordida padrão de todos os animais
        animalOponente.adicionarAtaque(new AtaqueMordida(null, animalOponente, animal.getIndole()));
        // mas arranhao é exclusivo de gato e latido é exclusivo de cachorro
        switch (animal.getEspecie()) {
            case GATO:
                animalOponente.adicionarAtaque(new AtaqueArranhao(null, animalOponente, animal.getIndole()));
                break;
            case CACHORRO:
                animalOponente.adicionarAtaque(new AtaqueLatido(null, animalOponente, animal.getIndole()));
                break;
        }

        return animalOponente;
    }

    public List<AcaoBatalha> gerarAcoesAnimal(Animal animal) {
        ArrayList<AcaoBatalha> acoesDisponiveis = new ArrayList<>();
        acoesDisponiveis.add(new AcaoBatalha(
                "FAZER CARINHO",
                ((float) (10 / animal.getIndole())),
                1,
                0,
                0,
                0));
        acoesDisponiveis.add(new AcaoBatalha(
                "TENTAR BRINCAR",
                ((float) (5 / animal.getIndole())),
                3,
                0,
                0,
                0));

        acoesDisponiveis.add(new AcaoBatalha(
                "COMER SALGADO(VOCE)",
                1,
                0,
                1,
                0,
                0));
        return acoesDisponiveis;
    }

    public Oponente criarOponenteQuestao(Questao questao, String spriteDirProva) {
        float hpCalculado = questao.getHp();
        // Spawna a questao na parte superior da tela
        Hitbox hitboxQuestao = new Hitbox(new Vector2D(960, 300), new Vector2D(50, 50), 0.0f);
        Oponente oponenteQuestao = new Oponente(hitboxQuestao, new Vector2D(0, 0), questao.getNome(), hpCalculado,
                questao.getAreaConhecimento(),
                spriteDirProva, questao.getDescricao(), questao.getTextoCaixa());
        oponenteQuestao.setMaxTurnos(30);
        // cada questão pode ter 1 ou múltiplos ataques atribuídos a ela, e o oponente recebe todos eles
        for (Ataque ataque : questao.getAtaques()) {
            oponenteQuestao.adicionarAtaque(ataque);
        }

        return oponenteQuestao;
    }
}

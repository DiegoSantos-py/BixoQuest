package controller;

import model.Batalha.EstadoBatalha;
import model.Batalha.Oponente;
import model.Disciplina.AreaConhecimento;
import model.Npc.Animal;
import model.Npc.Especie;
import model.Personagem;
import model.util.Hitbox;
import model.util.Vector2D;
import repository.NpcRepository;
import service.batalha.BatalhaService;

import java.util.ArrayList;
import java.util.List;

public class BatalhaController extends BaseController {

    private final BatalhaService batalhaService;
    private final NpcRepository npcRepository;

    public BatalhaController(BatalhaService batalhaService, NpcRepository npcRepository) {
        this.batalhaService = batalhaService;
        this.npcRepository = npcRepository;
    }

    public List<Oponente> getOponentesDeTeste() {
        List<Oponente> oponentes = new ArrayList<>();

        ArrayList<String> falas = new ArrayList<>();
        falas.add("oi");
        Animal animalBase = new Animal(
                "Atim",
                "assets/oponentes/animais/atim.png",
                "assets/oponentes/animais/atim.png",
                0,
                0,
                falas,
                Especie.CACHORRO,
                10
        );
        Personagem personagemBase = new Personagem();

        EstadoBatalha estado = batalhaService.iniciarBatalha(personagemBase, animalBase, this.npcRepository);
        oponentes.add(estado.getOponenteAtual());

        return oponentes;
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

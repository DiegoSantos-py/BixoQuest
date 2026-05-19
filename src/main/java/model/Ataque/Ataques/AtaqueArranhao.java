package model.Ataque.Ataques;

import java.util.Random;

import model.EntidadeBatalha;
import model.Ataque.Ataque;
import model.Player.PlayerProva;
import model.Projetil.ProjetilID;
import model.util.Vector2D;

public class AtaqueArranhao extends Ataque {
    
    private float timer = 0;
    private int projeteisSpawnados = 0;

    public AtaqueArranhao(PlayerProva target, EntidadeBatalha owner, float dificuldade) {

        super(target, owner, dificuldade, 60,0,0);
    }

    @Override
    protected void logicaAtaque(float dt) {

    }

    @Override
    public String toString() {
        return "Ataque Arranhao";
    }
}

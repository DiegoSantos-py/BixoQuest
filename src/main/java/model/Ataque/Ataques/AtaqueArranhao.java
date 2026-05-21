package model.Ataque.Ataques;

import model.Batalha.EntidadeBatalha;
import model.Ataque.Ataque;
import model.Player.PlayerProva;

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

package model.Ataque.Ataques;

import java.util.Random;

import model.Batalha.EntidadeBatalha;
import model.Ataque.Ataque;
import model.Player.PlayerProva;
import model.Projetil.ProjetilID;

public class AtaqueMordida extends Ataque {
    
    private float timer = 0;
    private int projeteisSpawnados = 0;
    private Random random;

    public AtaqueMordida(PlayerProva target, EntidadeBatalha owner, float dificuldade) {
        super(target, owner, dificuldade,80, 0, 10);
        this.random = new Random();
    }

    @Override
    protected void logicaAtaque(float dt) {

        timer += dt;

        float attackDuration = 15f;
        float interval = 1.5f / (dificuldade / 10f);
        int maxProjeteis = (int)(attackDuration / interval);

        if (timer >= interval && projeteisSpawnados < maxProjeteis) {
            //ew Vector2D(960, 800)
            float boxCentroX = 960f;
            float boxCentroY = 800f;

            // área 400x400 centrada em (0, -200)
            float posX = boxCentroX - 200 + (random.nextFloat() * 400);
            float posY = boxCentroY - 200 + (random.nextFloat() * 400);

            float anguloAleatorio = random.nextFloat() * (float)(Math.PI * 2);

            spawnProjetil(
                posX, posY,
                40, 40,
                0f, 0f,
                anguloAleatorio,
                ProjetilID.EXPLOSIVE,
                0,
                0f,
                1.5f
            );

            projeteisSpawnados++;
            timer = 0;
        }

        // mínimo necessário para encerrar o ataque
        int min = 8 + (int)(dificuldade / 5f);

        if (projeteisSpawnados >= min) {
            if(timer>= 2f) { //2s dps da ultima explosao
                this.encerrarAtaque();
            }
        }
    }



    @Override
    public String toString() {
        return "Ataque Mordida";
    }
}
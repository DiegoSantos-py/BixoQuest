package model.Ataque.Ataques;

import java.util.Random;

import model.EntidadeBatalha;
import model.Ataque.Ataque;
import model.Player.PlayerProva;
import model.Projetil.ProjetilID;

public class AtaqueMordida extends Ataque {
    
    private float timer = 0;
    private int projeteisSpawnados = 0;
    private Random random;

    public AtaqueMordida(PlayerProva target, EntidadeBatalha owner) {
        // aloca espaço para 80 básicos (para múltiplos projéteis) e 10 explosivos para performance
        super(target, owner, 80, 0, 10); 
        this.random = new Random();
    }

    @Override
    protected void logicaAtaque(float dt) {

        timer += dt;

        if (timer >= 750f && projeteisSpawnados < 10) {
        
            //quadrado 400x400
            float boxCentroX = 0f;
            float boxCentroY = -200f; // centro em (0, -200)

            //  sorteia uma pos aleatoria dentro da caixa
            float posX = boxCentroX - 200 + (random.nextFloat() * 400); 
            float posY = boxCentroY - 200 + (random.nextFloat() * 400);
            
            // rotação aleatoria
            float anguloAleatorio = random.nextFloat() * (float)(Math.PI * 2);

            // Spawna a "mordida"
            //ela leva uns 0.75s pra explodir (750 ms)
            spawnProjetil(posX, posY, 40, 40, 0f, 0f, anguloAleatorio, ProjetilID.EXPLOSIVE, 0, 0f, 750f);

            projeteisSpawnados++;
            timer = 0;
        }

        // Aguarda todos os 10 tiros saírem e garantirmos que explodiram pra limpar a factory da memoria
        if (projeteisSpawnados >= 10 && factory.getAtivos().isEmpty()) {
            this.encerrarAtaque();
        }
    }
}

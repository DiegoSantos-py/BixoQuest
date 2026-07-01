package model.Ataque.Ataques;

import model.Batalha.EntidadeBatalha;
import model.Ataque.Ataque;
import model.Player.PlayerProva;
import model.Projetil.Projetil;

import java.util.Random;

public class AtaqueArranhao extends Ataque {
    
    private float timer = 0;
    private int projeteisSpawnados = 0;
    private Random random;

    public AtaqueArranhao(PlayerProva target, EntidadeBatalha owner, float dificuldade) {
        super(target, owner, dificuldade, 60);
        this.random = new Random();
    }

    @Override
    protected void logicaAtaque(float dt) {
        timer += dt;

        float attackDuration = 10f;
        float interval = 0.25f / (dificuldade / 10f); // Ex: Dificuldade 10 spawna a cada 1 segundo
        int maxProjeteis = (int) (attackDuration / interval);

        if (timer >= interval && projeteisSpawnados < maxProjeteis) {
            float boxCentroX = (minX + maxX) / 2; 
            float boxCentroY = (minY + maxY) / 2;

            // Spawna aleatoriamente na area de ataque do inimigo
            float posX = boxCentroX - 200 + (random.nextFloat() * 400);
            float posY = boxCentroY - 200 + (random.nextFloat() * 400);
            
            // Ângulo aleatório para o arranhão
            float anguloAleatorio = random.nextFloat() * (float) (Math.PI * 2);

            // Spawna a PRÉVIA (3 pixels de largura, 600 de comprimento, sem dano)
            Projetil previa = spawnProjetil(
                    posX, posY,
                    3, 600, 
                    0f, 0f, 
                    anguloAleatorio,
                    0, 0f, // Nenhum dano
                    1.25f, // Permanece como prévia por 1.25 segundos
                    "arranhaoPrevia.png");

            if (previa != null) {
                // Ao desaparecer a prévia, spawna o arranhão real via Factory (Flyweight Pattern)
                previa.addComportamentoDespawn(
                        model.Projetil.Comportamentos.ComportamentoFactory.getDespawn("SPAWN_ARRANHAO")
                );
                
                projeteisSpawnados++;
                timer = 0;
            }
        }
        
        if (tempoDecorrido >= attackDuration && getProjeteis().isEmpty()) {
            encerrarAtaque();
        }
    }

    @Override
    public String toString() {
        return "Ataque Arranhao";
    }

    @Override
    public void reiniciarAtaque() {
        super.reiniciarAtaque();
        this.timer = 0;
        this.projeteisSpawnados = 0;
    }
}


package model.Ataque.Ataques.Prova.Hardware;

import model.Ataque.Ataque;
import model.Batalha.EntidadeBatalha;
import model.Player.PlayerProva;
import model.Projetil.Projetil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AtaquePulsos extends Ataque {

    private static class SpawnPoint {
        float x, y;
        float vx, vy;
        float angle;

        public SpawnPoint(float x, float y, float vx, float vy, float angle) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.angle = angle;
        }
    }

    private float timer = 0;
    private boolean gridCriado = false;
    private List<SpawnPoint> spawnPoints = new ArrayList<>();
    private Random random;

    public AtaquePulsos(PlayerProva target, EntidadeBatalha owner, float dificuldade) {
        super(target, owner, dificuldade, 60);
        this.random = new Random();
    }

    @Override
    protected void logicaAtaque(float dt) {
        timer += dt;
        float attackDuration = 15f;

        if (!gridCriado) {
            float width = (maxX - minX);
            float height = (maxY - minY);
            int NumLinhas = 12 + (int) dificuldade / 10;
            float espacamentoX = width / (NumLinhas + 1f);
            float espacamentoY = height / (NumLinhas + 1f);
            float boxCentroX = (minX + maxX) / 2;
            float boxCentroY = (minY + maxY) / 2;
            for (int i = 1; i <= NumLinhas; i++) {
                // Linha Vertical i
                float posX = minX + (espacamentoX * i);
                Projetil linhaV = spawnProjetil(
                        posX, boxCentroY,
                        3, height / 2,
                        0, 0, 0, // ângulo 0
                        0, 0f,
                        attackDuration,
                        "arranhaoPrevia.png");
                if (linhaV != null)
                    linhaV.setPersistente(true);

                // Topo indo pra baixo
                spawnPoints.add(new SpawnPoint(posX, minY - 10, 0, 1, 0));
                // Baixo indo pra cima
                spawnPoints.add(new SpawnPoint(posX, maxY + 10, 0, -1, (float) Math.PI));

                // Linha Horizontal i
                float posY = minY + (espacamentoY * i);
                Projetil linhaH = spawnProjetil(
                        boxCentroX, posY,
                        3, width / 2,
                        0, 0, (float) (Math.PI / 2), // rotação de 90 graus
                        0, 0f,
                        attackDuration,
                        "arranhaoPrevia.png");
                if (linhaH != null)
                    linhaH.setPersistente(true);

                // Esquerda indo pra direita
                spawnPoints.add(new SpawnPoint(minX - 10, posY, 1, 0, (float) (-Math.PI / 2)));
                // Direita indo pra esquerda
                spawnPoints.add(new SpawnPoint(maxX + 10, posY, -1, 0, (float) (Math.PI / 2)));
            }
            gridCriado = true;
        }

        float interval = 0.2f / (dificuldade / 10f);
        float speed = 250f + (dificuldade * 5f);

        if (timer >= interval && tempoDecorrido < attackDuration - 1.5f) {
            SpawnPoint sp = spawnPoints.get(random.nextInt(spawnPoints.size()));
            float despawnTime =  this.getMaxX()/(3 * speed) + 0.1f;
            System.out.println(despawnTime);
            spawnProjetil(
                    sp.x, sp.y,
                    16, 16, // Tamanho do raio
                    speed, // velocidade (magnitude)
                    (float) Math.atan2(sp.vy, sp.vx), // anguloSpawn (direcao do movimento)
                    sp.angle, // anguloHitbox (rotacao do sprite)
                    1, 0.75f, // Dano e knockback
                    despawnTime, // Tempo de vida garantido pra atravessar a arena
                    "raio.png");

            timer = 0;
        }

        // Aguarda os projéteis saírem antes de encerrar
        if (tempoDecorrido >= attackDuration) {
            encerrarAtaque();
        }
    }

    @Override
    public String toString() {
        return "Ataque Pulsos";
    }

    @Override
    public void reiniciarAtaque() {
        super.reiniciarAtaque();
        this.timer = 0;
        this.gridCriado = false;
        this.spawnPoints.clear();
    }
}

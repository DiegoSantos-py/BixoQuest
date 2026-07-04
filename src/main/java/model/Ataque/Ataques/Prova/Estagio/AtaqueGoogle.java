package model.Ataque.Ataques.Prova.Estagio;

import model.Ataque.Ataque;
import model.Batalha.EntidadeBatalha;
import model.Player.PlayerProva;
import model.Projetil.Projetil;
import model.Projetil.ProjetilFactory;
import model.Projetil.ComportamentoAoDespawnar;
import model.util.MathUtils;
import model.Projetil.Comportamentos.ProjetilAceleracao;
import model.Projetil.Comportamentos.ProjetilExplodeAoSerAtingido;

public class AtaqueGoogle extends Ataque {

    private boolean started = false;
    private boolean latidoAtirado = false;
    private int currentWave = 0;
    private float waveTimer = 3.5f;
    private final int TOTAL_WAVES = 15;
    private final ProjetilAceleracao desaceleracao = new ProjetilAceleracao(-300f);
    
    private final ProjetilExplodeAoSerAtingido EXPLOSAO_MC = new ProjetilExplodeAoSerAtingido(
            8, "fogo.png", 30f, 20f, 225f, 1, 1f, 3f
    );

    public AtaqueGoogle(PlayerProva target, EntidadeBatalha owner, float dificuldade) {
        super(target, owner, dificuldade, 220);
    }

    @Override
    protected void logicaAtaque(float dt) {

        if (!latidoAtirado) {
            latidoAtirado = true;
            
            Projetil latido = spawnProjetil(
                (minX + maxX) / 2f,
                minY - 50f,
                100f,
                (maxX - minX),
                500f,
                (float) (Math.PI / 2f),
                (float) (Math.PI/2f),
                0, 0f, 10f,
                "latido.png"
            );
            latido.setMultiplicadorSprite(1f);

            latido.addComportamento(desaceleracao);
            if (latido != null) {
                latido.setMultiplicadorSprite(1f);
                latido.addComportamentoColisao((projetil, player) -> {
                    player.setSoulMode(PlayerProva.SoulMode.YELLOW);
                });
            }
        }
        
        if (currentWave < TOTAL_WAVES) {
            waveTimer -= dt;
            
            if (waveTimer <= 0f) {
                spawnWave();
                currentWave++;
                
                float progress = (float) currentWave / TOTAL_WAVES;
                float nextCooldown = 1.5f - (progress * 0.1f);
                waveTimer = nextCooldown;
            }
        } else if (getProjeteis().isEmpty()) {
            encerrarAtaque();
        }
        if (tempoDecorrido > 20f) {
            encerrarAtaque();
        }
    }

    private void spawnWave() {
        int numProjeteis = 6;
        float espacamento = (maxX - minX) / numProjeteis;
        
        int indexGoogle = MathUtils.randomIntInRange(0, numProjeteis - 1);

        for (int i = 0; i < numProjeteis; i++) {
            float spawnX = minX + (espacamento / 2f) + (i * espacamento);
            float spawnY = minY;

            boolean isGoogle = (i == indexGoogle);
            String sprite = isGoogle ? "google.png" : "mcdonalds.png";
            
            Projetil p = spawnProjetil(
                spawnX, spawnY,
                45, 45,
                200f + (currentWave * 5),
                (float) (Math.PI / 2f),
                0,
                2,
                1f,
                5f,
                sprite
            );

            p.setMultiplicadorSprite(1.25f);
            
            if (p != null && !isGoogle) {
                p.addComportamentoDespawn(EXPLOSAO_MC);
            }
        }
    }

    @Override
    public void reiniciarAtaque() {
        super.reiniciarAtaque();
        started = false;
        currentWave = 0;
        waveTimer = 0f;
    }
}

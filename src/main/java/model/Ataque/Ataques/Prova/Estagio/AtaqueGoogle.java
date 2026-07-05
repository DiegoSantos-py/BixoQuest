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

    private boolean iniciado = false;
    private boolean latidoAtirado = false;
    private int ondas = 0;
    private float timerOndas = 3.5f;
    private final int totalOndas = 15;
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
            //spawna a onda do projetil q trasnsofmar o player no modo amaerlo
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

            latido.addComportamento(desaceleracao);
            //aq o comportamento pra isso vv
            if (latido != null) {
                latido.setMultiplicadorSprite(1f);
                latido.addComportamentoColisao((projetil, player) -> {
                    player.setSoulMode(PlayerProva.SoulMode.YELLOW);
                });
            }
        }
        //enquanto o numero
        if (ondas < totalOndas) {
            timerOndas -= dt;
            
            if (timerOndas <= 0f) {
                gerarOnda();
                ondas++;
                
                float progresso = (float) ondas / totalOndas;
                timerOndas = 1.5f - (progresso * 0.1f); //reduz o tempo entre cada onda conforme as ondas passam
            }
        } else if (getProjeteis().isEmpty()) {
            //encerra o ataque qndo acabarem as ondas e as antigas despawnarem
            encerrarAtaque();
        }
        //encerra o ataque em tempo 20 por precaucao
        if (tempoDecorrido > 20f) {
            encerrarAtaque();
        }
    }

    private void gerarOnda() {
        int numProjeteis = 6;
        float espacamento = (maxX - minX) / numProjeteis;
        
        int indiceGoogle = MathUtils.randomIntInRange(0, numProjeteis - 1);

        for (int i = 0; i < numProjeteis; i++) {
            float inicioX = minX + (espacamento / 2f) + (i * espacamento);
            float inicioY = minY;

            boolean ehGoogle = (i == indiceGoogle);
            String sprite = ehGoogle ? "google.png" : "mcdonalds.png";
            
            Projetil p = spawnProjetil(
                inicioX, inicioY,
                45, 45,
                200f + (ondas * 5),
                (float) (Math.PI / 2f),
                0,
                2,
                1f,
                5f,
                sprite
            );

            p.setMultiplicadorSprite(1.25f);
            //se n for google adicina o comportametno pra explodir
            if (p != null && !ehGoogle) {
                p.addComportamentoDespawn(EXPLOSAO_MC);
            }
        }
    }

    @Override
    public void reiniciarAtaque() {
        super.reiniciarAtaque();
        iniciado = false;
        ondas = 0;
        timerOndas = 0f;
    }
}

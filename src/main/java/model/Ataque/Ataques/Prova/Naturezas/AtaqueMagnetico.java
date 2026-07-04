package model.Ataque.Ataques.Prova.Naturezas;

import model.Ataque.Ataque;
import model.Batalha.EntidadeBatalha;
import model.Player.PlayerProva;
import model.Projetil.Comportamentos.ProjetilApontaParaVetor;
import model.Projetil.Comportamentos.ProjetilQueSegue;
import model.Projetil.ComportamentoProjetil;
import model.Projetil.Projetil;
import model.util.MathUtils;
import model.util.Vector2D;

public class AtaqueMagnetico extends Ataque {

    private Projetil magnet = null;
    private Vector2D magnetDestino = null;
    private ProjetilQueSegue segueMagnet = null;
    
    
    private final float DURACAO_ATAQUE = 15f;
    private final float INTERVALO_SPAWN_FERRO = 0.15f;
    private final ComportamentoProjetil apontarProVetor =  new ProjetilApontaParaVetor();
    private float timerFerro = 0;

    public AtaqueMagnetico(PlayerProva target, EntidadeBatalha owner, float dificuldade) {
        super(target, owner, dificuldade, 100);
    }

    @Override
    protected void logicaAtaque(float dt) {
        
        if (magnet == null) {
            float centroX = (minX + maxX) / 2f;
            float centroY = (minY + maxY) / 2f;
            magnet = spawnProjetil(centroX, centroY, 50, 50, 0, 0, 0, 0, 0f, DURACAO_ATAQUE, "ima.png");
            magnet.addComportamento(apontarProVetor);
            if (magnet != null) {
                segueMagnet = new ProjetilQueSegue(400f, 2.5f, magnet);
            }
        }
        
        if (magnet != null) {
            if (magnetDestino == null || magnet.getCentro().distancia(magnetDestino) < 15f) {
                float tempoNoFuturo = 5f / 60f; 
                float rx = target.getX() + target.getVelocidade().getX() * tempoNoFuturo;
                float ry = target.getY() + target.getVelocidade().getY() * tempoNoFuturo;
                
                magnetDestino = new Vector2D(rx, ry);

                Vector2D direcao = magnetDestino.subtrair(magnet.getCentro());
                if (direcao.magnitude() > 0) {
                    direcao = direcao.normalize();
                }
                magnet.setVelocidade(direcao.mult(150f));
            }
        }

        // 3. Spawna os projéteis de ferro
        timerFerro += dt;
        if (timerFerro >= INTERVALO_SPAWN_FERRO) {
            timerFerro = 0;

            float centroX = (minX + maxX) / 2f;
            float centroY = (minY + maxY) / 2f;
            
            float angulo = MathUtils.randomFloatInRange(0, (float) (Math.PI * 2));
            float raio = MathUtils.randomFloatInRange(350, 450); // Surge de longe, fora da caixa
            
            float startX = centroX + (float) Math.cos(angulo) * raio;
            float startY = centroY + (float) Math.sin(angulo) * raio;

            // Tempo de vida curto (1.5f), os ferros correm atrás do imã
            Projetil ferro = spawnProjetil(startX, startY, 20, 26.6f, 0, 0, 0, 1, 1f, 2.5f, "ferro.png");
            
            if (ferro != null && segueMagnet != null) {
                // Reutiliza a instância! Todos os ferros usam a mesma referência
                ferro.addComportamento(segueMagnet);
            }
        }

        // 3. Verifica fim do ataque
        if (tempoDecorrido >= DURACAO_ATAQUE) {
            encerrarAtaque();
        }
    }

    @Override
    public void reiniciarAtaque() {
        super.reiniciarAtaque();
        this.magnet = null;
        this.magnetDestino = null;
        this.segueMagnet = null;
        this.timerFerro = 0;
    }


}

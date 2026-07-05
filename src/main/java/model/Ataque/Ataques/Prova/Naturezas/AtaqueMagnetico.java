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

    private Projetil ima = null;
    private Vector2D imaDestino = null;
    private ProjetilQueSegue segueIma = null;
    
    
    private final float DURACAO_ATAQUE = 15f;
    private final float INTERVALO_GERACAO_FERRO = 0.15f;
    private final ComportamentoProjetil apontarProVetor =  new ProjetilApontaParaVetor();
    private float timerFerro = 0;

    public AtaqueMagnetico(PlayerProva target, EntidadeBatalha owner, float dificuldade) {
        super(target, owner, dificuldade, 100);
    }

    @Override
    protected void logicaAtaque(float dt) {
        
        if (ima == null) {
            float centroX = (minX + maxX) / 2f;
            float centroY = (minY + maxY) / 2f;
            ima = spawnProjetil(centroX, centroY, 50, 50, 0, 0, 0, 0, 0f, DURACAO_ATAQUE, "ima.png");
            ima.addComportamento(apontarProVetor);
            if (ima != null) {
                segueIma = new ProjetilQueSegue(400f, 2.5f, ima);
            }

            //cria o ima, dps cria o comportamento pros projeteis seguirem ele
        }
        
        if (ima != null) {
            if (imaDestino == null || ima.getCentro().distancia(imaDestino) < 15f) {

                float tempoNoFuturo = 5f / 60f; 
                float futuroX = target.getX() + target.getVelocidade().getX() * tempoNoFuturo;
                float futuroY = target.getY() + target.getVelocidade().getY() * tempoNoFuturo;
                
                imaDestino = new Vector2D(futuroX, futuroY);

                Vector2D direcao = imaDestino.subtrair(ima.getCentro());
                if (direcao.magnitude() > 0) {
                    direcao = direcao.normalize();
                }
                ima.setVelocidade(direcao.mult(150f));
            }
            //IA do ima pra ele se mover pra posições q o player esteve antes, + a velocidade dele, e so executa dps de ele percorrer no minimo 15 pixels.
        }

        // 3. Spawna os projéteis de ferro
        timerFerro += dt;
        if (timerFerro >= INTERVALO_GERACAO_FERRO) {
            timerFerro = 0;

            float centroX = (minX + maxX) / 2f;
            float centroY = (minY + maxY) / 2f;
            
            float angulo = MathUtils.randomFloatInRange(0, (float) (Math.PI * 2));
            float raio = MathUtils.randomFloatInRange(350, 450); // Surge de longe, fora da caixa
            
            float inicioX = centroX + (float) Math.cos(angulo) * raio;
            float inicioY = centroY + (float) Math.sin(angulo) * raio;

            // Tempo de vida curto (1.5f), os ferros correm atrás do imã
            Projetil ferro = spawnProjetil(inicioX, inicioY, 20, 26.6f, 0, 0, 0, 1, 1f, 2.5f, "ferro.png");
            
            if (ferro != null && segueIma != null) {
                ferro.addComportamento(segueIma);
            }
        }

        if (tempoDecorrido >= DURACAO_ATAQUE) {
            encerrarAtaque();
        }
    }

    @Override
    public void reiniciarAtaque() {
        super.reiniciarAtaque();
        this.ima = null;
        this.imaDestino = null;
        this.segueIma = null;
        this.timerFerro = 0;
    }


}

package model.Ataque.Ataques.Prova.Naturezas;

import model.Ataque.Ataque;
import model.Batalha.EntidadeBatalha;
import model.Player.PlayerProva;
import model.Projetil.ComportamentoAoColidirComPlayer;
import model.Projetil.Comportamentos.ProjetilGravidade;
import model.Projetil.Projetil;
import model.util.MathUtils;
import model.util.Vector2D;

public class AtaqueGravidade extends Ataque {

    private boolean projetilSpawnado = false;
    private float duracaoAtaque = 5f;
    
    private float timerCarro = 0;
    private final float SPAWN_DELAY_CARRO = 1.0f;
    private boolean segundoTurno = false;

    private final ProjetilGravidade gravidade = new ProjetilGravidade(-800f);



    public AtaqueGravidade(PlayerProva target, EntidadeBatalha owner, float dificuldade) {
        super(target, owner, dificuldade, 10);
        // Inicia como uma caixa pequena (55x55) no centro (960, 800)
        this.minX = 932.5f;
        this.maxX = 987.5f;
        this.minY = 772.5f;
        this.maxY = 827.5f;
    }

    @Override
    protected void logicaAtaque(float dt) {
        
        if (!projetilSpawnado && target != null) {
            // Spawna o projétil exatamente no centro do player
            float posX = target.getCentro().getX();
            float posY = owner.getCentro().getY();

            // Usando "apple.png" (ou qualquer sprite que faça sentido para gravidade)
            Projetil p = spawnProjetil(
                posX, posY,
                40, 40,
                0, // Velocidade 0 (já spawna nele)
                0, 
                0, 
                0, 0f, // 0 dano
                duracaoAtaque, 
                "maca.png" // Usando maça de Isaac Newton como referência :)
            );

            if (p != null) {
                p.addComportamento(gravidade);
                p.addComportamentoColisao(new ComportamentoAoColidirComPlayer() {
                    @Override
                    public void aoColidirComPlayer(Projetil projetil, PlayerProva playerTarget) {
                        float direcao = MathUtils.randomFloatInRange(-2f,2f);
                        int direcaoUnitario = (direcao >= 0) ? 1  : -1;
                        playerTarget.setSoulMode(PlayerProva.SoulMode.BLUE);
                        projetil.setVelocidade(
                                projetil.getVelocidade().
                                        add(
                                                new Vector2D(
                                                        50f * direcao + direcaoUnitario * 10f,
                                                        (float) (projetil.getVelocidade().getY()*(-1.8)))));
                        projetil.setVelocidadeAngular(  (float)(direcao * Math.PI));

                        // Volta o tamanho da caixa ao normal para o próximo ataque
                    }

                });
            }       


            projetilSpawnado = true;
        }
        
        if (projetilSpawnado && segundoTurno) {
            timerCarro += dt;
            if (timerCarro >= SPAWN_DELAY_CARRO) {
                timerCarro = 0;
                spawnCarro();
            }
        }

        if (tempoDecorrido >= duracaoAtaque) {
            encerrarAtaque();
        }
    }
    
    private void spawnCarro() {
        boolean vemDaEsquerda = MathUtils.randomFloatInRange(0, 1) > 0.5f;
        float startY = maxY - 20; // Apenas no chão
        float startX = vemDaEsquerda ? minX - 100 : maxX + 100;
        float angulo = vemDaEsquerda ? 0 : (float) Math.PI;
        String sprite = vemDaEsquerda ? "carro.png" : "carroEsq.png";

        spawnProjetil(
            startX, startY,
            80, 40,
            250f, angulo, 0f,
            1, 0.5f,
            5f, sprite
        );
    }



    @Override
    public void reiniciarAtaque() {
        super.reiniciarAtaque();
        this.timerCarro = 0;
        this.segundoTurno = true;
        
        // Garante que se o ataque reiniciar antes da maçã bater, a caixa destrava
        this.minX = 760f;
        this.maxX = 1160f;
        this.minY = 600f;
        this.maxY = 1000f;
    }
}

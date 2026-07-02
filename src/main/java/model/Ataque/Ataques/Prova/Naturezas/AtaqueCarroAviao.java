package model.Ataque.Ataques.Prova.Naturezas;

import model.Ataque.Ataque;
import model.Batalha.EntidadeBatalha;
import model.Player.PlayerProva;
import model.Projetil.Comportamentos.ComportamentoFactory;
import model.Projetil.Projetil;
import model.util.MathUtils;
import model.util.Vector2D;

public class AtaqueCarroAviao extends Ataque {

    private float timerCarro = 0;
    private float timerAviao = 0;
    
    private final float SPAWN_DELAY_CARRO = 1.1f;
    private final float SPAWN_DELAY_AVIAO = 0.8f;
    
    private final float FASE_1_DURACAO = 20f;

    private final float FASE_2_DURACAO = 30f;

    private boolean bombardeiroSpawnado = false;
    private Projetil bombardeiro = null;
    private boolean bombaDropada = false;

    public AtaqueCarroAviao(PlayerProva target, EntidadeBatalha owner, float dificuldade) {
        super(target, owner, dificuldade, 100);
        
        // Caixa mais larga!
        this.minX = 500f;
        this.maxX = 1420f;
        this.minY = 600f;
        this.maxY = 1000f;
    }

    @Override
    protected void logicaAtaque(float dt) {
        if(target.getSoulMode() !=  PlayerProva.SoulMode.BLUE) {
            target.setSoulMode(PlayerProva.SoulMode.BLUE); // se o player der hitkill no ataque 1
        }
        if (tempoDecorrido < FASE_1_DURACAO) {
            // Fase 1: Carros e Aviões
            timerCarro += dt;
            timerAviao += dt;
            
            if (timerCarro >= SPAWN_DELAY_CARRO) {
                timerCarro = 0;
                spawnCarro();
            }
            
            if (timerAviao >= SPAWN_DELAY_AVIAO) {
                timerAviao = 0;
                spawnAviaoNormal();
            }
        } 
        else if (tempoDecorrido >= FASE_1_DURACAO && tempoDecorrido < FASE_2_DURACAO) {
            // Fase 2: O bombardeiro final
            if (!bombardeiroSpawnado & tempoDecorrido >= FASE_1_DURACAO + 2f){

                spawnBombardeiro();
                bombardeiroSpawnado = true;
            }
            
            // Checa se o bombardeiro chegou no ponto ideal de drop usando Cinemática
            if (bombardeiro != null && bombardeiro.isAtivo() && !bombaDropada) {
                float centroX = (minX + maxX) / 2f;
                
                // Distância que a bomba vai percorrer no eixo Y
                float distanceY = maxY - bombardeiro.getY();
                
                // Tempo de queda: t = sqrt(2 * h / g)
                float tQueda = (float) Math.sqrt((2 * distanceY) / 800f);
                
                // Distância horizontal percorrida durante a queda: d = vx * t
                float vxAvião = bombardeiro.getVelocidade().getX();
                float distanceX = vxAvião * tQueda;
                
                // Ponto exato de drop (centro subtraindo a distância que ela vai andar pra frente)
                float pontoDeDrop = centroX - distanceX;

                // Se passou ou chegou no ponto de drop
                if (bombardeiro.getX() >= pontoDeDrop) {
                    droparBomba(bombardeiro.getX(), bombardeiro.getY(), vxAvião, tQueda);
                    bombaDropada = true;
                }
            }
        }
        else if (tempoDecorrido >= FASE_2_DURACAO) {
            encerrarAtaque();
        }
    }

    private void spawnCarro() {
        boolean vaiParaDireita = MathUtils.randomFloatInRange(0, 1) > 0.5f;
        float velocidade = 200f + MathUtils.randomFloatInRange(0, 100f);
        
        float startX = vaiParaDireita ? minX - 50 : maxX + 50;
        float startY = maxY - 30; // No chão
        float anguloStr = vaiParaDireita ? 0 : (float) Math.PI;
        String sprite = vaiParaDireita ? "carro.png" : "carroEsq.png";
        
        spawnProjetil(
            startX, startY, 
            80, 40, 
            velocidade, anguloStr, 0f, 
            1, 0.5f, 
            10f, sprite
        );
    }
    
    private void spawnAviaoNormal() {
        boolean vaiParaDireita = MathUtils.randomFloatInRange(0, 1) > 0.5f;
        float velocidade = 200f + MathUtils.randomFloatInRange(0, 100f);
        
        float startX = vaiParaDireita ? minX - 50 : maxX + 50;
        float startY = maxY - 80 + MathUtils.randomFloatInRange(-200,0); // Acima dos carros
        float anguloStr = vaiParaDireita ? 0 : (float) Math.PI;
        String sprite = vaiParaDireita ? "aviao.png" : "aviaoEsq.png";
        
        spawnProjetil(
            startX, startY, 
            60, 40, 
            velocidade, anguloStr, 0f, 
            1, 0.5f, 
            10f, sprite
        );
    }
    
    private void spawnBombardeiro() {
        // Vem da esquerda pra direita, bem no topo (fora da arena)
        float startX = minX - 100;
        float startY = minY - 50; // Acima da caixa
        float velocidade = 250f;
        
        bombardeiro = spawnProjetil(
            startX, startY, 
            100, 50, 
            velocidade, 0f, 0f, 
            1, 0.5f, 
            10f, "aviao.png" // Usando aviao.png como bombardeiro
        );
    }
    
    private void droparBomba(float x, float y, float vxAvião, float tempoQueda) {
        // A bomba cai herdando a velocidade horizontal do avião e sofre gravidade!
        
        Projetil bomba = spawnProjetil(
            x, y, 
            60, 32,
            Math.abs(vxAvião), (vxAvião > 0 ? 0f : (float)Math.PI), 0f, 
            1, 1.0f, 
            tempoQueda, "bomba.png" 
        );
        
        if (bomba != null) {
            bomba.addComportamento(ComportamentoFactory.getAI("GRAVIDADE"));
            bomba.addComportamento(ComportamentoFactory.getAI("APONTA_VETOR"));
            bomba.addComportamentoDespawn(ComportamentoFactory.getDespawn("EXPLOSAO_NUCLEAR"));
        }
    }

    @Override
    public String toString() {
        return "Cinemática Veículos";
    }

    @Override
    public void reiniciarAtaque() {
        super.reiniciarAtaque();
        this.timerCarro = 0;
        this.timerAviao = 0;
        this.bombardeiroSpawnado = false;
        this.bombardeiro = null;
        this.bombaDropada = false;
        
        this.minX = 500f;
        this.maxX = 1420f;
        this.minY = 600f;
        this.maxY = 1000f;
    }
}

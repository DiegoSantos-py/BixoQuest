package model.Ataque.Ataques.Prova.Naturezas;

import model.Ataque.Ataque;
import model.Batalha.EntidadeBatalha;
import model.Player.PlayerProva;
import model.Projetil.Comportamentos.ProjetilApontaParaVetor;
import model.Projetil.Comportamentos.ProjetilExplosaoNuclear;
import model.Projetil.Comportamentos.ProjetilGravidade;
import model.Projetil.Projetil;
import model.util.MathUtils;
import model.util.Vector2D;

public class AtaqueCarroAviao extends Ataque {

    private float timerCarro = 0;
    private float timerAviao = 0;
    
    private final float ATRASO_GERACAO_CARRO = 1.3f - (dificuldade/100);
    private final float ATRASO_GERACAO_AVIAO = 1f - (dificuldade/100);
    //os delays pra apsawnr aviao e carro
    
    private final float FASE_1_DURACAO = 20f;
    private final float FASE_2_DURACAO = 30f;

    private final ProjetilGravidade gravidade = new ProjetilGravidade(-800f);
    private final ProjetilApontaParaVetor apontaVetor = new ProjetilApontaParaVetor();
    private final ProjetilExplosaoNuclear explosaoNuclear = new ProjetilExplosaoNuclear(
            "fogo.png", 34, 24, 1, 0.5f, 7f, 50,
            new ProjetilGravidade(-100f), new ProjetilApontaParaVetor());
    //passa os comportamentos por referencia ja q os projeteis dessa explosam tbm usam comportamentos

    private boolean bombardeiroGerado = false;
    private Projetil bombardeiro = null;
    private boolean bombaLancada = false;

    public AtaqueCarroAviao(PlayerProva target, EntidadeBatalha owner, float dificuldade) {
        super(target, owner, dificuldade, 100);
        
        // Caixa mais larga
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
            
            if (timerCarro >= ATRASO_GERACAO_CARRO) {
                timerCarro = 0;
                spawnCarro();
            }
            
            if (timerAviao >= ATRASO_GERACAO_AVIAO) {
                timerAviao = 0;
                spawnAviaoNormal();
            }
        } 
        else if (tempoDecorrido >= FASE_1_DURACAO && tempoDecorrido < FASE_2_DURACAO) {
            // Fase 2: O bombardeiro
            if (!bombardeiroGerado & tempoDecorrido >= FASE_1_DURACAO + 2f){

                spawnBombardeiro();
                bombardeiroGerado = true;
            }
            
            // Checa se o bombardeiro chegou no ponto ideal
            if (bombardeiro != null && bombardeiro.isAtivo() && !bombaLancada) {
                float centroX = (minX + maxX) / 2f;
                
                // Distância que a bomba vai percorrer no eixo Y
                float distanciaY = maxY - bombardeiro.getY();
                
                // Tempo de queda: t = sqrt(2 * h / g)
                float tQueda = (float) Math.sqrt((2 * distanciaY) / 800f);

                // Distância horizontal percorrida durante a queda: d = vx * t
                float vxAviao = bombardeiro.getVelocidade().getX();
                float distanciaX = vxAviao * tQueda;
                
                // Ponto exato de drop (centro subtraindo a distância que ela vai andar pra frente)
                float pontoLancamento = centroX - distanciaX;

                // Se passou ou chegou no ponto de drop
                if (bombardeiro.getX() >= pontoLancamento) {
                    droparBomba(bombardeiro.getX(), bombardeiro.getY(), vxAviao, tQueda);
                    bombaLancada = true;
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
        
        float inicioX = vaiParaDireita ? minX - 50 : maxX + 50;
        float inicioY = maxY - 30; // No chão
        float angulo = vaiParaDireita ? 0 : (float) Math.PI;
        String sprite = vaiParaDireita ? "carro.png" : "carroEsq.png";
        
        spawnProjetil(
            inicioX, inicioY, 
            80, 40, 
            velocidade, angulo, 0f, 
            1, 0.5f, 
            10f, sprite
        );
    }
    
    private void spawnAviaoNormal() {
        boolean vaiParaDireita = MathUtils.randomFloatInRange(0, 1) > 0.5f;
        float velocidade = 200f + MathUtils.randomFloatInRange(0, 100f);
        
        float inicioX = vaiParaDireita ? minX - 50 : maxX + 50;
        float inicioY = maxY - 80 + MathUtils.randomFloatInRange(-200,0); // Acima dos carros
        float angulo = vaiParaDireita ? 0 : (float) Math.PI;
        String sprite = vaiParaDireita ? "aviao.png" : "aviaoEsq.png";
        
        spawnProjetil(
            inicioX, inicioY, 
            60, 40, 
            velocidade, angulo, 0f, 
            1, 0.5f, 
            10f, sprite
        );
    }
    
    private void spawnBombardeiro() {
        // Vem da esquerda pra direita, bem no topo (fora da arena)
        float inicioX = minX - 100;
        float inicioY = minY - 50; // Acima da caixa
        float velocidade = 250f;
        
        bombardeiro = spawnProjetil(
            inicioX, inicioY, 
            100, 50, 
            velocidade, 0f, 0f, 
            1, 0.5f, 
            10f, "aviao.png"
        );
    }
    
    private void droparBomba(float x, float y, float vxAviao, float tempoQueda) {
        // A bomba cai herdando a velocidade horizontal do avião
        
        Projetil bomba = spawnProjetil(
            x, y, 
            60, 32,
            Math.abs(vxAviao), (vxAviao > 0 ? 0f : (float)Math.PI), 0f, 
            1, 1.0f, 
            tempoQueda, "bomba.png" 
        );
        
        if (bomba != null) {
            bomba.addComportamento(gravidade);
            bomba.addComportamento(apontaVetor);
            bomba.addComportamentoDespawn(explosaoNuclear);
        }
    }



    @Override
    public void encerrarAtaque() {
        super.encerrarAtaque();
        if (target != null) target.setSoulMode(PlayerProva.SoulMode.RED);
    }

    @Override
    public void reiniciarAtaque() {
        super.reiniciarAtaque();
        if (target != null) target.setSoulMode(PlayerProva.SoulMode.RED);
        this.timerCarro = 0;
        this.timerAviao = 0;
        this.bombardeiroGerado = false;
        this.bombardeiro = null;
        this.bombaLancada = false;
        
        this.minX = 500f;
        this.maxX = 1420f;
        this.minY = 600f;
        this.maxY = 1000f;
    }
}

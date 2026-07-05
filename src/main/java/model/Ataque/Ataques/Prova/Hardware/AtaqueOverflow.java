package model.Ataque.Ataques.Prova.Hardware;

import model.Ataque.Ataque;
import model.Batalha.EntidadeBatalha;
import model.Player.PlayerProva;
import model.Projetil.ComportamentoProjetil;
import model.Projetil.Comportamentos.ProjetilAceleracao;
import model.Projetil.Projetil;
import model.util.MathUtils;
import model.util.Vector2D;

public class AtaqueOverflow extends Ataque {

    private Projetil overflowGif;
    private float timer = 0;
    private boolean explosaoInicialFeita = false;
    private boolean explosaoFinalFeita = false;
    private float timerBalas = 0;
    private ComportamentoProjetil desaceleracao = new ProjetilAceleracao(-600f);
    private float tempoVidaGif = 0f;

    public AtaqueOverflow(PlayerProva target, EntidadeBatalha owner, float dificuldade) {
        super(target, owner, dificuldade, 150); 
    }

    @Override
    protected void logicaAtaque(float dt) {
        if (tempoVidaGif == 0) {
            tempoVidaGif = 8f * (1f + (dificuldade / 20f));
        }

        timer += dt;

        if (overflowGif == null && timer < tempoVidaGif && !explosaoFinalFeita) {
            overflowGif = spawnProjetil(
                owner.getX(), owner.getY(),
                225f, 75f,
                500f, (float) (Math.PI / 2f), 0f,
                0, 0f, 
                tempoVidaGif, 
                ""
            );

            if (overflowGif != null) {
                overflowGif.addComportamento(desaceleracao);
                overflowGif.setSpriteUrl("/assets/batalha/projeteis/bits.gif");
                overflowGif.setMultiplicadorSprite(1.0f);
            }
        }

        // Fase 1: Burst inicial apos 1.5s
        if (timer >= 1.5f && !explosaoInicialFeita) {
            explosaoInicialFeita = true;
            spawnBurst(25 + (int)(dificuldade * 2));
        }

        // Fase 2: Bullet hell constante pro player
        if (explosaoInicialFeita && timer < tempoVidaGif) {
            timerBalas += dt;
            float taxaTiro = 0.25f / (dificuldade / 8f);
            if (taxaTiro < 0.1f) taxaTiro = 0.1f;
            
            if (timerBalas >= taxaTiro) {
                timerBalas = 0;
                spawnBulletHell();
            }
        }

        if (timer >= tempoVidaGif && !explosaoFinalFeita) {
            explosaoFinalFeita = true;
            spawnBurst(40 + (int)(dificuldade * 3));
        }

        // Fase 4: Encerra o ataque de forma segura após o burst voar
        if (explosaoFinalFeita && timer >= tempoVidaGif + 4f) {
            encerrarAtaque();
        }
    }
    
    private void spawnBurst(int numProjeteis) {
        float origemX = owner.getX();
        float origemY = owner.getY();
        if (overflowGif != null && overflowGif.isAtivo()) {
            origemX = overflowGif.getX();
            origemY = overflowGif.getY();
        }
        
        for (int i = 0; i < numProjeteis; i++) {
            boolean ehUm = MathUtils.randomFloatInRange(0, 1) > 0.5f;
            String sprite = ehUm ? "um.png" : "zero.png";
            float larg = ehUm ? 23f : 24f; //a largura é diferente por causa do sprite
            float alt = ehUm ? 25f : 23f;
            
            float angulo = MathUtils.randomFloatInRange(0, (float)(Math.PI * 2));
            float velocidade = MathUtils.randomFloatInRange(150f, 350f + (dificuldade * 15f));
            
            spawnProjetil(
                origemX, origemY,
                larg, alt,
                velocidade,
                angulo, angulo + (float) (Math.PI / 2f),
                1, 1f,
                4f,
                sprite
            );
        }
    }
    
    private void spawnBulletHell() {
        if (target == null) return;

        float origemX = owner.getX();
        float origemY = owner.getY();
        Vector2D centro = new Vector2D(origemX, origemY);
        
        if (overflowGif != null && overflowGif.isAtivo()) {
            origemX = overflowGif.getX();
            origemY = overflowGif.getY();
            centro = overflowGif.getCentro();
        }
        
        boolean ehUm = MathUtils.randomFloatInRange(0, 1) > 0.5f;
        String sprite = ehUm ? "um.png" : "zero.png"; //aleatoriamente escolhe 0 e 1 pra atiarr no player
        float larg = ehUm ? 23f : 24f;
        float alt = ehUm ? 25f : 23f;
        
        Vector2D direcao = target.getCentro().subtrair(centro);
        float anguloAlvo = (float) Math.atan2(direcao.getY(), direcao.getX());
        
        float dispersao = (float) Math.toRadians(35);
        float anguloReal = anguloAlvo + MathUtils.randomFloatInRange(-dispersao, dispersao);//escolhe um angulo aleatorio 35 pra esquedfda 35 pra direita do player
        
        float velocidade = 200f + (dificuldade * 15f);
        
        spawnProjetil(
            origemX, origemY,
            larg, alt,
            velocidade,
            anguloReal, anguloReal + (float) (Math.PI / 2f),
            1, 1f,
            2.5f,
            sprite
        );
    }

    @Override
    public void reiniciarAtaque() {
        super.reiniciarAtaque();
        if (overflowGif != null) {
            overflowGif.desativar();
            overflowGif = null;
        }
        timer = 0;
        tempoVidaGif = 0f;
        explosaoInicialFeita = false;
        explosaoFinalFeita = false;
        timerBalas = 0;
    }
}

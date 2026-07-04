package model.Ataque.Ataques.Prova.Estagio;

import model.Ataque.Ataque;
import model.Batalha.EntidadeBatalha;
import model.Player.PlayerProva;
import model.Player.PlayerProva.SoulMode;
import model.Projetil.Projetil;
import model.util.MathUtils;
import model.util.Vector2D;
import model.Projetil.Comportamentos.ProjetilExplodeAoSerAtingido;
import model.Projetil.Comportamentos.ProjetilOrbita;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AtaquePalavras extends Ataque {

    private boolean started = false;
    private boolean latidoAtirado = false;
    private boolean waveSpawned = false;
    private float fireTimer = 1.5f;
    private float fireOffsetAngle = 0f;
    
    private static final String[] PALAVRAS_BOAS = {
        "organizado.png", "proativo.png", "responsavel.png"
    };
    
    private static final String[] PALAVRAS_RUINS = {
        "atrasado.png", "desmotivado.png", "desorganizado.png", 
        "distraido.png", "indisciplinado.png", "irresponsavel.png", 
        "preguiçoso.png", "arrogante.png"
    };

    private final ProjetilExplodeAoSerAtingido EXPLOSAO_RUIM = new ProjetilExplodeAoSerAtingido(
            32, "fogo.png", 30f, 20f, 450f, 2, 2f, 3f
    );
    
    private Projetil palavraBoa;

    public AtaquePalavras(PlayerProva target, EntidadeBatalha owner, float dificuldade) {
        super(target, owner, dificuldade, 300);
    }

    @Override
    protected void logicaAtaque(float dt) {
        if(target.getSoulMode() != SoulMode.YELLOW) target.setSoulMode(SoulMode.YELLOW);

        
        if (!waveSpawned && tempoDecorrido > 0.5f) {
            waveSpawned = true;
            spawnPalavras();
        }

        if (waveSpawned) {
            fireTimer -= dt;
            if (fireTimer <= 0f) {
                spawnFogo();
                float progress = Math.min(1.0f, tempoDecorrido / 13.0f);
                float nextCooldown = 1.5f - (progress * 0.5f);
                fireTimer = nextCooldown;
            }
            
            if (palavraBoa == null || !palavraBoa.isAtivo()) {
                encerrarAtaque();
            }
        }

        if (tempoDecorrido > 30f) {
            encerrarAtaque();
        }
    }

    private void spawnPalavras() {
        Vector2D centro = new Vector2D(owner.getX(), owner.getY());
        float raio = 300f;
        float velocidadeAngular = 1.5f; 
        
        List<String> sprites = new ArrayList<>();
        sprites.add(PALAVRAS_BOAS[MathUtils.randomIntInRange(0, PALAVRAS_BOAS.length - 1)]);
    
        for (int i = 0; i < 5; i++) {
            sprites.add(PALAVRAS_RUINS[MathUtils.randomIntInRange(0, PALAVRAS_RUINS.length - 1)]);
        }
        
        Collections.shuffle(sprites); 
        
        for (int i = 0; i < 6; i++) {
            float anguloInicial = (float) (i * Math.PI * 2 / 6);
            float spawnX = owner.getX()+ (float) Math.cos(anguloInicial) * raio;
            float spawnY =  owner.getY() + (float) Math.sin(anguloInicial) * raio;
            
            String sprite = sprites.get(i);
            boolean isBoa = false;
            for (String boa : PALAVRAS_BOAS) {
                if (sprite.equals(boa)) {
                    isBoa = true;
                    break;
                }
            }
            
            Projetil p = spawnProjetil(
                spawnX, spawnY,
                160, 40,
                0f, 0f, 0f,
                2, 1f, 30f,
                sprite
            );

            
            if (p != null) {
                p.setMultiplicadorSprite(1f);
                p.setPersistente(true);
                p.addComportamento(new ProjetilOrbita(centro, velocidadeAngular, raio, anguloInicial));
                
                if (isBoa) {
                    palavraBoa = p;
                } else {
                    p.addComportamentoDespawn(EXPLOSAO_RUIM);
                }
            }
        }
    }

    private void spawnFogo() {
        float centerX = owner.getX();
        float centerY = owner.getY();
        
        for (int i = 0; i < 8; i++) {
            float angle = fireOffsetAngle + (float) (i * Math.PI * 2 / 8);
            spawnProjetil(
                centerX, centerY,
                50, 30, 
                200f, 
                angle, 
                angle, 
                1, 1f, 5f, 
                "fogo.png"
            );
        }
        
        fireOffsetAngle += 0.3f;
    }

    @Override
    public void reiniciarAtaque() {
        super.reiniciarAtaque();
        started = false;
        latidoAtirado = false;
        waveSpawned = false;
        fireTimer = 0.5f;
        fireOffsetAngle = 0f;
        palavraBoa = null;
    }
}

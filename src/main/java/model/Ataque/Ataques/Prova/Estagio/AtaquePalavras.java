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

    private boolean ondaGerada = false;
    private float timerFogo = 1.5f;
    private float anguloDeslocamentoFogo = 0f;
    // define as palavras boas e ruins
    private static final String[] PALAVRAS_BOAS = {
            "organizado.png", "proativo.png", "responsavel.png"
    };

    private static final String[] PALAVRAS_RUINS = {
            "atrasado.png", "desmotivado.png", "desorganizado.png",
            "distraido.png", "indisciplinado.png", "irresponsavel.png",
            "preguiçoso.png", "arrogante.png"
    };

    private final ProjetilExplodeAoSerAtingido EXPLOSAO_RUIM = new ProjetilExplodeAoSerAtingido(
            32, "fogo.png", 30f, 20f, 450f, 2, 2f, 3f);

    private Projetil palavraBoa;

    public AtaquePalavras(PlayerProva target, EntidadeBatalha owner, float dificuldade) {
        super(target, owner, dificuldade, 300);
    }

    @Override
    protected void logicaAtaque(float dt) {
        if (target.getSoulMode() != SoulMode.YELLOW)
            target.setSoulMode(SoulMode.YELLOW);

        if (!ondaGerada && tempoDecorrido > 0.5f) {
            ondaGerada = true;
            spawnPalavras();
        }

        if (ondaGerada) {
            timerFogo -= dt;
            if (timerFogo <= 0f) {
                spawnFogo();
                float progresso = Math.min(1.0f, tempoDecorrido / 13.0f);
                float proximoCooldown = 1.5f - (progresso * 0.5f);
                timerFogo = proximoCooldown;
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
            float inicioX = owner.getX() + (float) Math.cos(anguloInicial) * raio;
            float inicioY = owner.getY() + (float) Math.sin(anguloInicial) * raio;

            String sprite = sprites.get(i);
            boolean ehBoa = false;
            for (String boa : PALAVRAS_BOAS) {
                if (sprite.equals(boa)) {
                    ehBoa = true;
                    break;
                }
            }

            Projetil p = spawnProjetil(
                    inicioX, inicioY,
                    160, 40,
                    0f, 0f, 0f,
                    2, 1f, 30f,
                    sprite);

            if (p != null) {
                p.setMultiplicadorSprite(1f);
                p.setPersistente(true);
                p.addComportamento(new ProjetilOrbita(centro, velocidadeAngular, raio, anguloInicial));

                if (ehBoa) {
                    palavraBoa = p;
                } else {
                    p.addComportamentoDespawn(EXPLOSAO_RUIM);
                }
            }
        }
    }

    private void spawnFogo() {
        float centroX = owner.getX();
        float centroY = owner.getY();

        for (int i = 0; i < 8; i++) {
            float angulo = anguloDeslocamentoFogo + (float) (i * Math.PI * 2 / 8);
            spawnProjetil(
                    centroX, centroY,
                    50, 30,
                    200f,
                    angulo,
                    angulo,
                    1, 1f, 5f,
                    "fogo.png");
        }

        anguloDeslocamentoFogo += 0.3f;
    }

    @Override
    public void encerrarAtaque() {
        super.encerrarAtaque();
        if (target != null)
            target.setSoulMode(SoulMode.RED);
    }

    @Override
    public void reiniciarAtaque() {
        super.reiniciarAtaque();
        if (target != null)
            target.setSoulMode(SoulMode.RED);
        ondaGerada = false;
        timerFogo = 0.5f;
        anguloDeslocamentoFogo = 0f;
        palavraBoa = null;
    }
}

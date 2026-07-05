package model.Ataque.Ataques.Prova.Naturezas;

import model.Ataque.Ataque;
import model.Batalha.EntidadeBatalha;
import model.Player.PlayerProva;
import model.Projetil.ComportamentoProjetil;
import model.Projetil.Comportamentos.ProjetilAceleracao;
import model.Projetil.Projetil;
import model.util.MathUtils;
import model.util.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class AtaqueAtomosGravidade extends Ataque {

    private float timer = 0;
    private int fase = 0; 
    private List<Projetil> atomos = new ArrayList<>();
    private float timerExplosao = 0;
    private float tempoExplosao = 0;
    private float tempoMaximoExplosao = 7.5f;
    private float anguloExplosao = 0;

    private float timerCarro = 0;
    private final float ATRASO_GERACAO_CARRO = 1.0f;
    private boolean iniciado = false;

    private ComportamentoProjetil desacelaracao = new ProjetilAceleracao(-1400f);
    public AtaqueAtomosGravidade(PlayerProva target, EntidadeBatalha owner, float dificuldade) {
        super(target, owner, dificuldade, 150);
        this.minX = 760f;
        this.maxX = 1160f;
        this.minY = 600f;
        this.maxY = 1000f; 
    }

    @Override
    protected void logicaAtaque(float dt) {
        if (!iniciado) {
            if (target != null) {
                target.setSoulMode(PlayerProva.SoulMode.BLUE);
            }
            iniciado = true;
        }

        timerCarro += dt;
        if (timerCarro >= ATRASO_GERACAO_CARRO) {
            timerCarro = 0;
            spawnCarro();
        }

        if (fase == 0) {
            float inicioX = owner.getX();
            float inicioY = owner.getY() - 50f;
            
            float angulo1 = (float) Math.PI / 4f;
            Projetil p1 = spawnProjetil(
                    inicioX, inicioY, 
                    45, 45, 
                    900f, 
                    angulo1, 
                    angulo1 - (float) Math.PI / 2f,
                    1, 0f, 
                    8.5f, 
                    "atomo.png" 
            );
            if (p1 != null) {

                p1.addComportamento(desacelaracao);
                p1.setPersistente(true);
                p1.setDanoShield(0);
                p1.setDanoNota(0f);
                atomos.add(p1);
            }

            float angulo2 = (float) (3 * Math.PI / 4f);
            Projetil p2 = spawnProjetil(
                    inicioX, inicioY, 
                    45, 45, 
                    900f, 
                    angulo2, 
                    angulo2 - (float) Math.PI / 2f,
                    1, 0f, 
                    8.5f, 
                    "atomo.png" 
            );
            if (p2 != null) {

                p2.addComportamento(desacelaracao);
                p2.setPersistente(true);
                p2.setDanoShield(0);
                p2.setDanoNota(0f);
                atomos.add(p2);
            }
            fase = 1;
        } 
        else if (fase == 1) {
            timer += dt;
            //ai ativa os atomos faz eles fazerem algo
            if (timer >= 1.5f) {
                for (Projetil atomo : atomos) {
                    if (atomo != null) {
                        atomo.setVelocidade(new Vector2D(0f, 0f));
                        atomo.setDanoShield(1); 
                        atomo.setDanoNota(1f);
                    }
                }
                fase = 2;
                timer = 0;
            }
        } 
        else if (fase == 2) {
            for (Projetil atomo : atomos) {
                if (atomo != null && atomo.isAtivo()) {
                    break;
                }
            }

            
            timerExplosao += dt;
            tempoExplosao += dt;
            float intervaloExplosao = 0.7f - (dificuldade / 100f);

            if(tempoExplosao < tempoMaximoExplosao) {
                //pra cad atomo faz a logica do bullet hell de spawnar 4 projeteis girar um pouco atirar mais
                if (timerExplosao >= intervaloExplosao) {
                    for (Projetil atomo : atomos) {
                        if (atomo == null || !atomo.isAtivo()) continue;
                        float atomoX = atomo.getX();
                        float atomoY = atomo.getY();

                        for (int i = 0; i < 4; i++) {
                            float angulo = anguloExplosao + (i * (float) (Math.PI / 2f));
                            spawnProjetil(
                                    atomoX, atomoY,
                                    15, 15, 
                                    225f + (dificuldade * 10), 
                                    angulo,
                                    angulo,
                                    1, 0.5f,
                                    4f,
                                    "eletron.png" 
                            );
                        }
                    }
                    anguloExplosao += 0.367f;
                    timerExplosao = 0;
                }
            }
            
            if (tempoExplosao >= tempoMaximoExplosao) {
                timer += dt;
                if (timer > 1.5f) {
                    this.encerrarAtaque();
                }
            }
        }
    }

    private void spawnCarro() {
        boolean vemDaEsquerda = MathUtils.randomFloatInRange(0, 1) > 0.5f;
        float inicioY = maxY - 20; // Apenas no chão
        float inicioX = vemDaEsquerda ? minX - 100 : maxX + 100;
        float angulo = vemDaEsquerda ? 0 : (float) Math.PI;
        String sprite = vemDaEsquerda ? "carro.png" : "carroEsq.png";

        spawnProjetil(
            inicioX, inicioY,
            80, 40,
            250f, angulo, 0f,
            1, 0.5f,
            5f, sprite
        );
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
        this.timer = 0;
        this.fase = 0;
        this.timerExplosao = 0;
        this.tempoExplosao = 0;
        this.anguloExplosao = 0;
        this.timerCarro = 0;
        this.iniciado = false;
        for (Projetil atomo : atomos) {
            if (atomo != null) atomo.desativar();
        }
        this.atomos.clear();
    }


}

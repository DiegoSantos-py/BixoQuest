package model.Ataque.Ataques.Prova.Software;

import model.Ataque.Ataque;
import model.Batalha.EntidadeBatalha;
import model.Player.PlayerProva;
import model.Projetil.Comportamentos.ProjetilAceleracao;
import model.Projetil.Comportamentos.ProjetilQuicaNasBordas;
import model.Projetil.Projetil;
import model.util.MathUtils;

public class AtaqueArvoreBinaria extends Ataque {

    private boolean iniciado = false;
    private ProjetilAceleracao desacelerador = new ProjetilAceleracao(-125f);
    private ProjetilQuicaNasBordas quica;
    
    private int profundidadeMaxima = 5;

    public AtaqueArvoreBinaria(PlayerProva target, EntidadeBatalha owner, float dificuldade) {
        super(target, owner, dificuldade, 150);
    }

    @Override
    protected void logicaAtaque(float dt) {
        if (!iniciado) {
            iniciado = true;
            quica = new ProjetilQuicaNasBordas(getMinX(), getMaxX(), getMinY(), getMaxY(), 10f);
            
            spawnNode((getMinX() + getMaxX()) / 2f, getMinY() + 50f, (float) Math.PI / 2f, 1); //spana esse q spawma +2 q spqmnam + 2 e assi m por diante
        }
        
        if (iniciado && getProjeteis().isEmpty()) {
            encerrarAtaque();
        }

        // Tempo limite de segurança
        if (tempoDecorrido > 15f) {
            encerrarAtaque();
        }
    }
    
    private void spawnNode(float x, float y, float angulo, int profundidade) {
        if (profundidade > profundidadeMaxima) return;
        

        float velocidade = 120f + ((dificuldade / 20f) * 20f) + (profundidade * 15f);
        float duracao = 1.0f + (dificuldade / 20f);
        
        Projetil p = spawnProjetil(
            x, y,
            24f, 24f,
            velocidade,
            angulo, 0f, 
            1, 1f,
            duracao,
            "circulo.png"
        );
        
        if (p != null) {
            p.addComportamento(desacelerador);
            p.addComportamento(quica);
            //todo o projetil recebe o comportamento pra spawnar mais, os filhos dele tbm recebem até chegar no limite de profundidade
            p.addComportamentoDespawn((proj, factory) -> {
                if (profundidade >= profundidadeMaxima) return;
                
                // Filhos atiram em direções completamente aleatórias agora
                float ang1 = MathUtils.randomFloatInRange(0, (float)(Math.PI * 2));
                float ang2 = MathUtils.randomFloatInRange(0, (float)(Math.PI * 2));
                
                spawnNode(proj.getX(), proj.getY(), ang1, profundidade + 1);
                spawnNode(proj.getX(), proj.getY(), ang2, profundidade + 1);
            });
        }
    }

    @Override
    public void reiniciarAtaque() {
        super.reiniciarAtaque();
        iniciado = false;
    }
}

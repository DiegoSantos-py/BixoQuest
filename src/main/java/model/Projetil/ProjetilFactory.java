package model.Projetil;

import java.util.ArrayList;
import java.util.List;

import model.Batalha.EntidadeBatalha;
import model.Player.PlayerProva;
import model.Projetil.Projeteis.ProjetilBasico;
import model.Projetil.Projeteis.ProjetilExplosivo;
import model.Projetil.Projeteis.ProjetilQueSegue;
import model.util.Hitbox;
import model.util.Vector2D;

public class ProjetilFactory {

    private ProjetilBasico[] poolBasico;
    private ProjetilQueSegue[] poolHoming;
    private ProjetilExplosivo[] poolExplosivo;

    private int indexBasico = 0;
    private int indexHoming = 0;
    private int indexExplosivo = 0;

    public ProjetilFactory(PlayerProva target, EntidadeBatalha owner, int maxBasico, int maxHoming, int maxExplosivo) {
        if (target == null) {
            throw new exception.Projetil.NullTargetFactoryException();
        }
        if (owner == null) {
            throw new exception.Projetil.FactoryInvalidaException("owner", "não pode ser nulo");
        }
        if (maxBasico < 0 || maxHoming < 0 || maxExplosivo < 0) {
            throw new exception.Projetil.FactoryInvalidaException("capacidade", "não pode ser negativa");
        }

        poolBasico = new ProjetilBasico[maxBasico];
        poolHoming = new ProjetilQueSegue[maxHoming];
        poolExplosivo = new ProjetilExplosivo[maxExplosivo];
        // cria os projeteis todos vazios sem nada pra preencher eles conforme necessidade

        for (int i = 0; i < maxBasico; i++) {
            poolBasico[i] = new ProjetilBasico(new Hitbox(new Vector2D(0, 0), new Vector2D(0, 0), 0), new Vector2D(0, 0), 0, 0, 0);
            poolBasico[i].setFactory(this);
            poolBasico[i].setOwner(owner);
            poolBasico[i].setTarget(target);
        }

        for (int i = 0; i < maxHoming; i++) {
            poolHoming[i] = new ProjetilQueSegue(new Hitbox(new Vector2D(0, 0), new Vector2D(0, 0), 0), new Vector2D(0, 0), 0, 0, 0);
            poolHoming[i].setFactory(this);
            poolHoming[i].setOwner(owner);
            poolHoming[i].setTarget(target);
        }

        for (int i = 0; i < maxExplosivo; i++) {
            poolExplosivo[i] = new ProjetilExplosivo(new Hitbox(new Vector2D(0, 0), new Vector2D(0, 0), 0), new Vector2D(0, 0), 0, 0, 0);
            poolExplosivo[i].setFactory(this);
            poolExplosivo[i].setOwner(owner);
            poolExplosivo[i].setTarget(target);
        }
    }
    public void setTarget(PlayerProva target) {
        if (target == null) {
            throw new exception.Projetil.NullTargetFactoryException();
        }
        for (ProjetilBasico basico : poolBasico) {
            basico.setTarget(target);
        }
        for (ProjetilQueSegue homing : poolHoming) {
            homing.setTarget(target);
        }
        for (ProjetilExplosivo explosivo : poolExplosivo) {
            explosivo.setTarget(target);
        }
    }

    public Projetil spawn(float posX, float posY, float tamanhoX, float tamanhoY, float velocidade, float anguloSpawn, float anguloHitbox, ProjetilID id, int danoShield, float danoNota, float duracaoMaxima) {
        
    
        float velX = (float) (velocidade * Math.cos(anguloSpawn));
        float velY = (float) (velocidade * Math.sin(anguloSpawn));

        Projetil p = null;
        //calcula a velocidade x e y c base na matriz de rotacao la de geom. analitica
        // agradecimento especial a prof victor por me ensinar isso

        switch (id) {
            case BASICO:
                if (poolBasico.length > 0) {
                    p = poolBasico[indexBasico];
                    indexBasico = (indexBasico + 1) % poolBasico.length;
                }
                break;
            case HOMING:
                if (poolHoming.length > 0) {
                    p = poolHoming[indexHoming];
                    indexHoming = (indexHoming + 1) % poolHoming.length;
                }
                break;
            case EXPLOSIVE:
                if (poolExplosivo.length > 0) {
                    p = poolExplosivo[indexExplosivo];
                    indexExplosivo = (indexExplosivo + 1) % poolExplosivo.length;
                }
                break;
        }

        if (p != null) {
            p.reviver(posX, posY, tamanhoX, tamanhoY, velX, velY, anguloHitbox, danoShield, danoNota, duracaoMaxima);
        }
        return p;
    }

    public void atualizar(float dt) {
        for (ProjetilBasico p : poolBasico) {
            if (p.isAtivo()){
                p.atualizar(dt);
            }
        }
        for (ProjetilQueSegue p : poolHoming) {
            if (p.isAtivo()){
                p.atualizar(dt);
            }
        }
        for (ProjetilExplosivo p : poolExplosivo) {
            if (p.isAtivo()){
                p.atualizar(dt);
            }
        }
    }

    public List<Projetil> getAtivos() {
        List<Projetil> ativos = new ArrayList<>();
        for (ProjetilBasico p : poolBasico) {
            if (p.isAtivo()) {
                ativos.add(p);
            }
        }
        for (ProjetilQueSegue p : poolHoming) {
            if (p.isAtivo()) {
                ativos.add(p);
            }
        }
        for (ProjetilExplosivo p : poolExplosivo) {
            if (p.isAtivo()) ativos.add(p);
        }
        return ativos;
    }

}

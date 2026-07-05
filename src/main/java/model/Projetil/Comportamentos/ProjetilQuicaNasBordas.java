package model.Projetil.Comportamentos;

import model.Projetil.ComportamentoProjetil;
import model.Projetil.Projetil;

public class ProjetilQuicaNasBordas implements ComportamentoProjetil {
    private float minX, maxX, minY, maxY;
    private float raio;

    public ProjetilQuicaNasBordas(float minX, float maxX, float minY, float maxY, float raio) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.raio = raio;
    }

    @Override
    public void executar(Projetil p, float dt) {
        float px = p.getX();
        float py = p.getY();
        float raioAtual = raio * p.getMultiplicadorSprite();
        
        if (px - raioAtual <= minX) {
            p.setX(minX + raioAtual + 1);
            p.getVelocidade().setX(Math.abs(p.getVelocidade().getX()));
        } else if (px + raioAtual >= maxX) {
            p.setX(maxX - raioAtual - 1);
            p.getVelocidade().setX(-Math.abs(p.getVelocidade().getX()));
        }
        
        if (py - raioAtual <= minY) {
            p.setY(minY + raioAtual + 1);
            p.getVelocidade().setY(Math.abs(p.getVelocidade().getY()));
        } else if (py + raioAtual >= maxY) {
            p.setY(maxY - raioAtual - 1);
            p.getVelocidade().setY(-Math.abs(p.getVelocidade().getY()));
        }
    }
}

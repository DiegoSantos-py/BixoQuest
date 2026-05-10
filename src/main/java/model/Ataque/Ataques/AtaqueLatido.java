package model.Ataque.Ataques;

import java.util.Random;

import model.EntidadeBatalha;
import model.Ataque.Ataque;
import model.Player.PlayerProva;
import model.Projetil.ProjetilID;
import model.util.Vector2D;

public class AtaqueLatido extends Ataque {

    private float timer = 0;
    private int projeteisSpawnados = 0;

    public AtaqueLatido(PlayerProva target, EntidadeBatalha owner) {
        // aloca espaço para 80 básicos (para múltiplos projéteis) e 10 explosivos para performance
        super(target, owner, 20,0,0); 
    }

    @Override
    protected void logicaAtaque(float dt) {


        timer += dt;
        float velocidade =  10f * (dificuldade/2 + 0.5f); // 1 pra ind 10 2 pra ind 30
        
        
        if (timer >= (10 / dificuldade) && !this.isFinalizado()) {
        
            int framesNoFuturo = (int) (3 + (dificuldade/10) * 2);
            float anguloParaPlayer = (float) Math.atan2(
                (target.getY() + target.getVelocidade().getY() * dt *framesNoFuturo ) - owner.getY(),
                (target.getX() + target.getVelocidade().getX() * dt * framesNoFuturo )- owner.getX()
            );
            spawnProjetil(owner.getX(), owner.getY(), 40, 40, 10f, 0f, anguloParaPlayer, ProjetilID.BASICO, 1, 0f, 7.5f);

            projeteisSpawnados++;
            timer = 0;
        }

        // lanca 5 + (tiros adicionais baseado na dificuldade) e
        if (projeteisSpawnados >= 5 + (int)(dificuldade/10)) {
            if(timer>=1f){
                this.encerrarAtaque();
            }
        }
    }
}

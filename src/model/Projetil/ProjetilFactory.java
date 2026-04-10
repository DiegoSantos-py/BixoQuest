package model.Projetil;

import model.Projetil.Projeteis.*;
import model.util.Hitbox;
import model.util.Vector2D;

public class ProjetilFactory {

    public static Projetil novoProjetil(Hitbox h, Vector2D vel, ProjetilID id, int danoShield, float danoNota, int duracaoMaxima) {
        switch (id) {
            case BASICO:
                return new ProjetilBasico(h, vel, danoShield, danoNota, duracaoMaxima);
            case HOMING:
                return new ProjetilQueSegue(h, vel, danoShield, danoNota, duracaoMaxima);
            case EXPLOSIVE:
                return new ProjetilExplosivo(h, vel, danoShield, danoNota, duracaoMaxima);
            default:
                return new ProjetilBasico(h, vel, danoShield, danoNota, duracaoMaxima);
        }
    }
}

// === EXEMPLO DE USO  ===

// 1. definir a posição de spawn (ex: pegando do centro da prova)
// Vector2D spawnPos = prova.getInstance().getHitbox().getCentro(); 

// 2. chamar a Factory para criar o objeto completo em uma linha
// Parâmetros: Hitbox(pos, tam, ang(da hitbox)), vel, ID, danoShield, danoNota, duracao
/*
    Projetil p = ProjetilFactory.spawn(
        new Hitbox(new Vector2D(500, 100), new Vector2D(15, 15), 0), 
        new Vector2D(0, 5), 
        ProjetilID.BASICO, 
        1, 
        0.1f, 
        300 (5s a 60fps)
    );
*/

// 3. adicionaar na lista do manager
// listaProjeteis.add(p); (ainda n existe)

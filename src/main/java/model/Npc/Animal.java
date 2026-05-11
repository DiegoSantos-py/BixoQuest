package model.Npc;

import java.util.ArrayList;

import model.Ataque.Ataques.AtaqueArranhao;
import model.Ataque.Ataques.AtaqueLatido;
import model.Oponente;
import model.Personagem;
import model.Ataque.Ataque;
import model.Ataque.Ataques.AtaqueMordida;
import model.Disciplina.AreaConhecimento;
import model.util.Hitbox;
import model.util.Vector2D;

//TODO: ANIMAL NAO IMPLEMENTA GET OPONENTE
//public class Animal extends Npc implements Batalhavel{
public class Animal{
    private int indole;
    private boolean domado;
    private Especie especie;
    private Oponente oponente;
    public Animal(String nome, int cX, int cY, ArrayList<String> falas,Especie especie, int indole){
        //super(nome, cX, cY, falas);
        this.indole = indole;
        this.especie = especie;
    }



    //TODO: OVERRIDE N FUNCIONA NESSE CASO java: method does not override or implement a method from a supertype
    // posXspritebatalha, posYspritebatalha n foram declarados em nenhum lugar
    //@Override
    /*
    public String aoInteragir(Personagem player) {
        if (domado) {
            player.addMotivacao(indole / 5);
            return this.getFalas().get(1);
        }
        //fala 1 = boa, fala 0 = ruim...

        Hitbox hitbox = new Hitbox(new Vector2D(posXspritebatalha, posYspritebatalha), new Vector2D(0, 0), 0);
        Oponente oponenteAnimal = new Oponente(
                hitbox,
                new Vector2D(0, 0),
                getNome(),
                indole,
                AreaConhecimento.ANI

        );
        oponenteAnimal.adicionarAtaque(new AtaqueMordida(null, oponenteAnimal, indole));
        if (especie == Especie.CACHORRO) {
            oponenteAnimal.adicionarAtaque(new AtaqueLatido(null, oponenteAnimal));
        }
        if (especie == Especie.GATO) {
            oponenteAnimal.adicionarAtaque(new AtaqueArranhao(null, oponenteAnimal));
        }

        this.oponente = oponenteAnimal;
        return this.getFalas().get(0);
        //o player (target (playerProva) é definido no Servico de batalha
        //TODO: SERVICO DE BATALHA

    }

    @Override
    public Oponente getOponente() {
        return oponente;
    }
    public int getIndole() {
        return indole;
    }*/
}

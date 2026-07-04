package model.Ataque;

import java.util.List;

import exception.Ataque.AtaqueInvalidoException;
import exception.Projetil.NullTargetException;
import model.Batalha.EntidadeBatalha;
import model.Player.PlayerProva;
import model.Projetil.Projetil;
import model.Projetil.ProjetilFactory;

public abstract class Ataque {
    protected float tempoDecorrido = 0;
    protected boolean finalizado = false;
    protected float dificuldade;
    protected ProjetilFactory factory;
    protected EntidadeBatalha owner;
    protected PlayerProva target;
    protected float maxX;
    protected float maxY;
    protected float minX;
    protected float minY;//os 4 lados da caixa q vai prender o player

    //(new Vector2D(960, 800)
    public Ataque(PlayerProva target, EntidadeBatalha owner,float dificuldade, int maxProjeteis) {

        if(dificuldade<0){
            throw new AtaqueInvalidoException("dificuldade", "dificuldade precisa ser maior que 0");
        }

        this.target = target;
        this.owner = owner;
        this.dificuldade = dificuldade;
        this.maxX = 1160f;
        this.maxY = 1000f;
        this.minX = 760f;
        this.minY = 600f;
        this.factory = new ProjetilFactory(target, owner, maxProjeteis);
    }

    public final void atualizar(float dt) {
        if (finalizado) return;
        if(this.target == null){
            throw new NullTargetException();
        }
        if(this.owner == null){
            throw new AtaqueInvalidoException("owner","nao pode ser nulo");
        }
        tempoDecorrido += dt;
        logicaAtaque(dt);
        factory.atualizar(dt);  
    }

    protected Projetil spawnProjetil(float posX, float posY, float tamanhoX, float tamanhoY, float velocidade, 
                                 float anguloSpawn, float anguloHitbox, 
                                 int danoShield, float danoNota, float duracaoMaxima, String spriteDir) {
        
        return factory.spawn(posX, posY, tamanhoX, tamanhoY, velocidade, anguloSpawn, anguloHitbox, 
                      danoShield, danoNota, duracaoMaxima, spriteDir);
    }

    protected abstract void logicaAtaque(float dt);

    //(new Vector2D(960, 800)
    public float getMaxX() {
        return maxX;
    }
    public float getMaxY() {
        return maxY;
    }
    public float getMinX() {
        return minX;
    }

    public float getMinY() {
        return minY;
    }

    public float getTempoDecorrido() {
        return tempoDecorrido;
    }

    public float getDificuldade() {
        return dificuldade;
    }

    public void setMaxX(float maxX) {
        if(maxX <= 0){
            throw new AtaqueInvalidoException("maxX","maxX não pode ser nulo ou negativo");
        }
        if(maxX < this.minX){
            throw new AtaqueInvalidoException("maxX","maxX não pode menor que minX");
        }
        this.maxX = maxX;
    }
    public void setMinX(float minX) {
        if (minX <= 0) {
            throw new AtaqueInvalidoException(
                    "minX",
                    "minX não pode ser nulo ou negativo"
            );
        }
        if (minX > this.maxX) {
            throw new AtaqueInvalidoException(
                    "minX",
                    "minX não pode ser maior que maxX"
            );
        }
        this.minX = minX;
    }

    public void setMaxY(float maxY) {
        if (maxY <= 0) {
            throw new AtaqueInvalidoException(
                    "maxY",
                    "maxY não pode ser nulo ou negativo"
            );
        }
        if (maxY < this.minY) {
            throw new AtaqueInvalidoException(
                    "maxY",
                    "maxY não pode ser menor que minY"
            );
        }
        this.maxY = maxY;
    }

    public void setMinY(float minY) {
        if (minY <= 0) {
            throw new AtaqueInvalidoException(
                    "minY",
                    "minY não pode ser nulo ou negativo"
            );
        }
        if (minY > this.maxY) {
            throw new AtaqueInvalidoException(
                    "minY",
                    "minY não pode ser maior que maxY"
            );
        }
        this.minY = minY;
    }

    public void encerrarAtaque() {
        this.finalizado = true;
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    public void setTarget(PlayerProva target) {
        this.target = target;
        this.factory.setTarget(target);
    }

    public void setOwner(EntidadeBatalha owner) {
        this.owner = owner;
        this.factory.setOwner(owner);
    }

    public void reiniciarAtaque() {
        this.finalizado = false;
        this.tempoDecorrido = 0;
        //restarta o ataque e desliga todos os projeteis da pool de projeteis(pra eles n ficarem no ar parados)
        factory.desativarTodos();
    }

    public List<Projetil> getProjeteis() { 
        return factory.getAtivos(); 
    }

    public ProjetilFactory getFactory() {
        return factory;
    }
}

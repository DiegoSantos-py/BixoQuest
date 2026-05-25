package model.Projetil;

import exception.Projetil.NullTargetException;
import exception.Projetil.ProjetilInvalidoException;
import model.Batalha.EntidadeBatalha;
import model.Player.PlayerProva;
import model.util.Hitbox;
import model.util.Vector2D;

public abstract class Projetil extends EntidadeBatalha {

    protected int danoShield;
    protected float danoNota;

    // contador de tempo vivo em ms
    protected float tempoDeVida = 0;

    // duração máxima em ms
    protected float duracaoMaxima;

    // NÃO re-declarar campo ativo aqui
    // pertence ao pai EntidadeBatalha

    protected EntidadeBatalha owner;
    protected PlayerProva target;
    protected ProjetilFactory factory;

    public Projetil(
            Hitbox hitbox,
            Vector2D velocidade,
            int danoShield,
            float danoNota,
            float duracaoMaxima
    ) {

        super(hitbox, velocidade);

        if (hitbox == null) {
            throw new ProjetilInvalidoException(
                    "hitbox",
                    "não pode ser nula"
            );
        }

        if (velocidade == null) {
            throw new ProjetilInvalidoException(
                    "velocidade",
                    "não pode ser nula"
            );
        }

        if (danoShield < 0) {
            throw new ProjetilInvalidoException(
                    "danoShield",
                    "não pode ser negativo"
            );
        }

        if (danoNota < 0) {
            throw new ProjetilInvalidoException(
                    "danoNota",
                    "não pode ser negativo"
            );
        }

        if (duracaoMaxima < 0) {
            throw new ProjetilInvalidoException(
                    "duracaoMaxima",
                    "deve ser positiva"
            );
        }

        this.danoShield = danoShield;
        this.danoNota = danoNota;
        this.duracaoMaxima = duracaoMaxima;

        // garante que projéteis pré-alocados começam inativos
        desativar();
    }

    public void setFactory(ProjetilFactory factory) {

        if (factory == null) {
            throw new ProjetilInvalidoException(
                    "factory",
                    "não pode ser nula"
            );
        }

        this.factory = factory;
    }

    public void setOwner(EntidadeBatalha owner) {

        if (owner == null) {
            throw new ProjetilInvalidoException(
                    "owner",
                    "não pode ser nulo"
            );
        }

        this.owner = owner;
    }

    public void setTarget(PlayerProva target) {

        // target pode ser null(as vezes)
        this.target = target;
    }

    public void reviver(
            float posX,
            float posY,
            float sizeX,
            float sizeY,
            float velX,
            float velY,
            float anguloHitbox,
            int danoShield,
            float danoNota,
            float duracaoMaxima
    ) {

        if (sizeX <= 0) {
            throw new ProjetilInvalidoException(
                    "sizeX",
                    "deve ser maior que zero"
            );
        }

        if (sizeY <= 0) {
            throw new ProjetilInvalidoException(
                    "sizeY",
                    "deve ser maior que zero"
            );
        }

        if (danoShield < 0) {
            throw new ProjetilInvalidoException(
                    "danoShield",
                    "não pode ser negativo"
            );
        }

        if (danoNota < 0) {
            throw new ProjetilInvalidoException(
                    "danoNota",
                    "não pode ser negativo"
            );
        }

        if (duracaoMaxima < 0) {
            throw new ProjetilInvalidoException(
                    "duracaoMaxima",
                    "deve ser positiva"
            );
        }

        this.hitbox.setCentro(posX, posY);
        this.hitbox.setTamanho(sizeX, sizeY);
        this.hitbox.setAnguloRad(anguloHitbox);

        this.velocidade.set(velX, velY);

        this.danoShield = danoShield;
        this.danoNota = danoNota;
        this.duracaoMaxima = duracaoMaxima;

        this.tempoDeVida = 0;

        this.ativar();
    }

    public abstract void executarAI(float deltaTime);

    public void aoColidirComPlayer() {

        if (this.target == null) {
            throw new NullTargetException();
        }
        this.target.ReceberDano(
                this.danoShield,
                this.danoNota
        );
    }

    public void aoDespawnar() {

    }

    public final void atualizar(float deltaTime) {

        if(this.target == null){
            throw new NullTargetException();
        }

        if (!isAtivo()) {
            return;
        }

        if (tempoDeVida >= duracaoMaxima) {

            this.desativar();

            aoDespawnar();

            return;
        }

        this.tempoDeVida += deltaTime;

        executarAI(deltaTime);

        this.atualizarPosicao(deltaTime);

        if (
                this.target != null &&
                        this.hitbox.checarColisao(
                                this.target.getHitbox()
                        )
        ) {

            aoColidirComPlayer();
        }
    }

    public int getDanoShield() {
        return danoShield;
    }

    public float getDanoNota() {
        return danoNota;
    }
}
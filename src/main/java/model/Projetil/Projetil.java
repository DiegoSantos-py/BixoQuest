package model.Projetil;

import exception.Projetil.NullTargetException;
import exception.Projetil.ProjetilInvalidoException;
import model.Batalha.EntidadeBatalha;
import model.Player.PlayerProva;
import model.util.Hitbox;
import model.util.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class Projetil extends EntidadeBatalha {

    protected int danoShield;
    protected float danoNota;

    // contador de tempo vivo em ms
    protected float tempoDeVida = 0;

    // duração máxima em ms
    protected float duracaoMaxima;

    protected boolean persistente = false;

    protected EntidadeBatalha owner;
    protected PlayerProva target;
    protected ProjetilFactory factory;

    private List<ComportamentoProjetil> comportamentosAI = new ArrayList<>();
    private List<ComportamentoAoDespawnar> comportamentosDespawn = new ArrayList<>();

    private List<ComportamentoAoColidirComPlayer> comportamentosAoColidir = new ArrayList<>();

    public Projetil(
            Hitbox hitbox,
            Vector2D velocidade,
            int danoShield,
            float danoNota,
            float duracaoMaxima,
            String spriteDir) {

        super(hitbox, velocidade, "/assets/batalha/projeteis/" + spriteDir);

        if (hitbox == null) {
            throw new ProjetilInvalidoException(
                    "hitbox",
                    "não pode ser nula");
        }

        if (velocidade == null) {
            throw new ProjetilInvalidoException(
                    "velocidade",
                    "não pode ser nula");
        }

        if (danoShield < 0) {
            throw new ProjetilInvalidoException(
                    "danoShield",
                    "não pode ser negativo");
        }

        if (danoNota < 0) {
            throw new ProjetilInvalidoException(
                    "danoNota",
                    "não pode ser negativo");
        }

        if (duracaoMaxima < 0) {
            throw new ProjetilInvalidoException(
                    "duracaoMaxima",
                    "deve ser positiva");
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
                    "não pode ser nula");
        }

        this.factory = factory;
    }

    public void setOwner(EntidadeBatalha owner) {
        this.owner = owner;
    }

    public void setTarget(PlayerProva target) {
        this.target = target;
    }

    public PlayerProva getTarget() {
        return target;
    }

    public boolean isPersistente() {
        return persistente;
    }

    public void setPersistente(boolean persistente) {
        this.persistente = persistente;
    }

    public void addComportamentoColisao(ComportamentoAoColidirComPlayer colisao) {
        this.comportamentosAoColidir.add(colisao);
    }

    public void addComportamento(ComportamentoProjetil ai) {
        if (ai == null) {
            throw new ProjetilInvalidoException(
                    "comportamentoAI",
                    "não pode ser nulo");
        }
        this.comportamentosAI.add(ai);
    }

    public void addComportamentoDespawn(ComportamentoAoDespawnar despawn) {
        if (despawn == null) {
            throw new ProjetilInvalidoException(
                    "comportamentoDespawn",
                    "não pode ser nulo");
        }
        this.comportamentosDespawn.add(despawn);
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
            float duracaoMaxima,
            String spriteDir) {

        if (sizeX <= 0) {
            throw new ProjetilInvalidoException(
                    "sizeX",
                    "deve ser maior que zero");
        }

        if (sizeY <= 0) {
            throw new ProjetilInvalidoException(
                    "sizeY",
                    "deve ser maior que zero");
        }

        if (danoShield < 0) {
            throw new ProjetilInvalidoException(
                    "danoShield",
                    "não pode ser negativo");
        }

        if (danoNota < 0) {
            throw new ProjetilInvalidoException(
                    "danoNota",
                    "não pode ser negativo");
        }

        if (duracaoMaxima < 0) {
            throw new ProjetilInvalidoException(
                    "duracaoMaxima",
                    "deve ser positiva");
        }

        this.hitbox.setCentro(posX, posY);
        this.hitbox.setTamanho(sizeX, sizeY);
        this.hitbox.setAnguloRad(anguloHitbox);

        this.velocidade.set(velX, velY);

        this.danoShield = danoShield;
        this.danoNota = danoNota;
        this.duracaoMaxima = duracaoMaxima;

        this.tempoDeVida = 0;
        this.velocidadeAngular = 0f;

        this.comportamentosAI.clear();
        this.comportamentosDespawn.clear();
        this.comportamentosAoColidir.clear();
        this.persistente = false;
        this.multiplicadorSprite = 2.0f;
        this.spriteDir = "/assets/batalha/projeteis/" + spriteDir;
        this.ativar();
    }

    public void executar(float dt) {
        for (ComportamentoProjetil ai : comportamentosAI) {
            ai.executar(this, dt);
        }
    }

    public void aoDespawnar() {
        for (ComportamentoAoDespawnar d : comportamentosDespawn) {
            d.aoDespawnar(this, this.factory);
        }
    }

    public void aoColidirComPlayer() {

        if (this.target == null) {
            throw new NullTargetException();
        }
        for (ComportamentoAoColidirComPlayer comportamento : this.comportamentosAoColidir) {
            comportamento.aoColidirComPlayer(this, this.target);
        }
        this.target.ReceberDano(
                this.danoShield,
                this.danoNota);
    }

    public final void atualizar(float deltaTime) {

        if (this.target == null) {
            throw new NullTargetException();
        }

        if (this.owner == null) {
            throw new ProjetilInvalidoException("owner", "não pode ser nulo durante a atualização");
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

        executar(deltaTime);

        this.atualizarPosicao(deltaTime);

        if (this.target != null &&
                this.hitbox.checarColisao(
                        this.target.getHitbox())) {

            aoColidirComPlayer();
        }
    }

    public int getDanoShield() {
        return danoShield;
    }

    public void setDanoShield(int danoShield) {
        this.danoShield = danoShield;
    }

    public float getDanoNota() {
        return danoNota;
    }

    public void setDanoNota(float danoNota) {
        this.danoNota = danoNota;
    }

    public float getTempoDeVida() {
        return tempoDeVida;
    }

    public ProjetilFactory getFactory() {
        return factory;
    }

    public float getDuracaoMaxima() {
        return duracaoMaxima;
    }
}
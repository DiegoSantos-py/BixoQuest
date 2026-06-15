package model.Projetil;

import java.util.List;
import model.Batalha.EntidadeBatalha;
import model.Player.PlayerProva;

public class ProjetilFactory {

    private ProjetilPool pool;

    public ProjetilFactory(PlayerProva target, EntidadeBatalha owner, int maxProjeteis) {
        if (owner == null)
            throw new exception.Projetil.FactoryInvalidaException("owner", "não pode ser nulo");

        this.pool = new ProjetilPool(maxProjeteis);

        this.pool.configurar(this, owner, target);
    }

    public void setTarget(PlayerProva target) {

        pool.atualizarTarget(target);
    }

    public Projetil spawn(float posX, float posY, float tamanhoX, float tamanhoY,
                          float velocidade, float anguloSpawn, float anguloHitbox,
                          int danoShield, float danoNota, float duracaoMaxima) {

        float velX = (float) (velocidade * Math.cos(anguloSpawn));
        float velY = (float) (velocidade * Math.sin(anguloSpawn));

        return pool.spawn(posX, posY, tamanhoX, tamanhoY, velX, velY, anguloHitbox, danoShield, danoNota, duracaoMaxima);
    }

    public void atualizar(float dt)       {
        pool.atualizar(dt); }
    public void desativarTodos()          {
        pool.desativarTodos(); }
    public List<Projetil> getAtivos()     {
        return pool.getAtivos(); }
}

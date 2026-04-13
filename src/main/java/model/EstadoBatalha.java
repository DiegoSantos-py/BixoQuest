package model;

import java.util.Queue;
import model.Player.PlayerProva;
import model.util.Hitbox;
import model.util.Vector2D;

public class EstadoBatalha {
    private Personagem personagemOriginal; // Crucial guardarmos isso no Estado para salvar a prova dps!
    private PlayerProva player; 
    private Queue<Oponente> filaOponentes;
    private Oponente oponenteAtual;
    private boolean finalizado;
    private boolean vitoria;
    private boolean isBatalhaAnimal; 

    public EstadoBatalha(Personagem personagemBase, float conhecimentoDaMateria, Queue<Oponente> filaOponentes, boolean isBatalhaAnimal) {
        
        Hitbox caixaPlayer = new Hitbox(new Vector2D(0, 0), new Vector2D(20, 20), 0.0f);
        Vector2D velocidadeParada = new Vector2D(0, 0);

        this.player = new PlayerProva(caixaPlayer, velocidadeParada, conhecimentoDaMateria);
        this.personagemOriginal = personagemBase;
        
        this.filaOponentes = filaOponentes;
        this.isBatalhaAnimal = isBatalhaAnimal;
        this.finalizado = false;
        this.vitoria = false;
        this.oponenteAtual = filaOponentes.poll();
    }

    public Personagem getPersonagemOriginal() {
        return personagemOriginal;
    }

    public PlayerProva getPlayer() {
        return player;
    }

    public void setPlayer(PlayerProva player) {
        this.player = player;
    }

    public Queue<Oponente> getFilaOponentes() {
        return filaOponentes;
    }

    public void setFilaOponentes(Queue<Oponente> filaOponentes) {
        this.filaOponentes = filaOponentes;
    }

    public Oponente getOponenteAtual() {
        return oponenteAtual;
    }

    public void setOponenteAtual(Oponente oponenteAtual) {
        this.oponenteAtual = oponenteAtual;
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }

    public boolean isVitoria() {
        return vitoria;
    }

    public void setVitoria(boolean vitoria) {
        this.vitoria = vitoria;
    }

    public boolean isBatalhaAnimal() {
        return isBatalhaAnimal;
    }

    public void setBatalhaAnimal(boolean batalhaAnimal) {
        this.isBatalhaAnimal = batalhaAnimal;
    }
}

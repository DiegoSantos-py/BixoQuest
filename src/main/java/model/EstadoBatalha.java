package model;

import java.util.Queue;

import model.Ataque.Ataque;
import model.Evento.Prova.ProvaBatalha;
import model.Npc.Animal;
import model.Player.PlayerProva;

public class EstadoBatalha {
    private Personagem personagem;
    private PlayerProva playerProva;
    private Queue<Oponente> filaOponentes;
    private Oponente oponenteAtual;
    private Turno turnoAtual;
    private boolean finalizado;
    private boolean vitoria;
    private final boolean isBatalhaAnimal;
    private boolean inimigoAtacando;
    private Ataque ataqueAtual;
    private ProvaBatalha provaBatalha;
    private Animal animal;

    public EstadoBatalha(PlayerProva playerProva,Personagem personagem, Queue<Oponente> filaOponentes, Animal animal) {
        this.personagem = personagem;
        this.playerProva = playerProva;
        this.filaOponentes = filaOponentes;
        this.isBatalhaAnimal = true;
        this.animal = animal;
        this.turnoAtual = Turno.TURNO_PLAYER;
        this.inimigoAtacando = false;
        this.finalizado = false;
        this.vitoria = false;
        this.oponenteAtual = filaOponentes.poll();
    }

    public EstadoBatalha(PlayerProva playerProva,Personagem personagem, Queue<Oponente> filaOponentes, ProvaBatalha provaBatalha) {
        this.personagem = personagem;
        this.playerProva = playerProva;
        this.filaOponentes = filaOponentes;
        this.isBatalhaAnimal = false;
        this.provaBatalha = provaBatalha;
        this.turnoAtual = Turno.TURNO_PLAYER;
        this.inimigoAtacando = false;
        this.finalizado = false;
        this.vitoria = false;
        this.oponenteAtual = filaOponentes.poll();
    }
 

    public PlayerProva getPlayerProva() {
        return playerProva;
    }

    public void setPlayerProva(PlayerProva playerProva) {
        this.playerProva = playerProva;
    }

    public ProvaBatalha getProvaBatalha(){
        return provaBatalha;
    }

    public Animal getAnimal() {
        return animal;
    }

    public Queue<Oponente> getFilaOponentes() {
        return filaOponentes;
    }

    public void setFilaOponentes(Queue<Oponente> filaOponentes) {
        this.filaOponentes = filaOponentes;
    }

    public Personagem getPersonagem() {
        return personagem;
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

    public Turno getTurnoAtual() {
        return turnoAtual;
    }
    public void setTurnoAtual(Turno proxTurno) {
        this.turnoAtual = proxTurno;
    }

    public boolean getInimigoAtacando(){
        return this.inimigoAtacando;
    }

    public void setInimigoAtacando(boolean inimigoAtacando) {
        this.inimigoAtacando = inimigoAtacando;
    }
    public Ataque getAtaqueAtual(){
        return this.ataqueAtual;
    }
    public void setAtaqueAtual(Ataque ataqueAtual){
        this.ataqueAtual = ataqueAtual;
    }
}

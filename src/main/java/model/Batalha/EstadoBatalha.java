package model.Batalha;

import java.util.Queue;

import model.Ataque.Ataque;
import model.Evento.Prova.ProvaBatalha;
import model.Npc.Animal;
import model.Npc.Npc;
import model.Personagem;
import model.Player.PlayerProva;
import repository.NpcRepository;
import repository.ResultadoProvaRepository;

public class EstadoBatalha {
    private Personagem personagem;
    private PlayerProva playerProva;
    private Queue<Oponente> filaOponentes;
    private Oponente oponenteAtual;
    private Turno turnoAtual;
    private boolean finalizado;
    private boolean vitoria;
    private ResultadoProvaRepository resultadoProvaRepository;
    private NpcRepository npcRepository;
    private final boolean isBatalhaAnimal;
    private boolean inimigoAtacando;
    private Ataque ataqueAtual;
    private ProvaBatalha provaBatalha;
    private Animal animal;

    public EstadoBatalha(PlayerProva playerProva,Personagem personagem, Queue<Oponente> filaOponentes, Animal animal, NpcRepository npcRepository) {
        if (playerProva == null) {
            throw new exception.Batalha.EstadoBatalhaInvalidoException("playerProva", "não pode ser nulo");
        }

        if(npcRepository == null) {
            throw new exception.Batalha.EstadoBatalhaInvalidoException("npcRepository", "não deve ser nulo");
        }

        if (personagem == null) {
            throw new exception.Batalha.EstadoBatalhaInvalidoException("personagem", "não pode ser nulo");
        }
        if (filaOponentes == null || filaOponentes.isEmpty()) {
            throw new exception.Batalha.EstadoBatalhaInvalidoException("filaOponentes", "não pode ser nula ou vazia");
        }
        if (animal == null) {
            throw new exception.Batalha.EstadoBatalhaInvalidoException("animal", "não pode ser nulo");
        }
        this.personagem = personagem;
        this.playerProva = playerProva;
        this.filaOponentes = filaOponentes;
        this.isBatalhaAnimal = true;
        this.animal = animal;
        this.npcRepository = npcRepository;
        this.turnoAtual = Turno.TURNO_PLAYER;
        this.inimigoAtacando = false;
        this.finalizado = false;
        this.vitoria = false;
        this.oponenteAtual = filaOponentes.poll();
    }

    public EstadoBatalha(PlayerProva playerProva,Personagem personagem, Queue<Oponente> filaOponentes,
                         ProvaBatalha provaBatalha, ResultadoProvaRepository resultadoProvaRepository) {
        if (playerProva == null) {
            throw new exception.Batalha.EstadoBatalhaInvalidoException("playerProva", "não pode ser nulo");
        }
        if(resultadoProvaRepository == null) {
            throw new exception.Batalha.EstadoBatalhaInvalidoException("resultadoProvaRepository", "nao deve ser nulo");
        }
        if (personagem == null) {
            throw new exception.Batalha.EstadoBatalhaInvalidoException("personagem", "não pode ser nulo");
        }
        if (filaOponentes == null || filaOponentes.isEmpty()) {
            throw new exception.Batalha.EstadoBatalhaInvalidoException("filaOponentes", "não pode ser nula ou vazia");
        }
        if (provaBatalha == null) {
            throw new exception.Batalha.EstadoBatalhaInvalidoException("provaBatalha", "não pode ser nula");
        }
        this.personagem = personagem;
        this.playerProva = playerProva;
        this.filaOponentes = filaOponentes;
        this.isBatalhaAnimal = false;
        this.provaBatalha = provaBatalha;
        this.resultadoProvaRepository = resultadoProvaRepository;
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
        if (playerProva == null) {
            throw new exception.Batalha.EstadoBatalhaInvalidoException("playerProva", "não pode ser nulo");
        }
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
        if (filaOponentes == null) {
            throw new exception.Batalha.EstadoBatalhaInvalidoException("filaOponentes", "não pode ser nula");
        }
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
        if (proxTurno == null) {
            throw new exception.Batalha.EstadoBatalhaInvalidoException("turnoAtual", "não pode ser nulo");
        }
        this.turnoAtual = proxTurno;
    }

    public NpcRepository getNpcRepository(){
        return this.npcRepository;
    }
    public ResultadoProvaRepository getResultadoProvaRepository(){
        return this.resultadoProvaRepository;
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
        if (ataqueAtual == null) {
            throw new exception.Batalha.EstadoBatalhaInvalidoException("ataqueAtual", "não pode ser nulo");
        }
        this.ataqueAtual = ataqueAtual;
    }
}

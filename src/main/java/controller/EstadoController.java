package controller;

import model.Batalha.EstadoBatalha;
import model.Player.AcaoBatalha;
import model.Projetil.Projetil;
import model.Ataque.Ataque;

import java.util.ArrayList;
import java.util.List;

public class EstadoController {
    
    private final BatalhaController batalhaController;

    public EstadoController(BatalhaController batalhaController) {
        this.batalhaController = batalhaController;
    }

    public EstadoBatalha getEstadoBatalha() {
        return batalhaController.getEstadoAtual();
    }

    // --- PLAYER ---
    public String getPlayerNome() {
        if (getEstadoBatalha() == null || getEstadoBatalha().getPersonagem() == null) return "NOME";
        return getEstadoBatalha().getPersonagem().getNome();
    }

    public float getPlayerConhecimento() {
        if (getEstadoBatalha() == null || getEstadoBatalha().getPlayerProva() == null) return 0f;
        return getEstadoBatalha().getPlayerProva().getConhecimentoArea();
    }

    public float getPlayerDano() {
        if (getEstadoBatalha() == null || getEstadoBatalha().getPlayerProva() == null) return 0f;
        return getEstadoBatalha().getPlayerProva().getDanoAtaque();
    }

    public float getPlayerNota() {
        if (getEstadoBatalha() == null || getEstadoBatalha().getPlayerProva() == null) return 0f;
        return getEstadoBatalha().getPlayerProva().getDesempenhoQuestaoAtual();
    }

    public int getPlayerTurnosUsados() {
        if (getEstadoBatalha() == null || getEstadoBatalha().getPlayerProva() == null) return 0;
        return getEstadoBatalha().getPlayerProva().getTurnosUsados();
    }

    public int getPlayerShieldAtual() {
        if (getEstadoBatalha() == null || getEstadoBatalha().getPlayerProva() == null) return 0;
        return getEstadoBatalha().getPlayerProva().getShieldAtual();
    }

    public int getPlayerShieldMaximo() {
        if (getEstadoBatalha() == null || getEstadoBatalha().getPlayerProva() == null) return 0;
        return getEstadoBatalha().getPlayerProva().getShieldMaximo();
    }

    public boolean isPlayerAcertosPerfeitos() {
        if (getEstadoBatalha() == null || getEstadoBatalha().getPlayerProva() == null) return false;
        return getEstadoBatalha().getPlayerProva().getTodosAcertosPerfeitos();
    }

    public boolean isPlayerPerdeuNota() {
        if (getEstadoBatalha() == null || getEstadoBatalha().getPlayerProva() == null) return false;
        return getEstadoBatalha().getPlayerProva().getPerdeuNota();
    }

    public List<Float> getPlayerDesempenhoQuestoes() {
        if (getEstadoBatalha() == null || getEstadoBatalha().getPlayerProva() == null) return new ArrayList<>();
        return getEstadoBatalha().getPlayerProva().getDesempenhoQuestoes();
    }

    // --- OPONENTE ---
    public String getInimigoNome() {
        if (getEstadoBatalha() == null || getEstadoBatalha().getOponenteAtual() == null) return "";
        return getEstadoBatalha().getOponenteAtual().getNome();
    }

    public String getInimigoDescricao() {
        if (getEstadoBatalha() == null || getEstadoBatalha().getOponenteAtual() == null) return "";
        return getEstadoBatalha().getOponenteAtual().getDescricao();
    }

    public String getInimigoSprite() {
        if (getEstadoBatalha() == null || getEstadoBatalha().getOponenteAtual() == null) return "";
        return getEstadoBatalha().getOponenteAtual().getSpriteUrl();
    }

    public float getInimigoHpAtual() {
        if (getEstadoBatalha() == null || getEstadoBatalha().getOponenteAtual() == null) return 0f;
        return getEstadoBatalha().getOponenteAtual().getHpAtual();
    }

    public float getInimigoHpMaximo() {
        if (getEstadoBatalha() == null || getEstadoBatalha().getOponenteAtual() == null) return 0f;
        return getEstadoBatalha().getOponenteAtual().getHpMaximo();
    }

    public int getInimigoMaxTurnos() {
        if (getEstadoBatalha() == null || getEstadoBatalha().getOponenteAtual() == null) return 1;
        return getEstadoBatalha().getOponenteAtual().getMaxTurnos();
    }

    public float getInimigoX() {
        if (getEstadoBatalha() == null || getEstadoBatalha().getOponenteAtual() == null) return 0f;
        return getEstadoBatalha().getOponenteAtual().getX();
    }

    public float getInimigoY() {
        if (getEstadoBatalha() == null || getEstadoBatalha().getOponenteAtual() == null) return 0f;
        return getEstadoBatalha().getOponenteAtual().getY();
    }

    public Object getInimigoIdentificador() {
        if (getEstadoBatalha() == null) return null;
        return getEstadoBatalha().getOponenteAtual();
    }

    // --- BATALHA ESTADO ---
    public boolean isFinalizado() {
        if (getEstadoBatalha() == null) return false;
        return getEstadoBatalha().isFinalizado();
    }

    public boolean isVitoria() {
        if (getEstadoBatalha() == null) return false;
        return getEstadoBatalha().isVitoria();
    }

    public boolean isTurnoPlayer() {
        if (getEstadoBatalha() == null) return true;
        return getEstadoBatalha().getTurnoAtual() == model.Batalha.Turno.TURNO_PLAYER;
    }

    public List<AcaoBatalha> getAcoesBatalha() {
        if (getEstadoBatalha() == null || getEstadoBatalha().getAcoesBatalha() == null) return new ArrayList<>();
        return getEstadoBatalha().getAcoesBatalha();
    }

    public Ataque getAtaqueAtual() {
        if (getEstadoBatalha() == null) return null;
        return getEstadoBatalha().getAtaqueAtual();
    }
}

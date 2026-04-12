package model.Player;

import java.util.List;
import model.util.Hitbox;
import model.util.Vector2D;
import model.EntidadeBatalha;
public class AcaoBatalha {

    private String Nome;
    private float ChanceAcerto;
    
    private int BonusDano;
    private int BonusShield;
    private int BonusConhecimento;

    public AcaoBatalha(String nome, float chanceAcerto, int bonusDano, int bonusShield, int bonusConhecimento) {
        this.Nome = nome;
        this.ChanceAcerto = chanceAcerto;
        this.BonusDano = bonusDano;
        this.BonusShield = bonusShield;
        this.BonusConhecimento = bonusConhecimento;
    }
    // tem q ver isso ai
}
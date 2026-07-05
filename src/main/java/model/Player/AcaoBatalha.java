package model.Player;

public class AcaoBatalha {

    private String Nome;
    private float ChanceAcerto;
    
    private float BonusDano;
    private int BonusShield;
    private float BonusConhecimento;
    private float BonusNota;

    public AcaoBatalha(String nome, float chanceAcerto, float bonusDano, int bonusShield, float bonusConhecimento, float bonusNota) {
        this.Nome = nome;
        this.ChanceAcerto = chanceAcerto;
        this.BonusDano = bonusDano;
        this.BonusShield = bonusShield;
        this.BonusConhecimento = bonusConhecimento;
        this.BonusNota = bonusNota;
    }


    
    public String getNome() { return Nome; }
    public float getChanceAcerto() { return ChanceAcerto; }
    public float getBonusDano() { return BonusDano; }
    public int getBonusShield() { return BonusShield; }
    public float getBonusConhecimento() { return BonusConhecimento; }
    public float getBonusNota() {
        return BonusNota;
    }

    public void setBonusNota(float BonusNota) {
        this.BonusNota = BonusNota;
    }
    public void setNome(String nome) { this.Nome = nome; }
    public void setChanceAcerto(float chanceAcerto) { this.ChanceAcerto = chanceAcerto; }
    public void setBonusDano(float bonusDano) { this.BonusDano = bonusDano; }
    public void setBonusShield(int bonusShield) { this.BonusShield = bonusShield; }
    public void setBonusConhecimento(float bonusConhecimento) { this.BonusConhecimento = bonusConhecimento; }


}
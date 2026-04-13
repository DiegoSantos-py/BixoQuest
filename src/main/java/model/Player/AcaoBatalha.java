package model.Player;

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
    
    public String getNome() { return Nome; }
    public float getChanceAcerto() { return ChanceAcerto; }
    public int getBonusDano() { return BonusDano; }
    public int getBonusShield() { return BonusShield; }
    public int getBonusConhecimento() { return BonusConhecimento; }
    public void setNome(String nome) { this.Nome = nome; }
    public void setChanceAcerto(float chanceAcerto) { this.ChanceAcerto = chanceAcerto; }
    public void setBonusDano(int bonusDano) { this.BonusDano = bonusDano; }
    public void setBonusShield(int bonusShield) { this.BonusShield = bonusShield; }
    public void setBonusConhecimento(int bonusConhecimento) { this.BonusConhecimento = bonusConhecimento; }
}
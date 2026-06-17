package model.Batalha;

public class OponenteDados {
    private String spriteDir;
    private String descricao;
    private String textoCaixa;

    public OponenteDados() {}

    public OponenteDados(String spriteDir, String descricao, String textoCaixa) {
        this.spriteDir = spriteDir;
        this.descricao = descricao;
        this.textoCaixa = textoCaixa;
    }

    public String getSpriteDir() { return spriteDir; }
    public void setSpriteDir(String spriteDir) { this.spriteDir = spriteDir; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getTextoCaixa() { return textoCaixa; }
    public void setTextoCaixa(String textoCaixa) { this.textoCaixa = textoCaixa; }
}
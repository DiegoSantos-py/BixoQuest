package view.util;

public enum Borda {
    NORTE("ESQUERDA"),
    SUL("DIREITA"),
    LESTE("CIMA"),
    OESTE("BAIXO");

    private final String direcao;

    Borda(String direcao) {
        this.direcao = direcao;
    }

    public String getDirecao() {
        return direcao;
    }
}
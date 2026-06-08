package view.construtores;

import view.cena.CenaJogo;

import java.util.function.Consumer;

/**
 * Define o contrato mínimo que qualquer construtor de cena deve respeitar.
 */
public interface Construtor {
    void setBackground(String imagePath, double largura, double altura);
    void addElement(String imagePath, double largura, double x, double y);
    void addNPC(String imagePath, double largura, double x, double y);
    void addInteractiveZone(String id, String imagePath, double largura, double altura,
                            double x, double y, Consumer<String> onEnter);
    void setPlayer(String imagePath, double largura, double x, double y);
    CenaJogo getResult();
}

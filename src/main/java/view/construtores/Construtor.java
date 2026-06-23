package view.construtores;

import view.cena.CenaJogo;
import view.util.Borda;

import java.util.function.Consumer;

/**
 * Define o contrato mínimo que qualquer construtor de cena deve respeitar.
 */
public interface Construtor {
    void setBackground(String imagePath, double largura, double altura);

    void addElement(String imagePath, double largura, double x, double y,
                    double hitboxOffsetX, double hitboxOffsetY,
                    double hitboxLargura, double hitboxAltura);

    void addNPC(String imagePath, double largura, double x, double y,
                double hitboxOffsetX, double hitboxOffsetY,
                double hitboxLargura, double hitboxAltura);

    void addInteractiveZone(String id, String imagePath, double largura, double altura,
                            double x, double y, Consumer<String> onEnter);

    void setPlayer(String imagePath, double largura, double x, double y,
                   double hitboxOffsetX, double hitboxOffsetY,
                   double hitboxLargura, double hitboxAltura);

    void setOnBordaAtingida(Consumer<Borda> onBordaAtingida);

    CenaJogo getResult();
}

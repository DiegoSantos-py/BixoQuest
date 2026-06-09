package view.construtores;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import view.cena.CenaJogo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Builder concreto que implementa Construtor.
 * Responsável por carregar as imagens e acumular os dados visuais,
 * entregando uma CenaJogo via getResult().
 */
public class ConstrutorCenaJogo implements Construtor {

    private ImageView background;
    private final List<ImageView> elements        = new ArrayList<>();
    private final List<Rectangle> elementHitboxes = new ArrayList<>();
    private final List<ImageView> npcs            = new ArrayList<>();
    private final List<Rectangle> npcHitboxes     = new ArrayList<>();
    private final List<CenaJogo.ZoneEntry> zones  = new ArrayList<>();
    private ImageView playerView;
    private Rectangle playerHitbox;
    private double playerHitboxOffsetX;
    private double playerHitboxOffsetY;
    private Consumer<String> onBordaAtingida;

    @Override
    public void setBackground(String imagePath, double largura, double altura) {
        background = carregarBackground(imagePath, largura, altura);
    }

    @Override
    public void addElement(String imagePath, double largura, double x, double y,
                           double hitboxOffsetX, double hitboxOffsetY,
                           double hitboxLargura, double hitboxAltura) {
        elements.add(carregarImagem(imagePath, largura, x, y));
        elementHitboxes.add(criarHitbox(x + hitboxOffsetX, y + hitboxOffsetY,
                hitboxLargura, hitboxAltura));
    }

    @Override
    public void addNPC(String imagePath, double largura, double x, double y,
                       double hitboxOffsetX, double hitboxOffsetY,
                       double hitboxLargura, double hitboxAltura) {
        npcs.add(carregarImagem(imagePath, largura, x, y));
        npcHitboxes.add(criarHitbox(x + hitboxOffsetX, y + hitboxOffsetY,
                hitboxLargura, hitboxAltura));
    }

    @Override
    public void addInteractiveZone(String id, String imagePath,
                                   double largura, double altura,
                                   double x, double y,
                                   Consumer<String> onEnter) {
        ImageView view = carregarImagem(imagePath, largura, x, y);
        view.setFitHeight(altura);
        view.setPreserveRatio(false);
        zones.add(new CenaJogo.ZoneEntry(id, view, onEnter));
    }

    @Override
    public void setPlayer(String imagePath, double largura, double x, double y,
                          double hitboxOffsetX, double hitboxOffsetY,
                          double hitboxLargura, double hitboxAltura) {
        playerView          = carregarImagem(imagePath, largura, x, y);
        playerHitboxOffsetX = hitboxOffsetX;
        playerHitboxOffsetY = hitboxOffsetY;
        playerHitbox        = criarHitbox(x + hitboxOffsetX, y + hitboxOffsetY,
                hitboxLargura, hitboxAltura);
    }

    @Override
    public void setOnBordaAtingida(Consumer<String> onBordaAtingida) {
        this.onBordaAtingida = onBordaAtingida;
    }

    @Override
    public CenaJogo getResult() {
        return new CenaJogo(background,
                elements, elementHitboxes,
                npcs, npcHitboxes,
                zones,
                playerView, playerHitbox,
                playerHitboxOffsetX, playerHitboxOffsetY,
                onBordaAtingida);
    }

    // ── Utilitários ───────────────────────────────────────────────────────────

    private ImageView carregarBackground(String caminho, double largura, double altura) {
        var stream = getClass().getResourceAsStream(caminho);

        if (stream == null)
            throw new IllegalArgumentException("Imagem não encontrada: " + caminho);

        ImageView iv = new ImageView(new Image(stream));
        iv.setFitWidth(largura);
        iv.setFitHeight(altura);
        iv.setPreserveRatio(false);
        return iv;
    }

    private ImageView carregarImagem(String caminho, double largura, double x, double y) {
        var stream = getClass().getResourceAsStream(caminho);

        if (stream == null)
            throw new IllegalArgumentException("Imagem não encontrada: " + caminho);

        ImageView iv = new ImageView(new Image(stream));
        iv.setPreserveRatio(true);
        iv.setFitWidth(largura);
        iv.setLayoutX(x);
        iv.setLayoutY(y);
        return iv;
    }

    private Rectangle criarHitbox(double x, double y, double largura, double altura) {
        Rectangle hitbox = new Rectangle(x, y, largura, altura);
        hitbox.setVisible(true);
        return hitbox;
    }
}

package view.construtores;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import view.cena.CenaJogo;
import view.util.Borda;

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
    private final List<String> npcNomes           = new ArrayList<>();
    private ImageView playerView;
    private Rectangle playerHitbox;
    private double playerHitboxOffsetX;
    private double playerHitboxOffsetY;
    private Consumer<Borda> onBordaAtingida;
    private Consumer<String> onNpcAtingido;

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
    public void addNPC(String imagePath, String nome, double largura, double x, double y,
                       double hitboxOffsetX, double hitboxOffsetY,
                       double hitboxLargura, double hitboxAltura) {
        npcNomes.add(nome);
        npcs.add(carregarImagem(imagePath, largura, x, y));
        npcHitboxes.add(criarHitbox(x + hitboxOffsetX, y + hitboxOffsetY,
                hitboxLargura, hitboxAltura));
    }

    public void setOnNpcAtingido(Consumer<String> onNpcAtingido) {
        this.onNpcAtingido = onNpcAtingido;
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
    public void setOnBordaAtingida(Consumer<Borda> onBordaAtingida) {
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
                onBordaAtingida,
                npcNomes,
                onNpcAtingido
                );
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

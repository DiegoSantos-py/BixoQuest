package view.construtores;

import controller.GameController;
import controller.PersonagemController;
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
    private String spriteBase;

    private PersonagemController personagemController;
    private GameController gameController;
    private int personagemId;
    private Runnable onSairParaMenuPrincipal;
    private Runnable onFinalizar;

    @Override
    public void setPersonagem(PersonagemController personagemController, int personagemId) {
        this.personagemController = personagemController;
        this.personagemId = personagemId;
    }

    @Override
    public void setOnSairParaMenuPrincipal(Runnable onSairParaMenuPrincipal) {
        this.onSairParaMenuPrincipal = onSairParaMenuPrincipal;
    }

    @Override
    public void setBackground(String imagePath, double largura, double altura) {
        background = carregarBackground(imagePath, largura, altura);
    }

    @Override
    public void addElement(String imagePath, double largura, double x, double y,
                           double hitboxOffsetX, double hitboxOffsetY,
                           double hitboxLargura, double hitboxAltura) {
        if (imagePath != null && !imagePath.isEmpty()) {
            elements.add(carregarImagem(imagePath, largura, x, y));
        }
        Rectangle hitbox = criarHitbox(x + hitboxOffsetX, y + hitboxOffsetY,
                hitboxLargura, hitboxAltura);
        hitbox.setFill(javafx.scene.paint.Color.rgb(255, 0, 0, 0.3));
        elementHitboxes.add(hitbox);
    }

    @Override
    public void addNPC(String imagePath, String nome, double largura, double x, double y,
                       double hitboxOffsetX, double hitboxOffsetY,
                       double hitboxLargura, double hitboxAltura) {
        npcNomes.add(nome);
        npcs.add(carregarImagem(imagePath, largura, x, y));
        Rectangle hitbox = criarHitbox(x + hitboxOffsetX, y + hitboxOffsetY,
                hitboxLargura, hitboxAltura);
        hitbox.setFill(javafx.scene.paint.Color.rgb(0, 255, 0, 0.3));
        npcHitboxes.add(hitbox);
    }

    @Override
    public void setOnNpcAtingido(Consumer<String> onNpcAtingido) {
        this.onNpcAtingido = onNpcAtingido;
    }

    @Override
    public void addInteractiveZone(String id, String imagePath,
                                   double spriteLargura, double spriteAltura,
                                   double spriteX, double spriteY,
                                   double hitboxX, double hitboxY,
                                   double hitboxLargura, double hitboxAltura,
                                   Consumer<String> onEnter) {
        Rectangle hitbox = criarHitbox(hitboxX, hitboxY, hitboxLargura, hitboxAltura);
        hitbox.setFill(javafx.scene.paint.Color.rgb(0, 255, 0, 0.3));
        if (imagePath != null && !imagePath.isEmpty()) {
            ImageView view = carregarImagem(imagePath, spriteLargura, spriteX, spriteY);
            view.setFitHeight(spriteAltura);
            view.setPreserveRatio(false);
            zones.add(new CenaJogo.ZoneEntry(id, view, hitbox, onEnter));
        } else {
            zones.add(new CenaJogo.ZoneEntry(id, null, hitbox, onEnter));
        }
    }

    @Override
    public void setPlayer(String spriteBase, double largura, double x, double y,
                          double hitboxOffsetX, double hitboxOffsetY,
                          double hitboxLargura, double hitboxAltura) {
        playerView          = carregarImagem(spriteBase + "rotations/south.png", largura, x, y);
        playerHitboxOffsetX = hitboxOffsetX;
        playerHitboxOffsetY = hitboxOffsetY;
        playerHitbox        = criarHitbox(x + hitboxOffsetX, y + hitboxOffsetY,
                hitboxLargura, hitboxAltura);
        playerHitbox.setFill(javafx.scene.paint.Color.rgb(0, 0, 255, 0.3));
        this.spriteBase = spriteBase;
    }

    @Override
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void setOnBordaAtingida(Consumer<Borda> onBordaAtingida) {
        this.onBordaAtingida = onBordaAtingida;
    }

    @Override
    public void setOnFinalizar(Runnable onFinalizar) {
        this.onFinalizar = onFinalizar;
    }

    @Override
    public CenaJogo getResult() {
        return new CenaJogo(background, elements, elementHitboxes,
                npcs, npcHitboxes, zones,
                playerView, playerHitbox,
                playerHitboxOffsetX, playerHitboxOffsetY,
                onBordaAtingida, npcNomes, onNpcAtingido, spriteBase,
                personagemController, personagemId, onSairParaMenuPrincipal,
                gameController, onFinalizar
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

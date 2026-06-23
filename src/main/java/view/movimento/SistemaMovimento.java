package view.movimento;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import view.util.Borda;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class SistemaMovimento {

    private static final double VELOCIDADE   = 4.0;
    private static final double LARGURA_TELA = 1920;
    private static final double ALTURA_TELA  = 1080;

    private static final String GIF_SOUTH = "/Jogador/Jogador1/Animação/walk_south.gif";
    private static final String GIF_NORTH = "/Jogador/Jogador1/Animação/walk_north.gif";
    private static final String GIF_EAST  = "/Jogador/Jogador1/Animação/walk_east.gif";
    private static final String GIF_WEST  = "/Jogador/Jogador1/Animação/walk_west.gif";

    private static final String PARADO_SOUTH = "/Jogador/Jogador1/rotations/south.png";
    private static final String PARADO_NORTH = "/Jogador/Jogador1/rotations/north.png";
    private static final String PARADO_EAST  = "/Jogador/Jogador1/rotations/east.png";
    private static final String PARADO_WEST  = "/Jogador/Jogador1/rotations/west.png";

    private String gifAtual = GIF_SOUTH;
    private String ultimaDirecao = GIF_SOUTH;

    private final ImageView playerView;
    private final Rectangle playerHitbox;
    private final double hitboxOffsetX;
    private final double hitboxOffsetY;
    private final List<Rectangle> elementHitboxes;
    private final List<Rectangle> npcHitboxes;
    private final Consumer<Borda> onBordaAtingida;


    public SistemaMovimento(ImageView playerView,
                            Rectangle playerHitbox,
                            double hitboxOffsetX,
                            double hitboxOffsetY,
                            List<Rectangle> elementHitboxes,
                            List<Rectangle> npcHitboxes,
                            Consumer<Borda> onBordaAtingida) {
        this.playerView       = playerView;
        this.playerHitbox     = playerHitbox;
        this.hitboxOffsetX    = hitboxOffsetX;
        this.hitboxOffsetY    = hitboxOffsetY;
        this.elementHitboxes  = elementHitboxes;
        this.npcHitboxes      = npcHitboxes;
        this.onBordaAtingida  = onBordaAtingida;
    }

    public void atualizar(Set<KeyCode> teclasPressionadas) {
        double dx = 0;
        double dy = 0;

        if (teclasPressionadas.contains(KeyCode.W) || teclasPressionadas.contains(KeyCode.UP))    dy -= VELOCIDADE;
        if (teclasPressionadas.contains(KeyCode.S) || teclasPressionadas.contains(KeyCode.DOWN))  dy += VELOCIDADE;
        if (teclasPressionadas.contains(KeyCode.A) || teclasPressionadas.contains(KeyCode.LEFT))  dx -= VELOCIDADE;
        if (teclasPressionadas.contains(KeyCode.D) || teclasPressionadas.contains(KeyCode.RIGHT)) dx += VELOCIDADE;

        if (dx == 0 && dy == 0) {
            trocarGifParado();
            return;
        }

        atualizarGif(dx, dy);
        moverEixo(dx, 0);
        moverEixo(0, dy);
    }

    private void moverEixo(double dx, double dy) {
        double novoX = playerView.getLayoutX() + dx;
        double novoY = playerView.getLayoutY() + dy;

        // verifica bordas pela hitbox
        double hitX = novoX + hitboxOffsetX;
        double hitY = novoY + hitboxOffsetY;
        double hitW = playerHitbox.getWidth();
        double hitH = playerHitbox.getHeight();

        if (hitX <= 0) {
            dispararBorda(Borda.OESTE);
            return;
        }
        if (hitX + hitW >= LARGURA_TELA) {
            dispararBorda(Borda.LESTE);
            return;
        }
        if (hitY <= 0) {
            dispararBorda(Borda.NORTE);
            return;
        }
        if (hitY + hitH >= ALTURA_TELA) {
            dispararBorda(Borda.SUL);
            return;
        }

        // aplica posição provisória
        playerView.setLayoutX(novoX);
        playerView.setLayoutY(novoY);
        playerHitbox.setX(hitX);
        playerHitbox.setY(hitY);

        // reverte se colidiu
        if (colidindoComHitbox()) {
            double anteriorX = novoX - dx;
            double anteriorY = novoY - dy;
            playerView.setLayoutX(anteriorX);
            playerView.setLayoutY(anteriorY);
            playerHitbox.setX(anteriorX + hitboxOffsetX);
            playerHitbox.setY(anteriorY + hitboxOffsetY);
        }
    }

    private boolean colidindoComHitbox() {
        var bounds = playerHitbox.getBoundsInParent();

        return elementHitboxes.stream().anyMatch(h -> bounds.intersects(h.getBoundsInParent()))
                || npcHitboxes.stream().anyMatch(h -> bounds.intersects(h.getBoundsInParent()));
    }

    private void dispararBorda(Borda borda) {
        if (onBordaAtingida != null)
            onBordaAtingida.accept(borda);
    }

    private void atualizarGif(double dx, double dy) {
        String novo;
        if (Math.abs(dy) >= Math.abs(dx)) {
            novo = dy < 0 ? GIF_NORTH : GIF_SOUTH;
        } else {
            novo = dx < 0 ? GIF_WEST : GIF_EAST;
        }
        ultimaDirecao = novo;
        trocarGif(novo);
    }

    private void trocarGifParado() {

        String parado = switch (ultimaDirecao) {
            case GIF_NORTH -> PARADO_NORTH;
            case GIF_EAST  -> PARADO_EAST;
            case GIF_WEST  -> PARADO_WEST;
            default        -> PARADO_SOUTH;
        };


        var stream = getClass().getResourceAsStream(parado);

        if (gifAtual.equals(parado)) return;
        if (stream == null) return;
        gifAtual = parado;
        playerView.setImage(new Image(stream));
    }

    private void trocarGif(String caminho) {
        if (gifAtual.equals(caminho)) return;
        var stream = getClass().getResourceAsStream(caminho);
        if (stream == null) return;
        gifAtual = caminho;
        playerView.setImage(new Image(stream));
    }
}
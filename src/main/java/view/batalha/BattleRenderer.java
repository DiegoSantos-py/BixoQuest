package view.batalha;

import model.Batalha.EstadoBatalha;
import model.Ataque.Ataque;
import model.Batalha.EntidadeBatalha;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Responsável exclusivamente por renderizar a arena de combate:
 * player, projéteis e hitboxes de debug.
 * Não conhece lógica de negócio, apenas converte dados do EstadoBatalha em nós visuais.
 */
public class BattleRenderer {

    // Flyweight cache: evita re-instanciar Image a cada frame
    private final Map<String, Image> imageCache = new HashMap<>();

    private boolean renderHitbox = false;

    public void setRenderHitbox(boolean renderHitbox) {
        this.renderHitbox = renderHitbox;
    }

    public boolean isRenderHitbox() {
        return renderHitbox;
    }

    private boolean playerInvulneravel = false;

    public void setPlayerInvulneravel(boolean playerInvulneravel) {
        this.playerInvulneravel = playerInvulneravel;
    }

    /**
     * Redesenha a arena inteira a partir do estado atual.
     * Remove todos os filhos dinâmicos (índice > 4, preservando fundo + bordas)
     * e re-adiciona player e projéteis.
     */
    public void renderFrame(Pane arenaPane, EstadoBatalha estado) {
        if (arenaPane == null || estado == null || estado.getAtaqueAtual() == null)
            return;

        Ataque atk = estado.getAtaqueAtual();
        float minX = atk.getMinX();
        float minY = atk.getMinY();

        // Remove apenas os nós dinâmicos (fundo + 4 bordas = 5 filhos fixos)
        while (arenaPane.getChildren().size() > 5) {
            arenaPane.getChildren().remove(5);
        }

        // --- Player ---
        model.Player.PlayerProva p = estado.getPlayerProva();
        float pW = p.getHitbox().getTamanho().getX();
        float pH = p.getHitbox().getTamanho().getY();

        float visualW = 15f;
        float visualH = 15f;
        float spriteLX = p.getX() - minX - visualW / 2;
        float spriteLY = p.getY() - minY - visualH / 2;

        ImageView pSprite = criarSprite(p, visualW, visualH, spriteLX, spriteLY);
        javafx.scene.Node playerNode = pSprite != null ? pSprite : buildRect(spriteLX, spriteLY, visualW, visualH, Color.RED);

        if (this.playerInvulneravel) {
            // Blink effect: toggle visibility every 100ms
            if ((System.currentTimeMillis() / 50) % 2 == 0) {
                playerNode.setVisible(false);
            }
        }

        arenaPane.getChildren().add(playerNode);

        if (renderHitbox) {
            float hitboxLX = p.getX() - minX - pW / 2;
            float hitboxLY = p.getY() - minY - pH / 2;
            Rectangle pHb = buildRect(hitboxLX, hitboxLY, pW, pH, Color.TRANSPARENT);
            pHb.setStroke(Color.BLUE);
            pHb.setStrokeWidth(2);
            arenaPane.getChildren().add(pHb);
        }

        // --- Projéteis ---
        for (model.Projetil.Projetil proj : estado.getAtaqueAtual().getProjeteis()) {
            float prW = proj.getHitbox().getTamanho().getX();
            float prH = proj.getHitbox().getTamanho().getY();
            float prLX = proj.getX() - minX - prW / 2;
            float prLY = proj.getY() - minY - prH / 2;

            ImageView prSprite = criarSprite(proj, prW, prH, prLX, prLY);
            if (prSprite != null) {
                arenaPane.getChildren().add(prSprite);
            }

            if (renderHitbox) {
                Rectangle prHb = buildRect(prLX, prLY, prW, prH, Color.TRANSPARENT);
                prHb.setStroke(Color.RED);
                prHb.setStrokeWidth(2);
                double grausHitbox = Math.toDegrees(proj.getHitbox().getAnguloRotacao());
                if (grausHitbox != 0) prHb.setRotate(grausHitbox);
                arenaPane.getChildren().add(prHb);
            }
        }
    }

    /**
     * Constrói e devolve uma Pane representando a arena de combate.
     * Adiciona fundo preto e 4 bordas brancas.
     */
    public Pane criarArena(Ataque atk) {
        float minX = atk.getMinX();
        float minY = atk.getMinY();
        float boxW = atk.getMaxX() - minX;
        float boxH = atk.getMaxY() - minY;
        float border = 6f;

        Pane arenaPane = new Pane();
        arenaPane.setPrefSize(boxW, boxH);
        arenaPane.setMinSize(boxW, boxH);
        arenaPane.setMaxSize(boxW, boxH);

        Rectangle fundo = new Rectangle(0, 0, boxW, boxH);
        fundo.setFill(Color.BLACK);
        arenaPane.getChildren().add(fundo);

        for (Rectangle b : new Rectangle[]{
                new Rectangle(0, 0, boxW, border),
                new Rectangle(0, boxH - border, boxW, border),
                new Rectangle(0, 0, border, boxH),
                new Rectangle(boxW - border, 0, border, boxH)}) {
            b.setFill(Color.WHITE);
            arenaPane.getChildren().add(b);
        }

        return arenaPane;
    }

    // --- Utilitários privados ---

    private ImageView criarSprite(EntidadeBatalha entidade, float largura, float altura, float localX, float localY) {
        try {
            String sDir = entidade.getSpriteUrl();
            if (sDir == null || sDir.isEmpty()) return null;
            if (!sDir.startsWith("/")) sDir = "/" + sDir;

            Image img = imageCache.get(sDir);
            if (img == null) {
                img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(sDir)));
                if (img.isError()) return null;
                imageCache.put(sDir, img);
            }

            ImageView iv = new ImageView(img);
            iv.setFitWidth(largura);
            iv.setFitHeight(altura);
            iv.setLayoutX(localX);
            iv.setLayoutY(localY);
            iv.setScaleX(2);
            iv.setScaleY(2);

            double graus = Math.toDegrees(entidade.getHitbox().getAnguloRotacao());
            if (graus != 0) iv.setRotate(graus);

            return iv;
        } catch (Exception e) {
            try {
                Image fallback = new Image(Objects.requireNonNull(
                        getClass().getResourceAsStream("/assets/batalha/oponentes/animais/default.png")));
                ImageView iv = new ImageView(fallback);
                iv.setFitWidth(largura);
                iv.setFitHeight(altura);
                iv.setLayoutX(localX);
                iv.setLayoutY(localY);
                return iv;
            } catch (Exception ex) {
                return null;
            }
        }
    }

    private Rectangle buildRect(float x, float y, float w, float h, Color fill) {
        Rectangle r = new Rectangle(x, y, w, h);
        r.setFill(fill);
        return r;
    }
}

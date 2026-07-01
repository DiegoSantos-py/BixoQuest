package view.cena;

import javafx.scene.shape.Rectangle;

import java.util.List;
import java.util.function.Consumer;

public class SistemaColisao {

    private final Rectangle playerHitbox;
    private final List<CenaJogo.ZoneEntry> zones;
    private final List<Rectangle> npcHitboxes;
    private final List<String> npcNomes;
    private final Consumer<String> onNpcAtingido;

    public SistemaColisao(Rectangle playerHitbox,
                          List<CenaJogo.ZoneEntry> zones,
                          List<Rectangle> npcHitboxes,
                          List<String> npcNomes,
                          Consumer<String> onNpcAtingido) {
        this.playerHitbox  = playerHitbox;
        this.zones         = zones;
        this.npcHitboxes   = npcHitboxes;
        this.npcNomes      = npcNomes;
        this.onNpcAtingido = onNpcAtingido;
    }

    public void verificarZonas() {
        zones.forEach(zone -> {
            boolean colidindo = playerHitbox.getBoundsInParent()
                    .intersects(zone.hitbox().getBoundsInParent());
            if (colidindo) {
                zone.onEnter().accept(zone.id());
            }
        });
    }

    public void verificarNpcs() {
        for (int i = 0; i < npcHitboxes.size(); i++) {
            if (playerHitbox.getBoundsInParent()
                    .intersects(npcHitboxes.get(i).getBoundsInParent())) {
                if (onNpcAtingido != null)
                    onNpcAtingido.accept(npcNomes.get(i));
            }
        }
    }
}
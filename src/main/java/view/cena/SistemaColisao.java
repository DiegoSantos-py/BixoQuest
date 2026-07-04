package view.cena;

import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class SistemaColisao {

    private static final long COOLDOWN_NANOS = 1_500_000_000L;

    private final Rectangle playerHitbox;
    private final List<CenaJogo.ZoneEntry> zones;
    private final List<Rectangle> npcHitboxes;
    private final List<String> npcNomes;
    private final Consumer<String> onNpcAtingido;
    private final Set<CenaJogo.ZoneEntry> zonasAtualmenteColidindo = new HashSet<>(); // por instância, não por nome
    private final Map<String, Long> ultimoDisparo = new HashMap<>(); // cooldown ainda por nome, ok manter

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
        long agora = System.nanoTime();

        zones.forEach(zone -> {
            boolean colidindo = playerHitbox.getBoundsInParent()
                    .intersects(zone.hitbox().getBoundsInParent());

            boolean jaEstavaColidindo = zonasAtualmenteColidindo.contains(zone); // por instância

            if (colidindo && !jaEstavaColidindo) {
                Long ultimo = ultimoDisparo.get(zone.id());
                boolean emCooldown = ultimo != null && (agora - ultimo) < COOLDOWN_NANOS;

                zonasAtualmenteColidindo.add(zone);

                if (!emCooldown) {
                    ultimoDisparo.put(zone.id(), agora);
                    zone.onEnter().accept(zone.id());
                }
            } else if (!colidindo) {
                zonasAtualmenteColidindo.remove(zone); // remove só essa instância específica
            }
        });
    }

    public void verificarNpcs() {
        for (int i = 0; i < npcHitboxes.size(); i++) {
            if (playerHitbox.getBoundsInParent()
                    .intersects(npcHitboxes.get(i).getBoundsInParent())) {
                System.out.println("npc atingido");
                if (onNpcAtingido != null)
                    onNpcAtingido.accept(npcNomes.get(i));
            }
        }
    }
}
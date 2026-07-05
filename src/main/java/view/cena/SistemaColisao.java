package view.cena;

import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import model.Npc.Npc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class SistemaColisao {

    private static final long COOLDOWN_NANOS = 1_500_000_000L; // 1.5s

    // --- Zonas comuns (eventos, provas, navegação) ---
    private final Rectangle playerHitbox;
    private final List<CenaJogo.ZoneEntry> zones;
    private final Set<CenaJogo.ZoneEntry> zonasAtualmenteColidindo = new HashSet<>();
    private final Map<String, Long> ultimoDisparo = new HashMap<>();

    // --- NPCs (interação por proximidade + tecla) ---
    private final List<NpcEntry> npcs;
    private final Set<NpcEntry> npcsProximos = new HashSet<>();

    public record NpcEntry(Npc npc, Rectangle hitbox) {}

    public SistemaColisao(Rectangle playerHitbox,
                          List<CenaJogo.ZoneEntry> zones,
                          List<NpcEntry> npcs) {
        this.playerHitbox = playerHitbox;
        this.zones        = zones;
        this.npcs         = npcs;
    }

    // --- Zonas comuns ---

    public void verificarZonas() {
        long agora = System.nanoTime();

        zones.forEach(zone -> {
            boolean colidindo = playerHitbox.getBoundsInParent()
                    .intersects(zone.hitbox().getBoundsInParent());

            boolean jaEstavaColidindo = zonasAtualmenteColidindo.contains(zone);

            if (colidindo && !jaEstavaColidindo) {
                Long ultimo = ultimoDisparo.get(zone.id());
                boolean emCooldown = ultimo != null && (agora - ultimo) < COOLDOWN_NANOS;

                zonasAtualmenteColidindo.add(zone);

                if (!emCooldown) {
                    ultimoDisparo.put(zone.id(), agora);
                    zone.onEnter().accept(zone.id());
                }
            } else if (!colidindo) {
                zonasAtualmenteColidindo.remove(zone);
            }
        });
    }

    // --- NPCs ---

    /**
     * Atualiza quais NPCs estão fisicamente próximos do jogador.
     * Deve ser chamado todo frame, independente de tecla pressionada.
     */
    public void atualizarProximidadeNpcs() {
        npcs.forEach(entry -> {
            boolean colidindo = playerHitbox.getBoundsInParent()
                    .intersects(entry.hitbox().getBoundsInParent());

            if (colidindo) npcsProximos.add(entry);
            else npcsProximos.remove(entry);
        });
    }

    /**
     * Retorna o NPC a ser interagido se a tecla E foi pressionada
     * e há ao menos um NPC próximo. Null caso contrário.
     */
    public Npc verificarInteracaoNpc(Set<KeyCode> teclasPressionadas) {
        if (!teclasPressionadas.contains(KeyCode.E)) return null;
        if (npcsProximos.isEmpty()) return null;

        return npcsProximos.iterator().next().npc();
    }

    public Npc getNpcProximo() {
        return npcsProximos.isEmpty() ? null : npcsProximos.iterator().next().npc();
    }
}
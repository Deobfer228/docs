package oreon.src.targetbanspec;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import oreon.src.OreonClientMod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages hitbox visualization and interaction for targeting
 */
public class HitboxManager {
    
    private final MinecraftClient client;
    private boolean enabled = false;
    private boolean showPlayerHitboxes = true;
    private boolean showEntityHitboxes = false;
    private boolean showTargetHitbox = true;
    private double hitboxExpansion = 0.0;
    private Map<Entity, HitboxInfo> customHitboxes;

    public HitboxManager() {
        this.client = MinecraftClient.getInstance();
        this.customHitboxes = new HashMap<>();
    }

    /**
     * Enable/disable hitbox display
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        OreonClientMod.LOGGER.info("Hitbox display {}", enabled ? "enabled" : "disabled");
    }

    /**
     * Check if hitbox display is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Get entities with visible hitboxes
     */
    public List<Entity> getEntitiesWithHitboxes() {
        List<Entity> entities = new ArrayList<>();
        
        if (!enabled || client.world == null) {
            return entities;
        }

        for (Entity entity : client.world.getEntities()) {
            if (shouldShowHitbox(entity)) {
                entities.add(entity);
            }
        }

        return entities;
    }

    /**
     * Check if hitbox should be shown for entity
     */
    private boolean shouldShowHitbox(Entity entity) {
        if (entity instanceof PlayerEntity) {
            return showPlayerHitboxes;
        } else {
            return showEntityHitboxes;
        }
    }

    /**
     * Get hitbox for entity
     */
    public Box getEntityHitbox(Entity entity) {
        Box originalBox = entity.getBoundingBox();
        
        // Check for custom hitbox
        HitboxInfo customInfo = customHitboxes.get(entity);
        if (customInfo != null) {
            return customInfo.hitbox;
        }

        // Apply expansion if set
        if (hitboxExpansion > 0) {
            return originalBox.expand(hitboxExpansion);
        }

        return originalBox;
    }

    /**
     * Set custom hitbox for entity
     */
    public void setCustomHitbox(Entity entity, Box hitbox, int color) {
        customHitboxes.put(entity, new HitboxInfo(hitbox, color));
    }

    /**
     * Remove custom hitbox for entity
     */
    public void removeCustomHitbox(Entity entity) {
        customHitboxes.remove(entity);
    }

    /**
     * Get hitbox color for entity
     */
    public int getHitboxColor(Entity entity) {
        // Check for custom color
        HitboxInfo customInfo = customHitboxes.get(entity);
        if (customInfo != null) {
            return customInfo.color;
        }

        // Default colors based on entity type
        if (entity instanceof PlayerEntity) {
            return 0xFF00FF00; // Green for players
        } else {
            return 0xFFFFFF00; // Yellow for other entities
        }
    }

    /**
     * Get target hitbox color (special highlighting)
     */
    public int getTargetHitboxColor() {
        return 0xFFFF0000; // Red for targets
    }

    /**
     * Set whether to show player hitboxes
     */
    public void setShowPlayerHitboxes(boolean show) {
        this.showPlayerHitboxes = show;
    }

    /**
     * Set whether to show entity hitboxes
     */
    public void setShowEntityHitboxes(boolean show) {
        this.showEntityHitboxes = show;
    }

    /**
     * Set whether to show target hitbox
     */
    public void setShowTargetHitbox(boolean show) {
        this.showTargetHitbox = show;
    }

    /**
     * Set hitbox expansion amount
     */
    public void setHitboxExpansion(double expansion) {
        this.hitboxExpansion = Math.max(0.0, expansion);
    }

    /**
     * Get hitbox expansion amount
     */
    public double getHitboxExpansion() {
        return hitboxExpansion;
    }

    /**
     * Check if showing player hitboxes
     */
    public boolean isShowingPlayerHitboxes() {
        return showPlayerHitboxes;
    }

    /**
     * Check if showing entity hitboxes
     */
    public boolean isShowingEntityHitboxes() {
        return showEntityHitboxes;
    }

    /**
     * Check if showing target hitbox
     */
    public boolean isShowingTargetHitbox() {
        return showTargetHitbox;
    }

    /**
     * Clear all custom hitboxes
     */
    public void clearCustomHitboxes() {
        customHitboxes.clear();
    }

    /**
     * Get all entities with custom hitboxes
     */
    public List<Entity> getEntitiesWithCustomHitboxes() {
        return new ArrayList<>(customHitboxes.keySet());
    }

    /**
     * Check if entity has custom hitbox
     */
    public boolean hasCustomHitbox(Entity entity) {
        return customHitboxes.containsKey(entity);
    }

    /**
     * Update hitbox manager (call on tick)
     */
    public void update() {
        // Clean up custom hitboxes for entities that no longer exist
        customHitboxes.entrySet().removeIf(entry -> {
            Entity entity = entry.getKey();
            return client.world == null || !client.world.getEntities().contains(entity);
        });
    }

    /**
     * Information about a custom hitbox
     */
    private static class HitboxInfo {
        public final Box hitbox;
        public final int color;

        public HitboxInfo(Box hitbox, int color) {
            this.hitbox = hitbox;
            this.color = color;
        }
    }
}
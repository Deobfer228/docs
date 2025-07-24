package oreon.src.targetmenu;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import oreon.src.OreonClientMod;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Handles target menu functionality for selecting and managing targets
 */
public class TargetMenuHandler {
    
    private final MinecraftClient client;
    private PlayerEntity currentTarget;
    private List<PlayerEntity> nearbyPlayers;
    private boolean enabled;
    private double targetRange;

    public TargetMenuHandler() {
        this.client = MinecraftClient.getInstance();
        this.nearbyPlayers = new ArrayList<>();
        this.enabled = true;
        this.targetRange = 50.0;
    }

    /**
     * Update nearby players list
     */
    public void updateNearbyPlayers() {
        nearbyPlayers.clear();
        
        if (client.world == null || client.player == null) {
            return;
        }

        // Get all players within range
        for (Entity entity : client.world.getEntities()) {
            if (entity instanceof PlayerEntity player && entity != client.player) {
                double distance = client.player.distanceTo(player);
                if (distance <= targetRange) {
                    nearbyPlayers.add(player);
                }
            }
        }

        // Sort by distance
        nearbyPlayers.sort((p1, p2) -> {
            double dist1 = client.player.distanceTo(p1);
            double dist2 = client.player.distanceTo(p2);
            return Double.compare(dist1, dist2);
        });
    }

    /**
     * Get nearby players
     */
    public List<PlayerEntity> getNearbyPlayers() {
        return new ArrayList<>(nearbyPlayers);
    }

    /**
     * Set current target
     */
    public void setTarget(PlayerEntity player) {
        this.currentTarget = player;
        
        if (player != null) {
            OreonClientMod.LOGGER.info("Target set to: {}", player.getName().getString());
            
            // Send message to player
            if (client.player != null) {
                client.player.sendMessage(
                    Text.literal("Target: " + player.getName().getString()), 
                    false
                );
            }
        } else {
            OreonClientMod.LOGGER.info("Target cleared");
            
            if (client.player != null) {
                client.player.sendMessage(Text.literal("Target cleared"), false);
            }
        }
    }

    /**
     * Get current target
     */
    public PlayerEntity getCurrentTarget() {
        return currentTarget;
    }

    /**
     * Clear current target
     */
    public void clearTarget() {
        setTarget(null);
    }

    /**
     * Select next nearby player as target
     */
    public void selectNextTarget() {
        updateNearbyPlayers();
        
        if (nearbyPlayers.isEmpty()) {
            clearTarget();
            return;
        }

        int currentIndex = -1;
        if (currentTarget != null) {
            for (int i = 0; i < nearbyPlayers.size(); i++) {
                if (nearbyPlayers.get(i).equals(currentTarget)) {
                    currentIndex = i;
                    break;
                }
            }
        }

        int nextIndex = (currentIndex + 1) % nearbyPlayers.size();
        setTarget(nearbyPlayers.get(nextIndex));
    }

    /**
     * Select previous nearby player as target
     */
    public void selectPreviousTarget() {
        updateNearbyPlayers();
        
        if (nearbyPlayers.isEmpty()) {
            clearTarget();
            return;
        }

        int currentIndex = -1;
        if (currentTarget != null) {
            for (int i = 0; i < nearbyPlayers.size(); i++) {
                if (nearbyPlayers.get(i).equals(currentTarget)) {
                    currentIndex = i;
                    break;
                }
            }
        }

        int prevIndex = currentIndex <= 0 ? nearbyPlayers.size() - 1 : currentIndex - 1;
        setTarget(nearbyPlayers.get(prevIndex));
    }

    /**
     * Auto-select closest player as target
     */
    public void autoSelectTarget() {
        updateNearbyPlayers();
        
        if (!nearbyPlayers.isEmpty()) {
            setTarget(nearbyPlayers.get(0)); // Closest player (list is sorted)
        }
    }

    /**
     * Check if target is still valid (in range and exists)
     */
    public boolean isTargetValid() {
        if (currentTarget == null) {
            return false;
        }

        // Check if target still exists in world
        if (client.world == null || !client.world.getEntities().contains(currentTarget)) {
            return false;
        }

        // Check if target is still in range
        if (client.player != null) {
            double distance = client.player.distanceTo(currentTarget);
            return distance <= targetRange;
        }

        return false;
    }

    /**
     * Update target validity and clear if invalid
     */
    public void updateTargetValidity() {
        if (!isTargetValid()) {
            clearTarget();
        }
    }

    /**
     * Get target information
     */
    public TargetInfo getTargetInfo() {
        if (currentTarget == null) {
            return null;
        }

        TargetInfo info = new TargetInfo();
        info.name = currentTarget.getName().getString();
        info.health = currentTarget.getHealth();
        info.maxHealth = currentTarget.getMaxHealth();
        
        if (client.player != null) {
            info.distance = client.player.distanceTo(currentTarget);
        }
        
        info.isValid = isTargetValid();
        
        return info;
    }

    /**
     * Set targeting enabled/disabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            clearTarget();
        }
    }

    /**
     * Check if targeting is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Set target range
     */
    public void setTargetRange(double range) {
        this.targetRange = Math.max(1.0, Math.min(100.0, range));
    }

    /**
     * Get target range
     */
    public double getTargetRange() {
        return targetRange;
    }

    /**
     * Target information class
     */
    public static class TargetInfo {
        public String name;
        public float health;
        public float maxHealth;
        public double distance;
        public boolean isValid;
        
        public float getHealthPercentage() {
            return maxHealth > 0 ? (health / maxHealth) * 100 : 0;
        }
    }
}
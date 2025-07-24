package oreon.src.functions;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import oreon.src.OreonClientMod;

/**
 * Handles hiding other players functionality
 */
public class HidePlayersHandler {
    
    private final MinecraftClient client;
    private boolean enabled = false;
    private boolean hideNameTags = false;
    private boolean hideOwnPlayer = false;

    public HidePlayersHandler() {
        this.client = MinecraftClient.getInstance();
    }

    /**
     * Toggle hide players on/off
     */
    public void toggle() {
        if (enabled) {
            disable();
        } else {
            enable();
        }
    }

    /**
     * Enable hide players
     */
    public void enable() {
        enabled = true;
        OreonClientMod.LOGGER.info("Hide Players enabled");
    }

    /**
     * Disable hide players
     */
    public void disable() {
        enabled = false;
        OreonClientMod.LOGGER.info("Hide Players disabled");
    }

    /**
     * Check if a player should be hidden
     */
    public boolean shouldHidePlayer(PlayerEntity player) {
        if (!enabled) return false;
        
        ClientPlayerEntity clientPlayer = client.player;
        if (clientPlayer == null) return false;
        
        // Don't hide own player unless configured to do so
        if (player == clientPlayer) {
            return hideOwnPlayer;
        }
        
        // Hide other players
        return true;
    }

    /**
     * Check if name tags should be hidden
     */
    public boolean shouldHideNameTag(PlayerEntity player) {
        if (!enabled || !hideNameTags) return false;
        
        return shouldHidePlayer(player);
    }

    /**
     * Set whether to hide name tags
     */
    public void setHideNameTags(boolean hide) {
        this.hideNameTags = hide;
    }

    /**
     * Set whether to hide own player
     */
    public void setHideOwnPlayer(boolean hide) {
        this.hideOwnPlayer = hide;
    }

    /**
     * Check if hide players is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Check if hiding name tags
     */
    public boolean isHidingNameTags() {
        return hideNameTags;
    }

    /**
     * Check if hiding own player
     */
    public boolean isHidingOwnPlayer() {
        return hideOwnPlayer;
    }

    /**
     * Called on client tick
     */
    public void onClientTick(MinecraftClient client) {
        // Update based on config if needed
        if (OreonClientMod.getConfig() != null) {
            boolean configEnabled = OreonClientMod.getConfig().functions.hidePlayersEnabled;
            if (configEnabled != enabled) {
                if (configEnabled) {
                    enable();
                } else {
                    disable();
                }
            }
            
            // Update settings from config
            hideNameTags = OreonClientMod.getConfig().functions.hideNameTags;
            hideOwnPlayer = OreonClientMod.getConfig().functions.hideOwnPlayer;
        }
    }
}
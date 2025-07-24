package oreon.src.functions;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.SimpleOption;
import oreon.src.OreonClientMod;

/**
 * Handles fullbright functionality
 */
public class FullbrightHandler {
    
    private final MinecraftClient client;
    private boolean enabled = false;
    private double originalGamma = 1.0;

    public FullbrightHandler() {
        this.client = MinecraftClient.getInstance();
    }

    /**
     * Toggle fullbright on/off
     */
    public void toggle() {
        if (enabled) {
            disable();
        } else {
            enable();
        }
    }

    /**
     * Enable fullbright
     */
    public void enable() {
        if (client.options == null) return;
        
        // Store original gamma value
        originalGamma = client.options.getGamma().getValue();
        
        // Set gamma to maximum for fullbright effect
        client.options.getGamma().setValue(16.0);
        
        enabled = true;
        OreonClientMod.LOGGER.info("Fullbright enabled");
    }

    /**
     * Disable fullbright
     */
    public void disable() {
        if (client.options == null) return;
        
        // Restore original gamma value
        client.options.getGamma().setValue(originalGamma);
        
        enabled = false;
        OreonClientMod.LOGGER.info("Fullbright disabled");
    }

    /**
     * Set custom brightness level
     */
    public void setBrightness(double level) {
        if (client.options == null) return;
        
        client.options.getGamma().setValue(Math.max(0.0, Math.min(16.0, level)));
        enabled = level > 1.0;
    }

    /**
     * Check if fullbright is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Get current brightness level
     */
    public double getBrightness() {
        if (client.options == null) return 1.0;
        return client.options.getGamma().getValue();
    }

    /**
     * Called on client tick
     */
    public void onClientTick(MinecraftClient client) {
        // Update based on config if needed
        if (OreonClientMod.getConfig() != null) {
            boolean configEnabled = OreonClientMod.getConfig().functions.fullbrightEnabled;
            if (configEnabled != enabled) {
                if (configEnabled) {
                    enable();
                } else {
                    disable();
                }
            }
        }
    }
}
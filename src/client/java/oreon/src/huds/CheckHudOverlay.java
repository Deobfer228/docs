package oreon.src.huds;

import net.minecraft.client.MinecraftClient;

/**
 * HUD overlay for check/review functionality
 */
public class CheckHudOverlay {
    
    private final MinecraftClient client;
    private boolean enabled = false;

    public CheckHudOverlay() {
        this.client = MinecraftClient.getInstance();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
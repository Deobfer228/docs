package oreon.src.huds;

import net.minecraft.client.MinecraftClient;

/**
 * HUD overlay for reports
 */
public class ReportHud {
    
    private final MinecraftClient client;
    private boolean enabled = false;

    public ReportHud() {
        this.client = MinecraftClient.getInstance();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
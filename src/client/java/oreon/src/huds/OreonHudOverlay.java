package oreon.src.huds;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import oreon.src.OreonClientMod;

/**
 * Main HUD overlay for Oreon
 */
public class OreonHudOverlay {
    
    private final MinecraftClient client;

    public OreonHudOverlay() {
        this.client = MinecraftClient.getInstance();
        registerHudRenderer();
    }

    private void registerHudRenderer() {
        HudRenderCallback.EVENT.register((context, delta) -> {
            if (shouldRenderHud()) {
                renderHud(context, delta);
            }
        });
    }

    private boolean shouldRenderHud() {
        if (OreonClientMod.getConfig() == null) return false;
        return OreonClientMod.getConfig().gui.showHud && client.player != null;
    }

    private void renderHud(DrawContext context, float delta) {
        // Draw main HUD elements
        int x = OreonClientMod.getConfig().gui.hudX;
        int y = OreonClientMod.getConfig().gui.hudY;
        
        // Draw watermark
        if (OreonClientMod.getConfig().gui.showWatermark) {
            context.drawTextWithShadow(
                client.textRenderer,
                "Oreon v" + OreonClientMod.VERSION,
                x,
                y,
                0xFFFFFF
            );
        }
        
        // Draw function status
        int yOffset = 10;
        if (OreonClientMod.getFullbrightHandler() != null && OreonClientMod.getFullbrightHandler().isEnabled()) {
            context.drawTextWithShadow(
                client.textRenderer,
                "Fullbright: ON",
                x,
                y + yOffset,
                0x00FF00
            );
            yOffset += 10;
        }
        
        if (OreonClientMod.getHidePlayersHandler() != null && OreonClientMod.getHidePlayersHandler().isEnabled()) {
            context.drawTextWithShadow(
                client.textRenderer,
                "Hide Players: ON",
                x,
                y + yOffset,
                0x00FF00
            );
        }
    }
}
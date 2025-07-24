package oreon.src.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import oreon.src.OreonClientMod;

/**
 * Legacy/Old Oreon GUI for backwards compatibility
 */
public class OldOreonGui extends Screen {

    private static final int BUTTON_WIDTH = 150;
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_SPACING = 22;

    public OldOreonGui() {
        super(Text.translatable("gui.oreon.old.title"));
    }

    @Override
    protected void init() {
        super.init();
        
        int centerX = this.width / 2;
        int startY = this.height / 2 - 80;

        // Quick access buttons (old style)
        addDrawableChild(ButtonWidget.builder(
                Text.literal("Fullbright"),
                button -> toggleFullbright())
                .dimensions(centerX - BUTTON_WIDTH - 5, startY, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Hide Players"),
                button -> toggleHidePlayers())
                .dimensions(centerX + 5, startY, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Report Editor"),
                button -> openReportEditor())
                .dimensions(centerX - BUTTON_WIDTH - 5, startY + BUTTON_SPACING, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Target Menu"),
                button -> openTargetMenu())
                .dimensions(centerX + 5, startY + BUTTON_SPACING, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Check Tools"),
                button -> openCheckTools())
                .dimensions(centerX - BUTTON_WIDTH - 5, startY + BUTTON_SPACING * 2, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Settings"),
                button -> OreonClientMod.getGuiHandler().openSettingsGui())
                .dimensions(centerX + 5, startY + BUTTON_SPACING * 2, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build());

        // Close button
        addDrawableChild(ButtonWidget.builder(
                Text.literal("Close"),
                button -> this.close())
                .dimensions(centerX - BUTTON_WIDTH / 2, startY + BUTTON_SPACING * 4, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        
        // Draw title
        context.drawCenteredTextWithShadow(
                this.textRenderer,
                this.title,
                this.width / 2,
                20,
                0xFFFFFF
        );

        // Draw status info
        String statusText = "Legacy Mode";
        context.drawTextWithShadow(
                this.textRenderer,
                statusText,
                5,
                this.height - 15,
                0xFFFF00
        );

        super.render(context, mouseX, mouseY, delta);
    }

    private void toggleFullbright() {
        if (OreonClientMod.getFullbrightHandler() != null) {
            OreonClientMod.getFullbrightHandler().toggle();
            if (client != null && client.player != null) {
                client.player.sendMessage(Text.literal("Fullbright toggled"), false);
            }
        }
    }

    private void toggleHidePlayers() {
        if (OreonClientMod.getHidePlayersHandler() != null) {
            OreonClientMod.getHidePlayersHandler().toggle();
            if (client != null && client.player != null) {
                client.player.sendMessage(Text.literal("Hide Players toggled"), false);
            }
        }
    }

    private void openReportEditor() {
        if (client != null && client.player != null) {
            client.player.sendMessage(Text.literal("Report Editor - Coming Soon!"), false);
        }
    }

    private void openTargetMenu() {
        if (client != null && client.player != null) {
            client.player.sendMessage(Text.literal("Target Menu - Coming Soon!"), false);
        }
    }

    private void openCheckTools() {
        if (client != null && client.player != null) {
            client.player.sendMessage(Text.literal("Check Tools - Coming Soon!"), false);
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
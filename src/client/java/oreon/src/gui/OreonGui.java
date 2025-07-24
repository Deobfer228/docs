package oreon.src.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import oreon.src.OreonClientMod;
import oreon.src.config.OreonConfig;

/**
 * Main Oreon GUI screen with modern design
 */
public class OreonGui extends Screen {
    
    private final OreonConfig config;
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_SPACING = 25;

    public OreonGui() {
        super(Text.translatable("gui.oreon.main.title"));
        this.config = OreonClientMod.getConfig();
    }

    @Override
    protected void init() {
        super.init();
        
        int centerX = this.width / 2;
        int startY = this.height / 2 - 100;

        // Main function buttons
        addDrawableChild(ButtonWidget.builder(
                Text.translatable("gui.oreon.functions.title"),
                button -> openFunctionsGui())
                .dimensions(centerX - BUTTON_WIDTH / 2, startY, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build());

        addDrawableChild(ButtonWidget.builder(
                Text.translatable("gui.oreon.reports.title"),
                button -> openReportsGui())
                .dimensions(centerX - BUTTON_WIDTH / 2, startY + BUTTON_SPACING, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build());

        addDrawableChild(ButtonWidget.builder(
                Text.translatable("gui.oreon.targeting.title"),
                button -> openTargetingGui())
                .dimensions(centerX - BUTTON_WIDTH / 2, startY + BUTTON_SPACING * 2, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build());

        addDrawableChild(ButtonWidget.builder(
                Text.translatable("gui.oreon.check.title"),
                button -> openCheckGui())
                .dimensions(centerX - BUTTON_WIDTH / 2, startY + BUTTON_SPACING * 3, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build());

        // Settings and profile buttons
        addDrawableChild(ButtonWidget.builder(
                Text.translatable("gui.oreon.settings.title"),
                button -> OreonClientMod.getGuiHandler().openSettingsGui())
                .dimensions(centerX - BUTTON_WIDTH / 2, startY + BUTTON_SPACING * 5, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build());

        addDrawableChild(ButtonWidget.builder(
                Text.translatable("gui.oreon.profile.title"),
                button -> OreonClientMod.getGuiHandler().openProfileGui())
                .dimensions(centerX - BUTTON_WIDTH / 2, startY + BUTTON_SPACING * 6, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build());

        // Close button
        addDrawableChild(ButtonWidget.builder(
                Text.translatable("gui.oreon.close"),
                button -> this.close())
                .dimensions(centerX - BUTTON_WIDTH / 2, startY + BUTTON_SPACING * 8, BUTTON_WIDTH, BUTTON_HEIGHT)
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

        // Draw version info
        String versionText = "Oreon v" + OreonClientMod.VERSION;
        context.drawTextWithShadow(
                this.textRenderer,
                versionText,
                this.width - this.textRenderer.getWidth(versionText) - 5,
                this.height - 15,
                0x888888
        );

        super.render(context, mouseX, mouseY, delta);
    }

    private void openFunctionsGui() {
        // TODO: Implement functions GUI
        if (client != null) {
            client.player.sendMessage(Text.literal("Functions GUI - Coming Soon!"), false);
        }
    }

    private void openReportsGui() {
        // TODO: Implement reports GUI
        if (client != null) {
            client.player.sendMessage(Text.literal("Reports GUI - Coming Soon!"), false);
        }
    }

    private void openTargetingGui() {
        // TODO: Implement targeting GUI
        if (client != null) {
            client.player.sendMessage(Text.literal("Targeting GUI - Coming Soon!"), false);
        }
    }

    private void openCheckGui() {
        // TODO: Implement check GUI
        if (client != null) {
            client.player.sendMessage(Text.literal("Check GUI - Coming Soon!"), false);
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
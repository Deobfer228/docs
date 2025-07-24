package oreon.src.targetmenu;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import oreon.src.OreonClientMod;

import java.util.List;

/**
 * GUI for target menu - shows nearby players and allows selection
 */
public class TargetMenuGui extends Screen {

    private TargetMenuHandler targetHandler;
    private List<PlayerEntity> nearbyPlayers;
    private int selectedIndex = 0;
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;

    public TargetMenuGui() {
        super(Text.translatable("gui.oreon.target_menu.title"));
        this.targetHandler = new TargetMenuHandler(); // TODO: Get from main mod instance
    }

    @Override
    protected void init() {
        super.init();
        
        // Update nearby players
        targetHandler.updateNearbyPlayers();
        nearbyPlayers = targetHandler.getNearbyPlayers();
        
        int centerX = this.width / 2;
        int startY = this.height / 2 - 100;

        // Auto-select button
        addDrawableChild(ButtonWidget.builder(
                Text.literal("Auto Select Closest"),
                button -> autoSelectTarget())
                .dimensions(centerX - BUTTON_WIDTH / 2, startY, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build());

        // Clear target button
        addDrawableChild(ButtonWidget.builder(
                Text.literal("Clear Target"),
                button -> clearTarget())
                .dimensions(centerX - BUTTON_WIDTH / 2, startY + 25, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build());

        // Previous/Next buttons
        addDrawableChild(ButtonWidget.builder(
                Text.literal("< Previous"),
                button -> selectPrevious())
                .dimensions(centerX - BUTTON_WIDTH / 2, startY + 60, BUTTON_WIDTH / 2 - 5, BUTTON_HEIGHT)
                .build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Next >"),
                button -> selectNext())
                .dimensions(centerX + 5, startY + 60, BUTTON_WIDTH / 2 - 5, BUTTON_HEIGHT)
                .build());

        // Player list buttons (show first 5 players)
        for (int i = 0; i < Math.min(5, nearbyPlayers.size()); i++) {
            final int index = i;
            PlayerEntity player = nearbyPlayers.get(i);
            
            addDrawableChild(ButtonWidget.builder(
                    Text.literal(player.getName().getString()),
                    button -> selectPlayer(index))
                    .dimensions(centerX - BUTTON_WIDTH / 2, startY + 100 + (i * 25), BUTTON_WIDTH, BUTTON_HEIGHT)
                    .build());
        }

        // Close button
        addDrawableChild(ButtonWidget.builder(
                Text.literal("Close"),
                button -> this.close())
                .dimensions(centerX - 50, this.height - 40, 100, BUTTON_HEIGHT)
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

        // Draw current target info
        TargetMenuHandler.TargetInfo targetInfo = targetHandler.getTargetInfo();
        if (targetInfo != null) {
            context.drawCenteredTextWithShadow(
                    this.textRenderer,
                    "Current Target: " + targetInfo.name,
                    this.width / 2,
                    45,
                    0x00FF00
            );
            
            context.drawCenteredTextWithShadow(
                    this.textRenderer,
                    String.format("Health: %.1f/%.1f (%.1f%%) | Distance: %.1fm",
                                targetInfo.health, targetInfo.maxHealth, 
                                targetInfo.getHealthPercentage(), targetInfo.distance),
                    this.width / 2,
                    60,
                    0xFFFFFF
            );
        } else {
            context.drawCenteredTextWithShadow(
                    this.textRenderer,
                    "No target selected",
                    this.width / 2,
                    45,
                    0xFF0000
            );
        }

        // Draw nearby players count
        context.drawCenteredTextWithShadow(
                this.textRenderer,
                "Nearby Players: " + nearbyPlayers.size(),
                this.width / 2,
                this.height / 2 - 120,
                0xFFFFFF
        );

        // Draw player list info
        int startY = this.height / 2 + 85;
        for (int i = 0; i < Math.min(5, nearbyPlayers.size()); i++) {
            PlayerEntity player = nearbyPlayers.get(i);
            double distance = client.player != null ? client.player.distanceTo(player) : 0;
            
            String info = String.format("%.1fm | Health: %.1f", distance, player.getHealth());
            context.drawTextWithShadow(
                    this.textRenderer,
                    info,
                    this.width / 2 + BUTTON_WIDTH / 2 + 10,
                    startY + (i * 25) + 6,
                    0xCCCCCC
            );
        }

        super.render(context, mouseX, mouseY, delta);
    }

    private void autoSelectTarget() {
        targetHandler.autoSelectTarget();
        refreshGui();
    }

    private void clearTarget() {
        targetHandler.clearTarget();
        refreshGui();
    }

    private void selectPrevious() {
        targetHandler.selectPreviousTarget();
        refreshGui();
    }

    private void selectNext() {
        targetHandler.selectNextTarget();
        refreshGui();
    }

    private void selectPlayer(int index) {
        if (index >= 0 && index < nearbyPlayers.size()) {
            targetHandler.setTarget(nearbyPlayers.get(index));
            refreshGui();
        }
    }

    private void refreshGui() {
        // Refresh the nearby players list
        targetHandler.updateNearbyPlayers();
        nearbyPlayers = targetHandler.getNearbyPlayers();
        
        // Rebuild the GUI
        this.clearChildren();
        this.init();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
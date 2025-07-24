package oreon.src.targetmenu;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import java.util.List;

/**
 * GUI for selecting targets from a list
 */
public class TargetSelectGui extends Screen {

    private TargetMenuHandler targetHandler;
    private List<PlayerEntity> availablePlayers;
    private int scrollOffset = 0;
    private static final int PLAYERS_PER_PAGE = 8;
    private static final int BUTTON_WIDTH = 250;
    private static final int BUTTON_HEIGHT = 20;

    public TargetSelectGui() {
        super(Text.translatable("gui.oreon.target_select.title"));
        this.targetHandler = new TargetMenuHandler(); // TODO: Get from main mod instance
    }

    @Override
    protected void init() {
        super.init();
        
        // Update available players
        targetHandler.updateNearbyPlayers();
        availablePlayers = targetHandler.getNearbyPlayers();
        
        int centerX = this.width / 2;
        int startY = this.height / 2 - 120;

        // Scroll buttons
        if (scrollOffset > 0) {
            addDrawableChild(ButtonWidget.builder(
                    Text.literal("▲ Scroll Up"),
                    button -> scrollUp())
                    .dimensions(centerX - BUTTON_WIDTH / 2, startY, BUTTON_WIDTH, BUTTON_HEIGHT)
                    .build());
            startY += 25;
        }

        // Player selection buttons
        int endIndex = Math.min(availablePlayers.size(), scrollOffset + PLAYERS_PER_PAGE);
        for (int i = scrollOffset; i < endIndex; i++) {
            final int playerIndex = i;
            PlayerEntity player = availablePlayers.get(i);
            
            double distance = client.player != null ? client.player.distanceTo(player) : 0;
            String buttonText = String.format("%s (%.1fm)", 
                                            player.getName().getString(), distance);
            
            // Highlight current target
            boolean isCurrentTarget = targetHandler.getCurrentTarget() == player;
            String prefix = isCurrentTarget ? "► " : "";
            
            addDrawableChild(ButtonWidget.builder(
                    Text.literal(prefix + buttonText),
                    button -> selectPlayer(playerIndex))
                    .dimensions(centerX - BUTTON_WIDTH / 2, startY, BUTTON_WIDTH, BUTTON_HEIGHT)
                    .build());
            startY += 25;
        }

        // Scroll down button
        if (scrollOffset + PLAYERS_PER_PAGE < availablePlayers.size()) {
            addDrawableChild(ButtonWidget.builder(
                    Text.literal("▼ Scroll Down"),
                    button -> scrollDown())
                    .dimensions(centerX - BUTTON_WIDTH / 2, startY, BUTTON_WIDTH, BUTTON_HEIGHT)
                    .build());
            startY += 30;
        }

        // Action buttons
        addDrawableChild(ButtonWidget.builder(
                Text.literal("Clear Target"),
                button -> clearTarget())
                .dimensions(centerX - BUTTON_WIDTH / 2, startY, BUTTON_WIDTH / 2 - 5, BUTTON_HEIGHT)
                .build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Refresh List"),
                button -> refreshList())
                .dimensions(centerX + 5, startY, BUTTON_WIDTH / 2 - 5, BUTTON_HEIGHT)
                .build());

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
                    40,
                    0x00FF00
            );
        } else {
            context.drawCenteredTextWithShadow(
                    this.textRenderer,
                    "No target selected",
                    this.width / 2,
                    40,
                    0xFF0000
            );
        }

        // Draw player count and page info
        String pageInfo = String.format("Players: %d | Page: %d/%d", 
                                       availablePlayers.size(),
                                       (scrollOffset / PLAYERS_PER_PAGE) + 1,
                                       Math.max(1, (availablePlayers.size() + PLAYERS_PER_PAGE - 1) / PLAYERS_PER_PAGE));
        
        context.drawCenteredTextWithShadow(
                this.textRenderer,
                pageInfo,
                this.width / 2,
                60,
                0xCCCCCC
        );

        // Draw additional info for visible players
        int startY = this.height / 2 - 95;
        if (scrollOffset > 0) startY += 25;
        
        int endIndex = Math.min(availablePlayers.size(), scrollOffset + PLAYERS_PER_PAGE);
        for (int i = scrollOffset; i < endIndex; i++) {
            PlayerEntity player = availablePlayers.get(i);
            
            // Draw health bar
            float health = player.getHealth();
            float maxHealth = player.getMaxHealth();
            float healthPercent = maxHealth > 0 ? health / maxHealth : 0;
            
            int barX = this.width / 2 + BUTTON_WIDTH / 2 + 10;
            int barY = startY + 6;
            int barWidth = 60;
            int barHeight = 8;
            
            // Background
            context.fill(barX, barY, barX + barWidth, barY + barHeight, 0xFF333333);
            
            // Health bar
            int healthColor = healthPercent > 0.6f ? 0xFF00FF00 : 
                             healthPercent > 0.3f ? 0xFFFFFF00 : 0xFFFF0000;
            context.fill(barX, barY, barX + (int)(barWidth * healthPercent), barY + barHeight, healthColor);
            
            // Health text
            context.drawTextWithShadow(
                    this.textRenderer,
                    String.format("%.1f", health),
                    barX + barWidth + 5,
                    barY,
                    0xFFFFFF
            );
            
            startY += 25;
        }

        super.render(context, mouseX, mouseY, delta);
    }

    private void scrollUp() {
        scrollOffset = Math.max(0, scrollOffset - PLAYERS_PER_PAGE);
        refreshGui();
    }

    private void scrollDown() {
        scrollOffset = Math.min(availablePlayers.size() - PLAYERS_PER_PAGE, 
                               scrollOffset + PLAYERS_PER_PAGE);
        refreshGui();
    }

    private void selectPlayer(int index) {
        if (index >= 0 && index < availablePlayers.size()) {
            targetHandler.setTarget(availablePlayers.get(index));
            refreshGui();
        }
    }

    private void clearTarget() {
        targetHandler.clearTarget();
        refreshGui();
    }

    private void refreshList() {
        scrollOffset = 0;
        refreshGui();
    }

    private void refreshGui() {
        // Update player list
        targetHandler.updateNearbyPlayers();
        availablePlayers = targetHandler.getNearbyPlayers();
        
        // Rebuild GUI
        this.clearChildren();
        this.init();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
package oreon.src.revise;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import oreon.src.OreonClientMod;

/**
 * GUI for generating and managing clean verdicts
 */
public class CleanVerdictGui extends Screen {

    private TextFieldWidget playerNameField;
    private TextFieldWidget reasonField;
    private TextFieldWidget moderatorField;
    private ButtonWidget generateButton;
    private ButtonWidget copyButton;
    private ButtonWidget clearButton;
    
    private String generatedVerdict = "";
    private static final int FIELD_WIDTH = 250;
    private static final int FIELD_HEIGHT = 20;

    public CleanVerdictGui() {
        super(Text.translatable("gui.oreon.clean_verdict.title"));
    }

    @Override
    protected void init() {
        super.init();
        
        int centerX = this.width / 2;
        int startY = this.height / 2 - 120;

        // Player name field
        this.playerNameField = new TextFieldWidget(
                this.textRenderer,
                centerX - FIELD_WIDTH / 2,
                startY,
                FIELD_WIDTH,
                FIELD_HEIGHT,
                Text.literal("Player Name")
        );
        this.playerNameField.setPlaceholder(Text.literal("Enter player name..."));
        addDrawableChild(this.playerNameField);

        // Reason field (optional)
        this.reasonField = new TextFieldWidget(
                this.textRenderer,
                centerX - FIELD_WIDTH / 2,
                startY + 30,
                FIELD_WIDTH,
                FIELD_HEIGHT,
                Text.literal("Additional Notes")
        );
        this.reasonField.setPlaceholder(Text.literal("Additional notes (optional)..."));
        addDrawableChild(this.reasonField);

        // Moderator field
        this.moderatorField = new TextFieldWidget(
                this.textRenderer,
                centerX - FIELD_WIDTH / 2,
                startY + 60,
                FIELD_WIDTH,
                FIELD_HEIGHT,
                Text.literal("Moderator")
        );
        if (client != null && client.player != null) {
            this.moderatorField.setText(client.player.getName().getString());
        }
        addDrawableChild(this.moderatorField);

        // Generate verdict button
        this.generateButton = addDrawableChild(ButtonWidget.builder(
                Text.literal("Generate Clean Verdict"),
                button -> generateVerdict())
                .dimensions(centerX - 100, startY + 100, 200, 20)
                .build());

        // Copy to clipboard button
        this.copyButton = addDrawableChild(ButtonWidget.builder(
                Text.literal("Copy to Clipboard"),
                button -> copyToClipboard())
                .dimensions(centerX - 100, startY + 125, 200, 20)
                .build());

        // Clear button
        this.clearButton = addDrawableChild(ButtonWidget.builder(
                Text.literal("Clear"),
                button -> clearFields())
                .dimensions(centerX - 100, startY + 150, 200, 20)
                .build());

        // Close button
        addDrawableChild(ButtonWidget.builder(
                Text.literal("Close"),
                button -> this.close())
                .dimensions(centerX - 50, startY + 180, 100, 20)
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

        // Draw field labels
        int centerX = this.width / 2;
        int startY = this.height / 2 - 120;

        context.drawTextWithShadow(
                this.textRenderer,
                "Player Name:",
                centerX - FIELD_WIDTH / 2,
                startY - 15,
                0xFFFFFF
        );

        context.drawTextWithShadow(
                this.textRenderer,
                "Additional Notes:",
                centerX - FIELD_WIDTH / 2,
                startY + 15,
                0xFFFFFF
        );

        context.drawTextWithShadow(
                this.textRenderer,
                "Moderator:",
                centerX - FIELD_WIDTH / 2,
                startY + 45,
                0xFFFFFF
        );

        // Draw generated verdict if available
        if (!generatedVerdict.isEmpty()) {
            int verdictY = startY + 220;
            String[] lines = generatedVerdict.split("\n");
            for (int i = 0; i < Math.min(lines.length, 10); i++) {
                context.drawTextWithShadow(
                        this.textRenderer,
                        lines[i],
                        20,
                        verdictY + (i * 10),
                        0x00FF00
                );
            }
        }

        super.render(context, mouseX, mouseY, delta);
    }

    /**
     * Generate the clean verdict
     */
    private void generateVerdict() {
        String playerName = playerNameField.getText().trim();
        String additionalNotes = reasonField.getText().trim();
        String moderator = moderatorField.getText().trim();

        if (playerName.isEmpty()) {
            if (client != null && client.player != null) {
                client.player.sendMessage(Text.literal("Please enter a player name"), false);
            }
            return;
        }

        if (moderator.isEmpty()) {
            moderator = "Unknown";
        }

        // Generate verdict using CheckLogic
        CheckLogic checkLogic = new CheckLogic();
        generatedVerdict = checkLogic.generateCleanVerdict(playerName);

        // Add additional notes if provided
        if (!additionalNotes.isEmpty()) {
            generatedVerdict = generatedVerdict.replace(
                "No suspicious activity detected.",
                "No suspicious activity detected.\nAdditional Notes: " + additionalNotes
            );
        }

        // Replace moderator name
        generatedVerdict = generatedVerdict.replace(
            "Moderator: " + (client != null && client.player != null ? client.player.getName().getString() : "Unknown"),
            "Moderator: " + moderator
        );

        OreonClientMod.LOGGER.info("Generated clean verdict for player: {}", playerName);
    }

    /**
     * Copy verdict to clipboard
     */
    private void copyToClipboard() {
        if (generatedVerdict.isEmpty()) {
            if (client != null && client.player != null) {
                client.player.sendMessage(Text.literal("No verdict to copy. Generate one first."), false);
            }
            return;
        }

        if (client != null) {
            client.keyboard.setClipboard(generatedVerdict);
            if (client.player != null) {
                client.player.sendMessage(Text.literal("Verdict copied to clipboard!"), false);
            }
        }
    }

    /**
     * Clear all fields and generated verdict
     */
    private void clearFields() {
        playerNameField.setText("");
        reasonField.setText("");
        if (client != null && client.player != null) {
            moderatorField.setText(client.player.getName().getString());
        } else {
            moderatorField.setText("");
        }
        generatedVerdict = "";
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
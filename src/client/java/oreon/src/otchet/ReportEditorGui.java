package oreon.src.otchet;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import oreon.src.OreonClientMod;
import oreon.src.util.ReportInfo;

/**
 * GUI for creating and editing reports
 */
public class ReportEditorGui extends Screen {

    private TextFieldWidget playerNameField;
    private TextFieldWidget reasonField;
    private CustomTextEditorWidget descriptionEditor;
    private ButtonWidget templateButton;
    private ButtonWidget saveButton;
    private ButtonWidget submitButton;
    private ButtonWidget clearButton;
    
    private ReportInfo currentReport;
    private ReportManager reportManager;
    private String selectedTemplate = "";
    
    private static final int FIELD_WIDTH = 300;
    private static final int FIELD_HEIGHT = 20;
    private static final int EDITOR_HEIGHT = 100;

    public ReportEditorGui() {
        super(Text.translatable("gui.oreon.report_editor.title"));
        this.reportManager = new ReportManager(); // TODO: Get from main mod instance
    }

    public ReportEditorGui(ReportInfo existingReport) {
        this();
        this.currentReport = existingReport;
    }

    @Override
    protected void init() {
        super.init();
        
        int centerX = this.width / 2;
        int startY = this.height / 2 - 150;

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

        // Reason field
        this.reasonField = new TextFieldWidget(
                this.textRenderer,
                centerX - FIELD_WIDTH / 2,
                startY + 40,
                FIELD_WIDTH,
                FIELD_HEIGHT,
                Text.literal("Reason")
        );
        this.reasonField.setPlaceholder(Text.literal("Enter reason..."));
        addDrawableChild(this.reasonField);

        // Description editor (multiline)
        this.descriptionEditor = new CustomTextEditorWidget(
                this.textRenderer,
                centerX - FIELD_WIDTH / 2,
                startY + 80,
                FIELD_WIDTH,
                EDITOR_HEIGHT,
                Text.literal("Description")
        );
        this.descriptionEditor.setPlaceholder("Enter detailed description...");
        addDrawableChild(this.descriptionEditor);

        // Template selection button
        this.templateButton = addDrawableChild(ButtonWidget.builder(
                Text.literal("Use Template"),
                button -> openTemplateSelector())
                .dimensions(centerX - FIELD_WIDTH / 2, startY + 200, 100, 20)
                .build());

        // Save draft button
        this.saveButton = addDrawableChild(ButtonWidget.builder(
                Text.literal("Save Draft"),
                button -> saveDraft())
                .dimensions(centerX - FIELD_WIDTH / 2 + 110, startY + 200, 90, 20)
                .build());

        // Submit button
        this.submitButton = addDrawableChild(ButtonWidget.builder(
                Text.literal("Submit Report"),
                button -> submitReport())
                .dimensions(centerX - FIELD_WIDTH / 2 + 210, startY + 200, 90, 20)
                .build());

        // Clear button
        this.clearButton = addDrawableChild(ButtonWidget.builder(
                Text.literal("Clear All"),
                button -> clearAll())
                .dimensions(centerX - 75, startY + 230, 150, 20)
                .build());

        // Close button
        addDrawableChild(ButtonWidget.builder(
                Text.literal("Close"),
                button -> this.close())
                .dimensions(centerX - 50, startY + 260, 100, 20)
                .build());

        // Load existing report if provided
        if (currentReport != null) {
            loadReport(currentReport);
        }
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
        int startY = this.height / 2 - 150;

        context.drawTextWithShadow(
                this.textRenderer,
                "Player Name:",
                centerX - FIELD_WIDTH / 2,
                startY - 15,
                0xFFFFFF
        );

        context.drawTextWithShadow(
                this.textRenderer,
                "Reason:",
                centerX - FIELD_WIDTH / 2,
                startY + 25,
                0xFFFFFF
        );

        context.drawTextWithShadow(
                this.textRenderer,
                "Description:",
                centerX - FIELD_WIDTH / 2,
                startY + 65,
                0xFFFFFF
        );

        // Show selected template
        if (!selectedTemplate.isEmpty()) {
            context.drawTextWithShadow(
                    this.textRenderer,
                    "Template: " + selectedTemplate,
                    centerX - FIELD_WIDTH / 2,
                    startY + 185,
                    0x00FF00
            );
        }

        super.render(context, mouseX, mouseY, delta);
    }

    /**
     * Open template selector
     */
    private void openTemplateSelector() {
        // TODO: Implement template selector GUI
        if (client != null && client.player != null) {
            client.player.sendMessage(Text.literal("Template Selector - Coming Soon!"), false);
        }
    }

    /**
     * Save current report as draft
     */
    private void saveDraft() {
        String playerName = playerNameField.getText().trim();
        String reason = reasonField.getText().trim();
        String description = descriptionEditor.getText().trim();

        if (playerName.isEmpty()) {
            showMessage("Please enter a player name");
            return;
        }

        ReportInfo report = reportManager.createReport(playerName, reason, description);
        this.currentReport = report;
        
        showMessage("Draft saved for player: " + playerName);
    }

    /**
     * Submit the report
     */
    private void submitReport() {
        String playerName = playerNameField.getText().trim();
        String reason = reasonField.getText().trim();
        String description = descriptionEditor.getText().trim();

        if (playerName.isEmpty() || reason.isEmpty()) {
            showMessage("Please fill in player name and reason");
            return;
        }

        ReportInfo report;
        if (currentReport != null) {
            // Update existing report
            currentReport.setPlayerName(playerName);
            currentReport.setReason(reason);
            currentReport.setDescription(description);
            report = currentReport;
        } else {
            // Create new report
            report = reportManager.createReport(playerName, reason, description);
        }

        boolean success = reportManager.submitReport(report);
        if (success) {
            showMessage("Report submitted successfully!");
            clearAll();
        } else {
            showMessage("Failed to submit report");
        }
    }

    /**
     * Clear all fields
     */
    private void clearAll() {
        playerNameField.setText("");
        reasonField.setText("");
        descriptionEditor.setText("");
        selectedTemplate = "";
        currentReport = null;
    }

    /**
     * Load an existing report
     */
    private void loadReport(ReportInfo report) {
        playerNameField.setText(report.getPlayerName());
        reasonField.setText(report.getReason());
        descriptionEditor.setText(report.getDescription());
    }

    /**
     * Show a message to the user
     */
    private void showMessage(String message) {
        if (client != null && client.player != null) {
            client.player.sendMessage(Text.literal(message), false);
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
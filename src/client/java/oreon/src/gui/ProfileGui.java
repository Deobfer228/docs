package oreon.src.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import oreon.src.OreonClientMod;

/**
 * Profile management GUI
 */
public class ProfileGui extends Screen {

    private TextFieldWidget usernameField;
    private TextFieldWidget serverField;
    private static final int FIELD_WIDTH = 200;
    private static final int FIELD_HEIGHT = 20;

    public ProfileGui() {
        super(Text.translatable("gui.oreon.profile.title"));
    }

    @Override
    protected void init() {
        super.init();
        
        int centerX = this.width / 2;
        int startY = this.height / 2 - 60;

        // Username field
        this.usernameField = new TextFieldWidget(
                this.textRenderer,
                centerX - FIELD_WIDTH / 2,
                startY,
                FIELD_WIDTH,
                FIELD_HEIGHT,
                Text.literal("Username")
        );
        this.usernameField.setPlaceholder(Text.literal("Enter username..."));
        addDrawableChild(this.usernameField);

        // Server field
        this.serverField = new TextFieldWidget(
                this.textRenderer,
                centerX - FIELD_WIDTH / 2,
                startY + 30,
                FIELD_WIDTH,
                FIELD_HEIGHT,
                Text.literal("Server")
        );
        this.serverField.setPlaceholder(Text.literal("Enter server..."));
        addDrawableChild(this.serverField);

        // Save profile button
        addDrawableChild(ButtonWidget.builder(
                Text.literal("Save Profile"),
                button -> saveProfile())
                .dimensions(centerX - 100, startY + 70, 200, 20)
                .build());

        // Load profile button
        addDrawableChild(ButtonWidget.builder(
                Text.literal("Load Profile"),
                button -> loadProfile())
                .dimensions(centerX - 100, startY + 95, 200, 20)
                .build());

        // Close button
        addDrawableChild(ButtonWidget.builder(
                Text.literal("Close"),
                button -> this.close())
                .dimensions(centerX - 50, startY + 130, 100, 20)
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
        context.drawTextWithShadow(
                this.textRenderer,
                "Username:",
                this.width / 2 - FIELD_WIDTH / 2,
                this.height / 2 - 75,
                0xFFFFFF
        );

        context.drawTextWithShadow(
                this.textRenderer,
                "Server:",
                this.width / 2 - FIELD_WIDTH / 2,
                this.height / 2 - 45,
                0xFFFFFF
        );

        super.render(context, mouseX, mouseY, delta);
    }

    private void saveProfile() {
        String username = usernameField.getText();
        String server = serverField.getText();
        
        // TODO: Implement profile saving
        if (client != null && client.player != null) {
            client.player.sendMessage(Text.literal("Profile saved: " + username + "@" + server), false);
        }
    }

    private void loadProfile() {
        // TODO: Implement profile loading
        if (client != null && client.player != null) {
            client.player.sendMessage(Text.literal("Profile loading - Coming Soon!"), false);
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
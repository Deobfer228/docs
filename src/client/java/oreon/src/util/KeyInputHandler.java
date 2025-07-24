package oreon.src.util;

import net.minecraft.client.MinecraftClient;
import oreon.src.KeyBinds;
import oreon.src.OreonClientMod;

/**
 * Handles key input processing for Oreon
 */
public class KeyInputHandler {
    
    private final MinecraftClient client;

    public KeyInputHandler() {
        this.client = MinecraftClient.getInstance();
    }

    /**
     * Called on client tick to check for key presses
     */
    public void onClientTick(MinecraftClient client) {
        // Check main GUI key
        while (KeyBinds.OPEN_OREON_GUI.wasPressed()) {
            OreonClientMod.getGuiHandler().openMainGui();
        }

        // Check old GUI key
        while (KeyBinds.OPEN_OLD_GUI.wasPressed()) {
            OreonClientMod.getGuiHandler().openOldGui();
        }

        // Check settings key
        while (KeyBinds.OPEN_SETTINGS.wasPressed()) {
            OreonClientMod.getGuiHandler().openSettingsGui();
        }

        // Check profile key
        while (KeyBinds.OPEN_PROFILE.wasPressed()) {
            OreonClientMod.getGuiHandler().openProfileGui();
        }

        // Check function toggles
        while (KeyBinds.TOGGLE_FULLBRIGHT.wasPressed()) {
            if (OreonClientMod.getFullbrightHandler() != null) {
                OreonClientMod.getFullbrightHandler().toggle();
            }
        }

        while (KeyBinds.TOGGLE_HIDE_PLAYERS.wasPressed()) {
            if (OreonClientMod.getHidePlayersHandler() != null) {
                OreonClientMod.getHidePlayersHandler().toggle();
            }
        }

        // Check other feature keys
        while (KeyBinds.OPEN_CHECK_GUI.wasPressed()) {
            openCheckGui();
        }

        while (KeyBinds.CLEAN_VERDICT.wasPressed()) {
            cleanVerdict();
        }

        while (KeyBinds.OPEN_REPORT_EDITOR.wasPressed()) {
            openReportEditor();
        }

        while (KeyBinds.OPEN_BANSPEC_MANAGER.wasPressed()) {
            openBanspecManager();
        }

        while (KeyBinds.OPEN_TEMPLATE_MANAGER.wasPressed()) {
            openTemplateManager();
        }

        while (KeyBinds.OPEN_TARGET_MENU.wasPressed()) {
            openTargetMenu();
        }

        while (KeyBinds.OPEN_TARGET_SELECT.wasPressed()) {
            openTargetSelect();
        }
    }

    private void openCheckGui() {
        // TODO: Implement check GUI opening
        if (client.player != null) {
            client.player.sendMessage(net.minecraft.text.Text.literal("Check GUI - Coming Soon!"), false);
        }
    }

    private void cleanVerdict() {
        // TODO: Implement clean verdict functionality
        if (client.player != null) {
            client.player.sendMessage(net.minecraft.text.Text.literal("Clean Verdict - Coming Soon!"), false);
        }
    }

    private void openReportEditor() {
        // TODO: Implement report editor opening
        if (client.player != null) {
            client.player.sendMessage(net.minecraft.text.Text.literal("Report Editor - Coming Soon!"), false);
        }
    }

    private void openBanspecManager() {
        // TODO: Implement banspec manager opening
        if (client.player != null) {
            client.player.sendMessage(net.minecraft.text.Text.literal("Banspec Manager - Coming Soon!"), false);
        }
    }

    private void openTemplateManager() {
        // TODO: Implement template manager opening
        if (client.player != null) {
            client.player.sendMessage(net.minecraft.text.Text.literal("Template Manager - Coming Soon!"), false);
        }
    }

    private void openTargetMenu() {
        // TODO: Implement target menu opening
        if (client.player != null) {
            client.player.sendMessage(net.minecraft.text.Text.literal("Target Menu - Coming Soon!"), false);
        }
    }

    private void openTargetSelect() {
        // TODO: Implement target select opening
        if (client.player != null) {
            client.player.sendMessage(net.minecraft.text.Text.literal("Target Select - Coming Soon!"), false);
        }
    }
}
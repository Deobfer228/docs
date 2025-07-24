package oreon.src.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import oreon.src.OreonClientMod;
import oreon.src.config.OreonConfig;

/**
 * Handles opening and managing different GUI screens for Oreon
 */
public class GuiHandler {
    
    private final MinecraftClient client;
    private final OreonConfig config;

    // GUI screen instances
    private OreonGui mainGui;
    private OldOreonGui oldGui;
    private OreonSettingsGui settingsGui;
    private ProfileGui profileGui;

    public GuiHandler() {
        this.client = MinecraftClient.getInstance();
        this.config = OreonClientMod.getConfig();
    }

    /**
     * Open the main Oreon GUI
     */
    public void openMainGui() {
        if (client.player == null) return;
        
        if (mainGui == null) {
            mainGui = new OreonGui();
        }
        client.setScreen(mainGui);
    }

    /**
     * Open the old/legacy Oreon GUI
     */
    public void openOldGui() {
        if (client.player == null) return;
        
        if (oldGui == null) {
            oldGui = new OldOreonGui();
        }
        client.setScreen(oldGui);
    }

    /**
     * Open the settings GUI
     */
    public void openSettingsGui() {
        if (client.player == null) return;
        
        if (settingsGui == null) {
            settingsGui = new OreonSettingsGui();
        }
        client.setScreen(settingsGui);
    }

    /**
     * Open the profile GUI
     */
    public void openProfileGui() {
        if (client.player == null) return;
        
        if (profileGui == null) {
            profileGui = new ProfileGui();
        }
        client.setScreen(profileGui);
    }

    /**
     * Check if any Oreon GUI is currently open
     */
    public boolean isAnyGuiOpen() {
        Screen currentScreen = client.currentScreen;
        return currentScreen instanceof OreonGui ||
               currentScreen instanceof OldOreonGui ||
               currentScreen instanceof OreonSettingsGui ||
               currentScreen instanceof ProfileGui;
    }

    /**
     * Close all Oreon GUIs
     */
    public void closeAllGuis() {
        if (isAnyGuiOpen()) {
            client.setScreen(null);
        }
    }

    /**
     * Get the current active GUI screen
     */
    public Screen getCurrentGui() {
        Screen currentScreen = client.currentScreen;
        if (isAnyGuiOpen()) {
            return currentScreen;
        }
        return null;
    }

    /**
     * Refresh all GUI instances (useful after config changes)
     */
    public void refreshGuis() {
        mainGui = null;
        oldGui = null;
        settingsGui = null;
        profileGui = null;
    }
}
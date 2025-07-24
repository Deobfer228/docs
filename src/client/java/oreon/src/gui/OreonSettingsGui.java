package oreon.src.gui;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import oreon.src.config.OreonConfig;

/**
 * Settings GUI using Cloth Config
 */
public class OreonSettingsGui {

    /**
     * Create and return the settings screen
     */
    public static Screen createSettingsScreen(Screen parent) {
        return AutoConfig.getConfigScreen(OreonConfig.class, parent).get();
    }

    /**
     * Open the settings GUI (legacy method for compatibility)
     */
    public void open() {
        // This will be called from GuiHandler
    }
}
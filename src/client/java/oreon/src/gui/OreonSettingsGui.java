package oreon.src.gui;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import oreon.src.config.OreonConfig;

/**
 * Settings GUI using Cloth Config
 */
public class OreonSettingsGui extends Screen {

    public OreonSettingsGui() {
        super(Text.translatable("gui.oreon.settings.title"));
    }

    /**
     * Create and return the settings screen
     */
    public static Screen createSettingsScreen(Screen parent) {
        return AutoConfig.getConfigScreen(OreonConfig.class, parent).get();
    }

    @Override
    protected void init() {
        // This will be overridden by the auto-config screen
        super.init();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
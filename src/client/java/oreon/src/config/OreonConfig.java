package oreon.src.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

/**
 * Main configuration class for Oreon mod
 * Uses Cloth Config for GUI and auto-config for serialization
 */
@Config(name = "oreon")
public class OreonConfig implements ConfigData {

    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.TransitiveObject
    public GeneralConfig general = new GeneralConfig();

    @ConfigEntry.Category("gui")
    @ConfigEntry.Gui.TransitiveObject
    public GuiConfig gui = new GuiConfig();

    @ConfigEntry.Category("functions")
    @ConfigEntry.Gui.TransitiveObject
    public FunctionsConfig functions = new FunctionsConfig();

    @ConfigEntry.Category("reports")
    @ConfigEntry.Gui.TransitiveObject
    public ReportsConfig reports = new ReportsConfig();

    @ConfigEntry.Category("targeting")
    @ConfigEntry.Gui.TransitiveObject
    public TargetingConfig targeting = new TargetingConfig();

    @ConfigEntry.Category("security")
    @ConfigEntry.Gui.TransitiveObject
    public SecurityConfig security = new SecurityConfig();

    @ConfigEntry.Category("discord")
    @ConfigEntry.Gui.TransitiveObject
    public DiscordConfig discord = new DiscordConfig();

    public static class GeneralConfig {
        @ConfigEntry.Gui.Tooltip
        public boolean enabled = true;

        @ConfigEntry.Gui.Tooltip
        public boolean debugMode = false;

        @ConfigEntry.Gui.Tooltip
        public String language = "en_us";

        @ConfigEntry.Gui.Tooltip
        public boolean autoUpdate = true;

        @ConfigEntry.Gui.Tooltip
        public boolean sendAnalytics = false;
    }

    public static class GuiConfig {
        @ConfigEntry.Gui.Tooltip
        public boolean showHud = true;

        @ConfigEntry.Gui.Tooltip
        public boolean showCheckHud = true;

        @ConfigEntry.Gui.Tooltip
        public boolean showReportHud = true;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 50, max = 200)
        public int hudScale = 100;

        @ConfigEntry.Gui.Tooltip
        public int hudX = 10;

        @ConfigEntry.Gui.Tooltip
        public int hudY = 10;

        @ConfigEntry.Gui.Tooltip
        public boolean showWatermark = true;
    }

    public static class FunctionsConfig {
        @ConfigEntry.Gui.Tooltip
        public boolean fullbrightEnabled = false;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 15)
        public int fullbrightLevel = 15;

        @ConfigEntry.Gui.Tooltip
        public boolean hidePlayersEnabled = false;

        @ConfigEntry.Gui.Tooltip
        public boolean hideNameTags = false;

        @ConfigEntry.Gui.Tooltip
        public boolean hideOwnPlayer = false;
    }

    public static class ReportsConfig {
        @ConfigEntry.Gui.Tooltip
        public boolean autoSaveReports = true;

        @ConfigEntry.Gui.Tooltip
        public String defaultTemplate = "standard";

        @ConfigEntry.Gui.Tooltip
        public boolean enableReportHistory = true;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 10, max = 1000)
        public int maxReportHistory = 100;

        @ConfigEntry.Gui.Tooltip
        public boolean autoSubmitReports = false;

        @ConfigEntry.Gui.Tooltip
        public String reportServer = "";
    }

    public static class TargetingConfig {
        @ConfigEntry.Gui.Tooltip
        public boolean enableTargetMenu = true;

        @ConfigEntry.Gui.Tooltip
        public boolean showHitboxes = false;

        @ConfigEntry.Gui.Tooltip
        public boolean autoSelectTarget = false;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
        public double targetRange = 50.0;

        @ConfigEntry.Gui.Tooltip
        public boolean highlightTarget = true;
    }

    public static class SecurityConfig {
        @ConfigEntry.Gui.Tooltip
        public boolean enableHwidCheck = true;

        @ConfigEntry.Gui.Tooltip
        public boolean enableVersionCheck = true;

        @ConfigEntry.Gui.Tooltip
        public boolean strictMode = false;

        @ConfigEntry.Gui.Tooltip
        public String hwidSalt = "";

        @ConfigEntry.Gui.Tooltip
        public boolean encryptConfig = false;
    }

    public static class DiscordConfig {
        @ConfigEntry.Gui.Tooltip
        public boolean enableRichPresence = true;

        @ConfigEntry.Gui.Tooltip
        public boolean showServerInfo = true;

        @ConfigEntry.Gui.Tooltip
        public boolean showPlayerCount = true;

        @ConfigEntry.Gui.Tooltip
        public String customStatus = "";

        @ConfigEntry.Gui.Tooltip
        public boolean showActiveFunction = true;
    }

    /**
     * Initialize the configuration system
     */
    public static OreonConfig initialize() {
        AutoConfig.register(OreonConfig.class, GsonConfigSerializer::new);
        return AutoConfig.getConfigHolder(OreonConfig.class).getConfig();
    }

    /**
     * Save the current configuration
     */
    public void save() {
        AutoConfig.getConfigHolder(OreonConfig.class).save();
    }

    /**
     * Get the configuration holder for GUI integration
     */
    public static me.shedaniel.autoconfig.ConfigHolder<OreonConfig> getConfigHolder() {
        return AutoConfig.getConfigHolder(OreonConfig.class);
    }
}
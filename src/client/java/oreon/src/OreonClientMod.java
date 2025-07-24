package oreon.src;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import oreon.src.config.OreonConfig;
import oreon.src.gui.GuiHandler;
import oreon.src.huds.OreonHudOverlay;
import oreon.src.huds.CheckHudOverlay;
import oreon.src.huds.ReportHud;
import oreon.src.functions.FullbrightHandler;
import oreon.src.functions.HidePlayersHandler;
import oreon.src.util.ChatListener;
import oreon.src.util.KeyInputHandler;
import oreon.src.util.VersionChecker;
import oreon.src.hwid.HwidChecker;
import oreon.src.drpc.DiscordUtility;
import oreon.src.revise.ChatSendListener;
import oreon.src.targetbanspec.ClientEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main client mod initializer for Oreon
 * Handles all client-side initialization and setup
 */
public class OreonClientMod implements ClientModInitializer {
    public static final String MOD_ID = "oreon";
    public static final String MOD_NAME = "Oreon";
    public static final String VERSION = "2.0.0";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    // Core systems
    private static OreonConfig config;
    private static GuiHandler guiHandler;
    private static FullbrightHandler fullbrightHandler;
    private static HidePlayersHandler hidePlayersHandler;
    private static ChatListener chatListener;
    private static KeyInputHandler keyInputHandler;
    private static VersionChecker versionChecker;
    private static HwidChecker hwidChecker;
    private static DiscordUtility discordUtility;
    private static ChatSendListener chatSendListener;
    private static ClientEvents clientEvents;

    // HUD overlays
    private static OreonHudOverlay hudOverlay;
    private static CheckHudOverlay checkHudOverlay;
    private static ReportHud reportHud;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing {} v{}", MOD_NAME, VERSION);

        try {
            // Initialize configuration first
            config = new OreonConfig();
            LOGGER.info("Configuration initialized");

            // Initialize key bindings
            KeyBinds.init();
            LOGGER.info("Key bindings registered");

            // Initialize GUI handler
            guiHandler = new GuiHandler();
            LOGGER.info("GUI handler initialized");

            // Initialize core function handlers
            fullbrightHandler = new FullbrightHandler();
            hidePlayersHandler = new HidePlayersHandler();
            LOGGER.info("Function handlers initialized");

            // Initialize utility systems
            chatListener = new ChatListener();
            keyInputHandler = new KeyInputHandler();
            LOGGER.info("Utility systems initialized");

            // Initialize HUD overlays
            hudOverlay = new OreonHudOverlay();
            checkHudOverlay = new CheckHudOverlay();
            reportHud = new ReportHud();
            LOGGER.info("HUD overlays initialized");

            // Initialize security systems
            versionChecker = new VersionChecker();
            hwidChecker = new HwidChecker();
            LOGGER.info("Security systems initialized");

            // Initialize Discord integration
            discordUtility = new DiscordUtility();
            LOGGER.info("Discord integration initialized");

            // Initialize chat and client event listeners
            chatSendListener = new ChatSendListener();
            clientEvents = new ClientEvents();
            LOGGER.info("Event listeners initialized");

            // Register client tick events
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                keyInputHandler.onClientTick(client);
                fullbrightHandler.onClientTick(client);
                hidePlayersHandler.onClientTick(client);
            });

            LOGGER.info("{} v{} successfully initialized!", MOD_NAME, VERSION);

        } catch (Exception e) {
            LOGGER.error("Failed to initialize {}: {}", MOD_NAME, e.getMessage(), e);
        }
    }

    // Getters for accessing initialized systems
    public static OreonConfig getConfig() {
        return config;
    }

    public static GuiHandler getGuiHandler() {
        return guiHandler;
    }

    public static FullbrightHandler getFullbrightHandler() {
        return fullbrightHandler;
    }

    public static HidePlayersHandler getHidePlayersHandler() {
        return hidePlayersHandler;
    }

    public static ChatListener getChatListener() {
        return chatListener;
    }

    public static KeyInputHandler getKeyInputHandler() {
        return keyInputHandler;
    }

    public static VersionChecker getVersionChecker() {
        return versionChecker;
    }

    public static HwidChecker getHwidChecker() {
        return hwidChecker;
    }

    public static DiscordUtility getDiscordUtility() {
        return discordUtility;
    }

    public static OreonHudOverlay getHudOverlay() {
        return hudOverlay;
    }

    public static CheckHudOverlay getCheckHudOverlay() {
        return checkHudOverlay;
    }

    public static ReportHud getReportHud() {
        return reportHud;
    }
}
package oreon.src.util;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import oreon.src.OreonClientMod;

/**
 * Handles incoming chat messages and processes them
 */
public class ChatListener {
    
    private final MinecraftClient client;

    public ChatListener() {
        this.client = MinecraftClient.getInstance();
        registerEvents();
    }

    private void registerEvents() {
        // Register chat message event listener
        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            onChatMessage(message);
        });
    }

    /**
     * Process incoming chat message
     */
    private void onChatMessage(Text message) {
        String messageText = message.getString();
        
        // Log chat messages if debug mode is enabled
        if (OreonClientMod.getConfig() != null && OreonClientMod.getConfig().general.debugMode) {
            OreonClientMod.LOGGER.debug("Chat: {}", messageText);
        }
        
        // Process special commands or notifications
        processMessage(messageText);
    }

    /**
     * Process message for special patterns or commands
     */
    private void processMessage(String message) {
        // TODO: Add pattern matching for reports, alerts, etc.
        
        // Example: Check for admin notifications
        if (message.contains("Admin") || message.contains("Moderator")) {
            // Handle admin messages
            handleAdminMessage(message);
        }
        
        // Example: Check for player reports
        if (message.contains("reported") || message.contains("Report")) {
            // Handle report notifications
            handleReportMessage(message);
        }
    }

    /**
     * Handle admin/moderator messages
     */
    private void handleAdminMessage(String message) {
        OreonClientMod.LOGGER.info("Admin message detected: {}", message);
        // TODO: Implement admin message handling
    }

    /**
     * Handle report-related messages
     */
    private void handleReportMessage(String message) {
        OreonClientMod.LOGGER.info("Report message detected: {}", message);
        // TODO: Implement report message handling
    }

    /**
     * Send a chat message
     */
    public void sendMessage(String message) {
        if (client.player != null) {
            client.player.networkHandler.sendChatMessage(message);
        }
    }

    /**
     * Send a command
     */
    public void sendCommand(String command) {
        if (client.player != null) {
            // Ensure command starts with /
            if (!command.startsWith("/")) {
                command = "/" + command;
            }
            client.player.networkHandler.sendChatCommand(command.substring(1));
        }
    }
}
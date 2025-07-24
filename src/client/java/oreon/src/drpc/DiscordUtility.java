package oreon.src.drpc;

import oreon.src.OreonClientMod;

/**
 * Discord Rich Presence utility
 */
public class DiscordUtility {

    public DiscordUtility() {
        // Initialize Discord RPC
        OreonClientMod.LOGGER.info("Discord RPC initialized");
    }

    public void updatePresence(String details, String state) {
        // TODO: Implement Discord RPC update
        OreonClientMod.LOGGER.debug("Discord presence: {} - {}", details, state);
    }
}
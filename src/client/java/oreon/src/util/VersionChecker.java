package oreon.src.util;

import oreon.src.OreonClientMod;

/**
 * Version checking utility
 */
public class VersionChecker {
    
    private static final String CURRENT_VERSION = OreonClientMod.VERSION;

    public VersionChecker() {
        // Initialize version checker
    }

    public void checkVersion() {
        // TODO: Implement version checking
        OreonClientMod.LOGGER.info("Version check: {}", CURRENT_VERSION);
    }

    public String getCurrentVersion() {
        return CURRENT_VERSION;
    }
}
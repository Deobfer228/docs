package oreon.src.otchet;

import oreon.src.OreonClientMod;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Manages banspecs - predefined ban reasons and specifications
 */
public class BanspecManager {
    
    private static final String BANSPECS_DIR = "oreon/banspecs";
    private static final String BANSPECS_FILE = "banspecs.txt";
    
    private final Map<String, BanspecInfo> banspecs;
    private final List<String> categories;

    public BanspecManager() {
        this.banspecs = new HashMap<>();
        this.categories = new ArrayList<>();
        
        initializeDirectories();
        loadBanspecs();
        loadCategories();
    }

    /**
     * Initialize required directories
     */
    private void initializeDirectories() {
        try {
            Files.createDirectories(Paths.get(BANSPECS_DIR));
            OreonClientMod.LOGGER.info("Initialized banspec directories");
        } catch (IOException e) {
            OreonClientMod.LOGGER.error("Failed to create banspec directories", e);
        }
    }

    /**
     * Load banspecs from file
     */
    private void loadBanspecs() {
        Path banspecsPath = Paths.get(BANSPECS_DIR, BANSPECS_FILE);
        
        if (!Files.exists(banspecsPath)) {
            createDefaultBanspecs();
            saveBanspecs();
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(banspecsPath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue; // Skip comments and empty lines
                }
                
                BanspecInfo banspec = parseBanspecLine(line);
                if (banspec != null) {
                    banspecs.put(banspec.id, banspec);
                }
            }
            
            OreonClientMod.LOGGER.info("Loaded {} banspecs", banspecs.size());
            
        } catch (IOException e) {
            OreonClientMod.LOGGER.error("Failed to load banspecs", e);
            createDefaultBanspecs();
        }
    }

    /**
     * Parse a banspec line from file
     */
    private BanspecInfo parseBanspecLine(String line) {
        try {
            // Format: ID|Category|Reason|Duration|Evidence_Required
            String[] parts = line.split("\\|");
            if (parts.length >= 4) {
                BanspecInfo banspec = new BanspecInfo();
                banspec.id = parts[0].trim();
                banspec.category = parts[1].trim();
                banspec.reason = parts[2].trim();
                banspec.duration = parts[3].trim();
                banspec.evidenceRequired = parts.length > 4 ? Boolean.parseBoolean(parts[4].trim()) : true;
                return banspec;
            }
        } catch (Exception e) {
            OreonClientMod.LOGGER.warn("Failed to parse banspec line: {}", line);
        }
        return null;
    }

    /**
     * Create default banspecs
     */
    private void createDefaultBanspecs() {
        // Cheating banspecs
        addBanspec("KILL_AURA", "Cheating", "Using Kill Aura", "30d", true);
        addBanspec("FLY_HACK", "Cheating", "Using Fly Hack", "14d", true);
        addBanspec("SPEED_HACK", "Cheating", "Using Speed Hack", "14d", true);
        addBanspec("XRAY", "Cheating", "Using X-Ray", "30d", true);
        addBanspec("REACH", "Cheating", "Using Reach Hack", "14d", true);
        addBanspec("AUTO_CLICK", "Cheating", "Using Auto Clicker", "7d", true);
        addBanspec("BHOP", "Cheating", "Using Bunny Hop", "7d", true);
        addBanspec("SCAFFOLD", "Cheating", "Using Scaffold", "14d", true);
        addBanspec("ANTI_KB", "Cheating", "Using Anti-Knockback", "14d", true);
        addBanspec("NO_FALL", "Cheating", "Using No Fall Damage", "7d", true);

        // Griefing banspecs
        addBanspec("GRIEF_MAJOR", "Griefing", "Major Griefing", "30d", true);
        addBanspec("GRIEF_MINOR", "Griefing", "Minor Griefing", "7d", true);
        addBanspec("SPAWN_GRIEF", "Griefing", "Spawn Area Griefing", "14d", true);
        addBanspec("LAVA_GRIEF", "Griefing", "Lava/Water Griefing", "14d", true);

        // Chat violations
        addBanspec("SPAM", "Chat", "Spamming Chat", "3d", false);
        addBanspec("ADVERTISING", "Chat", "Advertising", "7d", false);
        addBanspec("INAPPROPRIATE", "Chat", "Inappropriate Language", "3d", false);
        addBanspec("HARASSMENT", "Chat", "Harassment", "14d", true);
        addBanspec("DOXXING", "Chat", "Doxxing/Personal Info", "permanent", true);

        // Exploit banspecs
        addBanspec("DUPE_EXPLOIT", "Exploiting", "Duplication Exploit", "30d", true);
        addBanspec("ITEM_EXPLOIT", "Exploiting", "Item Exploit", "14d", true);
        addBanspec("MAP_EXPLOIT", "Exploiting", "Map Exploit", "7d", true);

        // Other violations
        addBanspec("ALT_ACCOUNT", "Other", "Alt Account Abuse", "14d", false);
        addBanspec("EVADING", "Other", "Ban Evading", "permanent", true);
        addBanspec("STAFF_DISRESPECT", "Other", "Staff Disrespect", "7d", false);
    }

    /**
     * Add a banspec
     */
    public void addBanspec(String id, String category, String reason, String duration, boolean evidenceRequired) {
        BanspecInfo banspec = new BanspecInfo();
        banspec.id = id;
        banspec.category = category;
        banspec.reason = reason;
        banspec.duration = duration;
        banspec.evidenceRequired = evidenceRequired;
        
        banspecs.put(id, banspec);
        
        // Add category if not exists
        if (!categories.contains(category)) {
            categories.add(category);
        }
    }

    /**
     * Remove a banspec
     */
    public boolean removeBanspec(String id) {
        return banspecs.remove(id) != null;
    }

    /**
     * Get banspec by ID
     */
    public BanspecInfo getBanspec(String id) {
        return banspecs.get(id);
    }

    /**
     * Get all banspecs
     */
    public Collection<BanspecInfo> getAllBanspecs() {
        return banspecs.values();
    }

    /**
     * Get banspecs by category
     */
    public List<BanspecInfo> getBanspecsByCategory(String category) {
        return banspecs.values().stream()
                .filter(banspec -> banspec.category.equals(category))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Get all categories
     */
    public List<String> getCategories() {
        return new ArrayList<>(categories);
    }

    /**
     * Search banspecs by reason
     */
    public List<BanspecInfo> searchBanspecs(String query) {
        String lowerQuery = query.toLowerCase();
        return banspecs.values().stream()
                .filter(banspec -> 
                    banspec.reason.toLowerCase().contains(lowerQuery) ||
                    banspec.id.toLowerCase().contains(lowerQuery) ||
                    banspec.category.toLowerCase().contains(lowerQuery))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Save banspecs to file
     */
    public void saveBanspecs() {
        Path banspecsPath = Paths.get(BANSPECS_DIR, BANSPECS_FILE);
        
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(banspecsPath))) {
            writer.println("# Oreon Banspecs Configuration");
            writer.println("# Format: ID|Category|Reason|Duration|Evidence_Required");
            writer.println("# Duration examples: 1d, 7d, 30d, permanent");
            writer.println();
            
            // Group by category
            for (String category : categories) {
                writer.println("# " + category);
                
                List<BanspecInfo> categoryBanspecs = getBanspecsByCategory(category);
                for (BanspecInfo banspec : categoryBanspecs) {
                    writer.printf("%s|%s|%s|%s|%s%n",
                                banspec.id,
                                banspec.category,
                                banspec.reason,
                                banspec.duration,
                                banspec.evidenceRequired);
                }
                writer.println();
            }
            
            OreonClientMod.LOGGER.info("Saved {} banspecs to file", banspecs.size());
            
        } catch (IOException e) {
            OreonClientMod.LOGGER.error("Failed to save banspecs", e);
        }
    }

    /**
     * Load categories
     */
    private void loadCategories() {
        categories.clear();
        
        // Extract unique categories from banspecs
        Set<String> uniqueCategories = new HashSet<>();
        for (BanspecInfo banspec : banspecs.values()) {
            uniqueCategories.add(banspec.category);
        }
        
        categories.addAll(uniqueCategories);
        Collections.sort(categories);
    }

    /**
     * Generate ban command for banspec
     */
    public String generateBanCommand(String banspecId, String playerName, String evidence) {
        BanspecInfo banspec = getBanspec(banspecId);
        if (banspec == null) {
            return null;
        }

        StringBuilder command = new StringBuilder();
        command.append("/ban ").append(playerName);
        command.append(" ").append(banspec.duration);
        command.append(" ").append(banspec.reason);
        
        if (banspec.evidenceRequired && evidence != null && !evidence.isEmpty()) {
            command.append(" | Evidence: ").append(evidence);
        }

        return command.toString();
    }

    /**
     * Generate tempban command for banspec
     */
    public String generateTempbanCommand(String banspecId, String playerName, String customDuration, String evidence) {
        BanspecInfo banspec = getBanspec(banspecId);
        if (banspec == null) {
            return null;
        }

        String duration = customDuration != null && !customDuration.isEmpty() ? customDuration : banspec.duration;

        StringBuilder command = new StringBuilder();
        command.append("/tempban ").append(playerName);
        command.append(" ").append(duration);
        command.append(" ").append(banspec.reason);
        
        if (banspec.evidenceRequired && evidence != null && !evidence.isEmpty()) {
            command.append(" | Evidence: ").append(evidence);
        }

        return command.toString();
    }

    /**
     * Banspec information class
     */
    public static class BanspecInfo {
        public String id;
        public String category;
        public String reason;
        public String duration;
        public boolean evidenceRequired;

        public BanspecInfo() {}

        public BanspecInfo(String id, String category, String reason, String duration, boolean evidenceRequired) {
            this.id = id;
            this.category = category;
            this.reason = reason;
            this.duration = duration;
            this.evidenceRequired = evidenceRequired;
        }

        @Override
        public String toString() {
            return String.format("%s (%s) - %s [%s]", reason, category, duration, 
                               evidenceRequired ? "Evidence Required" : "No Evidence Required");
        }
    }
}
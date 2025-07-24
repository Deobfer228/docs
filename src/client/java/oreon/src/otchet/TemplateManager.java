package oreon.src.otchet;

import oreon.src.OreonClientMod;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Manages report templates and custom text templates
 */
public class TemplateManager {
    
    private static final String TEMPLATES_DIR = "oreon/templates";
    
    private final Map<String, TemplateInfo> templates;
    private final List<String> categories;

    public TemplateManager() {
        this.templates = new HashMap<>();
        this.categories = new ArrayList<>();
        
        initializeDirectories();
        loadTemplates();
    }

    /**
     * Initialize required directories
     */
    private void initializeDirectories() {
        try {
            Files.createDirectories(Paths.get(TEMPLATES_DIR));
            OreonClientMod.LOGGER.info("Initialized template directories");
        } catch (IOException e) {
            OreonClientMod.LOGGER.error("Failed to create template directories", e);
        }
    }

    /**
     * Load templates from files
     */
    private void loadTemplates() {
        try {
            Path templatesPath = Paths.get(TEMPLATES_DIR);
            if (Files.exists(templatesPath)) {
                Files.list(templatesPath)
                     .filter(path -> path.toString().endsWith(".template"))
                     .forEach(this::loadTemplateFile);
            }
            
            // Create default templates if none exist
            if (templates.isEmpty()) {
                createDefaultTemplates();
            }
            
            loadCategories();
            
        } catch (IOException e) {
            OreonClientMod.LOGGER.error("Failed to load templates", e);
            createDefaultTemplates();
        }
    }

    /**
     * Load a single template file
     */
    private void loadTemplateFile(Path templatePath) {
        try {
            List<String> lines = Files.readAllLines(templatePath);
            
            String fileName = templatePath.getFileName().toString();
            String templateId = fileName.substring(0, fileName.lastIndexOf('.'));
            
            TemplateInfo template = new TemplateInfo();
            template.id = templateId;
            template.name = templateId; // Default name
            template.category = "Custom"; // Default category
            
            StringBuilder contentBuilder = new StringBuilder();
            boolean inHeader = true;
            
            for (String line : lines) {
                if (inHeader && line.startsWith("#")) {
                    // Parse header information
                    if (line.startsWith("# Name: ")) {
                        template.name = line.substring(8).trim();
                    } else if (line.startsWith("# Category: ")) {
                        template.category = line.substring(12).trim();
                    } else if (line.startsWith("# Description: ")) {
                        template.description = line.substring(15).trim();
                    }
                } else {
                    if (inHeader && !line.trim().isEmpty() && !line.startsWith("#")) {
                        inHeader = false;
                    }
                    if (!inHeader) {
                        contentBuilder.append(line).append("\n");
                    }
                }
            }
            
            template.content = contentBuilder.toString().trim();
            templates.put(template.id, template);
            
        } catch (IOException e) {
            OreonClientMod.LOGGER.error("Failed to load template: {}", templatePath, e);
        }
    }

    /**
     * Create default templates
     */
    private void createDefaultTemplates() {
        // Report templates
        addTemplate("cheating_report", "Cheating Report", "Reports", 
                   "Player {player} was caught cheating on {date} at {time}.\n\n" +
                   "Cheat Type: {cheat_type}\n" +
                   "Evidence: {evidence}\n" +
                   "Location: {location}\n" +
                   "Server: {server}\n\n" +
                   "Additional Details:\n{details}\n\n" +
                   "Reported by: {reporter}",
                   "Template for reporting cheating violations");

        addTemplate("griefing_report", "Griefing Report", "Reports",
                   "Player {player} was caught griefing on {date} at {time}.\n\n" +
                   "Damage Type: {damage_type}\n" +
                   "Location: {location}\n" +
                   "Estimated Damage: {damage_amount}\n" +
                   "Screenshots: {screenshots}\n\n" +
                   "Description:\n{description}\n\n" +
                   "Reported by: {reporter}",
                   "Template for reporting griefing violations");

        addTemplate("chat_violation", "Chat Violation Report", "Reports",
                   "Player {player} violated chat rules on {date} at {time}.\n\n" +
                   "Violation Type: {violation_type}\n" +
                   "Messages:\n{messages}\n\n" +
                   "Context:\n{context}\n\n" +
                   "Reported by: {reporter}",
                   "Template for reporting chat violations");

        // Verdict templates
        addTemplate("clean_verdict", "Clean Verdict", "Verdicts",
                   "=== CLEAN VERDICT ===\n" +
                   "Player: {player}\n" +
                   "Checked by: {moderator}\n" +
                   "Date: {date}\n" +
                   "Time: {time}\n\n" +
                   "RESULT: CLEAN\n" +
                   "No suspicious activity detected.\n" +
                   "Player appears to be legitimate.\n\n" +
                   "Notes: {notes}\n" +
                   "==================",
                   "Template for clean verdicts");

        addTemplate("suspicious_verdict", "Suspicious Verdict", "Verdicts",
                   "=== SUSPICIOUS VERDICT ===\n" +
                   "Player: {player}\n" +
                   "Checked by: {moderator}\n" +
                   "Date: {date}\n" +
                   "Time: {time}\n\n" +
                   "RESULT: SUSPICIOUS\n" +
                   "Reason: {reason}\n" +
                   "Evidence: {evidence}\n\n" +
                   "Recommendation: {recommendation}\n" +
                   "Additional Notes: {notes}\n" +
                   "=======================",
                   "Template for suspicious verdicts");

        // Ban templates
        addTemplate("ban_appeal_response", "Ban Appeal Response", "Bans",
                   "Dear {player},\n\n" +
                   "Your ban appeal has been {decision}.\n\n" +
                   "Original Ban:\n" +
                   "Reason: {ban_reason}\n" +
                   "Date: {ban_date}\n" +
                   "Staff: {ban_staff}\n\n" +
                   "Appeal Decision: {decision_reason}\n\n" +
                   "{additional_info}\n\n" +
                   "Regards,\n{staff_name}",
                   "Template for ban appeal responses");

        // Custom commands
        addTemplate("welcome_message", "Welcome Message", "Commands",
                   "Welcome to the server, {player}!\n\n" +
                   "Please read our rules at /rules\n" +
                   "Need help? Use /help or ask staff\n" +
                   "Have fun and play fair!",
                   "Template for welcome messages");

        saveAllTemplates();
    }

    /**
     * Add a template
     */
    public void addTemplate(String id, String name, String category, String content, String description) {
        TemplateInfo template = new TemplateInfo();
        template.id = id;
        template.name = name;
        template.category = category;
        template.content = content;
        template.description = description;
        
        templates.put(id, template);
        
        // Add category if not exists
        if (!categories.contains(category)) {
            categories.add(category);
            Collections.sort(categories);
        }
    }

    /**
     * Remove a template
     */
    public boolean removeTemplate(String id) {
        TemplateInfo removed = templates.remove(id);
        if (removed != null) {
            // Remove template file
            try {
                Path templatePath = Paths.get(TEMPLATES_DIR, id + ".template");
                Files.deleteIfExists(templatePath);
                return true;
            } catch (IOException e) {
                OreonClientMod.LOGGER.error("Failed to delete template file: {}", id, e);
            }
        }
        return false;
    }

    /**
     * Get template by ID
     */
    public TemplateInfo getTemplate(String id) {
        return templates.get(id);
    }

    /**
     * Get all templates
     */
    public Collection<TemplateInfo> getAllTemplates() {
        return templates.values();
    }

    /**
     * Get templates by category
     */
    public List<TemplateInfo> getTemplatesByCategory(String category) {
        return templates.values().stream()
                .filter(template -> template.category.equals(category))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Get all categories
     */
    public List<String> getCategories() {
        return new ArrayList<>(categories);
    }

    /**
     * Search templates
     */
    public List<TemplateInfo> searchTemplates(String query) {
        String lowerQuery = query.toLowerCase();
        return templates.values().stream()
                .filter(template ->
                    template.name.toLowerCase().contains(lowerQuery) ||
                    template.content.toLowerCase().contains(lowerQuery) ||
                    template.category.toLowerCase().contains(lowerQuery) ||
                    (template.description != null && template.description.toLowerCase().contains(lowerQuery)))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Process template with variables
     */
    public String processTemplate(String templateId, Map<String, String> variables) {
        TemplateInfo template = getTemplate(templateId);
        if (template == null) {
            return null;
        }

        String content = template.content;
        
        // Replace variables
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            String placeholder = "{" + entry.getKey() + "}";
            content = content.replace(placeholder, entry.getValue());
        }

        // Replace common variables
        content = content.replace("{date}", new java.text.SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        content = content.replace("{time}", new java.text.SimpleDateFormat("HH:mm:ss").format(new Date()));

        return content;
    }

    /**
     * Save template to file
     */
    public void saveTemplate(TemplateInfo template) {
        Path templatePath = Paths.get(TEMPLATES_DIR, template.id + ".template");
        
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(templatePath))) {
            // Write header
            writer.println("# Name: " + template.name);
            writer.println("# Category: " + template.category);
            if (template.description != null && !template.description.isEmpty()) {
                writer.println("# Description: " + template.description);
            }
            writer.println();
            
            // Write content
            writer.print(template.content);
            
        } catch (IOException e) {
            OreonClientMod.LOGGER.error("Failed to save template: {}", template.id, e);
        }
    }

    /**
     * Save all templates
     */
    public void saveAllTemplates() {
        for (TemplateInfo template : templates.values()) {
            saveTemplate(template);
        }
        OreonClientMod.LOGGER.info("Saved {} templates", templates.size());
    }

    /**
     * Load categories from templates
     */
    private void loadCategories() {
        categories.clear();
        
        Set<String> uniqueCategories = new HashSet<>();
        for (TemplateInfo template : templates.values()) {
            uniqueCategories.add(template.category);
        }
        
        categories.addAll(uniqueCategories);
        Collections.sort(categories);
    }

    /**
     * Get template variables (placeholders) from content
     */
    public List<String> getTemplateVariables(String templateId) {
        TemplateInfo template = getTemplate(templateId);
        if (template == null) {
            return new ArrayList<>();
        }

        List<String> variables = new ArrayList<>();
        String content = template.content;
        
        int start = 0;
        while ((start = content.indexOf('{', start)) != -1) {
            int end = content.indexOf('}', start);
            if (end != -1) {
                String variable = content.substring(start + 1, end);
                if (!variables.contains(variable)) {
                    variables.add(variable);
                }
                start = end + 1;
            } else {
                break;
            }
        }
        
        return variables;
    }

    /**
     * Template information class
     */
    public static class TemplateInfo {
        public String id;
        public String name;
        public String category;
        public String content;
        public String description;

        public TemplateInfo() {}

        public TemplateInfo(String id, String name, String category, String content, String description) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.content = content;
            this.description = description;
        }

        @Override
        public String toString() {
            return name + " (" + category + ")";
        }
    }
}
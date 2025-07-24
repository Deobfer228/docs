package oreon.src.otchet;

import oreon.src.OreonClientMod;
import oreon.src.util.ReportInfo;
import oreon.src.util.DelayedReport;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages player reports - creation, storage, and submission
 */
public class ReportManager {
    
    private static final String REPORTS_DIR = "oreon/reports";
    private static final String TEMPLATES_DIR = "oreon/templates";
    
    private final Map<String, ReportInfo> activeReports;
    private final List<ReportInfo> reportHistory;
    private final Queue<DelayedReport> delayedReports;
    private final Map<String, String> reportTemplates;

    public ReportManager() {
        this.activeReports = new ConcurrentHashMap<>();
        this.reportHistory = new ArrayList<>();
        this.delayedReports = new LinkedList<>();
        this.reportTemplates = new HashMap<>();
        
        initializeDirectories();
        loadReportTemplates();
        loadReportHistory();
    }

    /**
     * Initialize required directories
     */
    private void initializeDirectories() {
        try {
            Files.createDirectories(Paths.get(REPORTS_DIR));
            Files.createDirectories(Paths.get(TEMPLATES_DIR));
            OreonClientMod.LOGGER.info("Initialized report directories");
        } catch (IOException e) {
            OreonClientMod.LOGGER.error("Failed to create report directories", e);
        }
    }

    /**
     * Create a new report
     */
    public ReportInfo createReport(String playerName, String reason, String description) {
        ReportInfo report = new ReportInfo(playerName, reason, description);
        activeReports.put(playerName.toLowerCase(), report);
        
        OreonClientMod.LOGGER.info("Created new report for player: {}", playerName);
        return report;
    }

    /**
     * Submit a report
     */
    public boolean submitReport(ReportInfo report) {
        try {
            // Save report to file
            saveReportToFile(report);
            
            // Add to history
            reportHistory.add(report);
            
            // Remove from active reports
            activeReports.remove(report.getPlayerName().toLowerCase());
            
            OreonClientMod.LOGGER.info("Submitted report for player: {}", report.getPlayerName());
            return true;
            
        } catch (Exception e) {
            OreonClientMod.LOGGER.error("Failed to submit report for player: {}", report.getPlayerName(), e);
            return false;
        }
    }

    /**
     * Schedule a delayed report
     */
    public void scheduleDelayedReport(ReportInfo report, long delayMs) {
        DelayedReport delayedReport = new DelayedReport(report, System.currentTimeMillis() + delayMs);
        delayedReports.offer(delayedReport);
        
        OreonClientMod.LOGGER.info("Scheduled delayed report for player: {} (delay: {}ms)", 
                                  report.getPlayerName(), delayMs);
    }

    /**
     * Process delayed reports (call this periodically)
     */
    public void processDelayedReports() {
        long currentTime = System.currentTimeMillis();
        
        while (!delayedReports.isEmpty()) {
            DelayedReport delayedReport = delayedReports.peek();
            if (delayedReport.getDelayTime() <= currentTime) {
                delayedReports.poll();
                submitReport(delayedReport.getReportInfo());
            } else {
                break; // Queue is ordered by time
            }
        }
    }

    /**
     * Get active report for a player
     */
    public ReportInfo getActiveReport(String playerName) {
        return activeReports.get(playerName.toLowerCase());
    }

    /**
     * Get all active reports
     */
    public Collection<ReportInfo> getActiveReports() {
        return activeReports.values();
    }

    /**
     * Get report history
     */
    public List<ReportInfo> getReportHistory() {
        return new ArrayList<>(reportHistory);
    }

    /**
     * Get report history for specific player
     */
    public List<ReportInfo> getReportHistory(String playerName) {
        return reportHistory.stream()
                .filter(report -> report.getPlayerName().equalsIgnoreCase(playerName))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Delete a report
     */
    public boolean deleteReport(String playerName) {
        ReportInfo removed = activeReports.remove(playerName.toLowerCase());
        if (removed != null) {
            OreonClientMod.LOGGER.info("Deleted report for player: {}", playerName);
            return true;
        }
        return false;
    }

    /**
     * Clear all active reports
     */
    public void clearActiveReports() {
        activeReports.clear();
        OreonClientMod.LOGGER.info("Cleared all active reports");
    }

    /**
     * Save report to file
     */
    private void saveReportToFile(ReportInfo report) throws IOException {
        String fileName = String.format("report_%s_%d.txt", 
                                       report.getPlayerName().toLowerCase(),
                                       report.getTimestamp());
        Path filePath = Paths.get(REPORTS_DIR, fileName);
        
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(filePath))) {
            writer.println("=== OREON REPORT ===");
            writer.println("Player: " + report.getPlayerName());
            writer.println("Reason: " + report.getReason());
            writer.println("Description: " + report.getDescription());
            writer.println("Timestamp: " + new Date(report.getTimestamp()));
            writer.println("Generated by: Oreon v" + OreonClientMod.VERSION);
            writer.println("===================");
        }
    }

    /**
     * Load report templates
     */
    private void loadReportTemplates() {
        try {
            Path templatesPath = Paths.get(TEMPLATES_DIR);
            if (Files.exists(templatesPath)) {
                Files.list(templatesPath)
                     .filter(path -> path.toString().endsWith(".txt"))
                     .forEach(this::loadTemplate);
            }
            
            // Create default templates if none exist
            if (reportTemplates.isEmpty()) {
                createDefaultTemplates();
            }
            
        } catch (IOException e) {
            OreonClientMod.LOGGER.error("Failed to load report templates", e);
            createDefaultTemplates();
        }
    }

    /**
     * Load a single template file
     */
    private void loadTemplate(Path templatePath) {
        try {
            String fileName = templatePath.getFileName().toString();
            String templateName = fileName.substring(0, fileName.lastIndexOf('.'));
            String content = Files.readString(templatePath);
            reportTemplates.put(templateName, content);
            
        } catch (IOException e) {
            OreonClientMod.LOGGER.error("Failed to load template: {}", templatePath, e);
        }
    }

    /**
     * Create default report templates
     */
    private void createDefaultTemplates() {
        reportTemplates.put("cheating", "Player {player} was caught cheating.\nEvidence: {evidence}\nAction taken: {action}");
        reportTemplates.put("griefing", "Player {player} was griefing.\nDamage: {damage}\nLocation: {location}");
        reportTemplates.put("inappropriate_chat", "Player {player} used inappropriate language.\nMessage: {message}");
        reportTemplates.put("spam", "Player {player} was spamming chat.\nMessages: {messages}");
        
        // Save default templates to files
        saveDefaultTemplates();
    }

    /**
     * Save default templates to files
     */
    private void saveDefaultTemplates() {
        for (Map.Entry<String, String> entry : reportTemplates.entrySet()) {
            try {
                Path templatePath = Paths.get(TEMPLATES_DIR, entry.getKey() + ".txt");
                Files.writeString(templatePath, entry.getValue());
            } catch (IOException e) {
                OreonClientMod.LOGGER.error("Failed to save template: {}", entry.getKey(), e);
            }
        }
    }

    /**
     * Get report template
     */
    public String getTemplate(String templateName) {
        return reportTemplates.get(templateName);
    }

    /**
     * Get all template names
     */
    public Set<String> getTemplateNames() {
        return reportTemplates.keySet();
    }

    /**
     * Add or update a template
     */
    public void setTemplate(String templateName, String content) {
        reportTemplates.put(templateName, content);
        
        // Save to file
        try {
            Path templatePath = Paths.get(TEMPLATES_DIR, templateName + ".txt");
            Files.writeString(templatePath, content);
        } catch (IOException e) {
            OreonClientMod.LOGGER.error("Failed to save template: {}", templateName, e);
        }
    }

    /**
     * Load report history from files
     */
    private void loadReportHistory() {
        try {
            Path reportsPath = Paths.get(REPORTS_DIR);
            if (Files.exists(reportsPath)) {
                // Load recent reports (limit to avoid memory issues)
                Files.list(reportsPath)
                     .filter(path -> path.toString().endsWith(".txt"))
                     .sorted((p1, p2) -> Long.compare(
                         getFileTimestamp(p2), getFileTimestamp(p1))) // Newest first
                     .limit(100) // Limit to 100 most recent
                     .forEach(this::loadReportFromFile);
            }
            
        } catch (IOException e) {
            OreonClientMod.LOGGER.error("Failed to load report history", e);
        }
    }

    /**
     * Get timestamp from file name
     */
    private long getFileTimestamp(Path filePath) {
        String fileName = filePath.getFileName().toString();
        try {
            // Extract timestamp from filename pattern: report_player_timestamp.txt
            String[] parts = fileName.split("_");
            if (parts.length >= 3) {
                String timestampStr = parts[parts.length - 1].replace(".txt", "");
                return Long.parseLong(timestampStr);
            }
        } catch (NumberFormatException e) {
            // Ignore invalid file names
        }
        return 0;
    }

    /**
     * Load report from file
     */
    private void loadReportFromFile(Path reportPath) {
        try {
            List<String> lines = Files.readAllLines(reportPath);
            
            String playerName = "";
            String reason = "";
            String description = "";
            long timestamp = 0;
            
            for (String line : lines) {
                if (line.startsWith("Player: ")) {
                    playerName = line.substring(8);
                } else if (line.startsWith("Reason: ")) {
                    reason = line.substring(8);
                } else if (line.startsWith("Description: ")) {
                    description = line.substring(13);
                } else if (line.startsWith("Timestamp: ")) {
                    // Parse timestamp if needed
                    timestamp = getFileTimestamp(reportPath);
                }
            }
            
            if (!playerName.isEmpty() && !reason.isEmpty()) {
                ReportInfo report = new ReportInfo(playerName, reason, description);
                report.setTimestamp(timestamp);
                reportHistory.add(report);
            }
            
        } catch (IOException e) {
            OreonClientMod.LOGGER.error("Failed to load report: {}", reportPath, e);
        }
    }
}
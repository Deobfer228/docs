package oreon.src.util;

/**
 * Information about a report
 */
public class ReportInfo {
    private String playerName;
    private String reason;
    private String description;
    private long timestamp;

    public ReportInfo(String playerName, String reason, String description) {
        this.playerName = playerName;
        this.reason = reason;
        this.description = description;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and setters
    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
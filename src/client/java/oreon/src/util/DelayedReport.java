package oreon.src.util;

/**
 * Delayed report functionality
 */
public class DelayedReport {
    private ReportInfo reportInfo;
    private long delayTime;

    public DelayedReport(ReportInfo reportInfo, long delayTime) {
        this.reportInfo = reportInfo;
        this.delayTime = delayTime;
    }

    public ReportInfo getReportInfo() { return reportInfo; }
    public long getDelayTime() { return delayTime; }
}
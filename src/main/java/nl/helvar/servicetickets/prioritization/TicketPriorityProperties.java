package nl.helvar.servicetickets.prioritization;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "priority.rules")
public class TicketPriorityProperties {
    // Example rules – add more as you need
    private int ageHighThresholdMinutes = 2880;
    private int ageCriticalThresholdMinutes = 10080;
    private int ageContractCriticalThresholdMinutes = 1440;
    private int contractCriticalRemainingMinutes = 60;
    private boolean enableScheduler = true;
    private int schedulerFixedRateSeconds = 3600;
    private int schedulerBatchSize = 200;

    // getters/setters...
    public int getAgeHighThresholdMinutes() { return ageHighThresholdMinutes; }
    public void setAgeHighThresholdMinutes(int v) { this.ageHighThresholdMinutes = v; }
    public int getAgeCriticalThresholdMinutes() { return ageCriticalThresholdMinutes; }
    public void setAgeCriticalThresholdMinutes(int v) { this.ageCriticalThresholdMinutes = v; }
    public int getAgeContractCriticalThresholdMinutes() { return ageContractCriticalThresholdMinutes; }
    public void setAgeContractCriticalThresholdMinutes(int v) { this.ageContractCriticalThresholdMinutes = v; }
    public int getContractCriticalRemainingMinutes() { return contractCriticalRemainingMinutes; }
    public void setContractCriticalRemainingMinutes(int v) { this.contractCriticalRemainingMinutes = v; }
    public boolean isEnableScheduler() { return enableScheduler; }
    public void setEnableScheduler(boolean v) { this.enableScheduler = v; }
    public int getSchedulerFixedRateSeconds() { return schedulerFixedRateSeconds; }
    public void setSchedulerFixedRateSeconds(int v) { this.schedulerFixedRateSeconds = v; }
    public int getSchedulerBatchSize() { return schedulerBatchSize; }
    public void setSchedulerBatchSize(int v) { this.schedulerBatchSize = v; }
}
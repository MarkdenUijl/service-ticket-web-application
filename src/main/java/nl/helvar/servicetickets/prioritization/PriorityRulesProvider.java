package nl.helvar.servicetickets.prioritization;

public interface PriorityRulesProvider {
    int ageHighThresholdMinutes();
    int ageCriticalThresholdMinutes();
    int ageContractCriticalThresholdMinutes();
    int contractCriticalRemainingMinutes();
}
package nl.helvar.servicetickets.prioritization;

import org.springframework.stereotype.Component;

@Component
public class YamlPriorityRulesProvider implements PriorityRulesProvider {

    private final TicketPriorityProperties properties;

    public YamlPriorityRulesProvider(TicketPriorityProperties properties) {
        this.properties = properties;
    }

    @Override
    public int ageHighThresholdMinutes() {
        return properties.getAgeHighThresholdMinutes();
    }

    @Override
    public int ageCriticalThresholdMinutes() {
        return properties.getAgeCriticalThresholdMinutes();
    }

    @Override
    public int ageContractCriticalThresholdMinutes() {
        return properties.getAgeContractCriticalThresholdMinutes();
    }

    @Override
    public int contractCriticalRemainingMinutes() {
        return properties.getContractCriticalRemainingMinutes();
    }
}
package nl.helvar.servicetickets.ticketresponses.subclasses;

import jakarta.persistence.*;
import nl.helvar.servicetickets.ticketresponses.TicketResponse;

@Entity
@DiscriminatorValue("engineer_response")
public class EngineerResponse extends TicketResponse {
    private int minutesSpent;

    public int getMinutesSpent() {
        return minutesSpent;
    }

    public void setMinutesSpent(int minutesSpent) {
        this.minutesSpent = minutesSpent;
    }
}

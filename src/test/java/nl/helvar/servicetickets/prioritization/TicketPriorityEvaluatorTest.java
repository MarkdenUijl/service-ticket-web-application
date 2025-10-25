//package nl.helvar.servicetickets.prioritization;
//
//import nl.helvar.servicetickets.servicetickets.ServiceTicket;
//import nl.helvar.servicetickets.servicetickets.enums.TicketPriority;
//import nl.helvar.servicetickets.servicetickets.enums.TicketStatus;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import java.time.Instant;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//class TicketPriorityEvaluatorTest {
//
//    private TicketPriorityEvaluator evaluator;
//    private PriorityRulesProvider mockRules;
//
//    @BeforeEach
//    void setup() {
//        mockRules = Mockito.mock(PriorityRulesProvider.class);
//        Mockito.when(mockRules.ageHighThresholdMinutes()).thenReturn(1440);    // 24h
//        Mockito.when(mockRules.ageCriticalThresholdMinutes()).thenReturn(2880); // 48h
//        Mockito.when(mockRules.contractCriticalRemainingMinutes()).thenReturn(60);
//
//        evaluator = new TicketPriorityEvaluator(mockRules);
//    }
//
//    @Test
//    void shouldReturnCriticalWhenTooOld() {
//        ServiceTicket ticket = new ServiceTicket();
//        ticket.setCreationDate(Instant.now().minus(60, ChronoUnit.HOURS)); // 3600 min
//        ticket.setStatus(TicketStatus.OPEN);
//
//        assertEquals(TicketPriority.CRITICAL, evaluator.evaluate(ticket));
//    }
//
//    @Test
//    void shouldReturnHighWhenOldButNotCritical() {
//        ServiceTicket ticket = new ServiceTicket();
//        ticket.setCreationDate(Instant.now().minus(25, ChronoUnit.HOURS)); // 1500 min
//        ticket.setStatus(TicketStatus.OPEN);
//
//        assertEquals(TicketPriority.HIGH, evaluator.evaluate(ticket));
//    }
//
//    @Test
//    void shouldReturnMediumForNewTicket() {
//        ServiceTicket ticket = new ServiceTicket();
//        ticket.setCreationDate(Instant.now());
//        ticket.setStatus(TicketStatus.OPEN);
//
//        assertEquals(TicketPriority.MEDIUM, evaluator.evaluate(ticket));
//    }
//
//    @Test
//    void shouldReturnCriticalIfContractTimeLow() {
//        ServiceTicket ticket = new ServiceTicket();
//        ticket.setCreationDate(Instant.now().minus(1, ChronoUnit.HOURS));
//        ticket.setStatus(TicketStatus.OPEN);
//
//        // mock a project + service contract if your entity supports it
//        var mockProject = Mockito.mock(nl.helvar.servicetickets.projects.Project.class);
//        var mockContract = Mockito.mock(nl.helvar.servicetickets.contracts.ServiceContract.class);
//        Mockito.when(mockProject.getServiceContract()).thenReturn(mockContract);
//        Mockito.when(mockContract.getRemainingMinutes()).thenReturn(30);
//
//        ticket.setProject(mockProject);
//
//        assertEquals(TicketPriority.CRITICAL, evaluator.evaluate(ticket));
//    }
//}
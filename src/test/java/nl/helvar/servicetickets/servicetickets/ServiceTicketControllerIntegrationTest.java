package nl.helvar.servicetickets.servicetickets;

import nl.helvar.servicetickets.email.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.http.MediaType.APPLICATION_JSON;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class ServiceTicketControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    ServiceTicketService service;

    @MockBean
    EmailService emailService;

    @Test
    void shouldCreateCorrectServiceTicket() throws Exception {
        String requestJson = """
                {
                    "name" : "This is a test ticket",
                    "status" : "open",
                    "type" : "support",
                    "description" : "I need to test this ticket.",
                    "projectId" : 1
                }
                """;

        MvcResult result = this.mockMvc
                .perform(MockMvcRequestBuilders.post("/serviceTickets")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        String createdId = result.getResponse().getContentAsString();

        assertThat(result.getResponse().getHeader("Location"), matchesPattern("^.*/serviceTickets/" + createdId));
    }

}

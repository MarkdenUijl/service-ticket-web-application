package nl.helvar.servicetickets.ticketresponses;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static nl.helvar.servicetickets.helpers.Assertions.*;
import static nl.helvar.servicetickets.helpers.CreateMockClasses.*;
import static nl.helvar.servicetickets.helpers.MockHttpRequests.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class TicketResponseControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldCreateCorrectTicketResponse() throws Exception {
        String requestJson = createMockTicketResponseJson("This is a test ticket response", 1);

        UserDetails userDetails = createMockUserDetails("user@tester.nl", new String[]{"ROLE_USER"});
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        SecurityContextHolder.setContext(securityContext);

        MvcResult result = performPostRequest(mockMvc, "/ticketResponses", requestJson, userDetails)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        assertCreatedIdAndLocation(result, "/ticketResponses");
    }

    @Test
    void shouldCreateCorrectEngineerTicketResponse() throws Exception {
        String requestJson = createMockEngineerTicketResponseJson("This is a test ticket response", 1, 10);

        UserDetails userDetails = createMockUserDetails("engineer@tester.nl", new String[]{"ROLE_ENGINEER"});
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        SecurityContextHolder.setContext(securityContext);

        MvcResult result = performPostRequest(mockMvc, "/ticketResponses", requestJson, userDetails)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        assertCreatedIdAndLocation(result, "/ticketResponses");
        assertIsEngineerResponse(result);
    }

    @Test
    void shouldGetCorrectTicketResponse() throws Exception {
        String requestJson = createMockTicketResponseJson("This is a test ticket response", 1);

        UserDetails userDetails = createMockUserDetails("engineer@tester.nl", new String[]{"ROLE_ENGINEER"});
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        SecurityContextHolder.setContext(securityContext);

        ResultActions createResult = performPostRequest(mockMvc, "/ticketResponses", requestJson, userDetails);

        long createdId = assertCreatedIdAndStatusIsOk(createResult.andReturn());

        performGetRequest(mockMvc, "/ticketResponses/" + createdId, userDetails)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(createdId));
    }

    @Test
    void shouldGetAllTicketResponses() throws Exception {
        performGetRequest(mockMvc, "/ticketResponses")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").exists());
    }

    @Test
    void shouldReplaceTicketResponse() throws Exception {
        String requestJson = createMockTicketResponseJson("This is a test ticket response", 51);

        UserDetails userDetails = createMockUserDetails("admin@tester.nl", new String[]{"ROLE_ADMIN", "CAN_MODERATE_TICKET_RESPONSES_PRIVILEGE"});
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        SecurityContextHolder.setContext(securityContext);

        ResultActions createResult = performPostRequest(mockMvc, "/ticketResponses", requestJson, userDetails);

        long createdId = assertCreatedIdAndStatusIsOk(createResult.andReturn());

        String replaceJson = createMockTicketResponseJson("This is a replaced ticket response", 51);

        performPutRequest(mockMvc, "/ticketResponses/", createdId, replaceJson, userDetails)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(createdId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").value("This is a replaced ticket response"));
    }

    @Test
    void shouldDeleteTicketResponse() throws Exception {
        String requestJson = createMockTicketResponseJson("This is a test ticket response", 1);

        UserDetails userDetails = createMockUserDetails("admin@tester.nl", new String[]{"ROLE_ADMIN"});
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        SecurityContextHolder.setContext(securityContext);

        ResultActions createResult = performPostRequest(mockMvc, "/ticketResponses", requestJson, userDetails);

        long createdId = assertCreatedIdAndStatusIsOk(createResult.andReturn());

        performDeleteRequest(mockMvc, "/ticketResponses", createdId)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Ticket response with id '" + createdId + "' was successfully deleted."));
    }
}
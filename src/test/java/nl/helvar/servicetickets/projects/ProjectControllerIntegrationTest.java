package nl.helvar.servicetickets.projects;

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

import static nl.helvar.servicetickets.helpers.Assertions.assertCreatedIdAndLocation;
import static nl.helvar.servicetickets.helpers.Assertions.assertCreatedIdAndStatusIsOk;
import static nl.helvar.servicetickets.helpers.CreateMockClasses.createMockProjectJson;
import static nl.helvar.servicetickets.helpers.CreateMockClasses.createMockUserDetails;
import static nl.helvar.servicetickets.helpers.MockHttpRequests.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class ProjectControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldCreateCorrectProject() throws Exception {
        String requestJson = createMockProjectJson("This is a test project", "Amsterdam", "1234 AB", "Teststraat", 1);

        MvcResult result = performPostRequest(mockMvc, "/projects", requestJson)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        assertCreatedIdAndLocation(result, "/projects");
    }

    @Test
    void shouldGetCorrectProject() throws Exception {
        String requestJson = createMockProjectJson("This is a test project", "Amsterdam", "5678 AB", "Teststraat", 2);

        UserDetails userDetails = createMockUserDetails("test", null);
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        SecurityContextHolder.setContext(securityContext);

        ResultActions createResult = performPostRequest(mockMvc, "/projects", requestJson , userDetails);

        long createdId = assertCreatedIdAndStatusIsOk(createResult.andReturn());

        performGetRequest(mockMvc, "/projects/" + createdId, userDetails)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(createdId));
    }

    @Test
    void shouldGetCorrectProjectWithExtraAuthorities() throws Exception {
        String requestJson = createMockProjectJson("This is a test project", "Amsterdam", "9101 AB", "Teststraat", 3);

        UserDetails userDetails = createMockUserDetails("test", new String[]{"CAN_SEE_PROJECTS_PRIVILEGE"});
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        SecurityContextHolder.setContext(securityContext);
        ResultActions createResult = performPostRequest(mockMvc, "/projects", requestJson , userDetails);

        long createdId = assertCreatedIdAndStatusIsOk(createResult.andReturn());

        performGetRequest(mockMvc, "/projects/" + createdId, userDetails)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(createdId));
    }

    @Test
    void shouldGetAllProjects() throws Exception {
        performGetRequest(mockMvc, "/projects")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").exists());
    }

    @Test
    void shouldReplaceProject() throws Exception {
        String requestJson = createMockProjectJson("Test Project 1", "Amsterdam", "1274 AB", "Teststraat", 6);

        UserDetails userDetails = createMockUserDetails("test", null);
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        SecurityContextHolder.setContext(securityContext);
        ResultActions createResult = performPostRequest(mockMvc, "/projects", requestJson , userDetails);

        long createdId = assertCreatedIdAndStatusIsOk(createResult.andReturn());

        String newProjectJson = createMockProjectJson("Updated Test Project", "New York", "5678 CD", "Updated Street", 2);

        performPutRequest(mockMvc, "/projects", createdId, newProjectJson)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(createdId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Test Project"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city").value("New York"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("5678 CD"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("Updated Street"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.houseNumber").value(2));
    }

    @Test
    void shouldDeleteProject() throws Exception {
        String requestJson = createMockProjectJson("Test Project 1", "Amsterdam", "1334 AB", "Teststraat", 4);

        UserDetails userDetails = createMockUserDetails("test", null);
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        SecurityContextHolder.setContext(securityContext);
        ResultActions createResult = performPostRequest(mockMvc, "/projects", requestJson , userDetails);

        long createdId = assertCreatedIdAndStatusIsOk(createResult.andReturn());

        performDeleteRequest(mockMvc, "/projects", createdId)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Project with id '" + createdId + "' was successfully deleted."));
    }
}

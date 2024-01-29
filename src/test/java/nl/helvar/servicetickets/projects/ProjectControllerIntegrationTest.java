package nl.helvar.servicetickets.projects;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;

import static nl.helvar.servicetickets.helpers.CreateMockClasses.createMockUserDetails;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class ProjectControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldCreateCorrectProject() throws Exception {
        String requestJson = createProjectJson("This is a test project", "Amsterdam", "1234 AB", "Teststraat", 1);

        MvcResult result = performPostRequest(requestJson)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        assertCreatedIdAndLocation(result);
    }

    @Test
    void shouldGetCorrectProject() throws Exception {
        String requestJson = createProjectJson("This is a test project", "Amsterdam", "5678 AB", "Teststraat", 2);

        UserDetails userDetails = createMockUserDetails(null);
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        SecurityContextHolder.setContext(securityContext);

        ResultActions createResult = performPostRequest(requestJson, userDetails);

        long createdId = assertCreatedIdAndStatusIsOk(createResult.andReturn());

        performGetRequest("/projects/" + createdId, userDetails)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(createdId));
    }

    @Test
    void shouldGetCorrectProjectWithExtraAuthorities() throws Exception {
        String requestJson = createProjectJson("This is a test project", "Amsterdam", "9101 AB", "Teststraat", 3);

        UserDetails userDetails = createMockUserDetails(new String[]{"CAN_SEE_PROJECTS_PRIVILEGE"});
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        SecurityContextHolder.setContext(securityContext);
        ResultActions createResult = performPostRequest(requestJson, userDetails);

        long createdId = assertCreatedIdAndStatusIsOk(createResult.andReturn());

        performGetRequest("/projects/" + createdId, userDetails)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(createdId));
    }

    @Test
    void shouldGetAllProjects() throws Exception {
        performGetRequest("/projects")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").exists());
    }

    @Test
    void shouldReplaceProject() throws Exception {
        String requestJson = createProjectJson("Test Project 1", "Amsterdam", "1274 AB", "Teststraat", 6);

        UserDetails userDetails = createMockUserDetails(null);
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        SecurityContextHolder.setContext(securityContext);
        ResultActions createResult = performPostRequest(requestJson, userDetails);

        long createdId = assertCreatedIdAndStatusIsOk(createResult.andReturn());

        String newProjectJson = createProjectJson("Updated Test Project", "New York", "5678 CD", "Updated Street", 2);

        performPutRequest(createdId, newProjectJson)
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
        String requestJson = createProjectJson("Test Project 1", "Amsterdam", "1334 AB", "Teststraat", 4);

        UserDetails userDetails = createMockUserDetails(null);
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        SecurityContextHolder.setContext(securityContext);
        ResultActions createResult = performPostRequest(requestJson, userDetails);

        long createdId = assertCreatedIdAndStatusIsOk(createResult.andReturn());

        performDeleteRequest(createdId)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Project with id '" + createdId + "' was successfully deleted."));
    }

    private String createProjectJson(String name, String city, String zipCode, String street, int houseNumber) {
        return String.format("""
                {
                    "name" : "%s",
                    "city" : "%s",
                    "zipCode" : "%s",
                    "street" : "%s",
                    "houseNumber" : %d
                }
                """, name, city, zipCode, street, houseNumber);
    }

    private ResultActions performPostRequest(String content) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(MockMvcResultHandlers.print());
    }

    private ResultActions performPostRequest(String content, UserDetails userDetails) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/projects")
                        .with(user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(MockMvcResultHandlers.print());
    }

    private ResultActions performGetRequest(String url) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());
    }

    private ResultActions performGetRequest(String url, UserDetails userDetails) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                        .with(user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());
    }

    private ResultActions performPutRequest(long id, String content) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.put("/projects/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(MockMvcResultHandlers.print());
    }

    private ResultActions performDeleteRequest(long id) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.delete("/projects/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());
    }

    private long assertCreatedIdAndStatusIsOk(MvcResult result) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
        long createdId = jsonNode.get("id").asLong();

        assertEquals(result.getResponse().getStatus(), HttpStatus.CREATED.value());

        return createdId;
    }

    private void assertCreatedIdAndLocation(MvcResult result) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
        long createdId = jsonNode.get("id").asLong();
        assertEquals(result.getResponse().getStatus(), HttpStatus.CREATED.value());
        assertThat(result.getResponse().getHeader("Location"), matchesPattern("^.*/projects/" + createdId));
    }
}

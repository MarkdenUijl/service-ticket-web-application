package nl.helvar.servicetickets.projects;

import nl.helvar.servicetickets.security.JwtService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;


@WebMvcTest(ProjectController.class)
@AutoConfigureMockMvc(addFilters = true)
class ProjectControllerUnitTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    JwtService jwtService;

    @MockBean
    ProjectService projectService;

    List<ProjectDTO> projectDTOS = new ArrayList<>();

    @BeforeEach
    void setUp() {
        ProjectDTO firstProjectDto = new ProjectDTO();
        firstProjectDto.setId(1L);
        firstProjectDto.setName("city hall");
        firstProjectDto.setCity("city");
        firstProjectDto.setZipCode("1234AB");
        firstProjectDto.setStreet("street");
        firstProjectDto.setHouseNumber(1);
        firstProjectDto.setServiceContract(null);
        firstProjectDto.setTickets(null);

        projectDTOS.add(firstProjectDto);

        ProjectDTO secondProjectDto = new ProjectDTO();
        secondProjectDto.setId(2L);
        secondProjectDto.setName("corporate offices");
        secondProjectDto.setCity("town");
        secondProjectDto.setZipCode("5678CD");
        secondProjectDto.setStreet("road");
        secondProjectDto.setHouseNumber(2);
        secondProjectDto.setServiceContract(null);
        secondProjectDto.setTickets(null);

        projectDTOS.add(secondProjectDto);

        ProjectDTO thirdProjectDto = new ProjectDTO();
        thirdProjectDto.setId(3L);
        thirdProjectDto.setName("town hall");
        thirdProjectDto.setCity("village");
        thirdProjectDto.setZipCode("9012EF");
        thirdProjectDto.setStreet("avenue");
        thirdProjectDto.setHouseNumber(3);
        thirdProjectDto.setServiceContract(null);
        thirdProjectDto.setTickets(null);

        projectDTOS.add(thirdProjectDto);
    }

    @AfterEach
    void tearDown() {
        projectDTOS.clear();
    }


    @Test
    @WithMockUser(username="testuser", roles="ADMIN")
    void shouldGetAllProjects() throws Exception {
        // Arrange
        Mockito.when(projectService.getAllProjects(Mockito.any(), Mockito.isNull(), Mockito.isNull(), Mockito.isNull(), Mockito.isNull(), Mockito.isNull(), Mockito.isNull()))
                .thenReturn(projectDTOS);

        // Act & Assert
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/projects"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("city hall"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].city").value("city"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].zipCode").value("1234AB"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].street").value("street"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].houseNumber").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].serviceContract").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].tickets").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("corporate offices"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].city").value("town"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].zipCode").value("5678CD"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].street").value("road"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].houseNumber").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].serviceContract").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].tickets").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name").value("town hall"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].city").value("village"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].zipCode").value("9012EF"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].street").value("avenue"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].houseNumber").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].serviceContract").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].tickets").isEmpty());
    }

    @Test
    @WithMockUser(username="testuser", roles="ADMIN")
    void shouldFindProjectById() throws Exception {
        // Arrange
        Mockito.when(projectService.findProjectById(Mockito.any(), Mockito.any())).thenReturn(projectDTOS.get(0));

        // Act & Assert
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/projects/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("city hall"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city").value("city"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("1234AB"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("street"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.houseNumber").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.serviceContract").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tickets").isEmpty());
    }

    @Test
    @WithMockUser(username = "testuser", authorities = {"CAN_MODIFY_PROJECTS_PRIVILEGE", "ADMIN"})
    void shouldCreateProject() throws Exception {
        // Arrange
        ProjectDTO createdProjectDTO = new ProjectDTO();
        createdProjectDTO.setName("New Project");
        createdProjectDTO.setCity("New City");
        createdProjectDTO.setZipCode("1234AB");
        createdProjectDTO.setStreet("New Street");
        createdProjectDTO.setHouseNumber(1);

        Mockito.when(projectService.createProject(Mockito.any(ProjectCreationDTO.class)))
                .thenReturn(createdProjectDTO);

        // Act & Assert
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/projects")
                        .contentType("application/json")
                        .content("{\"name\":\"New Project\",\"city\":\"New City\",\"zipCode\":\"1234AB\",\"street\":\"New Street\",\"houseNumber\":1}")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("New Project"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city").value("New City"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("1234AB"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("New Street"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.houseNumber").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.serviceContract").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tickets").isEmpty());
    }

    @Test
    @WithMockUser(username = "testuser", authorities = {"CAN_MODIFY_PROJECTS_PRIVILEGE", "ADMIN"})
    void shouldReplaceProject() throws Exception {
        // Arrange
        ProjectDTO replacedProjectDTO = new ProjectDTO();
        replacedProjectDTO.setId(1L);
        replacedProjectDTO.setName("Replaced Project");
        replacedProjectDTO.setCity("New City");
        replacedProjectDTO.setZipCode("1234AB");
        replacedProjectDTO.setStreet("New Street");
        replacedProjectDTO.setHouseNumber(1);

        Mockito.when(projectService.replaceProject(Mockito.any(), Mockito.any(ProjectCreationDTO.class)))
                .thenReturn(replacedProjectDTO);

        // Act & Assert
        this.mockMvc
                .perform(MockMvcRequestBuilders.put("/projects/1")
                        .contentType("application/json")
                        .content("{\"name\":\"Replaced Project\",\"city\":\"New City\",\"zipCode\":\"1234AB\",\"street\":\"New Street\",\"houseNumber\":1}")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Replaced Project"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city").value("New City"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("1234AB"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("New Street"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.houseNumber").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.serviceContract").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tickets").isEmpty());
    }

    @Test
    @WithMockUser(username = "testuser", authorities = {"CAN_MODIFY_PROJECTS_PRIVILEGE", "ADMIN"})
    public void shouldDeleteProject() throws Exception {
        // Arrange
        Mockito.when(projectService.deleteProject(Mockito.any())).thenReturn("Project deleted");

        // Act & Assert
        this.mockMvc
                .perform(MockMvcRequestBuilders.delete("/projects/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
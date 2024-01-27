package nl.helvar.servicetickets.projects;


import nl.helvar.servicetickets.exceptions.DuplicateInDatabaseException;
import nl.helvar.servicetickets.exceptions.RecordNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    ProjectRepository projectRepository;

    @InjectMocks
    ProjectService service;

    @Test
    public void shouldCreateProject_Success() {
        // Arrange
        ProjectCreationDTO projectCreationDTO = createProjectCreationDTO();

        doReturn(Optional.empty()).when(projectRepository).findOne(Mockito.any(Specification.class));

        // Act
        ProjectDTO result = service.createProject(projectCreationDTO);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(projectCreationDTO.getCity(), result.getCity(), "City should match");
        assertEquals(projectCreationDTO.getStreet(), result.getStreet(), "Street should match");
        assertEquals(projectCreationDTO.getZipCode(), result.getZipCode(), "Zip code should match");
        assertEquals(projectCreationDTO.getHouseNumber(), result.getHouseNumber(), "House number should match");

        Mockito.verify(projectRepository, times(1)).save(any());
    }

    @Test
    public void shouldNotCreateProject_DuplicateInDatabaseException() {
        // Arrange
        ProjectCreationDTO projectCreationDTO = createProjectCreationDTO();

        doReturn(Optional.of(createMockProject())).when(projectRepository).findOne(Mockito.any(Specification.class));

        // Act and Assert
        DuplicateInDatabaseException exception = assertThrows(DuplicateInDatabaseException.class, () -> {
            service.createProject(projectCreationDTO);
        });

        assertEquals("There was already a project registered at this address.", exception.getMessage(),
                "Exception message should match");

        Mockito.verify(projectRepository, never()).save(any());
    }

    @Test
    public void shouldGetAllProjects_Success() {
        // Arrange
        UserDetails userDetails = createMockUserDetails(null);
        String name = "Test Project";
        String city = "Test City";
        String zipCode = "12345";
        String street = "Test Street";
        Integer houseNumber = 123;
        Boolean hasServiceContract = true;

        List<Project> mockProjects = Arrays.asList(createMockProject(), createMockProject());

        doReturn(mockProjects).when(projectRepository).findAll(Mockito.any(Specification.class));

        // Act
        List<ProjectDTO> result = service.getAllProjects(userDetails, name, city, zipCode, street, houseNumber, hasServiceContract);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(2, result.size(), "Number of projects in the result should be 2");

        Mockito.verify(projectRepository, times(1)).findAll(Mockito.any(Specification.class));
    }

    @Test
    public void shouldNotGetAllProjects_RecordNotFoundException() {
        // Arrange
        UserDetails userDetails = createMockUserDetails(null);
        String name = "Nonexistent Project";
        String city = "Nonexistent City";
        String zipCode = "54321";
        String street = "Nonexistent Street";
        Integer houseNumber = 456;
        Boolean hasServiceContract = false;

        doReturn(Collections.emptyList()).when(projectRepository).findAll(Mockito.any(Specification.class));

        // Act and Assert
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {
            service.getAllProjects(userDetails, name, city, zipCode, street, houseNumber, hasServiceContract);
        });

        assertEquals("There were no projects found with those parameters", exception.getMessage(),
                "Exception message should match");

        Mockito.verify(projectRepository, times(1)).findAll(Mockito.any(Specification.class));
    }

    @Test
    public void shouldFindProjectById_Success() {
        // Arrange
        UserDetails userDetails = createMockUserDetails(null);
        Long id = 1L;

        doReturn(Optional.of(createMockProject())).when(projectRepository).findById(anyLong());

        // Act
        ProjectDTO result = service.findProjectById(userDetails, id);

        // Assert
        assertNotNull(result, "Result should not be null");

        Mockito.verify(projectRepository, times(1)).findById(anyLong());
    }

    @Test
    public void shouldFindProjectById_WithPrivilege_Success() {
        // Arrange
        UserDetails userDetails = createMockUserDetails(new String[]{"CAN_SEE_PROJECTS_PRIVILEGE"});
        Long id = 1L;
        Project mockProject = createMockProject();

        doReturn(Optional.of(mockProject)).when(projectRepository).findById(anyLong());

        // Act
        ProjectDTO result = service.findProjectById(userDetails, id);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(mockProject.getName(), result.getName(), "Project names should match");

        Mockito.verify(projectRepository, times(1)).findById(anyLong());
    }

    @Test
    public void shouldNotFindProjectById_RecordNotFoundException() {
        // Arrange
        UserDetails userDetails = createMockUserDetails(null);
        Long id = 1L;

        doReturn(Optional.empty()).when(projectRepository).findById(anyLong());

        // Act and Assert
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {
            service.findProjectById(userDetails, id);
        });

        assertEquals("No project found with id " + id, exception.getMessage(),
                "Exception message should match");

        Mockito.verify(projectRepository, times(1)).findById(anyLong());
    }

    @Test
    public void shouldReplaceProject_Success() {
        // Arrange
        Long id = 1L;
        ProjectCreationDTO projectCreationDTO = createProjectCreationDTO();

        doReturn(Optional.of(createMockProject())).when(projectRepository).findById(anyLong());
        doReturn(createMockProject()).when(projectRepository).save(any());

        // Act
        ProjectDTO result = service.replaceProject(id, projectCreationDTO);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(projectCreationDTO.getCity(), result.getCity(), "City should match");
        assertEquals(projectCreationDTO.getStreet(), result.getStreet(), "Street should match");
        assertEquals(projectCreationDTO.getZipCode(), result.getZipCode(), "Zip code should match");
        assertEquals(projectCreationDTO.getHouseNumber(), result.getHouseNumber(), "House number should match");

        Mockito.verify(projectRepository, times(1)).findById(anyLong());
        Mockito.verify(projectRepository, times(1)).save(any());
    }

    @Test
    public void shouldNotReplaceProject_RecordNotFoundException() {
        // Arrange
        Long id = 1L;
        ProjectCreationDTO projectCreationDTO = createProjectCreationDTO();

        doReturn(Optional.empty()).when(projectRepository).findById(anyLong());

        // Act and Assert
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {
            service.replaceProject(id, projectCreationDTO);
        });

        assertEquals("Could not find any project with id '" + id + "' in database.", exception.getMessage(),
                "Exception message should match");

        Mockito.verify(projectRepository, times(1)).findById(anyLong());
        Mockito.verify(projectRepository, never()).save(any());
    }

    @Test
    public void shouldDeleteProject_Success() {
        // Arrange
        Long id = 1L;

        doReturn(Optional.of(createMockProject())).when(projectRepository).findById(anyLong());

        // Act
        String result = service.deleteProject(id);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals("Project with id '" + id + "' was successfully deleted.", result, "Result should match");

        Mockito.verify(projectRepository, times(1)).findById(anyLong());
        Mockito.verify(projectRepository, times(1)).delete((Project) any());
    }

    @Test
    public void shouldNotDeleteProject_RecordNotFoundException() {
        // Arrange
        Long id = 1L;

        doReturn(Optional.empty()).when(projectRepository).findById(anyLong());

        // Act and Assert
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {
            service.deleteProject(id);
        });

        assertEquals("Could not find any project with id '" + id + "' in database.", exception.getMessage(),
                "Exception message should match");

        Mockito.verify(projectRepository, times(1)).findById(anyLong());
        Mockito.verify(projectRepository, never()).delete((Project) any());
    }


    private ProjectCreationDTO createProjectCreationDTO() {
        // You can create a utility method to generate a ProjectCreationDTO for test data
        // For simplicity, you can return a fixed DTO or use a mocking library for DTO creation
        ProjectCreationDTO dto = new ProjectCreationDTO();
        dto.setName("TestProject");
        dto.setCity("TestCity");
        dto.setZipCode("1234 AB");
        dto.setStreet("TestStreet");
        dto.setHouseNumber(123);
        return dto;
    }

    private Project createMockProject() {
        Project project = new Project();
        project.setName("TestProject");
        project.setCity("TestCity");
        project.setZipCode("1234 AB");
        project.setStreet("TestStreet");
        project.setHouseNumber(123);
        return project;
    }

    private UserDetails createMockUserDetails(String[] extraAuthorities) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (extraAuthorities != null) {
            for (String authority : extraAuthorities) {
                authorities.add(new SimpleGrantedAuthority(authority));
            }
        }

        return new User("testuser", "password", authorities);
    }
}
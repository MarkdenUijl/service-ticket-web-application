package nl.helvar.servicetickets.helpers;

import nl.helvar.servicetickets.projects.Project;
import nl.helvar.servicetickets.projects.ProjectCreationDTO;
import nl.helvar.servicetickets.servicetickets.ServiceTicket;
import nl.helvar.servicetickets.servicetickets.ServiceTicketCreationDTO;
import nl.helvar.servicetickets.servicetickets.enums.TicketStatus;
import nl.helvar.servicetickets.servicetickets.enums.TicketType;
import nl.helvar.servicetickets.users.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CreateMockClasses {
    public static UserDetails createMockUserDetails(String[] extraAuthorities) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (extraAuthorities != null) {
            for (String authority : extraAuthorities) {
                authorities.add(new SimpleGrantedAuthority(authority));
            }
        }

        return new org.springframework.security.core.userdetails.User("testuser", "password", authorities);
    }

    public static ProjectCreationDTO createProjectCreationDTO() {
        ProjectCreationDTO dto = new ProjectCreationDTO();
        dto.setName("TestProject");
        dto.setCity("TestCity");
        dto.setZipCode("1234 AB");
        dto.setStreet("TestStreet");
        dto.setHouseNumber(123);
        return dto;
    }

    public static Project createMockProject() {
        Project project = new Project();
        project.setName("TestProject");
        project.setCity("TestCity");
        project.setZipCode("1234 AB");
        project.setStreet("TestStreet");
        project.setHouseNumber(123);
        return project;
    }

    public static User createMockUser() {
        User user = new User();
        user.setFirstName("TestFirstName");
        user.setLastName("TestLastName");
        user.setEmail("testmail@mail.com");
        user.setPassword("TestPassword");
        user.setPhoneNumber("0612345678");
        user.setRoles(null);
        user.setServiceTickets(null);
        user.setTicketResponses(null);

        return user;
    }

    public static ServiceTicketCreationDTO createMockServiceTicketCreationDTO() {
        ServiceTicketCreationDTO dto = new ServiceTicketCreationDTO();
        dto.setCreationDate(null);
        dto.setName("TestName");
        dto.setStatus("OPEN");
        dto.setType("SUPPORT");
        dto.setDescription("TestDescription");
        dto.setResponses(null);
        dto.setMinutesSpent(0);
        dto.setCreationDate(LocalDateTime.now());

        return dto;
    }

    public static ServiceTicket createMockServiceTicket() {
        ServiceTicket serviceTicket = new ServiceTicket();
        serviceTicket.setCreationDate(LocalDateTime.now());
        serviceTicket.setName("TestName");
        serviceTicket.setDescription("TestDescription");
        serviceTicket.setType(TicketType.valueOf("SUPPORT"));
        serviceTicket.setStatus(TicketStatus.valueOf("OPEN"));
        serviceTicket.setMinutesSpent(0);
        serviceTicket.setResponses(null);
        serviceTicket.setSubmittedBy(createMockUser());
        serviceTicket.setProject(null);

        return serviceTicket;
    }
}

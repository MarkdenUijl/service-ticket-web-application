package nl.helvar.servicetickets.servicetickets;

import nl.helvar.servicetickets.exceptions.InvalidRequestException;
import nl.helvar.servicetickets.exceptions.RecordNotFoundException;
import nl.helvar.servicetickets.users.User;
import nl.helvar.servicetickets.users.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static nl.helvar.servicetickets.helpers.CreateMockClasses.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class ServiceTicketServiceUnitTest {
    @Mock
    ServiceTicketRepository serviceTicketRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    ServiceTicketService service;

    @Test
    public void shouldCreateServiceTicket_Success() {
        // Arrange
        ServiceTicketCreationDTO dto = createMockServiceTicketCreationDTO();

        doReturn(Optional.of(createMockUser())).when(userRepository).findOne((Specification<User>) any());

        // Act
        ServiceTicketDTO result = service.createServiceTicket(createMockUserDetails("test", null), dto);

        // Assert
        assertNotNull(result, "ServiceTicketDTO should not be null");
        assertEquals(dto.getName(), result.getName(), "Name should be equal");
        assertEquals(dto.getDescription(), result.getDescription(), "Description should be equal");
        assertEquals(dto.getType(), result.getType(), "Type should be equal");
        assertEquals(dto.getStatus(), result.getStatus(), "Status should be equal");
        assertEquals(dto.getMinutesSpent(), result.getMinutesSpent(), "MinutesSpent should be equal");
        assertEquals(dto.getCreationDate(), result.getCreationDate(), "CreationDate should be equal");

        Mockito.verify(serviceTicketRepository, Mockito.times(1)).save(any());
    }

    @Test
    public void shouldCreateServiceTicket_InvalidUser() {
        // Arrange
        ServiceTicketCreationDTO dto = createMockServiceTicketCreationDTO();

        doReturn(Optional.empty()).when(userRepository).findOne((Specification<User>) any());

        // Act
        Exception exception = assertThrows(RecordNotFoundException.class, () -> {
            service.createServiceTicket(createMockUserDetails("test", null), dto);
        });

        // Assert
        String expectedMessage = "Could not find user with email 'testuser' in database.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void shouldGetAllServiceTickets_Success() {
        // Arrange
        UserDetails userDetails = createMockUserDetails("test", null);
        String type = "SUPPORT";
        String status = "OPEN";
        Long projectId = 1L;
        String projectName = "TestProjectName";
        LocalDate issuedBefore = LocalDate.now();
        LocalDate issuedAfter = LocalDate.now();
        String submitterFirstName = "TestFirstName";
        String submitterLastName = "TestLastName";
        String submitterEmail = "testmail@mail.com";
        Long submitterId = 1L;

        List<ServiceTicket> mockServiceTickets = Arrays.asList(createMockServiceTicket(), createMockServiceTicket());
        doReturn(mockServiceTickets).when(serviceTicketRepository).findAll((Specification<ServiceTicket>) any());

        // Act
        List<ServiceTicket> result = service.getAllServiceTickets(
                type,
                status,
                projectId,
                projectName,
                issuedBefore,
                issuedAfter,
                submitterFirstName,
                submitterLastName,
                submitterEmail,
                submitterId
        );

        // Assert
        assertNotNull(result, "ServiceTicketDTO should not be null");
        assertEquals(mockServiceTickets.size(), result.size(), "Size should be equal");

        Mockito.verify(serviceTicketRepository, Mockito.times(1)).findAll((Specification<ServiceTicket>) any());
    }

    @Test
    public void shouldGetAllServiceTickets_NoServiceTicketsFound() {
        // Arrange
        UserDetails userDetails = createMockUserDetails("test", null);
        String type = "SUPPORT";
        String status = "OPEN";
        Long projectId = 1L;
        String projectName = "TestProjectName";
        LocalDate issuedBefore = LocalDate.now();
        LocalDate issuedAfter = LocalDate.now();
        String submitterFirstName = "TestFirstName";
        String submitterLastName = "TestLastName";
        String submitterEmail = "testmail@mail.com";
        Long submitterId = 1L;

        doReturn(new ArrayList<ServiceTicket>()).when(serviceTicketRepository).findAll((Specification<ServiceTicket>) any());

        // Act & Assert
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {
            service.getAllServiceTickets(
                    type,
                    status,
                    projectId,
                    projectName,
                    issuedBefore,
                    issuedAfter,
                    submitterFirstName,
                    submitterLastName,
                    submitterEmail,
                    submitterId
            );
        });

        assertEquals("Could not find service tickets with these parameters in the database.", exception.getMessage(),
                "Exception message should be equal");

        Mockito.verify(serviceTicketRepository, Mockito.times(1)).findAll((Specification<ServiceTicket>) any());
    }

    @Test
    public void shouldFindServiceTicketById_Success() {
        // Arrange
        UserDetails userDetails = createMockUserDetails("test", new String[]{"CAN_MODERATE_SERVICE_TICKETS_PRIVILEGE"});
        Long id = 1L;
        doReturn(Optional.of(createMockServiceTicket())).when(serviceTicketRepository).findById(id);

        // Act
        ServiceTicket result = service.findById(userDetails, id);

        // Assert
        assertNotNull(result, "ServiceTicket should not be null");

        Mockito.verify(serviceTicketRepository, Mockito.times(1)).findById(id);
    }

    @Test
    public void shouldFindServiceTicketById_NoServiceTicketFound() {
        // Arrange
        UserDetails userDetails = createMockUserDetails("test", new String[]{"CAN_MODERATE_SERVICE_TICKETS_PRIVILEGE"});
        Long id = 1L;
        doReturn(Optional.empty()).when(serviceTicketRepository).findById(id);

        // Act & Assert
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {
            service.findById(userDetails, id);
        });

        assertEquals("Could not find any ticket with id '1' in database.", exception.getMessage(),
                "Exception message should be equal");

        Mockito.verify(serviceTicketRepository, Mockito.times(1)).findById(id);
    }

    @Test
    public void shouldFindServiceTicketById_NoPrivileges() {
        // Arrange
        UserDetails userDetails = createMockUserDetails("test", null);
        Long id = 1L;
        doReturn(Optional.of(createMockServiceTicket())).when(serviceTicketRepository).findById(id);

        // Act & Assert
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
            service.findById(userDetails, id);
        });

        assertEquals("You do not have the required privileges to access this ticket.", exception.getMessage(),
                "Exception message should be equal");

        Mockito.verify(serviceTicketRepository, Mockito.times(1)).findById(id);
    }

    @Test
    public void shouldReplaceServiceTicket_Success() {
        // Arrange
        UserDetails userDetails = createMockUserDetails("test", new String[]{"CAN_MODERATE_SERVICE_TICKETS_PRIVILEGE"});
        Long id = 1L;
        ServiceTicketCreationDTO dto = createMockServiceTicketCreationDTO();
        doReturn(Optional.of(createMockServiceTicket())).when(serviceTicketRepository).findById(id);

        // Act
        ServiceTicket result = service.replaceServiceTicket(userDetails, id, dto);

        // Assert
        assertNotNull(result, "ServiceTicket should not be null");

        Mockito.verify(serviceTicketRepository, Mockito.times(1)).findById(id);
        Mockito.verify(serviceTicketRepository, Mockito.times(1)).save(any());
    }

    @Test
    public void shouldReplaceServiceTicket_NoServiceTicketFound() {
        // Arrange
        UserDetails userDetails = createMockUserDetails("test", new String[]{"CAN_MODERATE_SERVICE_TICKETS_PRIVILEGE"});
        Long id = 1L;
        ServiceTicketCreationDTO dto = createMockServiceTicketCreationDTO();
        doReturn(Optional.empty()).when(serviceTicketRepository).findById(id);

        // Act & Assert
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {
            service.replaceServiceTicket(userDetails, id, dto);
        });

        assertEquals("Could not find any ticket with id '1' in database.", exception.getMessage(),
                "Exception message should be equal");

        Mockito.verify(serviceTicketRepository, Mockito.times(1)).findById(id);
    }

    @Test
    public void shouldReplaceServiceTicket_NoPrivileges() {
        // Arrange
        UserDetails userDetails = createMockUserDetails("test", null);
        Long id = 1L;
        ServiceTicketCreationDTO dto = createMockServiceTicketCreationDTO();
        doReturn(Optional.of(createMockServiceTicket())).when(serviceTicketRepository).findById(id);

        // Act & Assert
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
            service.replaceServiceTicket(userDetails, id, dto);
        });

        assertEquals("You do not have the required privileges to change this ticket.", exception.getMessage(),
                "Exception message should be equal");

        Mockito.verify(serviceTicketRepository, Mockito.times(1)).findById(id);
    }

    @Test
    public void shouldDeleteServiceTicket_Success() {
        // Arrange
        UserDetails userDetails = createMockUserDetails("test", new String[]{"CAN_MODERATE_SERVICE_TICKETS_PRIVILEGE"});
        Long id = 1L;
        doReturn(Optional.of(createMockServiceTicket())).when(serviceTicketRepository).findById(id);

        // Act
        String result = service.deleteServiceTicket(userDetails, id);

        // Assert
        assertNotNull(result, "String should not be null");

        Mockito.verify(serviceTicketRepository, Mockito.times(1)).findById(id);
        Mockito.verify(serviceTicketRepository, Mockito.times(1)).delete((ServiceTicket) any());
    }

    @Test
    public void shouldDeleteServiceTicket_NoServiceTicketFound() {
        // Arrange
        UserDetails userDetails = createMockUserDetails("test", new String[]{"CAN_MODERATE_SERVICE_TICKETS_PRIVILEGE"});
        Long id = 1L;
        doReturn(Optional.empty()).when(serviceTicketRepository).findById(id);

        // Act & Assert
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {
            service.deleteServiceTicket(userDetails, id);
        });

        assertEquals("Could not find any ticket with id '1' in database.", exception.getMessage(),
                "Exception message should be equal");

        Mockito.verify(serviceTicketRepository, Mockito.times(1)).findById(id);
    }

    @Test
    public void shouldDeleteServiceTicket_NoPrivileges() {
        // Arrange
        UserDetails userDetails = createMockUserDetails("test", null);
        Long id = 1L;
        doReturn(Optional.of(createMockServiceTicket())).when(serviceTicketRepository).findById(id);

        // Act & Assert
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
            service.deleteServiceTicket(userDetails, id);
        });

        assertEquals("You do not have the required privileges to delete this ticket.", exception.getMessage(),
                "Exception message should be equal");

        Mockito.verify(serviceTicketRepository, Mockito.times(1)).findById(id);
    }
}
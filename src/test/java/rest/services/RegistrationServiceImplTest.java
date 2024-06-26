package rest.services;

import io.ylab.tom13.coworkingservice.in.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.in.rest.services.user.implementation.RegistrationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class RegistrationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateUser() throws UserAlreadyExistsException {
//        RegistrationDTO registrationDTO = new RegistrationDTO("John", "Doe", "john.doe@example.com", "password", Role.USER);
//        UserDTO expectedUserDTO = new UserDTO(1L, registrationDTO.firstName(), registrationDTO.lastName(), registrationDTO.email(), Role.USER);
//
//        when(userRepository.createUser(any(RegistrationDTO.class))).thenReturn(expectedUserDTO);
//
//        UserDTO actualUserDTO = registrationService.createUser(registrationDTO);
//
//        assertEquals(expectedUserDTO, actualUserDTO);
    }
}

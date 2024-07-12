//package rest.controller;
//
//import io.ylab.tom13.coworkingservice.out.entity.dto.RegistrationDTO;
//import io.ylab.tom13.coworkingservice.out.entity.dto.ResponseDTO;
//import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
//import io.ylab.tom13.coworkingservice.out.entity.enumeration.Role;
//import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
//import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserAlreadyExistsException;
//import io.ylab.tom13.coworkingservice.out.rest.controller.implementation.RegistrationControllerImpl;
//import io.ylab.tom13.coworkingservice.out.rest.services.RegistrationService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.lang.reflect.Field;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("Тесты контроллера регистрации пользователя")
//class RegistrationControllerImplTest {
//
//    @Mock
//    private RegistrationService registrationService;
//
//    @InjectMocks
//    private RegistrationControllerImpl registrationController;
//
//    private RegistrationDTO registrationDTO;
//    private UserDTO userDTO;
//
//    @BeforeEach
//    void setUp() throws NoSuchFieldException, IllegalAccessException {
//        registrationDTO = new RegistrationDTO("John", "Doe", "john.doe@example.com", "password");
//
//        userDTO = new UserDTO(1L, "John" , "Doe", "john.doe@example.com", Role.USER);
//
//        Field registrationControllerField = RegistrationControllerImpl.class.getDeclaredField("registrationService");
//        registrationControllerField.setAccessible(true);
//        registrationControllerField.set(registrationController, registrationService);
//
//    }
//
//    @Test
//    @DisplayName("Тест успешно создания пользователя")
//    void testCreateUserSuccess() throws UserAlreadyExistsException, RepositoryException {
//        when(registrationService.createUser(registrationDTO)).thenReturn(userDTO);
//
//        ResponseDTO<UserDTO> response = registrationController.createUser(registrationDTO);
//
//        assertThat(response.data()).isEqualTo(userDTO);
//        assertThat(response.message()).isNull();
//    }
//
//    @Test
//    @DisplayName("Тест попытки создания пользователя с существующим email")
//    void testCreateUserUserAlreadyExistsException() throws UserAlreadyExistsException, RepositoryException {
//        String errorMessage = "User already exists";
//        when(registrationService.createUser(registrationDTO)).thenThrow(new UserAlreadyExistsException(errorMessage));
//
//        ResponseDTO<UserDTO> response = registrationController.createUser(registrationDTO);
//
//        assertThat(response.message()).contains(errorMessage);
//        assertThat(response.data()).isNull();
//        verify(registrationService, times(1)).createUser(registrationDTO);
//    }
//}

//package rest.controller;
//
//import io.ylab.tom13.coworkingservice.out.entity.dto.AuthorizationDTO;
//import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
//import io.ylab.tom13.coworkingservice.out.entity.enumeration.Role;
//import io.ylab.tom13.coworkingservice.out.rest.controller.implementation.AuthorizationControllerSpring;
//import io.ylab.tom13.coworkingservice.out.rest.services.AuthorizationService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.lang.reflect.Field;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("Тесты сервиса авторизации")
//public class AuthorizationControllerImplTest {
//
//    @Mock
//    private AuthorizationService authorizationService;
//
//    @InjectMocks
//    private AuthorizationControllerSpring authorizationController;
//
//    private AuthorizationDTO validAuthorizationDTO;
//    private AuthorizationDTO invalidAuthorizationDTO;
//    private UserDTO userDTO;
//
//    @BeforeEach
//    void setUp() throws NoSuchFieldException, IllegalAccessException {
//        validAuthorizationDTO = new AuthorizationDTO("john.doe@example.com", "password");
//        invalidAuthorizationDTO = new AuthorizationDTO("john.doe@example.com", "wrongPassword");
//        userDTO = new UserDTO(1L, "john.doe@example.com", "John", "Doe", Role.USER);
//
//        Field authorizationControllerField = AuthorizationControllerSpring.class.getDeclaredField("authorizationService");
//        authorizationControllerField.setAccessible(true);
//        authorizationControllerField.set(authorizationController, authorizationService);
//    }
//
////    @Test
////    @DisplayName("Тест успешной авторизации")
////    void testLoginSuccess() throws UnauthorizedException {
////        when(authorizationService.login(validAuthorizationDTO)).thenReturn(userDTO);
////
////        ResponseDTO<UserDTO> response = authorizationController.login(validAuthorizationDTO);
////
////        assertThat(response.data()).isEqualTo(userDTO);
////        assertThat(response.message()).isNull();
////
////        verify(authorizationService, times(1)).login(validAuthorizationDTO);
////    }
////
////    @Test
////    @DisplayName("Тест безуспешной авторизации")
////    void testLoginUnauthorizedException() throws UnauthorizedException {
////        when(authorizationService.login(invalidAuthorizationDTO)).thenThrow(new UnauthorizedException());
////
////
////        ResponseDTO<UserDTO> response = authorizationController.login(invalidAuthorizationDTO);
////
////        assertThat(response.message()).isNotNull();
////        assertThat(response.data()).isNull();
////
////
////        verify(authorizationService, times(1)).login(invalidAuthorizationDTO);
////    }
//}

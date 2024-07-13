package rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ylab.tom13.coworkingservice.out.entity.dto.AuthorizationDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.out.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.out.rest.controller.implementation.AuthorizationControllerSpring;
import io.ylab.tom13.coworkingservice.out.rest.services.AuthorizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import utils.MvcTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("Тесты контроллера авторизации")
public class AuthorizationControllerSpringMvcTest extends MvcTest {

    private static final String LOGIN = "/login";
    @Mock
    private AuthorizationService authorizationService;


    @InjectMocks
    private AuthorizationControllerSpring authorizationController;

    private AuthorizationDTO validAuthorizationDTO;
    private AuthorizationDTO invalidAuthorizationDTO;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authorizationController).build();
        validAuthorizationDTO = new AuthorizationDTO("john.doe@example.com", "password");
        invalidAuthorizationDTO = new AuthorizationDTO("john.doe@example.com", "wrongPassword");
        userDTO = new UserDTO(1L, "john.doe@example.com", "John", "Doe", Role.USER);
    }

    @Test
    @DisplayName("Тест успешной авторизации")
    void testLoginSuccess() throws Exception {
        when(authorizationService.login(validAuthorizationDTO)).thenReturn(userDTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(validAuthorizationDTO));


        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        assertThat(responseContent).isEqualTo(dummyToken);
    }

    @Test
    @DisplayName("Тест неуспешной авторизации")
    void testLoginUnauthorizedException() throws Exception {

        when(authorizationService.login(invalidAuthorizationDTO)).thenThrow(new UnauthorizedException());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(invalidAuthorizationDTO));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isUnauthorized());
    }
}

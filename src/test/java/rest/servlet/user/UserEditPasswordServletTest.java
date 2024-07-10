package rest.servlet.user;

import io.ylab.tom13.coworkingservice.out.entity.dto.PasswordChangeDTO;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.out.rest.servlet.user.UserEditPasswordServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import rest.servlet.UserServletTest;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Тестирование сервлета изменения пароля пользователя")
public class UserEditPasswordServletTest extends UserServletTest {

    @InjectMocks
    private UserEditPasswordServlet servlet;

    @BeforeEach
    public void setService() throws Exception {
        injectMocksUserServiceIntoServlet(servlet);
    }

    @Test
    @DisplayName("Успешное изменение пароля пользователя")
    public void testDoPostSuccess() throws Exception {
        when(request.getMethod()).thenReturn(POST_METHOD);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(passwordChangeDTOJson)));

        servlet.service(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String expectedResponse = String.format("Пароль пользователя с email: %s успешно изменен!", email);
        String actualResponse = objectMapper.readValue(responseWriter.toString(), String.class);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Ошибка авторизации")
    public void testDoPostUnauthorizedException() throws Exception {
        when(request.getMethod()).thenReturn(POST_METHOD);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(passwordChangeDTOJson)));

        doThrow(new UnauthorizedException("Unauthorized")).when(userEditService).editPassword(any(PasswordChangeDTO.class));

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

    @Test
    @DisplayName("Ошибка репозитория")
    public void testDoPostRepositoryException() throws Exception {
        when(request.getMethod()).thenReturn(POST_METHOD);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(passwordChangeDTOJson)));

        doThrow(new RepositoryException(repositoryException)).when(userEditService).editPassword(any(PasswordChangeDTO.class));

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка репозитория: " + repositoryException);
    }

}

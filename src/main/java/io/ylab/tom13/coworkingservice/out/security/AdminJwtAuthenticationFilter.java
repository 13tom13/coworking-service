package io.ylab.tom13.coworkingservice.out.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Фильтр аутентификации для администратора на основе JWT.
 */
@RequiredArgsConstructor
@Component("adminJwtAuthenticationFilter")
public class AdminJwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String ADMINISTRATOR = "ADMINISTRATOR";
    private static final String UNAUTHORIZED = "Пользователь не авторизован";
    private static final String FORBIDDEN = "Недостаточно прав для выполнения запроса";
    private static final String INVALID_AUTHORIZATION_TOKEN = "Неверный токен авторизации";
    private static final String BEARER = "Bearer ";
    private static final String ADMIN = "/admin";

    private final JwtUtil jwtUtil;

    /**
     * Метод фильтрации HTTP запроса для проверки авторизации администратора по JWT токену.
     *
     * @param request     HTTP запрос
     * @param response    HTTP ответ
     * @param filterChain цепочка фильтров
     * @throws ServletException если произошла ошибка в сервлете
     * @throws IOException      если произошла ошибка ввода-вывода
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, UNAUTHORIZED);
            return;
        }
        String token = authorizationHeader.substring(7);
        try {
            if (jwtUtil.validJwt(token)) {
                String roleFromToken = jwtUtil.getRoleFromToken(token);
                if (!ADMINISTRATOR.equals(roleFromToken)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, FORBIDDEN);
                    return;
                }
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, INVALID_AUTHORIZATION_TOKEN);
                return;
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Определяет, следует ли пропустить фильтрацию для указанного HTTP запроса.
     *
     * @param request HTTP запрос
     * @return true если фильтрация не требуется, false в противном случае
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith(ADMIN);
    }
}
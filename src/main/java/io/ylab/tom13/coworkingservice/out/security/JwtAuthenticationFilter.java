package io.ylab.tom13.coworkingservice.out.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Фильтр аутентификации на основе JWT.
 */
@Component("jwtAuthenticationFilter")
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String UNAUTHORIZED = "Пользователь не авторизован";
    private static final String INVALID_AUTHORIZATION_TOKEN = "Неверный токен авторизации";
    private static final String BEARER = "Bearer ";
    private static final String ID = "id";
    private static final String ROLE = "role";

    private static final List<String> PUBLIC_URLS = List.of(
            "/index.html",
            "/login",
            "/registration"
    );

    private final JwtUtil jwtUtil;

    /**
     * Конструктор для создания экземпляра фильтра с указанием зависимости от JwtUtil.
     *
     * @param jwtUtil утилита для работы с JWT
     */
    @Autowired
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Метод фильтрации HTTP запроса для проверки авторизации по JWT токену.
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
            if (!jwtUtil.validJwt(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            Long idFromToken = jwtUtil.getIdFromToken(token);
            String roleFromToken = jwtUtil.getRoleFromToken(token);
            request.setAttribute(ID, idFromToken);
            request.setAttribute(ROLE, roleFromToken);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, INVALID_AUTHORIZATION_TOKEN);
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
        String path = request.getServletPath();
        return PUBLIC_URLS.stream().anyMatch(path::equals);
    }
}
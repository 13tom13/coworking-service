package io.ylab.tom13.coworkingservice.out.security;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Фильтр аутентификации для модератора.
 */
@Component("moderatorJwtAuthenticationFilter")
public class ModeratorJwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String ADMINISTRATOR = "ADMINISTRATOR";
    private static final String MODERATOR = "MODERATOR";
    private static final String UNAUTHORIZED = "Пользователь не авторизован";
    private static final String FORBIDDEN = "Недостаточно прав для выполнения запроса";
    private static final String INVALID_AUTHORIZATION_TOKEN = "Неверный токен авторизации";
    private static final String BEARER = "Bearer ";
    public static final String COWORKING = "/coworking";

    private final JwtUtil jwtUtil;

    @Autowired
    public ModeratorJwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Проверяет JWT токен пользователя на валидность и наличие прав модератора или администратора.
     * Если токен не валиден или пользователь не имеет необходимых прав, возвращает соответствующую ошибку.
     * В противном случае передает запрос на выполнение следующего фильтра в цепочке.
     *
     * @param request     HTTP запрос
     * @param response    HTTP ответ
     * @param filterChain цепочка фильтров
     * @throws ServletException если происходит ошибка при фильтрации HTTP запроса
     * @throws IOException      если происходит ошибка ввода-вывода при фильтрации HTTP запроса
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
                if (!ADMINISTRATOR.equals(roleFromToken) && !MODERATOR.equals(roleFromToken)) {
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
     * Определяет, необходимо ли применять фильтр для текущего HTTP запроса.
     * Фильтр применяется только для URL, начинающихся с "/coworking".
     *
     * @param request HTTP запрос
     * @return true, если фильтр не должен применяться, иначе false
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return !uri.startsWith(COWORKING);
    }
}

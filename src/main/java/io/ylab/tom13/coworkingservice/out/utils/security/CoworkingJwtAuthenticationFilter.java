package io.ylab.tom13.coworkingservice.out.utils.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component("coworkingJwtAuthenticationFilter")
public class CoworkingJwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Autowired
    public CoworkingJwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Пользователь не авторизован");
            return;
        }
            String token = authorizationHeader.substring(7);
            try {
                if (jwtUtil.validJwt(token)) {
                    String roleFromToken = jwtUtil.getRoleFromToken(token);
                    if (!"ADMINISTRATOR".equals(roleFromToken) && !"MODERATOR".equals(roleFromToken)) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Недостаточно прав для выполнения запроса");
                        return;
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Неверный токен авторизации");
                    return;
                }
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
                return;
            }

        filterChain.doFilter(request, response);
    }

//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) {
//        return !request.getRequestURI().startsWith("/coworking");
//    }
}

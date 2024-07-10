package io.ylab.tom13.coworkingservice.out.utils.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component("jwtAuthenticationFilter")
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Autowired
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    private static final List<String> PUBLIC_URLS = List.of(
            "/",
            "/login",
            "/registration"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            System.out.println("User is not authorized");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Пользователь не авторизован");
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
            request.setAttribute("id", idFromToken);
            request.setAttribute("role", roleFromToken);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return PUBLIC_URLS.stream().anyMatch(path::equals);
    }
}
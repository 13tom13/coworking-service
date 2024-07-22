package io.ylab.tom13.coworkingservice.out.utils.audit.aspect;

import io.ylab.tom13.coworkingservice.out.security.JwtUtil;
import io.ylab.tom13.coworkingservice.out.utils.audit.service.UserAuditService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public abstract class UserAuditAspect {

    protected final UserAuditService userAuditService;
    protected final JwtUtil jwtUtil;

    private static final String BEARER = "Bearer ";
    private static final String HEADER_WARNING = "Заголовок Authorization не найден или имеет неверный формат.";
    protected static final String TOKEN_WARNING= "Не удалось извлечь ID пользователя из токена.";

    protected Optional<Long> getUserIdFromRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith(BEARER)) {
            String token = authHeader.substring(7);
            Long userId = jwtUtil.getIdFromToken(token);
            return Optional.of(userId);
        } else {
            log.warn(HEADER_WARNING);
            return Optional.empty();
        }
    }

    protected Optional<Long> getUserIdFromResponseEntity(ResponseEntity<?> responseEntity) {
        if (responseEntity != null && responseEntity.getBody() instanceof String token) {
            return Optional.of(jwtUtil.getIdFromToken(token));
        } else {
            return Optional.empty();
        }
    }

    protected abstract String determineAction(String methodName);
}

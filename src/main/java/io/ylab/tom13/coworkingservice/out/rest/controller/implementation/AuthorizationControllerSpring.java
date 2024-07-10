package io.ylab.tom13.coworkingservice.out.rest.controller.implementation;

import io.ylab.tom13.coworkingservice.out.entity.dto.AuthorizationDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.out.rest.controller.AuthorizationController;
import io.ylab.tom13.coworkingservice.out.rest.services.AuthorizationService;
import io.ylab.tom13.coworkingservice.out.utils.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Реализация интерфейса {@link AuthorizationController}.
 * Обрабатывает запросы на аутентификацию пользователя и возвращает соответствующие результаты.
 *
 * @inheritDoc
 */
@RestController
public class AuthorizationControllerSpring implements AuthorizationController {

    private final AuthorizationService authorizationService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthorizationControllerSpring(AuthorizationService authorizationService, JwtUtil jwtUtil) {
        this.authorizationService = authorizationService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthorizationDTO authorizationDTO) {
        try {
            UserDTO user = authorizationService.login(authorizationDTO);
            String token = jwtUtil.generateJwt(user.id(), user.role().name());
            return ResponseEntity.ok(token);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok("До свидание");
    }
}


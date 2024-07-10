//package io.ylab.tom13.coworkingservice.out.utils.aspects;
//
//import io.ylab.tom13.coworkingservice.out.utils.security.JwtUtil;
//import jakarta.servlet.http.HttpServletRequest;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.JoinPoint;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Component
//public class JwtAspect {
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Autowired
//    private HttpServletRequest request;
//
//    @Before("execution(* io.ylab.tom13.coworkingservice.out.rest.controller.UserEditController.*(..)) || " +
//            "execution(* io.ylab.tom13.coworkingservice.out.rest.controller.BookingController.*(..))")
//    public void checkUserAuthorization(JoinPoint joinPoint) {
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            throw new SecurityException("Missing or invalid Authorization header");
//        }
//        String token = authHeader.substring(7);
//        if (!jwtUtil.isValidJwt(token)) {
//            throw new SecurityException("Invalid JWT token");
//        }
//    }
//
//    @Before("execution(* io.ylab.tom13.coworkingservice.out.rest.controller.AdministrationController.*(..))")
//    public void checkAdminAuthorization(JoinPoint joinPoint) {
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            throw new SecurityException("Missing or invalid Authorization header");
//        }
//        String token = authHeader.substring(7);
//        if (!jwtUtil.isValidJwt(token)) {
//            throw new SecurityException("Invalid JWT token");
//        }
//        String role = jwtUtil.getRoleFromToken(token);
//        if (!"ADMINISTRATOR".equals(role)) {
//            throw new SecurityException("User is not authorized");
//        }
//    }
//
//    @Before("execution(* io.ylab.tom13.coworkingservice.out.rest.controller.CoworkingController.*(..))")
//    public void checkAdminOrModeratorAuthorization(JoinPoint joinPoint) {
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            throw new SecurityException("Missing or invalid Authorization header");
//        }
//        String token = authHeader.substring(7);
//        if (!jwtUtil.isValidJwt(token)) {
//            throw new SecurityException("Invalid JWT token");
//        }
//        String role = jwtUtil.getRoleFromToken(token);
//        if (!"ADMINISTRATOR".equals(role) && !"MODERATOR".equals(role)) {
//            throw new SecurityException("User is not authorized");
//        }
//    }
//}

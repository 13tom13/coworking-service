//package io.ylab.tom13.coworkingservice.out.utils.aspects;
//
//import io.ylab.tom13.coworkingservice.out.utils.security.JwtUtil;
//import jakarta.servlet.http.HttpServletRequest;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Component
//public class JwtFilterAspect {
//
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Before("@annotation(org.springframework.web.bind.annotation.GetMapping) && execution(* *(..)) && args(.., request)")
//    public void modifyRequestAttributes(HttpServletRequest request) {
//        String authorizationHeader = request.getHeader("Authorization");
//        System.out.println("Authorization header: " + authorizationHeader);
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            String token = authorizationHeader.substring(7);
//            try {
//                if (jwtUtil.isValidJwt(token)) {
//                    Long idFromToken = jwtUtil.getIdFromToken(token);
//                    String roleFromToken = jwtUtil.getRoleFromToken(token);
//
//                    request.setAttribute("id", idFromToken);
//                    request.setAttribute("role", roleFromToken);
//
//                    // Логирование установленных атрибутов (для отладки)
//                    System.out.println("Modified HttpServletRequest:");
//                    System.out.println("Request attribute 'id': " + request.getAttribute("id"));
//                    System.out.println("Request attribute 'role': " + request.getAttribute("role"));
//                } else {
//                    System.out.println("Token validation failed");
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//}

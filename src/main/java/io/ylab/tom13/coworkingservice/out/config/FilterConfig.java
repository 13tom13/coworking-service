//package io.ylab.tom13.coworkingservice.out.config;
//
//import io.ylab.tom13.coworkingservice.out.security.AdminJwtAuthenticationFilter;
//import io.ylab.tom13.coworkingservice.out.security.JwtAuthenticationFilter;
//import io.ylab.tom13.coworkingservice.out.security.JwtUtil;
//import io.ylab.tom13.coworkingservice.out.security.ModeratorJwtAuthenticationFilter;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class FilterConfig {
//
//    private static final String MAIN_PATH= "/*";
//    private static final String ADMIN_PATH= "/admin/*";
//    private static final String COWORKING_PATH= "/coworking/*";
//
//    @Bean
//    public FilterRegistrationBean<JwtAuthenticationFilter> jwtAuthenticationFilter(JwtUtil jwtUtil) {
//        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new JwtAuthenticationFilter(jwtUtil));
//        registrationBean.addUrlPatterns(MAIN_PATH);
//        registrationBean.setOrder(1);
//        return registrationBean;
//    }
//
//    @Bean
//    public FilterRegistrationBean<AdminJwtAuthenticationFilter> adminJwtAuthenticationFilter(JwtUtil jwtUtil) {
//        FilterRegistrationBean<AdminJwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new AdminJwtAuthenticationFilter(jwtUtil));
//        registrationBean.addUrlPatterns(ADMIN_PATH);
//        registrationBean.setOrder(2);
//        return registrationBean;
//    }
//
//    @Bean
//    public FilterRegistrationBean<ModeratorJwtAuthenticationFilter> moderatorJwtAuthenticationFilter(JwtUtil jwtUtil) {
//        FilterRegistrationBean<ModeratorJwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new ModeratorJwtAuthenticationFilter(jwtUtil));
//        registrationBean.addUrlPatterns(COWORKING_PATH);
//        registrationBean.setOrder(3);
//        return registrationBean;
//    }
//}
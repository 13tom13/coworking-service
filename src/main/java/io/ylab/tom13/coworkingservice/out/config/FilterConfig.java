package io.ylab.tom13.coworkingservice.out.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<DelegatingFilterProxy> jwtAuthenticationFilter() {
        FilterRegistrationBean<DelegatingFilterProxy> registrationBean = new FilterRegistrationBean<>();

        DelegatingFilterProxy delegatingFilterProxy = new DelegatingFilterProxy("jwtAuthenticationFilter");
        registrationBean.setFilter(delegatingFilterProxy);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);

        return registrationBean;
    }
}
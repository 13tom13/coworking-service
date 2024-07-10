package io.ylab.tom13.coworkingservice.out.config;

import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import org.springframework.stereotype.Component;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

@Component
public class MainWebAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext container) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.scan("io.ylab.tom13.coworkingservice");
        context.register(ApplicationConfig.class);
        container.addListener(new ContextLoaderListener(context));

        ServletRegistration.Dynamic dispatcher = container.addServlet("mvc", new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        FilterRegistration.Dynamic jwtFilter = container.addFilter("jwtAuthenticationFilter", new DelegatingFilterProxy("jwtAuthenticationFilter"));
        jwtFilter.addMappingForUrlPatterns(null, false, "/*");


        FilterRegistration.Dynamic adminFilter = container.addFilter("adminJwtAuthenticationFilter", new DelegatingFilterProxy("adminJwtAuthenticationFilter"));
        adminFilter.addMappingForUrlPatterns(null, false, "/admin/*");


        FilterRegistration.Dynamic coworkingFilter = container.addFilter("coworkingJwtAuthenticationFilter", new DelegatingFilterProxy("coworkingJwtAuthenticationFilter"));
        coworkingFilter.addMappingForUrlPatterns(null, false, "/coworking/*");
    }

}

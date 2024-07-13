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

/**
 * Инициализатор основного веб-приложения.
 */
@Component
public class MainWebAppInitializer implements WebApplicationInitializer {

    private static final String COWORKINGSERVICE = "io.ylab.tom13.coworkingservice";
    private static final String AUTHENTICATION_FILTER = "jwtAuthenticationFilter";
    private static final String ADMIN_FILTER = "adminJwtAuthenticationFilter";
    private static final String MODERATOR_FILTER = "moderatorJwtAuthenticationFilter";
    private static final String ADMIN = "/admin/*";
    private static final String COWORKING = "/coworking/*";

    @Override
    public void onStartup(ServletContext container) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.scan(COWORKINGSERVICE);

        container.addListener(new ContextLoaderListener(context));

        ServletRegistration.Dynamic dispatcher = container.addServlet("mvc", new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        FilterRegistration.Dynamic jwtFilter = container.addFilter(AUTHENTICATION_FILTER, new DelegatingFilterProxy(AUTHENTICATION_FILTER));
        jwtFilter.addMappingForUrlPatterns(null, false, "/*");

        FilterRegistration.Dynamic adminFilter = container.addFilter(ADMIN_FILTER, new DelegatingFilterProxy(ADMIN_FILTER));
        adminFilter.addMappingForUrlPatterns(null, false, ADMIN);

        FilterRegistration.Dynamic coworkingFilter = container.addFilter(MODERATOR_FILTER, new DelegatingFilterProxy(MODERATOR_FILTER));
        coworkingFilter.addMappingForUrlPatterns(null, false, COWORKING);
    }
}

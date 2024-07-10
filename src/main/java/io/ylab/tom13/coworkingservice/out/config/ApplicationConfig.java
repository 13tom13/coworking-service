package io.ylab.tom13.coworkingservice.out.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.*;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "io.ylab.tom13.coworkingservice")
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
public class ApplicationConfig {

    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Bean
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
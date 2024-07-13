package io.ylab.tom13.coworkingservice.out.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.ylab.tom13.coworkingservice.out.utils.YamlPropertySourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Конфигурационный класс приложения.
 */
@Configuration
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
public class ApplicationConfig {

    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    /**
     * Предоставляет настроенный экземпляр ObjectMapper с зарегистрированным модулем JavaTimeModule.
     *
     * @return Настроенный экземпляр ObjectMapper.
     */
    @Bean
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

}
package io.ylab.tom13.coworkingservice.out.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * Конфигурационный класс приложения.
 */
@Configuration
//@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
@RequiredArgsConstructor
public class ApplicationConfig {

    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private final DataSource dataSource;


    /**
     * Предоставляет настроенный экземпляр ObjectMapper с зарегистрированным модулем JavaTimeModule.
     *
     * @return Настроенный экземпляр ObjectMapper.
     */
    @Bean
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }


}
package io.ylab.tom13.coworkingservice.out.utils;


import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.util.Properties;

/**
 * Фабрика для загрузки YAML файлов в качестве источников свойств.
 */
public class YamlPropertySourceFactory implements PropertySourceFactory {

    /**
     * Создает источник свойств на основе указанного YAML файла.
     *
     * @param name     имя источника свойств
     * @param resource ресурс YAML файла
     * @return источник свойств на основе YAML
     */
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) {
        Properties propertiesFromYaml = loadYamlIntoProperties(resource);
        String sourceName = name != null ? name : determineSourceName(resource.getResource());
        return new PropertiesPropertySource(sourceName, propertiesFromYaml);
    }

    /**
     * Загружает содержимое YAML файла в объект свойств.
     *
     * @param resource ресурс YAML файла
     * @return объект свойств, содержащий загруженные из YAML файла свойства
     */
    private Properties loadYamlIntoProperties(EncodedResource resource) {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(resource.getResource());
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    /**
     * Определяет имя источника свойств на основе имени ресурса.
     *
     * @param resource ресурс
     * @return имя источника свойств
     */
    private String determineSourceName(org.springframework.core.io.Resource resource) {
        return resource instanceof org.springframework.core.io.FileSystemResource ?
                "file:" + resource.getFilename() :
                resource.getDescription();
    }
}
package com.openclassroom.devops.orion.microcrm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class SpringDataRestCustomization implements RepositoryRestConfigurer {

    private static final Logger log = LoggerFactory.getLogger(SpringDataRestCustomization.class);

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        String currentMethodName = LogContext.currentMethodName();
        log.info(LogMessages.REST_CONFIGURATION_START,
                getClass().getSimpleName(),
                currentMethodName,
                Person.class.getSimpleName() + "," + Organization.class.getSimpleName(),
                "CORS");

        config.exposeIdsFor(Person.class, Organization.class);
        cors.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PATCH", "DELETE")
                .exposedHeaders("Access-Control-Allow-Origin")
                .allowCredentials(false).maxAge(3600);

        log.info(LogMessages.REST_CONFIGURATION_DONE,
                getClass().getSimpleName(),
                currentMethodName,
                "*",
                "GET,POST,PATCH,DELETE");

        RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);
    }
}

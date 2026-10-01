package ru.ulstu.is.server.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping(Constants.API_URL + "/**")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedOrigins(Constants.DEV_ORIGIN);
        WebMvcConfigurer.super.addCorsMappings(registry);
    }
}
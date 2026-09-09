package com.scms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// CORS configuration - allows a frontend (e.g. React app) running on a different
// port/domain to call this backend's APIs from the browser.
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")               // apply to all API endpoints
                        .allowedOrigins("http://localhost:5173") // allowed frontend origin (change as needed)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")                  // allow all headers (e.g. Authorization, Content-Type)
                        .allowCredentials(true);               // allow cookies/auth headers to be sent
            }
        };
    }
}
package com.duoc.recetas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración de recursos estáticos.
 * 
 * Permite servir imágenes subidas desde el directorio /app/uploads.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // Servir archivos desde /app/uploads
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:/app/uploads/");
    }
}

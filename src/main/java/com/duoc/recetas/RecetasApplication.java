package com.duoc.recetas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación de Recetas Seguras.
 *
 * Esta aplicación implementa un sistema web seguro para gestión de recetas
 * con autenticación y autorización mediante Spring Security, cumpliendo
 * con los estándares de seguridad OWASP Top 10.
 *
 * @author Equipo de Desarrollo
 * @version 1.0.0
 */
@SpringBootApplication
public class RecetasApplication {

    private static final Logger logger = LoggerFactory.getLogger(RecetasApplication.class);

    /**
     * Método principal que inicia la aplicación Spring Boot.
     *
     * @param args Argumentos de línea de comandos
     */
    public static void main(String[] args) {
        if (Boolean.getBoolean("app.test.skipRun")) {
            logStartup();
            return;
        }
        SpringApplication.run(RecetasApplication.class, args);
        logStartup();
    }

    /**
     * Log de inicio centralizado para poder probar sin levantar el servidor.
     */
    private static void logStartup() {
        logger.info("============================================");
        logger.info("Aplicación iniciada correctamente");
        logger.info("Accede a: http://localhost:8082");
        logger.info("============================================");
    }
}

package com.duoc.recetas;

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

    /**
     * Método principal que inicia la aplicación Spring Boot.
     * 
     * @param args Argumentos de línea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(RecetasApplication.class, args);
        System.out.println("\n============================================");
        System.out.println("✅ Aplicación iniciada correctamente");
        System.out.println("🌐 Accede a: http://localhost:8080");
        System.out.println("============================================\n");
    }
}


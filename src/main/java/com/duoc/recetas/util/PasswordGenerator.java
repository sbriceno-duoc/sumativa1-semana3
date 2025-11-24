package com.duoc.recetas.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utilidad para generar contraseñas BCrypt.
 * 
 * Ejecutar este archivo para generar hashes de contraseñas.
 */
public class PasswordGenerator {

    private static final Logger logger = LoggerFactory.getLogger(PasswordGenerator.class);
    private static final String SEPARATOR = "===========================================";
    private static final String DEFAULT_PLACEHOLDER = "set-a-strong-password";

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        
        logger.info(SEPARATOR);
        logger.info("GENERADOR DE CONTRASEÑAS BCRYPT");
        logger.info(SEPARATOR);
        
        // Credenciales obtenidas desde variables de entorno para evitar valores fijos.
        String[] usuarios = {"admin", "usuario1", "usuario2", "chef"};
        String[] passwords = {
            getEnvOrPlaceholder("ADMIN_PASSWORD"),
            getEnvOrPlaceholder("DEFAULT_USER_PASSWORD"),
            getEnvOrPlaceholder("USUARIO2_PASSWORD"),
            getEnvOrPlaceholder("CHEF_PASSWORD")
        };
        
        for (int i = 0; i < usuarios.length; i++) {
            String hash = encoder.encode(passwords[i]);
            logger.info("Usuario: {}", usuarios[i]);
            logger.info("Contraseña: {}", mask(passwords[i]));
            logger.info("BCrypt Hash: {}", hash);
            logger.info("");
            
            // SQL para actualizar
            logger.info("UPDATE usuarios SET password = '{}' WHERE username = '{}';", hash, usuarios[i]);
            logger.info("");
        }
        
        logger.info(SEPARATOR);
        logger.info("COPIA ESTOS COMANDOS SQL Y EJECÚTALOS EN LA BD");
        logger.info(SEPARATOR);
    }

    private static String getEnvOrPlaceholder(String key) {
        return java.util.Optional.ofNullable(System.getenv(key)).orElse(DEFAULT_PLACEHOLDER);
    }

    private static String mask(String value) {
        if (value == null || value.isEmpty()) {
            return "<empty>";
        }
        return "*".repeat(Math.min(4, value.length())) + "...";
    }
}

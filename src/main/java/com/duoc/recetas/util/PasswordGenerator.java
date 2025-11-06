package com.duoc.recetas.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utilidad para generar contraseñas BCrypt.
 * 
 * Ejecutar este archivo para generar hashes de contraseñas.
 */
public class PasswordGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        
        System.out.println("===========================================");
        System.out.println("GENERADOR DE CONTRASEÑAS BCRYPT");
        System.out.println("===========================================\n");
        
        // Generar hashes para los usuarios de prueba
        String[] usuarios = {"admin", "usuario1", "usuario2", "chef"};
        String[] passwords = {"admin123", "usuario123", "usuario456", "chef2025"};
        
        for (int i = 0; i < usuarios.length; i++) {
            String hash = encoder.encode(passwords[i]);
            System.out.println("Usuario: " + usuarios[i]);
            System.out.println("Contraseña: " + passwords[i]);
            System.out.println("BCrypt Hash: " + hash);
            System.out.println();
            
            // SQL para actualizar
            System.out.println("UPDATE usuarios SET password = '" + hash + "' WHERE username = '" + usuarios[i] + "';");
            System.out.println();
        }
        
        System.out.println("===========================================");
        System.out.println("COPIA ESTOS COMANDOS SQL Y EJECÚTALOS EN LA BD");
        System.out.println("===========================================");
    }
}


package com.duoc.recetas.config;

import com.duoc.recetas.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Inicializador de datos para asegurar que los usuarios tengan contraseñas correctas.
 *
 * Este componente se ejecuta al iniciar la aplicación y actualiza las contraseñas
 * de los usuarios existentes con hashes BCrypt correctos.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private static final String DEFAULT_FALLBACK = "changeMe!123";

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("========================================");
        logger.info("INICIALIZANDO CONTRASEÑAS DE USUARIOS");
        logger.info("========================================");

        String adminPassword = getPasswordFromEnv(AppConstants.ENV_ADMIN_PASSWORD);
        String userPassword = getPasswordFromEnv(AppConstants.ENV_DEFAULT_USER_PASSWORD);

        // Actualizar contraseña de admin
        actualizarContrasena("admin", adminPassword);

        // Actualizar contraseñas de otros usuarios
        actualizarContrasena("usuario1", userPassword);
        actualizarContrasena("usuario2", userPassword);
        actualizarContrasena("chef", userPassword);

        logger.info("========================================");
        logger.info("USUARIOS LISTOS PARA USAR");
        logger.info("========================================");
        logger.info("Credenciales inicializadas (no se muestran contraseñas por seguridad). "
                + "Configura {} y {} en el entorno para personalizarlas.",
            AppConstants.ENV_ADMIN_PASSWORD, AppConstants.ENV_DEFAULT_USER_PASSWORD);
        logger.info("========================================");
    }

    private void actualizarContrasena(String username, String password) {
        usuarioRepository.findByUsername(username).ifPresent(usuario -> {
            String hashedPassword = passwordEncoder.encode(password);
            usuario.setPassword(hashedPassword);
            usuarioRepository.save(usuario);
            logger.info("Usuario '{}' actualizado", username);
        });
    }

    private String getPasswordFromEnv(String key) {
        return java.util.Optional.ofNullable(System.getenv(key)).orElse(DEFAULT_FALLBACK);
    }
}

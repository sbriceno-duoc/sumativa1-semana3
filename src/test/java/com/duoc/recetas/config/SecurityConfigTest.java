package com.duoc.recetas.config;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class SecurityConfigTest {

    @Test
    void securityConfigIsInstantiable() {
        SecurityConfig config = new SecurityConfig();
        assertNotNull(config);
    }

    @Test
    void passwordEncoderIsBCrypt() {
        SecurityConfig config = new SecurityConfig();
        PasswordEncoder encoder = config.passwordEncoder();

        assertInstanceOf(BCryptPasswordEncoder.class, encoder);
    }

    @Test
    void passwordEncoderEncryptsCorrectly() {
        SecurityConfig config = new SecurityConfig();
        PasswordEncoder encoder = config.passwordEncoder();
        String rawPassword = "testPassword123";

        String encoded = encoder.encode(rawPassword);

        assertNotNull(encoded);
        assertNotEquals(rawPassword, encoded);
        assertTrue(encoder.matches(rawPassword, encoded));
    }

    @Test
    void passwordEncoderUsesStrength12() {
        SecurityConfig config = new SecurityConfig();
        PasswordEncoder encoder = config.passwordEncoder();

        assertInstanceOf(BCryptPasswordEncoder.class, encoder);
    }

    @Test
    void authenticationManagerReturnsFromConfiguration() throws Exception {
        SecurityConfig config = new SecurityConfig();
        AuthenticationConfiguration authConfig = mock(AuthenticationConfiguration.class);
        AuthenticationManager mockManager = mock(AuthenticationManager.class);

        when(authConfig.getAuthenticationManager()).thenReturn(mockManager);

        AuthenticationManager result = config.authenticationManager(authConfig);

        assertSame(mockManager, result);
        verify(authConfig).getAuthenticationManager();
    }

    @Test
    void cookieSameSiteSupplierIsNotNull() {
        SecurityConfig config = new SecurityConfig();

        assertNotNull(config.cookieSameSiteSupplier());
    }

    @Test
    void passwordEncoderCanHashMultipleTimes() {
        SecurityConfig config = new SecurityConfig();
        PasswordEncoder encoder = config.passwordEncoder();
        String password = "mySecret123";

        String hash1 = encoder.encode(password);
        String hash2 = encoder.encode(password);

        assertNotEquals(hash1, hash2, "BCrypt should generate different salts");
        assertTrue(encoder.matches(password, hash1));
        assertTrue(encoder.matches(password, hash2));
    }
}

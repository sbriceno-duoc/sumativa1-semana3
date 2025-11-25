package com.duoc.recetas.config;

/**
 * Políticas y valores comunes de headers de seguridad para reutilizar en los
 * filtros y configuración de Spring Security.
 */
public final class SecurityPolicies {

    private SecurityPolicies() {
        // Utilidad estática
    }

    public static final String CONTENT_SECURITY_POLICY =
        "default-src 'self'; " +
        "script-src 'self'; " +
        "style-src 'self'; " +
        "img-src 'self' data: https://images.unsplash.com; " +
        "media-src 'self'; " +
        "font-src 'self'; " +
        "connect-src 'self'; " +
        "frame-ancestors 'none'; " +
        "base-uri 'self'; " +
        "form-action 'self'";

    public static final String PERMISSIONS_POLICY =
        "geolocation=(), microphone=(), camera=()";

    public static final String EXTENDED_PERMISSIONS_POLICY =
        "geolocation=(), microphone=(), camera=(), payment=(), usb=(), magnetometer=(), gyroscope=()";

    public static final String REFERRER_POLICY = "no-referrer";
}

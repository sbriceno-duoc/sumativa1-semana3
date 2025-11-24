package com.duoc.recetas.config;

import java.io.IOException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro para agregar headers de seguridad adicionales.
 * 
 * Soluciona alertas de ZAP Proxy relacionadas con headers faltantes:
 * - X-Content-Type-Options
 * - X-Permitted-Cross-Domain-Policies
 * - Referrer-Policy
 * - Permissions-Policy
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecurityHeadersFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        httpResponse.setHeader("Content-Security-Policy", SecurityPolicies.CONTENT_SECURITY_POLICY);

        // Solución: Falta encabezado X-Content-Type-Options (20)
        httpResponse.setHeader("X-Content-Type-Options", "nosniff");
        
        // X-Permitted-Cross-Domain-Policies
        httpResponse.setHeader("X-Permitted-Cross-Domain-Policies", "none");
        
        // Referrer-Policy adicional
        httpResponse.setHeader("Referrer-Policy", SecurityPolicies.REFERRER_POLICY);
        
        // Permissions-Policy (reemplazo de Feature-Policy)
        httpResponse.setHeader("Permissions-Policy", SecurityPolicies.EXTENDED_PERMISSIONS_POLICY);
        
        // X-Frame-Options adicional (por si no viene de Spring Security)
        if (httpResponse.getHeader("X-Frame-Options") == null) {
            httpResponse.setHeader("X-Frame-Options", "DENY");
        }
        
        // Cache-Control para páginas sensibles
        String requestURI = ((jakarta.servlet.http.HttpServletRequest) request).getRequestURI();
        if (requestURI.contains(AppConstants.LOGIN_PATH) || requestURI.contains("/recetas/detalle")) {
            httpResponse.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
            httpResponse.setHeader("Pragma", "no-cache");
            httpResponse.setHeader("Expires", "0");
        }
        
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inicialización si es necesaria
    }

    @Override
    public void destroy() {
        // Limpieza si es necesaria
    }
}

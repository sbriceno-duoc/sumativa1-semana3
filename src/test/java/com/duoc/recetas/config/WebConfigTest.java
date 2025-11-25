package com.duoc.recetas.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

class WebConfigTest {

    private WebConfig webConfig;

    @BeforeEach
    void setUp() {
        webConfig = new WebConfig();
    }

    @Test
    void addResourceHandlersConfiguresUploadsPath() {
        ResourceHandlerRegistry registry = mock(ResourceHandlerRegistry.class);
        ResourceHandlerRegistration registration = mock(ResourceHandlerRegistration.class);

        when(registry.addResourceHandler("/uploads/**")).thenReturn(registration);
        when(registration.addResourceLocations("file:/app/uploads/")).thenReturn(registration);

        webConfig.addResourceHandlers(registry);

        verify(registry).addResourceHandler("/uploads/**");
        verify(registration).addResourceLocations("file:/app/uploads/");
    }

    @Test
    void webConfigIsInstantiable() {
        assertNotNull(webConfig);
    }

    @Test
    @SuppressWarnings("null")
    void addResourceHandlersDoesNotThrowException() {
        ResourceHandlerRegistry registry = mock(ResourceHandlerRegistry.class);
        ResourceHandlerRegistration registration = mock(ResourceHandlerRegistration.class);

        when(registry.addResourceHandler(anyString())).thenReturn(registration);
        when(registration.addResourceLocations(anyString())).thenReturn(registration);

        assertDoesNotThrow(() -> webConfig.addResourceHandlers(registry));
    }

    @Test
    void webConfigImplementsWebMvcConfigurer() {
        assertTrue(webConfig instanceof org.springframework.web.servlet.config.annotation.WebMvcConfigurer);
    }

    @Test
    void addResourceHandlersVerifiesCorrectPath() {
        ResourceHandlerRegistry registry = mock(ResourceHandlerRegistry.class);
        ResourceHandlerRegistration registration = mock(ResourceHandlerRegistration.class);

        when(registry.addResourceHandler(anyString())).thenReturn(registration);
        when(registration.addResourceLocations(anyString())).thenReturn(registration);

        webConfig.addResourceHandlers(registry);

        verify(registry).addResourceHandler(eq("/uploads/**"));
        verify(registration).addResourceLocations(eq("file:/app/uploads/"));
    }
}

package com.duoc.recetas.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.duoc.recetas.model.Receta;
import com.duoc.recetas.model.Usuario;
import com.duoc.recetas.repository.UsuarioRepository;
import com.duoc.recetas.service.RecetaService;

@ExtendWith(MockitoExtension.class)
class PublicarRecetaControllerTest {

    @Mock
    private RecetaService recetaService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private PublicarRecetaController controller;

    @Test
    void mostrarFormularioDebePonerTitulo() {
        Model model = new ExtendedModelMap();

        String view = controller.mostrarFormulario(model);

        assertEquals("publicar-receta", view);
        assertEquals("Publicar Nueva Receta", model.getAttribute(com.duoc.recetas.config.AppConstants.ATTR_TITULO));
    }

    @Test
    void publicarRecetaSinUsuarioDebeRedirigir() {
        UserDetails userDetails = User.withUsername("testuser")
            .password("password")
            .roles("USER")
            .build();

        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        String result = controller.publicarReceta(
            "Receta Test", "Italiana", "Italia", "Fácil", 30,
            "Ingredientes", "Instrucciones", "Descripción", 4,
            null, userDetails, redirectAttributes
        );

        assertEquals("redirect:/recetas/publicar", result);
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("mensajeError"));
    }

    @Test
    void publicarRecetaConExitoSinMedia() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");

        UserDetails userDetails = User.withUsername("testuser")
            .password("password")
            .roles("USER")
            .build();

        Receta recetaGuardada = new Receta();
        recetaGuardada.setId(100L);

        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));
        when(recetaService.guardarReceta(any(Receta.class))).thenReturn(recetaGuardada);

        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        String result = controller.publicarReceta(
            "Pasta Carbonara", "Italiana", "Italia", "Media", 30,
            "Pasta, Huevos, Panceta", "Cocinar pasta...", "Deliciosa pasta", 4,
            null, userDetails, redirectAttributes
        );

        assertEquals("redirect:/recetas/detalle/100", result);
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("mensajeExito"));
        verify(recetaService).guardarReceta(any(Receta.class));
    }

    @Test
    void publicarRecetaConMediaVacia() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");

        UserDetails userDetails = User.withUsername("testuser")
            .password("password")
            .roles("USER")
            .build();

        Receta recetaGuardada = new Receta();
        recetaGuardada.setId(100L);

        MockMultipartFile emptyFile = new MockMultipartFile(
            "media", "", "image/jpeg", new byte[0]
        );

        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));
        when(recetaService.guardarReceta(any(Receta.class))).thenReturn(recetaGuardada);

        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        String result = controller.publicarReceta(
            "Receta", "Italiana", "Italia", "Fácil", 30,
            "Ingredientes", "Instrucciones", "Descripción", 4,
            new MultipartFile[]{emptyFile}, userDetails, redirectAttributes
        );

        assertEquals("redirect:/recetas/detalle/100", result);
    }

    @Test
    void publicarRecetaConError() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");

        UserDetails userDetails = User.withUsername("testuser")
            .password("password")
            .roles("USER")
            .build();

        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));
        when(recetaService.guardarReceta(any(Receta.class)))
            .thenThrow(new RuntimeException("Error de base de datos"));

        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        String result = controller.publicarReceta(
            "Receta", "Italiana", "Italia", "Fácil", 30,
            "Ingredientes", "Instrucciones", "Descripción", 4,
            null, userDetails, redirectAttributes
        );

        assertEquals("redirect:/recetas/publicar", result);
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("mensajeError"));
    }
}

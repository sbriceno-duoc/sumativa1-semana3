package com.duoc.recetas.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.duoc.recetas.model.Comentario;
import com.duoc.recetas.model.Receta;
import com.duoc.recetas.model.Usuario;
import com.duoc.recetas.model.Valoracion;
import com.duoc.recetas.repository.UsuarioRepository;
import com.duoc.recetas.service.ComentarioService;
import com.duoc.recetas.service.RecetaService;
import com.duoc.recetas.service.ValoracionService;

@ExtendWith(MockitoExtension.class)
class RecetaControllerTest {

    @Mock
    private RecetaService recetaService;

    @Mock
    private ComentarioService comentarioService;

    @Mock
    private ValoracionService valoracionService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private RecetaController recetaController;

    private Receta receta;
    private UserDetails userDetails;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        receta = new Receta();
        receta.setId(10L);
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("user");
        userDetails = new User("user", "pwd", List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    void buscarSinFiltrosRetornaTodasLasRecetas() {
        Model model = new ExtendedModelMap();
        when(recetaService.obtenerTodasLasRecetas()).thenReturn(List.of(receta));

        String view = recetaController.buscar(null, null, null, null, model);

        assertEquals("buscar", view);
        assertEquals(1, ((List<?>) model.getAttribute("recetas")).size());
    }

    @Test
    void detalleConRecetaEncontrada() {
        Model model = new ExtendedModelMap();
        when(recetaService.obtenerRecetaPorId(10L)).thenReturn(Optional.of(receta));
        when(comentarioService.obtenerComentariosPorReceta(receta)).thenReturn(List.of(new Comentario()));
        when(valoracionService.obtenerPromedioValoracion(receta)).thenReturn(4.0);
        when(valoracionService.contarValoraciones(receta)).thenReturn(2L);
        when(usuarioRepository.findByUsername("user")).thenReturn(Optional.of(usuario));
        when(valoracionService.obtenerValoracionUsuario(receta, usuario)).thenReturn(Optional.of(new Valoracion()));

        String view = recetaController.detalle(10L, model, userDetails);

        assertEquals("detalle", view);
        assertTrue(model.containsAttribute("receta"));
        assertEquals(1, model.getAttribute("totalComentarios"));
    }

    @Test
    void detalleCuandoNoExisteRecetaDevuelveError() {
        Model model = new ExtendedModelMap();
        when(recetaService.obtenerRecetaPorId(99L)).thenReturn(Optional.empty());

        String view = recetaController.detalle(99L, model, userDetails);

        assertEquals("error", view);
    }

    @Test
    void crearComentarioRedirigeAlDetalle() {
        RedirectAttributes redirect = new RedirectAttributesModelMap();
        when(recetaService.obtenerRecetaPorId(10L)).thenReturn(Optional.of(receta));
        when(usuarioRepository.findByUsername("user")).thenReturn(Optional.of(usuario));

        String view = recetaController.crearComentario(10L, "texto", userDetails, redirect);

        assertEquals("redirect:/recetas/detalle/10", view);
        verify(comentarioService).crearComentario(receta, usuario, "texto");
    }

    @Test
    void valorarRecetaGuardaValoracion() {
        RedirectAttributes redirect = new RedirectAttributesModelMap();
        when(recetaService.obtenerRecetaPorId(10L)).thenReturn(Optional.of(receta));
        when(usuarioRepository.findByUsername("user")).thenReturn(Optional.of(usuario));

        String view = recetaController.valorarReceta(10L, 5, userDetails, redirect);

        assertEquals("redirect:/recetas/detalle/10", view);
        verify(valoracionService).crearOActualizarValoracion(receta, usuario, 5);
    }

    @Test
    void eliminarRecetaPropiaExito() {
        RedirectAttributes redirect = new RedirectAttributesModelMap();
        when(usuarioRepository.findByUsername("user")).thenReturn(Optional.of(usuario));
        when(recetaService.eliminarRecetaPorAutor(10L, usuario)).thenReturn(true);

        String view = recetaController.eliminarReceta(10L, userDetails, redirect);

        assertEquals("redirect:/recetas/buscar", view);
        assertTrue(redirect.getFlashAttributes().containsKey("mensajeExito"));
    }
}

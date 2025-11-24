package com.duoc.recetas.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.duoc.recetas.model.Comentario;
import com.duoc.recetas.model.Receta;
import com.duoc.recetas.model.Usuario;
import com.duoc.recetas.repository.ComentarioRepository;

@ExtendWith(MockitoExtension.class)
class ComentarioServiceTest {

    @Mock
    private ComentarioRepository comentarioRepository;

    @InjectMocks
    private ComentarioService comentarioService;

    @Test
    void debeObtenerComentariosPorReceta() {
        Receta receta = new Receta();
        when(comentarioRepository.findByRecetaOrderByFechaCreacionDesc(receta))
            .thenReturn(List.of(new Comentario()));

        List<Comentario> resultado = comentarioService.obtenerComentariosPorReceta(receta);

        assertEquals(1, resultado.size());
    }

    @Test
    void debeCrearComentario() {
        Receta receta = new Receta();
        Usuario usuario = new Usuario();
        when(comentarioRepository.save(any(Comentario.class))).thenAnswer(inv -> inv.getArgument(0));

        Comentario comentario = comentarioService.crearComentario(receta, usuario, "texto");

        assertEquals(receta, comentario.getReceta());
        assertEquals(usuario, comentario.getUsuario());
        assertEquals("texto", comentario.getTexto());
    }

    @Test
    void debeContarComentarios() {
        Receta receta = new Receta();
        when(comentarioRepository.countByReceta(receta)).thenReturn(2L);

        Long total = comentarioService.contarComentarios(receta);

        assertEquals(2L, total);
    }
}

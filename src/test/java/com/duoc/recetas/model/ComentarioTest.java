package com.duoc.recetas.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class ComentarioTest {

    @Test
    void gettersSettersYPersistencia() {
        Comentario c = new Comentario();
        Receta receta = new Receta();
        Usuario usuario = new Usuario();
        LocalDateTime now = LocalDateTime.now();

        c.setId(10L);
        c.setReceta(receta);
        c.setUsuario(usuario);
        c.setTexto("texto");
        c.setFechaCreacion(now);

        assertEquals(10L, c.getId());
        assertSame(receta, c.getReceta());
        assertSame(usuario, c.getUsuario());
        assertEquals("texto", c.getTexto());
        assertEquals(now, c.getFechaCreacion());

        Comentario nuevo = new Comentario();
        nuevo.onCreate();
        assertNotNull(nuevo.getFechaCreacion());
    }
}

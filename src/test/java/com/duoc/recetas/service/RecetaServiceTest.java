package com.duoc.recetas.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import com.duoc.recetas.model.Receta;
import com.duoc.recetas.model.Usuario;
import com.duoc.recetas.repository.RecetaRepository;

@ExtendWith(MockitoExtension.class)
class RecetaServiceTest {

    @Mock
    private RecetaRepository recetaRepository;

    @InjectMocks
    private RecetaService recetaService;

    private Receta receta;

    @BeforeEach
    void setUp() {
        receta = new Receta();
        receta.setId(1L);
        receta.setVisualizaciones(1);
    }

    @Test
    void debeObtenerTodasLasRecetas() {
        when(recetaRepository.findAll()).thenReturn(List.of(receta));

        List<Receta> resultado = recetaService.obtenerTodasLasRecetas();

        assertEquals(1, resultado.size());
        verify(recetaRepository).findAll();
    }

    @Test
    void debeLimitarRecetasPopularesATres() {
        List<Receta> populares = Arrays.asList(new Receta(), new Receta(), new Receta(), new Receta());
        when(recetaRepository.findTopRecetasByValoracionDesc()).thenReturn(populares);

        List<Receta> resultado = recetaService.obtenerRecetasPopulares();

        assertEquals(3, resultado.size());
    }

    @Test
    void debeIncrementarVisualizaciones() {
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(receta));

        recetaService.incrementarVisualizaciones(1L);

        ArgumentCaptor<Receta> captor = ArgumentCaptor.forClass(Receta.class);
        verify(recetaRepository).save(captor.capture());
        assertEquals(2, captor.getValue().getVisualizaciones());
    }

    @Test
    void debeEliminarRecetaCuandoAutorCoincide() {
        Usuario autor = new Usuario();
        autor.setId(5L);
        receta.setAutor(autor);
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(receta));

        boolean resultado = recetaService.eliminarRecetaPorAutor(1L, autor);

        assertTrue(resultado);
        verify(recetaRepository).deleteById(1L);
    }

    @Test
    void noDebeEliminarRecetaSiAutorNoCoincide() {
        Usuario autor = new Usuario();
        autor.setId(5L);
        Usuario otro = new Usuario();
        otro.setId(6L);
        receta.setAutor(autor);
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(receta));

        boolean resultado = recetaService.eliminarRecetaPorAutor(1L, otro);

        assertFalse(resultado);
        verify(recetaRepository, never()).deleteById(anyLong());
    }

    @Test
    void debeBuscarRecetasPorCriterios() {
        List<Receta> lista = new ArrayList<>();
        when(recetaRepository.buscarRecetas("a", "b", "c", "d")).thenReturn(lista);

        List<Receta> resultado = recetaService.buscarRecetas("a", "b", "c", "d");

        assertSame(lista, resultado);
    }
}

package com.duoc.recetas.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.duoc.recetas.model.Receta;
import com.duoc.recetas.model.Usuario;
import com.duoc.recetas.model.Valoracion;
import com.duoc.recetas.repository.ValoracionRepository;

@ExtendWith(MockitoExtension.class)
class ValoracionServiceTest {

    @Mock
    private ValoracionRepository valoracionRepository;

    @InjectMocks
    private ValoracionService valoracionService;

    @Test
    void debeCrearNuevaValoracionCuandoNoExiste() {
        Receta receta = new Receta();
        Usuario usuario = new Usuario();
        when(valoracionRepository.findByRecetaAndUsuario(receta, usuario)).thenReturn(Optional.empty());
        when(valoracionRepository.save(any(Valoracion.class))).thenAnswer(inv -> inv.getArgument(0));

        Valoracion valoracion = valoracionService.crearOActualizarValoracion(receta, usuario, 4);

        assertEquals(4, valoracion.getEstrellas());
        assertEquals(receta, valoracion.getReceta());
        assertEquals(usuario, valoracion.getUsuario());
    }

    @Test
    void debeActualizarValoracionExistente() {
        Receta receta = new Receta();
        Usuario usuario = new Usuario();
        Valoracion existente = new Valoracion();
        existente.setEstrellas(2);
        when(valoracionRepository.findByRecetaAndUsuario(receta, usuario)).thenReturn(Optional.of(existente));
        when(valoracionRepository.save(any(Valoracion.class))).thenAnswer(inv -> inv.getArgument(0));

        Valoracion actualizada = valoracionService.crearOActualizarValoracion(receta, usuario, 5);

        assertEquals(5, actualizada.getEstrellas());
        assertSame(existente, actualizada);
    }

    @Test
    void debeValidarRangoDeEstrellas() {
        assertThrows(IllegalArgumentException.class, () -> {
            valoracionService.crearOActualizarValoracion(new Receta(), new Usuario(), 0);
        });
    }

    @Test
    void debeRetornarPromedioCeroCuandoNoHayValoraciones() {
        Receta receta = new Receta();
        when(valoracionRepository.getPromedioValoracion(receta)).thenReturn(null);

        Double promedio = valoracionService.obtenerPromedioValoracion(receta);

        assertEquals(0.0, promedio);
    }

    @Test
    void debeContarValoraciones() {
        Receta receta = new Receta();
        when(valoracionRepository.countByReceta(receta)).thenReturn(3L);

        Long total = valoracionService.contarValoraciones(receta);

        assertEquals(3L, total);
        verify(valoracionRepository).countByReceta(receta);
    }
}

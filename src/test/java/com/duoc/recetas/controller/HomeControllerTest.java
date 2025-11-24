package com.duoc.recetas.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import com.duoc.recetas.model.Receta;
import com.duoc.recetas.service.RecetaService;
import com.duoc.recetas.service.ValoracionService;

@ExtendWith(MockitoExtension.class)
class HomeControllerTest {

    @Mock
    private ValoracionService valoracionService;

    @Mock
    private RecetaService recetaService;

    @InjectMocks
    private HomeController homeController;

    @Test
    void indexDebeAgregarListasYRatings() {
        Receta r1 = new Receta();
        r1.setId(1L);
        Receta r2 = new Receta();
        r2.setId(2L);
        when(recetaService.obtenerRecetasPopulares()).thenReturn(List.of(r1, r2));
        when(recetaService.obtenerRecetasRecientes()).thenReturn(List.of(r1));
        when(valoracionService.obtenerPromedioValoracion(r1)).thenReturn(4.5);
        when(valoracionService.obtenerPromedioValoracion(r2)).thenReturn(3.0);
        Model model = new ExtendedModelMap();

        String view = homeController.index(model);

        assertEquals("index", view);
        assertTrue(model.containsAttribute("recetasPopulares"));
        Map<Long, Double> ratingMap = (Map<Long, Double>) model.getAttribute("ratingMap");
        assertEquals(2, ratingMap.size());
    }

    @Test
    void loginDebeMostrarError() {
        Model model = new ExtendedModelMap();

        String view = homeController.login("true", null, model);

        assertEquals("login", view);
        assertEquals("Usuario o contraseña incorrectos", model.getAttribute("error"));
    }

    @Test
    void buscarAtajoRedirige() {
        assertEquals("redirect:/recetas/buscar", homeController.buscarAtajo());
    }

    @Test
    void errorDevuelveVistaError() {
        Model model = new ExtendedModelMap();

        String view = homeController.error(model);

        assertEquals("error", view);
        assertEquals("Ha ocurrido un error. Por favor, intenta nuevamente.", model.getAttribute("mensaje"));
    }
}

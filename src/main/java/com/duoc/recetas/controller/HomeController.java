package com.duoc.recetas.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.duoc.recetas.model.Receta;
import com.duoc.recetas.service.RecetaService;

/**
 * Controlador para la página de inicio y navegación principal.
 *
 * Maneja las rutas públicas de la aplicación.
 */
@Controller
public class HomeController {
    private static final String ATTR_TITULO = com.duoc.recetas.config.AppConstants.ATTR_TITULO;

    private final com.duoc.recetas.service.ValoracionService valoracionService;
    private final RecetaService recetaService;

    public HomeController(com.duoc.recetas.service.ValoracionService valoracionService,
                         RecetaService recetaService) {
        this.valoracionService = valoracionService;
        this.recetaService = recetaService;
    }

    /**
     * Página de inicio (HOME) - PÚBLICA
     * 
     * Muestra las recetas más recientes y populares, además de banners comerciales.
     * 
     * @param model Modelo para pasar datos a la vista
     * @return Nombre de la vista index.html
     */
    @GetMapping({"/", "/home", "/index"})
    public String index(Model model) {
        
        // Obtener recetas populares y recientes
        List<Receta> recetasPopulares = recetaService.obtenerRecetasPopulares();
        List<Receta> recetasRecientes = recetaService.obtenerRecetasRecientes();

        // Calcular el promedio de valoración para cada receta popular
        java.util.Map<Long, Double> ratingMap = new java.util.HashMap<>();
        for (Receta receta : recetasPopulares) {
            ratingMap.put(receta.getId(), valoracionService.obtenerPromedioValoracion(receta));
        }

        // Limitar a 3 recetas de cada tipo para no sobrecargar la página
        if (recetasPopulares.size() > 3) {
            recetasPopulares = recetasPopulares.subList(0, 3);
        }
        if (recetasRecientes.size() > 3) {
            recetasRecientes = recetasRecientes.subList(0, 3);
        }

        // Agregar datos al modelo
        model.addAttribute("recetasPopulares", recetasPopulares);
        model.addAttribute("recetasRecientes", recetasRecientes);
        model.addAttribute("ratingMap", ratingMap);
        model.addAttribute(ATTR_TITULO, "Bienvenido a Recetas Seguras");

        return "index";
    }

    /**
     * Página de login - PÚBLICA
     *
     * Muestra el formulario de inicio de sesión.
     *
     * @param error Indica si hubo un error en el login
     * @param logout Indica si el usuario cerró sesión
     * @param model Modelo para pasar datos a la vista
     * @return Nombre de la vista login.html
     */
    @GetMapping(com.duoc.recetas.config.AppConstants.LOGIN_PATH)
    public String login(
            @org.springframework.web.bind.annotation.RequestParam(required = false) String error,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String logout,
            Model model) {

        if (error != null) {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
        }

        if (logout != null) {
            model.addAttribute("mensaje", "Sesión cerrada correctamente");
        }

        model.addAttribute(ATTR_TITULO, "Iniciar Sesión");

        return "login";
    }

    /**
     * Redirección corta a la página de búsqueda.
     *
     * Atajo: /buscar → /recetas/buscar
     *
     * @return Redirección a /recetas/buscar
     */
    @GetMapping("/buscar")
    public String buscarAtajo() {
        return "redirect:/recetas/buscar";
    }

    /**
     * Manejo de errores personalizados.
     *
     * @param model Modelo para pasar datos a la vista
     * @return Nombre de la vista error.html
     */
    @GetMapping("/error")
    public String error(Model model) {
        model.addAttribute(ATTR_TITULO, "Error");
        model.addAttribute("mensaje", "Ha ocurrido un error. Por favor, intenta nuevamente.");
        return "error";
    }
}

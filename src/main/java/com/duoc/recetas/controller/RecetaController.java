package com.duoc.recetas.controller;

import com.duoc.recetas.model.Receta;
import com.duoc.recetas.service.RecetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

/**
 * Controlador para la gestión de recetas.
 * 
 * Maneja las operaciones de búsqueda y visualización de recetas.
 */
@Controller
@RequestMapping("/recetas")
public class RecetaController {

    @Autowired
    private RecetaService recetaService;

    /**
     * Página de búsqueda de recetas - PÚBLICA
     * 
     * Permite buscar recetas por nombre, tipo de cocina, país de origen y dificultad.
     * 
     * @param nombre Nombre de la receta (opcional)
     * @param tipoCocina Tipo de cocina (opcional)
     * @param paisOrigen País de origen (opcional)
     * @param dificultad Dificultad (opcional)
     * @param model Modelo para pasar datos a la vista
     * @return Nombre de la vista buscar.html
     */
    @GetMapping("/buscar")
    public String buscar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String tipoCocina,
            @RequestParam(required = false) String paisOrigen,
            @RequestParam(required = false) String dificultad,
            Model model) {
        
        // Convertir strings vacíos a null para que la búsqueda funcione correctamente
        nombre = (nombre != null && nombre.trim().isEmpty()) ? null : nombre;
        tipoCocina = (tipoCocina != null && tipoCocina.trim().isEmpty()) ? null : tipoCocina;
        paisOrigen = (paisOrigen != null && paisOrigen.trim().isEmpty()) ? null : paisOrigen;
        dificultad = (dificultad != null && dificultad.trim().isEmpty()) ? null : dificultad;
        
        List<Receta> recetas;
        
        // Si no se proporcionó ningún criterio de búsqueda, mostrar todas las recetas
        if (nombre == null && tipoCocina == null && paisOrigen == null && dificultad == null) {
            recetas = recetaService.obtenerTodasLasRecetas();
        } else {
            // Buscar con los criterios proporcionados
            recetas = recetaService.buscarRecetas(nombre, tipoCocina, paisOrigen, dificultad);
        }
        
        // Agregar datos al modelo
        model.addAttribute("recetas", recetas);
        model.addAttribute("nombre", nombre);
        model.addAttribute("tipoCocina", tipoCocina);
        model.addAttribute("paisOrigen", paisOrigen);
        model.addAttribute("dificultad", dificultad);
        model.addAttribute("titulo", "Buscar Recetas");
        model.addAttribute("totalResultados", recetas.size());
        
        return "buscar";
    }

    /**
     * Página de detalle de una receta - PRIVADA (requiere autenticación)
     * 
     * Muestra toda la información detallada de una receta específica:
     * - Ingredientes
     * - Instrucciones de preparación
     * - Tiempo de cocción
     * - Dificultad
     * - Fotografías
     * 
     * SEGURIDAD: Solo usuarios autenticados pueden ver los detalles de las recetas.
     * Esto cumple con A01: Broken Access Control del OWASP Top 10.
     * 
     * @param id ID de la receta
     * @param model Modelo para pasar datos a la vista
     * @return Nombre de la vista detalle.html o error.html si no existe
     */
    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        
        // Buscar la receta por ID
        Optional<Receta> recetaOpt = recetaService.obtenerRecetaPorId(id);
        
        if (recetaOpt.isPresent()) {
            Receta receta = recetaOpt.get();
            
            // Incrementar contador de visualizaciones
            recetaService.incrementarVisualizaciones(id);
            
            // Agregar receta al modelo
            model.addAttribute("receta", receta);
            model.addAttribute("titulo", receta.getNombre());
            
            return "detalle";
        } else {
            // Si no existe la receta, mostrar error
            model.addAttribute("titulo", "Receta no encontrada");
            model.addAttribute("mensaje", "La receta solicitada no existe o ha sido eliminada.");
            return "error";
        }
    }

    /**
     * Lista todas las recetas - PÚBLICA
     * 
     * Muestra un listado simple de todas las recetas disponibles.
     * 
     * @param model Modelo para pasar datos a la vista
     * @return Nombre de la vista buscar.html
     */
    @GetMapping("/lista")
    public String lista(Model model) {
        List<Receta> recetas = recetaService.obtenerTodasLasRecetas();
        
        model.addAttribute("recetas", recetas);
        model.addAttribute("titulo", "Todas las Recetas");
        model.addAttribute("totalResultados", recetas.size());
        
        return "buscar";
    }
}


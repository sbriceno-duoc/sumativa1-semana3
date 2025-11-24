package com.duoc.recetas.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.duoc.recetas.model.Comentario;
import com.duoc.recetas.model.Receta;
import com.duoc.recetas.model.Usuario;
import com.duoc.recetas.model.Valoracion;
import com.duoc.recetas.repository.UsuarioRepository;
import com.duoc.recetas.service.ComentarioService;
import com.duoc.recetas.service.RecetaService;
import com.duoc.recetas.service.ValoracionService;

/**
 * Controlador para la gestión de recetas.
 *
 * Maneja las operaciones de búsqueda y visualización de recetas.
 */
@Controller
@RequestMapping("/recetas")
public class RecetaController {
    private static final String ATTR_TITULO = com.duoc.recetas.config.AppConstants.ATTR_TITULO;

    private final RecetaService recetaService;
    private final ComentarioService comentarioService;
    private final ValoracionService valoracionService;
    private final UsuarioRepository usuarioRepository;

    public RecetaController(RecetaService recetaService,
                           ComentarioService comentarioService,
                           ValoracionService valoracionService,
                           UsuarioRepository usuarioRepository) {
        this.recetaService = recetaService;
        this.comentarioService = comentarioService;
        this.valoracionService = valoracionService;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Elimina una receta publicada por el usuario autenticado (solo autor).
     * Muestra mensaje de éxito o error y redirige a la búsqueda.
     */
    @PostMapping("/detalle/{id}/eliminar")
    public String eliminarReceta(@PathVariable Long id,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(userDetails.getUsername());
        if (usuarioOpt.isPresent()) {
            boolean eliminado = recetaService.eliminarRecetaPorAutor(id, usuarioOpt.get());
            if (eliminado) {
                redirectAttributes.addFlashAttribute("mensajeExito", "Receta eliminada correctamente.");
                return "redirect:/recetas/buscar";
            } else {
                redirectAttributes.addFlashAttribute("mensajeError", "No tienes permiso para eliminar esta receta.");
                return "redirect:/recetas/detalle/" + id;
            }
        }
        redirectAttributes.addFlashAttribute("mensajeError", "Usuario no autenticado.");
        return "redirect:/recetas/detalle/" + id;
    }

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
        model.addAttribute(ATTR_TITULO, "Buscar Recetas");
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
     * - Comentarios
     * - Valoraciones
     *
     * SEGURIDAD: Solo usuarios autenticados pueden ver los detalles de las recetas.
     * Esto cumple con A01: Broken Access Control del OWASP Top 10.
     *
     * @param id ID de la receta
     * @param model Modelo para pasar datos a la vista
     * @param userDetails Usuario autenticado
     * @return Nombre de la vista detalle.html o error.html si no existe
     */
    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model,
                         @AuthenticationPrincipal UserDetails userDetails) {

        // Buscar la receta por ID
        Optional<Receta> recetaOpt = recetaService.obtenerRecetaPorId(id);

        if (recetaOpt.isPresent()) {
            Receta receta = recetaOpt.get();

            // Inicializar la colección de mediaFiles para evitar LazyInitializationException
            receta.getMediaFiles().size();

            // Incrementar contador de visualizaciones
            recetaService.incrementarVisualizaciones(id);

            // Obtener comentarios
            List<Comentario> comentarios = comentarioService.obtenerComentariosPorReceta(receta);

            // Obtener estadísticas de valoración
            Double promedioValoracion = valoracionService.obtenerPromedioValoracion(receta);
            Long totalValoraciones = valoracionService.contarValoraciones(receta);

            // Obtener valoración del usuario actual si existe
            Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(userDetails.getUsername());
            Optional<Valoracion> valoracionUsuario = Optional.empty();
            if (usuarioOpt.isPresent()) {
                valoracionUsuario = valoracionService.obtenerValoracionUsuario(receta, usuarioOpt.get());
            }

            // Agregar datos al modelo
            model.addAttribute("receta", receta);
            model.addAttribute(ATTR_TITULO, receta.getNombre());
            model.addAttribute("comentarios", comentarios);
            model.addAttribute("totalComentarios", comentarios.size());
            model.addAttribute("promedioValoracion", promedioValoracion);
            model.addAttribute("totalValoraciones", totalValoraciones);
            model.addAttribute("valoracionUsuario", valoracionUsuario.orElse(null));

            return "detalle";
        } else {
            // Si no existe la receta, mostrar error
            model.addAttribute(ATTR_TITULO, "Receta no encontrada");
            model.addAttribute("mensaje", "La receta solicitada no existe o ha sido eliminada.");
            return "error";
        }
    }
    
    /**
     * Crea un comentario en una receta - PRIVADA
     * 
     * @param id ID de la receta
     * @param texto Texto del comentario
     * @param userDetails Usuario autenticado
     * @param redirectAttributes Atributos para redirección
     * @return Redirección al detalle de la receta
     */
    @PostMapping("/detalle/{id}/comentario")
    public String crearComentario(@PathVariable Long id, 
                                  @RequestParam String texto,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  RedirectAttributes redirectAttributes) {
        
        Optional<Receta> recetaOpt = recetaService.obtenerRecetaPorId(id);
        
        if (recetaOpt.isPresent() && texto != null && !texto.trim().isEmpty()) {
            Receta receta = recetaOpt.get();
            Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(userDetails.getUsername());
            
            if (usuarioOpt.isPresent()) {
                comentarioService.crearComentario(receta, usuarioOpt.get(), texto.trim());
                redirectAttributes.addFlashAttribute("mensajeExito", "Comentario agregado correctamente");
            }
        }
        
        return "redirect:/recetas/detalle/" + id;
    }
    
    /**
     * Crea o actualiza una valoración de una receta - PRIVADA
     * 
     * @param id ID de la receta
     * @param estrellas Cantidad de estrellas (1-5)
     * @param userDetails Usuario autenticado
     * @param redirectAttributes Atributos para redirección
     * @return Redirección al detalle de la receta
     */
    @PostMapping("/detalle/{id}/valorar")
    public String valorarReceta(@PathVariable Long id, 
                               @RequestParam Integer estrellas,
                               @AuthenticationPrincipal UserDetails userDetails,
                               RedirectAttributes redirectAttributes) {
        
        Optional<Receta> recetaOpt = recetaService.obtenerRecetaPorId(id);
        
        if (recetaOpt.isPresent()) {
            try {
                Receta receta = recetaOpt.get();
                Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(userDetails.getUsername());
                
                if (usuarioOpt.isPresent()) {
                    valoracionService.crearOActualizarValoracion(receta, usuarioOpt.get(), estrellas);
                    redirectAttributes.addFlashAttribute("mensajeExito", "Valoración registrada correctamente");
                }
            } catch (IllegalArgumentException e) {
                redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
            }
        }
        
        return "redirect:/recetas/detalle/" + id;
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
        model.addAttribute(ATTR_TITULO, "Todas las Recetas");
        model.addAttribute("totalResultados", recetas.size());

        return "buscar";
    }
}

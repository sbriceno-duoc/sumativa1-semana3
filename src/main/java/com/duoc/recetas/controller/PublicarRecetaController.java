package com.duoc.recetas.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.duoc.recetas.model.Receta;
import com.duoc.recetas.model.RecetaMedia;
import com.duoc.recetas.model.Usuario;
import com.duoc.recetas.repository.UsuarioRepository;
import com.duoc.recetas.service.RecetaService;

/**
 * Controlador para la publicación de recetas.
 * 
 * PRIVADO: Solo usuarios autenticados pueden publicar recetas.
 */
@Controller
@RequestMapping("/recetas")
@PreAuthorize("isAuthenticated()")
public class PublicarRecetaController {
    
    private final RecetaService recetaService;
    private final UsuarioRepository usuarioRepository;

    public PublicarRecetaController(RecetaService recetaService, UsuarioRepository usuarioRepository) {
        this.recetaService = recetaService;
        this.usuarioRepository = usuarioRepository;
    }
    
    // Directorio donde se guardarán las imágenes y videos subidos
    private static final String UPLOAD_DIR = "/app/uploads/";
    private static final String ATTR_TITULO = com.duoc.recetas.config.AppConstants.ATTR_TITULO;
    
    // Tipos de archivo permitidos
    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of(
        "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
    );
    private static final Set<String> ALLOWED_VIDEO_TYPES = Set.of(
        "video/mp4", "video/webm", "video/ogg", "video/quicktime"
    );
    private static final long MAX_FILE_SIZE_BYTES = 10 * 1024 * 1024; // 10MB
    
    /**
     * Muestra el formulario para publicar una nueva receta - PRIVADO
     * 
     * @param model Modelo para pasar datos a la vista
     * @return Nombre de la vista publicar-receta.html
     */
    @GetMapping("/publicar")
    public String mostrarFormulario(Model model) {
        model.addAttribute(ATTR_TITULO, "Publicar Nueva Receta");
        return "publicar-receta";
    }
    
    /**
     * Procesa el formulario de publicación de receta - PRIVADO
     * 
     * @param nombre Nombre de la receta
     * @param tipoCocina Tipo de cocina
     * @param paisOrigen País de origen
     * @param dificultad Dificultad (Fácil, Media, Difícil)
     * @param tiempoPreparacion Tiempo de preparación en minutos
     * @param ingredientes Ingredientes (separados por saltos de línea)
     * @param instrucciones Instrucciones de preparación
     * @param descripcion Descripción breve
     * @param porciones Número de porciones
     * @param media Lista de archivos de imagen o video (opcional)
     * @param userDetails Usuario autenticado
     * @param redirectAttributes Atributos para redirección
     * @return Redirección al detalle de la receta creada
     */
    @PostMapping("/publicar")
    public String publicarReceta(
            @RequestParam String nombre,
            @RequestParam String tipoCocina,
            @RequestParam String paisOrigen,
            @RequestParam String dificultad,
            @RequestParam Integer tiempoPreparacion,
            @RequestParam String ingredientes,
            @RequestParam String instrucciones,
            @RequestParam(required = false) String descripcion,
            @RequestParam Integer porciones,
            @RequestParam(required = false) MultipartFile[] media,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Obtener el usuario actual
            Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(userDetails.getUsername());
            
            if (!usuarioOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("mensajeError", "Usuario no encontrado");
                return "redirect:/recetas/publicar";
            }
            
            Usuario usuario = usuarioOpt.get();
            
            // Crear nueva receta
            Receta receta = new Receta();
            receta.setNombre(nombre);
            receta.setTipoCocina(tipoCocina);
            receta.setPaisOrigen(paisOrigen);
            receta.setDificultad(dificultad);
            receta.setTiempoPreparacion(tiempoPreparacion);
            receta.setIngredientes(ingredientes);
            receta.setInstrucciones(instrucciones);
            receta.setDescripcion(descripcion);
            receta.setPorciones(porciones);
            receta.setAutor(usuario);
            receta.setPopular(false);
            receta.setReciente(true);
            
            // Procesar múltiples archivos si se proporcionan
            if (media != null && media.length > 0) {
                for (int i = 0; i < media.length; i++) {
                    MultipartFile file = media[i];
                    if (!file.isEmpty()) {
                        String mediaUrl = guardarMedia(file);
                        String mediaType = determinarTipoMedia(file.getContentType());
                        
                        // Crear entidad RecetaMedia
                        RecetaMedia recetaMedia = new RecetaMedia();
                        recetaMedia.setReceta(receta);
                        recetaMedia.setMediaUrl(mediaUrl);
                        recetaMedia.setMediaType(mediaType);
                        recetaMedia.setOrden(i);
                        
                        receta.getMediaFiles().add(recetaMedia);
                        
                        // Mantener compatibilidad: el primer archivo también se guarda en fotoUrl
                        if (i == 0) {
                            receta.setFotoUrl(mediaUrl);
                            receta.setMediaType(mediaType);
                        }
                    }
                }
            }
            
            // Guardar receta en la base de datos
            Receta recetaGuardada = recetaService.guardarReceta(receta);
            
            redirectAttributes.addFlashAttribute("mensajeExito", "Receta publicada correctamente");
            return "redirect:/recetas/detalle/" + recetaGuardada.getId();
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", 
                "Error al publicar la receta: " + e.getMessage());
            return "redirect:/recetas/publicar";
        }
    }
    
    /**
     * Guarda un archivo de imagen o video en el sistema de archivos.
     * 
     * @param media Archivo MultipartFile (imagen o video)
     * @return URL relativa del archivo guardado
     * @throws IOException Si hay error al guardar el archivo
     */
    private String guardarMedia(MultipartFile media) throws IOException {
        // Validar tipo de archivo
        String contentType = media.getContentType();
        if (!esArchivoValido(contentType)) {
            throw new IOException("Tipo de archivo no permitido. Solo se aceptan imágenes (JPG, PNG, GIF, WebP) y videos (MP4, WebM, OGG, MOV)");
        }
        if (media.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new IOException("Archivo demasiado grande. Tamaño máximo 10MB");
        }
        
        // Crear directorio si no existe
        Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Generar nombre único para el archivo
        String extension = "";
        String originalFilename = media.getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String filename = UUID.randomUUID().toString() + extension;
        
        // Guardar archivo
        Path filePath = uploadPath.resolve(filename).normalize();
        if (!filePath.startsWith(uploadPath)) {
            throw new IOException("Ruta de archivo no válida");
        }
        Files.copy(media.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        // Retornar URL relativa
        return "/uploads/" + filename;
    }
    
    /**
     * Valida si el tipo de archivo es permitido (imagen o video).
     * 
     * @param contentType Tipo MIME del archivo
     * @return true si es válido, false en caso contrario
     */
    private boolean esArchivoValido(String contentType) {
        if (contentType == null) {
            return false;
        }
        
        return ALLOWED_IMAGE_TYPES.contains(contentType) || ALLOWED_VIDEO_TYPES.contains(contentType);
    }
    
    /**
     * Determina si el archivo es una imagen o un video.
     * 
     * @param contentType Tipo MIME del archivo
     * @return "image" o "video"
     */
    private String determinarTipoMedia(String contentType) {
        if (contentType == null) {
            return "image";
        }
        
        return contentType.startsWith("video/") ? "video" : "image";
    }
}

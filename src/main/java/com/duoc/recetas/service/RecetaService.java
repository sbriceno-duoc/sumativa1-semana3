package com.duoc.recetas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.duoc.recetas.model.Receta;
import com.duoc.recetas.repository.RecetaRepository;

/**
 * Servicio para la lógica de negocio de Recetas.
 *
 * Proporciona métodos para buscar, crear y gestionar recetas.
 */
@Service
@Transactional
public class RecetaService {

    private final RecetaRepository recetaRepository;

    public RecetaService(RecetaRepository recetaRepository) {
        this.recetaRepository = recetaRepository;
    }

    /**
     * Obtiene todas las recetas.
     * 
     * @return Lista de todas las recetas
     */
    public List<Receta> obtenerTodasLasRecetas() {
        return recetaRepository.findAll();
    }

    /**
     * Obtiene una receta por su ID.
     * 
     * @param id ID de la receta
     * @return Optional con la receta si existe
     */
    public Optional<Receta> obtenerRecetaPorId(Long id) {
        return recetaRepository.findById(id);
    }

    /**
     * Obtiene las 3 recetas más populares por valoración promedio.
     */
    public List<Receta> obtenerRecetasPopulares() {
        List<Receta> populares = recetaRepository.findTopRecetasByValoracionDesc();
        return populares.size() > 3 ? populares.subList(0, 3) : populares;
    }

    /**
     * Obtiene las 3 recetas más recientes por fecha de creación.
     */
    public List<Receta> obtenerRecetasRecientes() {
        return recetaRepository.findTop3ByOrderByFechaCreacionDesc();
    }

    /**
     * Busca recetas según múltiples criterios.
     * 
     * @param nombre Nombre de la receta (opcional)
     * @param tipoCocina Tipo de cocina (opcional)
     * @param paisOrigen País de origen (opcional)
     * @param dificultad Dificultad (opcional)
     * @return Lista de recetas que coinciden con los criterios
     */
    public List<Receta> buscarRecetas(String nombre, String tipoCocina, 
                                       String paisOrigen, String dificultad) {
        return recetaRepository.buscarRecetas(nombre, tipoCocina, paisOrigen, dificultad);
    }

    /**
     * Guarda o actualiza una receta.
     * 
     * @param receta Receta a guardar
     * @return Receta guardada
     */
    public Receta guardarReceta(Receta receta) {
        return recetaRepository.save(receta);
    }

    /**
     * Incrementa el contador de visualizaciones de una receta.
     * 
     * @param id ID de la receta
     */
    public void incrementarVisualizaciones(Long id) {
        Optional<Receta> recetaOpt = recetaRepository.findById(id);
        recetaOpt.ifPresent(receta -> {
            receta.setVisualizaciones(receta.getVisualizaciones() + 1);
            recetaRepository.save(receta);
        });
    }
    /**
     * Elimina una receta solo si el usuario es el autor.
     * @param recetaId ID de la receta
     * @param usuario Usuario autenticado
     * @return true si se eliminó, false si no es el autor
     */
    public boolean eliminarRecetaPorAutor(Long recetaId, com.duoc.recetas.model.Usuario usuario) {
        java.util.Optional<com.duoc.recetas.model.Receta> recetaOpt = recetaRepository.findById(recetaId);
        if (recetaOpt.isPresent()) {
            com.duoc.recetas.model.Receta receta = recetaOpt.get();
            if (receta.getAutor() != null && receta.getAutor().getId().equals(usuario.getId())) {
                recetaRepository.deleteById(recetaId);
                return true;
            }
        }
        return false;
    }
}


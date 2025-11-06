package com.duoc.recetas.service;

import com.duoc.recetas.model.Receta;
import com.duoc.recetas.repository.RecetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la lógica de negocio de Recetas.
 * 
 * Proporciona métodos para buscar, crear y gestionar recetas.
 */
@Service
@Transactional
public class RecetaService {

    @Autowired
    private RecetaRepository recetaRepository;

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
     * Obtiene las recetas populares.
     * 
     * @return Lista de recetas populares
     */
    public List<Receta> obtenerRecetasPopulares() {
        return recetaRepository.findByPopularTrue();
    }

    /**
     * Obtiene las recetas recientes.
     * 
     * @return Lista de recetas recientes
     */
    public List<Receta> obtenerRecetasRecientes() {
        return recetaRepository.findByRecienteTrue();
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
}


package com.duoc.recetas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.duoc.recetas.model.Comentario;
import com.duoc.recetas.model.Receta;

/**
 * Repositorio para la entidad Comentario.
 */
@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    
    /**
     * Encuentra todos los comentarios de una receta ordenados por fecha descendente.
     * 
     * @param receta La receta de la cual obtener comentarios
     * @return Lista de comentarios
     */
    List<Comentario> findByRecetaOrderByFechaCreacionDesc(Receta receta);
    
    /**
     * Cuenta los comentarios de una receta.
     * 
     * @param receta La receta a contar comentarios
     * @return Número de comentarios
     */
    Long countByReceta(Receta receta);
}

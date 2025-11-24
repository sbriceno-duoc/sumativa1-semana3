package com.duoc.recetas.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.duoc.recetas.model.Receta;
import com.duoc.recetas.model.Usuario;
import com.duoc.recetas.model.Valoracion;

/**
 * Repositorio para la entidad Valoracion.
 */
@Repository
public interface ValoracionRepository extends JpaRepository<Valoracion, Long> {
    
    /**
     * Encuentra la valoración de un usuario para una receta.
     * 
     * @param receta La receta
     * @param usuario El usuario
     * @return Optional con la valoración si existe
     */
    Optional<Valoracion> findByRecetaAndUsuario(Receta receta, Usuario usuario);
    
    /**
     * Calcula el promedio de valoración de una receta.
     * 
     * @param receta La receta
     * @return Promedio de valoración
     */
    @Query("SELECT AVG(v.estrellas) FROM Valoracion v WHERE v.receta = :receta")
    Double getPromedioValoracion(@Param("receta") Receta receta);
    
    /**
     * Cuenta las valoraciones de una receta.
     * 
     * @param receta La receta a contar valoraciones
     * @return Número de valoraciones
     */
    Long countByReceta(Receta receta);
}

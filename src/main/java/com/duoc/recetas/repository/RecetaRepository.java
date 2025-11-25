package com.duoc.recetas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.duoc.recetas.model.Receta;

/**
 * Repositorio para operaciones de base de datos relacionadas con Receta.
 */
@Repository
public interface RecetaRepository extends JpaRepository<Receta, Long> {
       /**
        * Obtiene las recetas ordenadas por promedio de valoración (rating) descendente.
        * Solo recetas con al menos una valoración.
        */
       @Query("SELECT r FROM Receta r JOIN Valoracion v ON v.receta = r GROUP BY r.id ORDER BY AVG(v.estrellas) DESC")
       List<Receta> findTopRecetasByValoracionDesc();

       /**
        * Obtiene las recetas ordenadas por fecha de creación descendente.
        */
       List<Receta> findTop3ByOrderByFechaCreacionDesc();

    /**
     * Encuentra recetas marcadas como populares.
     * 
     * @return Lista de recetas populares
     */
    List<Receta> findByPopularTrue();

    /**
     * Encuentra recetas marcadas como recientes.
     * 
     * @return Lista de recetas recientes
     */
    List<Receta> findByRecienteTrue();

    /**
     * Busca recetas por nombre (búsqueda parcial, case insensitive).
     * 
     * @param nombre Parte del nombre a buscar
     * @return Lista de recetas que coinciden
     */
    List<Receta> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Busca recetas por tipo de cocina.
     * 
     * @param tipoCocina Tipo de cocina
     * @return Lista de recetas
     */
    List<Receta> findByTipoCocinaContainingIgnoreCase(String tipoCocina);

    /**
     * Busca recetas por país de origen.
     * 
     * @param paisOrigen País de origen
     * @return Lista de recetas
     */
    List<Receta> findByPaisOrigenContainingIgnoreCase(String paisOrigen);

    /**
     * Busca recetas por dificultad.
     * 
     * @param dificultad Dificultad (Fácil, Intermedio, Difícil)
     * @return Lista de recetas
     */
    List<Receta> findByDificultadIgnoreCase(String dificultad);

    /**
     * Búsqueda avanzada de recetas por múltiples criterios.
     * 
     * @param nombre Nombre de la receta
     * @param tipoCocina Tipo de cocina
     * @param paisOrigen País de origen
     * @param dificultad Dificultad
     * @return Lista de recetas que coinciden con los criterios
     */
    @Query("SELECT r FROM Receta r WHERE " +
           "(:nombre IS NULL OR LOWER(r.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
           "(:tipoCocina IS NULL OR LOWER(r.tipoCocina) LIKE LOWER(CONCAT('%', :tipoCocina, '%'))) AND " +
           "(:paisOrigen IS NULL OR LOWER(r.paisOrigen) LIKE LOWER(CONCAT('%', :paisOrigen, '%'))) AND " +
           "(:dificultad IS NULL OR LOWER(r.dificultad) = LOWER(:dificultad))")
    List<Receta> buscarRecetas(@Param("nombre") String nombre,
                                @Param("tipoCocina") String tipoCocina,
                                @Param("paisOrigen") String paisOrigen,
                                @Param("dificultad") String dificultad);
}


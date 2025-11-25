package com.duoc.recetas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.duoc.recetas.model.RecetaMedia;

/**
 * Repositorio para gestionar los archivos multimedia de las recetas.
 */
@Repository
public interface RecetaMediaRepository extends JpaRepository<RecetaMedia, Long> {
    
}

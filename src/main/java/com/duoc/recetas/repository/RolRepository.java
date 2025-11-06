package com.duoc.recetas.repository;

import com.duoc.recetas.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para operaciones de base de datos relacionadas con Rol.
 */
@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {

    /**
     * Busca un rol por su nombre.
     * 
     * @param nombre Nombre del rol (ej: ROLE_USER)
     * @return Optional con el rol si existe
     */
    Optional<Rol> findByNombre(String nombre);
}


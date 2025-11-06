package com.duoc.recetas.repository;

import com.duoc.recetas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para operaciones de base de datos relacionadas con Usuario.
 * 
 * Extiende JpaRepository para obtener métodos CRUD automáticos.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario por su nombre de usuario.
     * 
     * @param username Nombre de usuario a buscar
     * @return Optional con el usuario si existe
     */
    Optional<Usuario> findByUsername(String username);

    /**
     * Verifica si existe un usuario con el username dado.
     * 
     * @param username Nombre de usuario a verificar
     * @return true si existe, false en caso contrario
     */
    Boolean existsByUsername(String username);
}


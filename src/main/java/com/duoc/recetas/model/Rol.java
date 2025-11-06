package com.duoc.recetas.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa un rol de usuario en el sistema.
 * 
 * Los roles definen los permisos y nivel de acceso de los usuarios.
 * Ejemplos: ROLE_USER, ROLE_ADMIN
 */
@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del rol (debe comenzar con ROLE_)
     * Ejemplo: ROLE_USER, ROLE_ADMIN
     */
    @Column(nullable = false, unique = true, length = 50)
    private String nombre;

    /**
     * Constructor vacío requerido por JPA.
     */
    public Rol() {
        // Constructor por defecto
    }

    /**
     * Constructor para crear un rol con solo el nombre.
     * 
     * @param nombre Nombre del rol
     */
    public Rol(String nombre) {
        this.nombre = nombre;
    }

    // Getters y Setters manuales (por si Lombok falla)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}


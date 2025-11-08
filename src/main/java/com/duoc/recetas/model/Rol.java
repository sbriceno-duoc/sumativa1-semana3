package com.duoc.recetas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
@Data //Genera getter & setter
@NoArgsConstructor //Genera constructor vacío
@AllArgsConstructor //Genera constructor poblado
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
}

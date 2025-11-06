package com.duoc.recetas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Entidad que representa un usuario del sistema.
 * 
 * Esta clase almacena la información de autenticación y autorización
 * de los usuarios que pueden acceder al sistema.
 */
@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de usuario único para el login.
     */
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    /**
     * Contraseña encriptada del usuario.
     * IMPORTANTE: Siempre debe almacenarse encriptada con BCrypt.
     */
    @NotBlank(message = "La contraseña es obligatoria")
    @Column(nullable = false)
    private String password;

    /**
     * Nombre completo del usuario (opcional).
     */
    @Column(length = 100)
    private String nombreCompleto;

    /**
     * Correo electrónico del usuario (opcional).
     */
    @Column(length = 100)
    private String email;

    /**
     * Indica si la cuenta está habilitada.
     */
    @Column(nullable = false)
    private Boolean enabled = true;

    /**
     * Roles asignados al usuario.
     * Un usuario puede tener múltiples roles.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuarios_roles",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Rol> roles = new HashSet<>();

    /**
     * Constructor vacío requerido por JPA.
     */
    public Usuario() {
        // Constructor por defecto
    }

    /**
     * Constructor para crear un usuario con datos básicos.
     * 
     * @param username Nombre de usuario
     * @param password Contraseña (será encriptada)
     * @param nombreCompleto Nombre completo
     * @param enabled Si la cuenta está habilitada
     */
    public Usuario(String username, String password, String nombreCompleto, Boolean enabled) {
        this.username = username;
        this.password = password;
        this.nombreCompleto = nombreCompleto;
        this.enabled = enabled;
    }

    /**
     * Agrega un rol al usuario.
     * 
     * @param rol Rol a agregar
     */
    public void agregarRol(Rol rol) {
        this.roles.add(rol);
    }

    // Getters y Setters manuales (por si Lombok falla)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Rol> getRoles() {
        return roles;
    }

    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }
}


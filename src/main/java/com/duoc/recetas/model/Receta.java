package com.duoc.recetas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad que representa una receta de cocina.
 * 
 * Almacena toda la información necesaria para mostrar una receta,
 * incluyendo ingredientes, instrucciones, tiempo de preparación, etc.
 */
@Entity
@Table(name = "recetas")
@Data
@NoArgsConstructor
public class Receta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de la receta.
     */
    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false, length = 150)
    private String nombre;

    /**
     * Tipo de cocina (ej: Italiana, Mexicana, Asiática, etc.)
     */
    @Column(length = 50)
    private String tipoCocina;

    /**
     * País de origen de la receta.
     */
    @Column(length = 50)
    private String paisOrigen;

    /**
     * Nivel de dificultad: Fácil, Intermedio, Difícil
     */
    @NotBlank(message = "La dificultad es obligatoria")
    @Column(nullable = false, length = 20)
    private String dificultad;

    /**
     * Tiempo de cocción en minutos.
     */
    @NotNull(message = "El tiempo de cocción es obligatorio")
    @Positive(message = "El tiempo debe ser positivo")
    @Column(nullable = false)
    private Integer tiempoCoccion;

    /**
     * Lista de ingredientes necesarios (texto largo).
     */
    @Column(columnDefinition = "TEXT")
    private String ingredientes;

    /**
     * Instrucciones de preparación paso a paso (texto largo).
     */
    @Column(columnDefinition = "TEXT")
    private String instrucciones;

    /**
     * URL de la fotografía de la receta.
     */
    @Column(length = 255)
    private String fotoUrl;

    /**
     * Descripción corta de la receta.
     */
    @Column(length = 500)
    private String descripcion;

    /**
     * Número de porciones que rinde la receta.
     */
    @Column
    private Integer porciones;

    /**
     * Indica si es una receta popular.
     */
    @Column(nullable = false)
    private Boolean popular = false;

    /**
     * Indica si es una receta reciente.
     */
    @Column(nullable = false)
    private Boolean reciente = false;

    /**
     * Fecha de creación de la receta.
     */
    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    /**
     * Número de visualizaciones de la receta.
     */
    @Column
    private Integer visualizaciones = 0;

    /**
     * Constructor vacío requerido por JPA.
     */
    public Receta() {
        // Constructor por defecto
    }

    /**
     * Inicializa la fecha de creación antes de persistir.
     */
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }

    /**
     * Constructor simplificado para crear recetas rápidamente.
     */
    public Receta(String nombre, String tipoCocina, String paisOrigen, String dificultad, 
                  Integer tiempoCoccion, String ingredientes, String instrucciones, 
                  String fotoUrl, String descripcion, Integer porciones) {
        this.nombre = nombre;
        this.tipoCocina = tipoCocina;
        this.paisOrigen = paisOrigen;
        this.dificultad = dificultad;
        this.tiempoCoccion = tiempoCoccion;
        this.ingredientes = ingredientes;
        this.instrucciones = instrucciones;
        this.fotoUrl = fotoUrl;
        this.descripcion = descripcion;
        this.porciones = porciones;
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

    public String getTipoCocina() {
        return tipoCocina;
    }

    public void setTipoCocina(String tipoCocina) {
        this.tipoCocina = tipoCocina;
    }

    public String getPaisOrigen() {
        return paisOrigen;
    }

    public void setPaisOrigen(String paisOrigen) {
        this.paisOrigen = paisOrigen;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public Integer getTiempoCoccion() {
        return tiempoCoccion;
    }

    public void setTiempoCoccion(Integer tiempoCoccion) {
        this.tiempoCoccion = tiempoCoccion;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getPorciones() {
        return porciones;
    }

    public void setPorciones(Integer porciones) {
        this.porciones = porciones;
    }

    public Boolean getPopular() {
        return popular;
    }

    public void setPopular(Boolean popular) {
        this.popular = popular;
    }

    public Boolean getReciente() {
        return reciente;
    }

    public void setReciente(Boolean reciente) {
        this.reciente = reciente;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Integer getVisualizaciones() {
        return visualizaciones;
    }

    public void setVisualizaciones(Integer visualizaciones) {
        this.visualizaciones = visualizaciones;
    }
}


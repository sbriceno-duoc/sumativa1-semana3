package com.duoc.recetas.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa una receta de cocina.
 * 
 * Almacena toda la información necesaria para mostrar una receta,
 * incluyendo ingredientes, instrucciones, tiempo de preparación, etc.
 */
@Entity
@Table(name = "recetas")
@Data //Genera getter & setter
@NoArgsConstructor //Genera constructor vacío
@AllArgsConstructor //Genera constructor poblado
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
     * Tiempo de preparación en minutos.
     */
    @NotNull(message = "El tiempo de preparación es obligatorio")
    @Positive(message = "El tiempo debe ser positivo")
    @Column(nullable = false)
    private Integer tiempoPreparacion;

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
     * URL de la fotografía o video de la receta.
     */
    @Column(length = 255)
    private String fotoUrl;
    
    /**
     * Tipo de media: "image" o "video".
     */
    @Column(length = 10)
    private String mediaType = "image";

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
     * Usuario autor de la receta.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario autor;

    /**
     * Archivos multimedia asociados a esta receta.
     */
    @OneToMany(mappedBy = "receta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<RecetaMedia> mediaFiles = new ArrayList<>();

    /**
     * Inicializa la fecha de creación antes de persistir.
     */
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}

package com.duoc.recetas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa archivos multimedia asociados a una receta.
 * 
 * Permite almacenar múltiples imágenes y videos por receta.
 */
@Entity
@Table(name = "recetas_media")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecetaMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Receta a la que pertenece este archivo multimedia.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receta_id", nullable = false)
    private Receta receta;

    /**
     * URL del archivo multimedia.
     */
    @Column(name = "media_url", nullable = false, length = 255)
    private String mediaUrl;

    /**
     * Tipo de archivo: "image" o "video".
     */
    @Column(name = "media_type", nullable = false, length = 10)
    private String mediaType;

    /**
     * Orden de visualización (para ordenar galería).
     */
    @Column(name = "orden")
    private Integer orden;
}

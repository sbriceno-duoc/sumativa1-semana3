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
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "valoraciones", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"receta_id", "usuario_id"})
})
public class Valoracion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receta_id", nullable = false)
    private Receta receta;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @Column(nullable = false)
    private Integer estrellas;
    
    public Valoracion() {}
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Receta getReceta() {
        return receta;
    }
    
    public void setReceta(Receta receta) {
        this.receta = receta;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public Integer getEstrellas() {
        return estrellas;
    }
    
    public void setEstrellas(Integer estrellas) {
        if (estrellas < 1 || estrellas > 5) {
            throw new IllegalArgumentException("Las estrellas deben estar entre 1 y 5");
        }
        this.estrellas = estrellas;
    }
}

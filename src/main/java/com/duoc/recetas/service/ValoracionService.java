package com.duoc.recetas.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.duoc.recetas.model.Receta;
import com.duoc.recetas.model.Usuario;
import com.duoc.recetas.model.Valoracion;
import com.duoc.recetas.repository.ValoracionRepository;

/**
 * Servicio para gestionar valoraciones.
 */
@Service
public class ValoracionService {
    
    private final ValoracionRepository valoracionRepository;

    public ValoracionService(ValoracionRepository valoracionRepository) {
        this.valoracionRepository = valoracionRepository;
    }
    
    /**
     * Obtiene la valoración de un usuario para una receta.
     * 
     * @param receta La receta
     * @param usuario El usuario
     * @return Optional con la valoración si existe
     */
    public Optional<Valoracion> obtenerValoracionUsuario(Receta receta, Usuario usuario) {
        return valoracionRepository.findByRecetaAndUsuario(receta, usuario);
    }
    
    /**
     * Crea o actualiza una valoración.
     * 
     * @param receta La receta a valorar
     * @param usuario El usuario que valora
     * @param estrellas La cantidad de estrellas (1-5)
     * @return La valoración creada o actualizada
     */
    @Transactional
    public Valoracion crearOActualizarValoracion(Receta receta, Usuario usuario, Integer estrellas) {
        if (estrellas < 1 || estrellas > 5) {
            throw new IllegalArgumentException("Las estrellas deben estar entre 1 y 5");
        }
        
        Optional<Valoracion> existente = valoracionRepository.findByRecetaAndUsuario(receta, usuario);
        
        Valoracion valoracion;
        if (existente.isPresent()) {
            valoracion = existente.get();
            valoracion.setEstrellas(estrellas);
        } else {
            valoracion = new Valoracion();
            valoracion.setReceta(receta);
            valoracion.setUsuario(usuario);
            valoracion.setEstrellas(estrellas);
        }
        
        return valoracionRepository.save(valoracion);
    }
    
    /**
     * Obtiene el promedio de valoración de una receta.
     * 
     * @param receta La receta
     * @return Promedio de valoración, 0.0 si no hay valoraciones
     */
    public Double obtenerPromedioValoracion(Receta receta) {
        Double promedio = valoracionRepository.getPromedioValoracion(receta);
        return promedio != null ? promedio : 0.0;
    }
    
    /**
     * Cuenta las valoraciones de una receta.
     * 
     * @param receta La receta
     * @return Número de valoraciones
     */
    public Long contarValoraciones(Receta receta) {
        return valoracionRepository.countByReceta(receta);
    }
}

package com.duoc.recetas.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.duoc.recetas.model.Comentario;
import com.duoc.recetas.model.Receta;
import com.duoc.recetas.model.Usuario;
import com.duoc.recetas.repository.ComentarioRepository;

/**
 * Servicio para gestionar comentarios.
 */
@Service
public class ComentarioService {
    
    private final ComentarioRepository comentarioRepository;

    public ComentarioService(ComentarioRepository comentarioRepository) {
        this.comentarioRepository = comentarioRepository;
    }
    
    /**
     * Obtiene todos los comentarios de una receta.
     * 
     * @param receta La receta
     * @return Lista de comentarios ordenados por fecha descendente
     */
    public List<Comentario> obtenerComentariosPorReceta(Receta receta) {
        return comentarioRepository.findByRecetaOrderByFechaCreacionDesc(receta);
    }
    
    /**
     * Crea un nuevo comentario.
     * 
     * @param receta La receta a comentar
     * @param usuario El usuario que comenta
     * @param texto El texto del comentario
     * @return El comentario creado
     */
    @Transactional
    public Comentario crearComentario(Receta receta, Usuario usuario, String texto) {
        Comentario comentario = new Comentario();
        comentario.setReceta(receta);
        comentario.setUsuario(usuario);
        comentario.setTexto(texto);
        
        return comentarioRepository.save(comentario);
    }
    
    /**
     * Cuenta los comentarios de una receta.
     * 
     * @param receta La receta
     * @return Número de comentarios
     */
    public Long contarComentarios(Receta receta) {
        return comentarioRepository.countByReceta(receta);
    }
}

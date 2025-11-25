package com.duoc.recetas.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.duoc.recetas.model.Rol;
import com.duoc.recetas.model.Usuario;
import com.duoc.recetas.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void cargaUsuarioConRoles() {
        Usuario usuario = new Usuario();
        usuario.setUsername("demo");
        usuario.setPassword("hash");
        usuario.setEnabled(true);
        Rol rol = new Rol();
        rol.setNombre("ROLE_USER");
        usuario.setRoles(Set.of(rol));
        when(usuarioRepository.findByUsername("demo")).thenReturn(java.util.Optional.of(usuario));

        UserDetails details = userDetailsService.loadUserByUsername("demo");

        assertEquals("demo", details.getUsername());
        assertEquals("hash", details.getPassword());
        assertTrue(details.isAccountNonLocked());
        assertEquals(1, details.getAuthorities().size());
    }

    @Test
    void lanzaExcepcionCuandoUsuarioNoExiste() {
        when(usuarioRepository.findByUsername("unknown")).thenReturn(java.util.Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("unknown"));
    }
}

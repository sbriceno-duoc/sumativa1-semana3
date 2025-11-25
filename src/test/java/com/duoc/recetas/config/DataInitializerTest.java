package com.duoc.recetas.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.duoc.recetas.model.Usuario;
import com.duoc.recetas.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class DataInitializerTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DataInitializer dataInitializer;

    @Test
    void runShouldUpdateKnownUsers() throws Exception {
        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.of(new Usuario()));
        when(usuarioRepository.findByUsername("usuario1")).thenReturn(Optional.of(new Usuario()));
        when(usuarioRepository.findByUsername("usuario2")).thenReturn(Optional.of(new Usuario()));
        when(usuarioRepository.findByUsername("chef")).thenReturn(Optional.of(new Usuario()));
        when(passwordEncoder.encode(anyString())).thenReturn("hash");

        dataInitializer.run();

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository, times(4)).save(captor.capture());
        captor.getAllValues().forEach(user -> assertEquals("hash", user.getPassword()));
    }
}

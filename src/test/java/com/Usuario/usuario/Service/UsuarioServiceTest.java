package com.Usuario.usuario.Service;

import com.Usuario.usuario.dto.UsuarioRequestDTO;
import com.Usuario.usuario.dto.UsuarioResponseDTO;
import com.Usuario.usuario.model.Usuario;
import com.Usuario.usuario.repository.UsuarioRepository;
import com.Usuario.usuario.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void agregarUsuario_debeRetornarDTO_cuandoDatosValidos() {
        // Arrange
        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setNombre("Sebastian");
        dto.setCorreo("sebas@gmail.com");
        dto.setRun("12345678-9");

        Usuario usuarioGuardado = new Usuario();
        usuarioGuardado.setIdUsuario(1L);
        usuarioGuardado.setNombre("Sebastian");
        usuarioGuardado.setCorreo("sebas@gmail.com");
        usuarioGuardado.setRun("12345678-9");

        when(usuarioRepository.existsByrun("12345678-9")).thenReturn(false);
        when(usuarioRepository.save(any())).thenReturn(usuarioGuardado);

        // Act
        UsuarioResponseDTO resultado = usuarioService.agregarUsuario(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals("Sebastian", resultado.getNombre());
        verify(usuarioRepository, times(1)).save(any());
    }

    @Test
    void agregarUsuario_debeLanzarExcepcion_cuandoRunDuplicado() {
        // Arrange
        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setRun("12345678-9");

        when(usuarioRepository.existsByrun("12345678-9")).thenReturn(true);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> usuarioService.agregarUsuario(dto));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void obtenerUsuarios_debeRetornarListaVacia_cuandoNoHayUsuarios() {
        // Arrange
        when(usuarioRepository.findAll()).thenReturn(java.util.List.of());

        // Act
        var resultado = usuarioService.obtenerUsuarios();

        // Assert
        assertNotNull(resultado);
        assertEquals(0, resultado.size());
    }
}

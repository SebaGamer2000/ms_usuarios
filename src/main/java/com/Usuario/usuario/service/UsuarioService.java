package com.Usuario.usuario.service;

import com.Usuario.usuario.dto.UsuarioRequestDTO;
import com.Usuario.usuario.dto.UsuarioResponseDTO;
import com.Usuario.usuario.model.Membresia;
import com.Usuario.usuario.model.Usuario;
import com.Usuario.usuario.repository.MembresiaRepository;
import com.Usuario.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final MembresiaRepository membresiaRepository;
    private final UsuarioRepository usuarioRepository;

    public UsuarioResponseDTO obtenerPorRUN(String RUN){
        Usuario usuarioEncontrado = usuarioRepository.findByrun(RUN)
                .orElseThrow(() -> new RuntimeException("No existe un usuario con ese RUN"));

        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setIdUsuario(usuarioEncontrado.getIdUsuario());
        dto.setNombre(usuarioEncontrado.getNombre());

        if(usuarioEncontrado.getMembresia() != null){
            dto.setIdmembresia(usuarioEncontrado.getMembresia().getNombre_membresia());
        }

        return dto;
    }

    public UsuarioResponseDTO agregarUsuario(UsuarioRequestDTO dto){
        if(usuarioRepository.existsByrun(dto.getRun())){
            throw new RuntimeException("ERROR: Ya existe un socio con el RUN" + dto.getRun());
        }

        Membresia buscarMembresia = membresiaRepository.findById(dto.getIdmembresia())
                .orElseThrow(() -> new RuntimeException("Esa id de membresia no existe"));

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(dto.getNombre());
        nuevoUsuario.setCorreo(dto.getCorreo());
        nuevoUsuario.setRun(dto.getRun());
        nuevoUsuario.setMembresia(buscarMembresia);

        usuarioRepository.save(nuevoUsuario);

        UsuarioResponseDTO respuesta = new UsuarioResponseDTO();
        respuesta.setIdUsuario(nuevoUsuario.getIdUsuario());
        respuesta.setNombre(nuevoUsuario.getNombre());
        respuesta.setIdmembresia(buscarMembresia.getNombre_membresia());
        return respuesta;
    }


    public List<Usuario> obtenerUsuarios(){
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerPorId(Long idUsuario){
        return usuarioRepository.findById(idUsuario);
    }

    public Usuario registrarUsuario(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public void eliminarUsuario(Long idUsuario){
        usuarioRepository.deleteById(idUsuario);
    }
}

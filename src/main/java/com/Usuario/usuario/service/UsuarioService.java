package com.Usuario.usuario.service;

import com.Usuario.usuario.dto.UsuarioRequestDTO;
import com.Usuario.usuario.dto.UsuarioResponseDTO;
import com.Usuario.usuario.dto.actualizarDTO;
import com.Usuario.usuario.model.Membresia;
import com.Usuario.usuario.model.Usuario;
import com.Usuario.usuario.repository.MembresiaRepository;
import com.Usuario.usuario.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final MembresiaRepository membresiaRepository;
    private final UsuarioRepository usuarioRepository;

    private UsuarioResponseDTO mapToDTO(Usuario usuario){
        return new UsuarioResponseDTO(
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getRun(),
                usuario.getMembresia().getNombre_membresia()
        );
    }

    public UsuarioResponseDTO obtenerPorRUN(String RUN){
        Usuario usuarioEncontrado = usuarioRepository.findByrun(RUN)
                .orElseThrow(() -> new RuntimeException("No existe un usuario con ese RUN"));

        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setNombre(usuarioEncontrado.getNombre());
        dto.setCorreo(usuarioEncontrado.getCorreo());
        dto.setRun(usuarioEncontrado.getRun());

        if(usuarioEncontrado.getMembresia() != null){
            dto.setIdmembresia(usuarioEncontrado.getMembresia().getNombre_membresia());
        }

        return dto;
    }

    // Añadir usuarios
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
        respuesta.setNombre(nuevoUsuario.getNombre());
        respuesta.setCorreo(nuevoUsuario.getCorreo());
        respuesta.setRun(nuevoUsuario.getRun());
        respuesta.setIdmembresia(buscarMembresia.getNombre_membresia());
        return respuesta;
    }

    // buscar para borrar
    public Optional<UsuarioResponseDTO> buscarRun(String run){
        return usuarioRepository.findByrun(run).map(this::mapToDTO);
    }

    // Eliminar usuarios
    @Transactional
    public void eliminarUsuario(String run){
        usuarioRepository.deleteByRun(run);
    }

    // Listar todos los usuarios
    public List<Usuario> obtenerUsuarios(){
        return usuarioRepository.findAll();
    }


    // Modificar usuarios
    public Optional<UsuarioResponseDTO> actualizarUsuario(String run, @Valid actualizarDTO dto){
        return usuarioRepository.findByrun(run).map(existe -> {
            Membresia membresia = membresiaRepository
                    .findById(dto.getIdmembresia())
                    .orElseThrow(() -> new RuntimeException(
                            "Membresia NO encontrada con el id " + dto.getIdmembresia()));
            existe.setRun(existe.getRun());
            existe.setNombre(dto.getNombre());
            existe.setCorreo(dto.getCorreo());
            existe.setMembresia(membresia);

        return mapToDTO(usuarioRepository.save(existe));
        });
    }
}

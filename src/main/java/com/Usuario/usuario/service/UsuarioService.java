package com.Usuario.usuario.service;

import com.Usuario.usuario.dto.MembresiaDTO;
import com.Usuario.usuario.dto.UsuarioRequestDTO;
import com.Usuario.usuario.dto.UsuarioResponseDTO;
import com.Usuario.usuario.dto.actualizarDTO;
import com.Usuario.usuario.model.Usuario;
import com.Usuario.usuario.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private WebClient.Builder webClientBuilder;

    private UsuarioResponseDTO mapToDTO(Usuario usuario){
        return new UsuarioResponseDTO(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getRun(),
                usuario.isPagoAlDia(),
                obtenerTipoMembresia(usuario.getIdMembresia())
        );
    }
    //Encontrar usuario por ID
    public Optional<UsuarioResponseDTO> findById(Long idUsuario) {return usuarioRepository.findById(idUsuario).map(this::mapToDTO);}
    //Encontrar usuario por run
    public UsuarioResponseDTO obtenerPorRUN(String RUN){
        Usuario usuarioEncontrado = usuarioRepository.findByrun(RUN)
                .orElseThrow(() -> new RuntimeException("No existe un usuario con ese RUN"));

        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setNombre(usuarioEncontrado.getNombre());
        dto.setCorreo(usuarioEncontrado.getCorreo());
        dto.setRun(usuarioEncontrado.getRun());

        return dto;
    }

    // Añadir usuarios
    public UsuarioResponseDTO agregarUsuario(UsuarioRequestDTO dto){
        log.info("Creando usuario");
        if(usuarioRepository.existsByrun(dto.getRun())){
            log.warn("Ya existe un socio con este RUN");
            throw new RuntimeException("ERROR: Ya existe un socio con el RUN" + dto.getRun());
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(dto.getNombre());
        nuevoUsuario.setCorreo(dto.getCorreo());
        nuevoUsuario.setRun(dto.getRun());
        log.info("Usuario creado exitosamente");
        return mapToDTO(usuarioRepository.save(nuevoUsuario));
    }

    // buscar para borrar
    public Optional<UsuarioResponseDTO> buscarRun(String run){
        return usuarioRepository.findByrun(run).map(this::mapToDTO);
    }

    // Eliminar usuarios
    @Transactional
    public void eliminarUsuario(String run){
        log.info("Eliminando usuario");
        usuarioRepository.deleteByRun(run);
    }

    // Listar todos los usuarios
    public List<Usuario> obtenerUsuarios(){
        return usuarioRepository.findAll();
    }

    // Obtener tipo de membresia
    private String obtenerTipoMembresia(Long idMembresia) {
        if (idMembresia == null) return null;
        MembresiaDTO membresiaDTO = webClientBuilder.build()
                .get()
                .uri("http://MEMBRESIAS/api/membresias/" + idMembresia)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response -> Mono.empty())
                .bodyToMono(MembresiaDTO.class)
                .block();
        return membresiaDTO != null ? membresiaDTO.getTipoPlan() : null;
    }




    // Modificar usuarios
    public Optional<UsuarioResponseDTO> actualizarUsuario(String run, @Valid actualizarDTO dto){
        return usuarioRepository.findByrun(run).map(existe -> {
            log.info("Actualizando socio");
            MembresiaDTO membresiaDTO = webClientBuilder.build()
                    .get()
                    .uri("http://MEMBRESIAS/api/membresias/" + dto.getIdMembresia())
                    .retrieve()
                    .bodyToMono(MembresiaDTO.class)
                    .block();

            if (membresiaDTO == null) {
                throw new RuntimeException("Membresia NO encontrada con el id " + dto.getIdMembresia());
            }

            existe.setRun(existe.getRun());
            existe.setNombre(dto.getNombre());
            existe.setCorreo(dto.getCorreo());
            existe.setIdMembresia(dto.getIdMembresia());
            log.info("Socio actualizado correctamente");

        return mapToDTO(usuarioRepository.save(existe));
        });
    }
}

package com.Usuario.usuario.controller;



import com.Usuario.usuario.dto.UsuarioRequestDTO;
import com.Usuario.usuario.dto.UsuarioResponseDTO;
import com.Usuario.usuario.dto.actualizarDTO;
import com.Usuario.usuario.model.Usuario;

import com.Usuario.usuario.repository.UsuarioRepository;
import com.Usuario.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/gym/socios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;


    // Listar todos los usuarios registrados
    @GetMapping("/listarsocios")
    public ResponseEntity<List<UsuarioResponseDTO>> obtenerUsuarios(){
        List<UsuarioResponseDTO> lista = usuarioService.obtenerUsuarios();
        lista.forEach(dto ->
                dto.add(linkTo(methodOn(UsuarioController.class).findById(dto.getIdUsuario())).withSelfRel())
        );
        return ResponseEntity.ok(lista);
    }
    @GetMapping("{idUsuario}")
    public ResponseEntity<UsuarioResponseDTO> findById(@PathVariable Long idUsuario){
        return usuarioService.findById(idUsuario).map(dto -> {
            dto.add(linkTo(methodOn(UsuarioController.class).findById(idUsuario)).withSelfRel());
            dto.add(linkTo(methodOn(UsuarioController.class).obtenerUsuarios()).withRel("todos"));
            return ResponseEntity.ok(dto);
        }).orElse(ResponseEntity.notFound().build());
    }



    // Buscar usuario por RUN
    @GetMapping("/busqueda/{run}")
    public ResponseEntity<?> obtenerUsuarioRUN(@PathVariable String run) {
        if(usuarioService.buscarRun(run).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("Mensaje:","No se encontro ningun socio con ese run asociado"));
        }

        return ResponseEntity.ok(usuarioService.obtenerPorRUN(run));
    }

    // Agregar usuarios
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> registrarUsuario(@Valid @RequestBody UsuarioRequestDTO dto){
        UsuarioResponseDTO nuevo = usuarioService.agregarUsuario(dto);
        nuevo.add(linkTo(methodOn(UsuarioController.class).findById(nuevo.getIdUsuario())).withSelfRel());
        nuevo.add(linkTo(methodOn(UsuarioController.class).obtenerUsuarios()).withRel("todos"));
        return ResponseEntity.status(201).body(nuevo);
    }

    // Borrar usuarios
    @DeleteMapping("/eliminarsocio/{run}")
    public ResponseEntity<?> borrarUsuario(@PathVariable String run){
        if(usuarioService.buscarRun(run).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("Mensaje","No se encontró ningun run asociado a algún socio."));
        }

        usuarioService.eliminarUsuario(run);

        Map<String,String> respuesta = new HashMap<>();
        respuesta.put("Mensaje","Socio eliminado correctamente");
        respuesta.put("run",run);
        respuesta.put("status","success");

        return ResponseEntity.ok(respuesta);
    }

    // Actualizar usuarios
    @PutMapping("/actualizar/{run}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable String run, @Valid @RequestBody actualizarDTO dto){
        if(usuarioService.buscarRun(run).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("Mensaje","No existe ningún socio con ese run."));
        }

        usuarioService.actualizarUsuario(run,dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

        Map<String,String> respuesta = new HashMap<>();
        respuesta.put("Mensaje","Socio modificado correctamente");
        respuesta.put("run",run);


        return ResponseEntity.ok(respuesta);
    }
//Procesar pagos
    @PutMapping("/procesarpago/{run}")
    public ResponseEntity<?> procesarPago(@PathVariable String run){
        Usuario usuario  = usuarioRepository.findByrun(run)
                .orElseThrow(() -> new RuntimeException("Socio no encontrado"));

        usuario.setPagoAlDia(true);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Pago procesado exitosamente");
    }
}


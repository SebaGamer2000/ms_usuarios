package com.Usuario.usuario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO extends RepresentationModel<UsuarioResponseDTO> {
    private Long idUsuario;
    private String nombre;
    private String correo;
    private String run;
    private boolean pagoAlDia;
    private String nombreMembresia;
}

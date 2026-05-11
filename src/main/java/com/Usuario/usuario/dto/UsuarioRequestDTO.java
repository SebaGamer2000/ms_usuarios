package com.Usuario.usuario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestDTO {
    @NotBlank(message = "Debe ingresar un nombre")
    private String nombre;

    @NotBlank(message = "Debe ingresar un correo valido")
    private String correo;

    @NotNull(message = "El RUN es obligatorio")
    private String run;

    @NotNull(message = "El usuario debe tener una membresia")
    private Long idmembresia;

}

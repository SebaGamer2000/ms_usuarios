package com.Usuario.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class actualizarDTO {
    private String run;
    @NotBlank(message = "El nombre no puede ser nulo")
    private String nombre;

    @Email(message = "Debe ingresar una direccion de correo valida")
    @NotBlank(message = "El correo es obligatorio")
    private String correo;

    @NotNull(message = "Debe ingresar una id de membresia valida")
    @Min(1)
    private Long idmembresia;

}

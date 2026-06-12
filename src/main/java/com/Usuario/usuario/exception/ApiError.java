package com.Usuario.usuario.exception;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ApiError {
    private int status;
    private String error;
    private String mensaje;
    private LocalDateTime timestamp;
}

package com.Usuario.usuario.config;

import com.Usuario.usuario.model.Usuario;
import com.Usuario.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final UsuarioRepository usuarioRepository;
//Mensajes para saber si hay datos guardados
    @Override
    public void run(String... args){
        if(usuarioRepository.count() > 0){
            log.info("Datos cargados");
            return;
        }

        log.info("No hay datos guardados, creando datos");


    }
}

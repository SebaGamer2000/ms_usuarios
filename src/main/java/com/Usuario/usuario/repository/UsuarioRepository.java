package com.Usuario.usuario.repository;

import com.Usuario.usuario.model.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsuarioRepository  extends JpaRepository<Usuario, Long> {

    boolean existsByrun(String run);

    Optional<Usuario> findByrun(String run);

    @Modifying
    @Transactional
    void deleteByRun(String run);


}

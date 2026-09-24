package com.securedocs.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @EntityGraph(attributePaths = {
            "rol",
            "rol.permisos",
            "rol.permisos.permiso"
    })
    Optional<Usuario> findByCorreo(String correo);

    boolean existsByCorreo(String correo);
}

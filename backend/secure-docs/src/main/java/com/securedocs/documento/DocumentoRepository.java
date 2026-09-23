package com.securedocs.documento;

import com.securedocs.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentoRepository extends JpaRepository<Documento, Long> {

    List<Documento> findByPropietario(Usuario propietario);

}
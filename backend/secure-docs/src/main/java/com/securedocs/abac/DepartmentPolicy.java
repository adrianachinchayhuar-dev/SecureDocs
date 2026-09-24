package com.securedocs.abac;

import com.securedocs.documento.Documento;
import com.securedocs.usuario.Usuario;
import org.springframework.stereotype.Component;

@Component
public class DepartmentPolicy implements AbacPolicy {

    @Override
    public AbacDecision evaluar(
            Usuario usuario,
            Documento documento,
            String accion,
            String dispositivo) {

        if (documento == null ||
                documento.getDepartamento() == null ||
                AbacSupport.esAdministrador(usuario) ||
                AbacSupport.esPropietario(usuario, documento)) {
            return AbacDecision.permitido();
        }

        if (usuario.getDepartamento() != null &&
                usuario.getDepartamento().getId() != null &&
                usuario.getDepartamento().getId().equals(
                        documento.getDepartamento().getId())) {
            return AbacDecision.permitido();
        }

        return AbacDecision.denegado(
                "El usuario no pertenece al departamento del documento");
    }
}

package com.securedocs.abac;

import com.securedocs.documento.Documento;
import com.securedocs.usuario.Usuario;
import org.springframework.stereotype.Component;

@Component
public class OwnershipPolicy implements AbacPolicy {

    @Override
    public AbacDecision evaluar(
            Usuario usuario,
            Documento documento,
            String accion,
            String dispositivo) {

        if (documento == null ||
                AbacSupport.esAdministrador(usuario) ||
                "CONSULTAR_DOCUMENTO".equals(accion) ||
                "CREAR_DOCUMENTO".equals(accion) ||
                "APROBAR_DOCUMENTO".equals(accion)) {
            return AbacDecision.permitido();
        }

        if (AbacSupport.esPropietario(usuario, documento)) {
            return AbacDecision.permitido();
        }

        return AbacDecision.denegado(
                "Solo el propietario puede modificar o eliminar el documento");
    }
}

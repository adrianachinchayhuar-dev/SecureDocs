package com.securedocs.abac;

import com.securedocs.documento.Documento;
import com.securedocs.usuario.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UserStatusPolicy implements AbacPolicy {

    @Override
    public AbacDecision evaluar(
            Usuario usuario,
            Documento documento,
            String accion,
            String dispositivo) {

        if (AbacSupport.mismoTexto(usuario.getEstado(), "ACTIVO")) {
            return AbacDecision.permitido();
        }

        return AbacDecision.denegado(
                "El usuario no está activo");
    }
}

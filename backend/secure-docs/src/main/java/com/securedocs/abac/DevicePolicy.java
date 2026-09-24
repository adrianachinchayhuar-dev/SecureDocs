package com.securedocs.abac;

import com.securedocs.documento.Documento;
import com.securedocs.usuario.Usuario;
import org.springframework.stereotype.Component;

@Component
public class DevicePolicy implements AbacPolicy {

    @Override
    public AbacDecision evaluar(
            Usuario usuario,
            Documento documento,
            String accion,
            String dispositivo) {

        if (dispositivo == null ||
                !"PUBLICO".equalsIgnoreCase(dispositivo)) {
            return AbacDecision.permitido();
        }

        if ("ELIMINAR_DOCUMENTO".equals(accion) ||
                "APROBAR_DOCUMENTO".equals(accion)) {
            return AbacDecision.denegado(
                    "La acción no está permitida desde un dispositivo público");
        }

        return AbacDecision.permitido();
    }
}

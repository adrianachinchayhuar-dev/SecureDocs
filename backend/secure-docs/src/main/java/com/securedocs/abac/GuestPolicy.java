package com.securedocs.abac;

import com.securedocs.documento.Documento;
import com.securedocs.usuario.Usuario;
import org.springframework.stereotype.Component;

@Component
public class GuestPolicy implements AbacPolicy {

    @Override
    public AbacDecision evaluar(
            Usuario usuario,
            Documento documento,
            String accion,
            String dispositivo) {

        if (!AbacSupport.tieneRol(usuario, "INVITADO")) {
            return AbacDecision.permitido();
        }

        if (!"CONSULTAR_DOCUMENTO".equals(accion)) {
            return AbacDecision.denegado(
                    "El rol INVITADO solo puede consultar documentos");
        }

        if (documento == null ||
                AbacSupport.mismoTexto(
                        documento.getEstado(),
                        "APROBADO")) {
            return AbacDecision.permitido();
        }

        return AbacDecision.denegado(
                "El invitado solo puede consultar documentos aprobados");
    }
}

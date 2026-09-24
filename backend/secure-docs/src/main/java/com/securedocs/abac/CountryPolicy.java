package com.securedocs.abac;

import com.securedocs.documento.Documento;
import com.securedocs.usuario.Usuario;
import org.springframework.stereotype.Component;

@Component
public class CountryPolicy implements AbacPolicy {

    @Override
    public AbacDecision evaluar(
            Usuario usuario,
            Documento documento,
            String accion,
            String dispositivo) {

        if (documento == null ||
                AbacSupport.esAdministrador(usuario) ||
                AbacSupport.tieneRol(usuario, "AUDITOR")) {
            return AbacDecision.permitido();
        }

        if (AbacSupport.mismoTexto(
                usuario.getPais(),
                documento.getPais())) {
            return AbacDecision.permitido();
        }

        return AbacDecision.denegado(
                "El país del usuario no coincide con el documento");
    }
}

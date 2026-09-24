package com.securedocs.abac;

import com.securedocs.documento.Documento;
import com.securedocs.usuario.Usuario;
import org.springframework.stereotype.Component;

@Component
public class SecurityLevelPolicy implements AbacPolicy {

    @Override
    public AbacDecision evaluar(
            Usuario usuario,
            Documento documento,
            String accion,
            String dispositivo) {

        if (documento == null ||
                AbacSupport.esAdministrador(usuario)) {
            return AbacDecision.permitido();
        }

        if (nivel(usuario.getNivelSeguridad()) >=
                nivel(documento.getNivelConfidencialidad())) {
            return AbacDecision.permitido();
        }

        return AbacDecision.denegado(
                "El nivel de seguridad del usuario es insuficiente");
    }

    private int nivel(String valor) {
        if (valor == null) {
            return 0;
        }

        return switch (valor.toUpperCase()) {
            case "ALTO" -> 3;
            case "MEDIO" -> 2;
            case "BAJO" -> 1;
            default -> 0;
        };
    }
}

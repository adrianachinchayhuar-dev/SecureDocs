package com.securedocs.abac;

import com.securedocs.documento.Documento;
import com.securedocs.usuario.Usuario;

public interface AbacPolicy {

    AbacDecision evaluar(
            Usuario usuario,
            Documento documento,
            String accion,
            String dispositivo);
}

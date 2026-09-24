package com.securedocs.abac;

import com.securedocs.documento.Documento;
import com.securedocs.usuario.Usuario;

final class AbacSupport {

    private AbacSupport() {
    }

    static boolean tieneRol(Usuario usuario, String rol) {
        return usuario.getRol() != null &&
                rol.equalsIgnoreCase(usuario.getRol().getNombre());
    }

    static boolean esAdministrador(Usuario usuario) {
        return tieneRol(usuario, "ADMINISTRADOR");
    }

    static boolean esPropietario(
            Usuario usuario,
            Documento documento) {

        return documento != null &&
                documento.getPropietario() != null &&
                usuario.getId() != null &&
                usuario.getId().equals(
                        documento.getPropietario().getId());
    }

    static boolean mismoTexto(String a, String b) {
        return a != null && b != null &&
                a.equalsIgnoreCase(b);
    }
}

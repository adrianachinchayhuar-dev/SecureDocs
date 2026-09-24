package com.securedocs.usuario;

import jakarta.validation.constraints.NotBlank;

public class CambiarEstadoUsuarioRequest {

    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}

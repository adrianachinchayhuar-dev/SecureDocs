package com.securedocs.exception;

import java.time.LocalDateTime;
import java.util.List;

public class ApiError {

    private LocalDateTime fecha;
    private int estado;
    private String error;
    private String mensaje;
    private String ruta;
    private List<String> detalles;

    public ApiError(
            int estado,
            String error,
            String mensaje,
            String ruta,
            List<String> detalles) {

        this.fecha = LocalDateTime.now();
        this.estado = estado;
        this.error = error;
        this.mensaje = mensaje;
        this.ruta = ruta;
        this.detalles = detalles;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public int getEstado() {
        return estado;
    }

    public String getError() {
        return error;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getRuta() {
        return ruta;
    }

    public List<String> getDetalles() {
        return detalles;
    }
}

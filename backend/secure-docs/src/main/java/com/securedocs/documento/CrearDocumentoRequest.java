package com.securedocs.documento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CrearDocumentoRequest {

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    private String descripcion;

    @NotNull(message = "El departamento es obligatorio")
    private Long departamentoId;

    @NotBlank(message = "El nivel de confidencialidad es obligatorio")
    private String nivelConfidencialidad;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    @NotBlank(message = "El país es obligatorio")
    private String pais;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getDepartamentoId() {
        return departamentoId;
    }

    public void setDepartamentoId(Long departamentoId) {
        this.departamentoId = departamentoId;
    }

    public String getNivelConfidencialidad() {
        return nivelConfidencialidad;
    }

    public void setNivelConfidencialidad(String nivelConfidencialidad) {
        this.nivelConfidencialidad = nivelConfidencialidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
}

package com.securedocs.documento;

public class ActualizarDocumentoRequest {

    private String titulo;
    private String descripcion;
    private Long departamentoId;
    private String nivelConfidencialidad;
    private String estado;
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

package com.securedocs.auth;

public class LoginResponse {

    private String token;
    private String tipo;
    private Long usuarioId;
    private String nombre;
    private String rol;

    public LoginResponse(
            String token,
            String tipo,
            Long usuarioId,
            String nombre,
            String rol) {

        this.token = token;
        this.tipo = tipo;
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.rol = rol;
    }

    public String getToken() {
        return token;
    }

    public String getTipo() {
        return tipo;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getRol() {
        return rol;
    }
}
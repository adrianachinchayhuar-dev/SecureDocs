package com.securedocs.abac;

public class AbacDecision {

    private final boolean permitido;
    private final String motivo;

    private AbacDecision(boolean permitido, String motivo) {
        this.permitido = permitido;
        this.motivo = motivo;
    }

    public static AbacDecision permitido() {
        return new AbacDecision(true, "Permitido");
    }

    public static AbacDecision denegado(String motivo) {
        return new AbacDecision(false, motivo);
    }

    public boolean isPermitido() {
        return permitido;
    }

    public String getMotivo() {
        return motivo;
    }
}

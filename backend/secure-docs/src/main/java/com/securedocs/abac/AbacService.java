package com.securedocs.abac;

import com.securedocs.auditoria.AuditoriaService;
import com.securedocs.documento.Documento;
import com.securedocs.exception.ForbiddenOperationException;
import com.securedocs.usuario.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AbacService {

    private final List<AbacPolicy> policies;
    private final AuditoriaService auditoriaService;

    public AbacService(
            List<AbacPolicy> policies,
            AuditoriaService auditoriaService) {

        this.policies = policies;
        this.auditoriaService = auditoriaService;
    }

    public void verificar(
            Usuario usuario,
            Documento documento,
            String accion,
            String dispositivo) {

        AbacDecision decision =
                evaluar(usuario, documento, accion, dispositivo);

        if (!decision.isPermitido()) {
            auditoriaService.registrar(
                    usuario,
                    "DOCUMENTO",
                    accion,
                    "DENEGADO",
                    decision.getMotivo()
            );

            throw new ForbiddenOperationException(
                    "ABAC: " + decision.getMotivo());
        }
    }

    public AbacDecision evaluar(
            Usuario usuario,
            Documento documento,
            String accion,
            String dispositivo) {

        for (AbacPolicy policy : policies) {
            AbacDecision decision =
                    policy.evaluar(
                            usuario,
                            documento,
                            accion,
                            dispositivo
                    );

            if (!decision.isPermitido()) {
                return decision;
            }
        }

        return AbacDecision.permitido();
    }
}

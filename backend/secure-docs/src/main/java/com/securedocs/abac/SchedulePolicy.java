package com.securedocs.abac;

import com.securedocs.documento.Documento;
import com.securedocs.usuario.Usuario;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class SchedulePolicy implements AbacPolicy {

    @Override
    public AbacDecision evaluar(
            Usuario usuario,
            Documento documento,
            String accion,
            String dispositivo) {

        if (AbacSupport.esAdministrador(usuario) ||
                "CONSULTAR_DOCUMENTO".equals(accion)) {
            return AbacDecision.permitido();
        }

        int hora = LocalTime.now().getHour();
        if (hora >= 6 && hora <= 22) {
            return AbacDecision.permitido();
        }

        return AbacDecision.denegado(
                "La acción solo está permitida en horario operativo");
    }
}

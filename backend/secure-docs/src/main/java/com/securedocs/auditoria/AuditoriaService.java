package com.securedocs.auditoria;

import com.securedocs.exception.ResourceNotFoundException;
import com.securedocs.usuario.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;

    public AuditoriaService(
            AuditoriaRepository auditoriaRepository) {

        this.auditoriaRepository = auditoriaRepository;
    }

    public Auditoria registrar(
            Usuario usuario,
            String recurso,
            String accion,
            String resultado,
            String motivo) {

        Auditoria auditoria = new Auditoria();
        auditoria.setUsuario(usuario);
        auditoria.setRecurso(recurso);
        auditoria.setAccion(accion);
        auditoria.setResultado(resultado);
        auditoria.setMotivo(motivo);

        return auditoriaRepository.save(auditoria);
    }

    public List<Auditoria> listar() {
        return auditoriaRepository.findAll();
    }

    public Auditoria buscarPorId(Long id) {
        return auditoriaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Registro de auditoría no encontrado"));
    }

    public List<Auditoria> listarPorUsuario(Usuario usuario) {
        return auditoriaRepository.findByUsuario(usuario);
    }
}

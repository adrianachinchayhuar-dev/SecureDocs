package com.securedocs.auditoria;

import com.securedocs.usuario.Usuario;
import com.securedocs.usuario.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auditorias")
public class AuditoriaController {

    private final AuditoriaService auditoriaService;
    private final UsuarioService usuarioService;

    public AuditoriaController(
            AuditoriaService auditoriaService,
            UsuarioService usuarioService) {

        this.auditoriaService = auditoriaService;
        this.usuarioService = usuarioService;
    }

    @PreAuthorize("hasAuthority('VER_AUDITORIA')")
    @GetMapping
    public ResponseEntity<List<Auditoria>> listar() {
        return ResponseEntity.ok(
                auditoriaService.listar()
        );
    }

    @PreAuthorize("hasAuthority('VER_AUDITORIA')")
    @GetMapping("/{id}")
    public ResponseEntity<Auditoria> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                auditoriaService.buscarPorId(id)
        );
    }

    @PreAuthorize("hasAuthority('VER_AUDITORIA')")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Auditoria>> listarPorUsuario(
            @PathVariable Long usuarioId) {

        Usuario usuario = usuarioService.buscarPorId(usuarioId);

        return ResponseEntity.ok(
                auditoriaService.listarPorUsuario(usuario)
        );
    }
}

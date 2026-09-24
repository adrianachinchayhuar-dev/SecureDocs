package com.securedocs.usuario;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // ==========================================
    // CREAR USUARIO
    // ==========================================

    @PreAuthorize("hasAuthority('GESTIONAR_USUARIOS')")
    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(
            @Valid @RequestBody CrearUsuarioRequest request) {

        Usuario usuario = usuarioService.crearUsuario(
                request.getNombre(),
                request.getCorreo(),
                request.getPassword(),
                request.getRolId(),
                request.getDepartamentoId(),
                request.getNivelSeguridad(),
                request.getPais(),
                request.getTipoContrato(),
                request.getEstado()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usuario);
    }

    // ==========================================
    // BUSCAR POR CORREO
    // ==========================================

    @PreAuthorize("hasAuthority('GESTIONAR_USUARIOS')")
    @GetMapping("/correo/{correo}")
    public ResponseEntity<Usuario> buscarPorCorreo(
            @PathVariable String correo) {

        return ResponseEntity.ok(
                usuarioService.buscarPorCorreo(correo)
        );
    }

    // ==========================================
    // BUSCAR POR ID
    // ==========================================

    @PreAuthorize("hasAuthority('GESTIONAR_USUARIOS')")
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                usuarioService.buscarPorId(id)
        );
    }

    // ==========================================
    // LISTAR USUARIOS
    // ==========================================

    @PreAuthorize("hasAuthority('GESTIONAR_USUARIOS')")
    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {

        return ResponseEntity.ok(
                usuarioService.listarUsuarios()
        );
    }

    @PreAuthorize("hasAuthority('GESTIONAR_USUARIOS')")
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarUsuarioRequest request) {

        return ResponseEntity.ok(
                usuarioService.actualizarUsuario(id, request)
        );
    }

    @PreAuthorize("hasAuthority('GESTIONAR_USUARIOS')")
    @PutMapping("/{id}/rol/{rolId}")
    public ResponseEntity<Usuario> cambiarRol(
            @PathVariable Long id,
            @PathVariable Long rolId) {

        return ResponseEntity.ok(
                usuarioService.cambiarRol(id, rolId)
        );
    }

    @PreAuthorize("hasAuthority('GESTIONAR_USUARIOS')")
    @PutMapping("/{id}/departamento/{departamentoId}")
    public ResponseEntity<Usuario> cambiarDepartamento(
            @PathVariable Long id,
            @PathVariable Long departamentoId) {

        return ResponseEntity.ok(
                usuarioService.cambiarDepartamento(
                        id,
                        departamentoId
                )
        );
    }

    @PreAuthorize("hasAuthority('GESTIONAR_USUARIOS')")
    @PutMapping("/{id}/estado")
    public ResponseEntity<Usuario> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody CambiarEstadoUsuarioRequest request) {

        return ResponseEntity.ok(
                usuarioService.cambiarEstado(
                        id,
                        request.getEstado()
                )
        );
    }

    @PreAuthorize("hasAuthority('GESTIONAR_USUARIOS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(
            @PathVariable Long id) {

        usuarioService.eliminarUsuario(id);

        return ResponseEntity.noContent().build();
    }
}

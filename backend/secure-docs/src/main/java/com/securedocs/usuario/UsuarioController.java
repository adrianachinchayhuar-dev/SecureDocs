package com.securedocs.usuario;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(
            @RequestBody CrearUsuarioRequest request) {

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

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {

        return ResponseEntity.ok(
                usuarioService.listarUsuarios()
        );
    }
}
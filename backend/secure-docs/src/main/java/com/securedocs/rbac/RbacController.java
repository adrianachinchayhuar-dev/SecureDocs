package com.securedocs.rbac;

import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.securedocs.usuario.Usuario;

import java.util.List;

@RestController
@RequestMapping("/api/rbac")
@Validated
public class RbacController {

    private final RbacService rbacService;

    public RbacController(RbacService rbacService) {
        this.rbacService = rbacService;
    }

    // ==========================================
    // CREAR ROL
    // ==========================================

    @PreAuthorize("hasAuthority('ASIGNAR_ROLES')")
    @PostMapping("/roles")
    public ResponseEntity<Rol> crearRol(
            @NotBlank(message = "El nombre del rol es obligatorio")
            @RequestParam String nombre,
            Authentication authentication) {

        Rol rol = rbacService.crearRol(
                nombre,
                obtenerUsuario(authentication)
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(rol);
    }

    @PreAuthorize("hasAuthority('ASIGNAR_ROLES')")
    @GetMapping("/roles")
    public ResponseEntity<List<Rol>> listarRoles() {
        return ResponseEntity.ok(
                rbacService.listarRoles()
        );
    }

    @PreAuthorize("hasAuthority('ASIGNAR_ROLES')")
    @GetMapping("/roles/{rolId}")
    public ResponseEntity<Rol> buscarRol(
            @PathVariable Long rolId) {

        return ResponseEntity.ok(
                rbacService.buscarRolPorId(rolId)
        );
    }

    // ==========================================
    // CREAR PERMISO
    // ==========================================

    @PreAuthorize("hasAuthority('ASIGNAR_ROLES')")
    @PostMapping("/permisos")
    public ResponseEntity<Permiso> crearPermiso(
            @NotBlank(message = "El nombre del permiso es obligatorio")
            @RequestParam String nombre,
            @NotBlank(message = "La descripción del permiso es obligatoria")
            @RequestParam String descripcion,
            Authentication authentication) {

        Permiso permiso =
                rbacService.crearPermiso(
                        nombre,
                        descripcion,
                        obtenerUsuario(authentication)
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(permiso);
    }

    @PreAuthorize("hasAuthority('ASIGNAR_ROLES')")
    @GetMapping("/permisos")
    public ResponseEntity<List<Permiso>> listarPermisos() {
        return ResponseEntity.ok(
                rbacService.listarPermisos()
        );
    }

    @PreAuthorize("hasAuthority('ASIGNAR_ROLES')")
    @GetMapping("/permisos/{permisoId}")
    public ResponseEntity<Permiso> buscarPermiso(
            @PathVariable Long permisoId) {

        return ResponseEntity.ok(
                rbacService.buscarPermisoPorId(permisoId)
        );
    }

    // ==========================================
    // ASIGNAR PERMISO A ROL
    // ==========================================

    @PreAuthorize("hasAuthority('ASIGNAR_ROLES')")
    @PostMapping(
            "/roles/{rolId}/permisos/{permisoId}"
    )
    public ResponseEntity<RolPermiso> asignarPermisoARol(
            @PathVariable Long rolId,
            @PathVariable Long permisoId,
            Authentication authentication) {

        RolPermiso rolPermiso =
                rbacService.asignarPermisoARol(
                        rolId,
                        permisoId,
                        obtenerUsuario(authentication)
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(rolPermiso);
    }

    // ==========================================
    // OBTENER PERMISOS DE UN ROL
    // ==========================================

    @PreAuthorize("hasAuthority('ASIGNAR_ROLES')")
    @GetMapping("/roles/{rolId}/permisos")
    public ResponseEntity<List<RolPermiso>>
    obtenerPermisosDelRol(
            @PathVariable Long rolId) {

        return ResponseEntity.ok(
                rbacService.obtenerPermisosDelRol(rolId)
        );
    }

    @PreAuthorize("hasAuthority('ASIGNAR_ROLES')")
    @DeleteMapping(
            "/roles/{rolId}/permisos/{permisoId}"
    )
    public ResponseEntity<Void> quitarPermisoDeRol(
            @PathVariable Long rolId,
            @PathVariable Long permisoId,
            Authentication authentication) {

        rbacService.quitarPermisoDeRol(
                rolId,
                permisoId,
                obtenerUsuario(authentication)
        );

        return ResponseEntity.noContent().build();
    }

    private Usuario obtenerUsuario(Authentication authentication) {
        return (Usuario) authentication.getPrincipal();
    }
}

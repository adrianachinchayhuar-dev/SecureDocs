package com.securedocs.documento;

import com.securedocs.usuario.Usuario;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documentos")
public class DocumentoController {

    private final DocumentoService documentoService;

    public DocumentoController(DocumentoService documentoService) {
        this.documentoService = documentoService;
    }

    @PreAuthorize("hasAuthority('CREAR_DOCUMENTO')")
    @PostMapping
    public ResponseEntity<Documento> crearDocumento(
            @Valid @RequestBody CrearDocumentoRequest request,
            Authentication authentication,
            @RequestHeader(
                    value = "X-Device-Type",
                    required = false
            ) String dispositivo) {

        Documento documento =
                documentoService.crearDocumento(
                        obtenerUsuarioId(authentication),
                        request,
                        dispositivo
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(documento);
    }

    @PreAuthorize("hasAuthority('CONSULTAR_DOCUMENTO')")
    @GetMapping
    public ResponseEntity<List<Documento>> listarDocumentos(
            Authentication authentication,
            @RequestHeader(
                    value = "X-Device-Type",
                    required = false
            ) String dispositivo) {

        return ResponseEntity.ok(
                documentoService.listarDocumentos(
                        obtenerUsuarioId(authentication),
                        dispositivo
                )
        );
    }

    @PreAuthorize("hasAuthority('CONSULTAR_DOCUMENTO')")
    @GetMapping("/{id}")
    public ResponseEntity<Documento> consultarDocumento(
            @PathVariable Long id,
            Authentication authentication,
            @RequestHeader(
                    value = "X-Device-Type",
                    required = false
            ) String dispositivo) {

        return ResponseEntity.ok(
                documentoService.buscarPorId(
                        id,
                        obtenerUsuarioId(authentication),
                        dispositivo
                )
        );
    }

    @PreAuthorize("hasAuthority('CONSULTAR_DOCUMENTO')")
    @GetMapping("/mis-documentos")
    public ResponseEntity<List<Documento>> listarMisDocumentos(
            Authentication authentication,
            @RequestHeader(
                    value = "X-Device-Type",
                    required = false
            ) String dispositivo) {

        return ResponseEntity.ok(
                documentoService.listarDocumentosDelUsuario(
                        obtenerUsuarioId(authentication),
                        dispositivo
                )
        );
    }

    @PreAuthorize("hasAuthority('MODIFICAR_DOCUMENTO')")
    @PutMapping("/{id}")
    public ResponseEntity<Documento> actualizarDocumento(
            @PathVariable Long id,
            @RequestBody ActualizarDocumentoRequest request,
            Authentication authentication,
            @RequestHeader(
                    value = "X-Device-Type",
                    required = false
            ) String dispositivo) {

        return ResponseEntity.ok(
                documentoService.actualizarDocumento(
                        id,
                        request,
                        obtenerUsuarioId(authentication),
                        dispositivo
                )
        );
    }

    @PreAuthorize("hasAuthority('APROBAR_DOCUMENTO')")
    @PutMapping("/{id}/aprobar")
    public ResponseEntity<Documento> aprobarDocumento(
            @PathVariable Long id,
            Authentication authentication,
            @RequestHeader(
                    value = "X-Device-Type",
                    required = false
            ) String dispositivo) {

        return ResponseEntity.ok(
                documentoService.aprobarDocumento(
                        id,
                        obtenerUsuarioId(authentication),
                        dispositivo
                )
        );
    }

    @PreAuthorize("hasAuthority('ELIMINAR_DOCUMENTO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDocumento(
            @PathVariable Long id,
            Authentication authentication,
            @RequestHeader(
                    value = "X-Device-Type",
                    required = false
            ) String dispositivo) {

        documentoService.eliminarDocumento(
                id,
                obtenerUsuarioId(authentication),
                dispositivo
        );

        return ResponseEntity.noContent().build();
    }

    private Long obtenerUsuarioId(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return usuario.getId();
    }
}

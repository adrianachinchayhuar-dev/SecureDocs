package com.securedocs.documento;

import com.securedocs.abac.AbacService;
import com.securedocs.auditoria.AuditoriaService;
import com.securedocs.departamento.Departamento;
import com.securedocs.departamento.DepartamentoRepository;
import com.securedocs.exception.ResourceNotFoundException;
import com.securedocs.usuario.Usuario;
import com.securedocs.usuario.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class DocumentoService {

    private final DocumentoRepository documentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final DepartamentoRepository departamentoRepository;
    private final AbacService abacService;
    private final AuditoriaService auditoriaService;

    public DocumentoService(
            DocumentoRepository documentoRepository,
            UsuarioRepository usuarioRepository,
            DepartamentoRepository departamentoRepository,
            AbacService abacService,
            AuditoriaService auditoriaService) {

        this.documentoRepository = documentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.departamentoRepository = departamentoRepository;
        this.abacService = abacService;
        this.auditoriaService = auditoriaService;
    }

    public Documento crearDocumento(
            Long usuarioId,
            CrearDocumentoRequest request,
            String dispositivo) {

        Usuario propietario = buscarUsuario(usuarioId);
        Departamento departamento =
                buscarDepartamento(request.getDepartamentoId());

        Documento documento = new Documento();
        documento.setTitulo(request.getTitulo());
        documento.setDescripcion(request.getDescripcion());
        documento.setPropietario(propietario);
        documento.setDepartamento(departamento);
        documento.setNivelConfidencialidad(
                request.getNivelConfidencialidad());
        documento.setEstado(request.getEstado());
        documento.setPais(request.getPais());

        abacService.verificar(
                propietario,
                documento,
                "CREAR_DOCUMENTO",
                dispositivo
        );

        Documento creado = documentoRepository.save(documento);

        auditoriaService.registrar(
                propietario,
                "DOCUMENTO",
                "CREAR_DOCUMENTO",
                "PERMITIDO",
                "Documento creado"
        );

        return creado;
    }

    public List<Documento> listarDocumentos(
            Long usuarioId,
            String dispositivo) {

        Usuario usuario = buscarUsuario(usuarioId);

        List<Documento> documentos = documentoRepository.findAll()
                .stream()
                .filter(documento ->
                        abacService.evaluar(
                                        usuario,
                                        documento,
                                        "CONSULTAR_DOCUMENTO",
                                        dispositivo
                                )
                                .isPermitido())
                .toList();

        auditoriaService.registrar(
                usuario,
                "DOCUMENTO",
                "CONSULTAR_DOCUMENTO",
                "PERMITIDO",
                "Listado de documentos consultado"
        );

        return documentos;
    }

    public Documento buscarPorId(
            Long id,
            Long usuarioId,
            String dispositivo) {

        Documento documento = documentoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Documento no encontrado"));

        Usuario usuario = buscarUsuario(usuarioId);

        abacService.verificar(
                usuario,
                documento,
                "CONSULTAR_DOCUMENTO",
                dispositivo
        );

        auditoriaService.registrar(
                usuario,
                "DOCUMENTO",
                "CONSULTAR_DOCUMENTO",
                "PERMITIDO",
                "Documento consultado"
        );

        return documento;
    }

    public List<Documento> listarDocumentosDelUsuario(
            Long usuarioId,
            String dispositivo) {

        Usuario usuario = buscarUsuario(usuarioId);

        List<Documento> documentos = documentoRepository.findByPropietario(usuario)
                .stream()
                .filter(documento ->
                        abacService.evaluar(
                                        usuario,
                                        documento,
                                        "CONSULTAR_DOCUMENTO",
                                        dispositivo
                                )
                                .isPermitido())
                .toList();

        auditoriaService.registrar(
                usuario,
                "DOCUMENTO",
                "CONSULTAR_DOCUMENTO",
                "PERMITIDO",
                "Documentos propios consultados"
        );

        return documentos;
    }

    public Documento actualizarDocumento(
            Long id,
            ActualizarDocumentoRequest request,
            Long usuarioId,
            String dispositivo) {

        Documento documento = buscarDocumento(id);

        Usuario usuario = buscarUsuario(usuarioId);

        abacService.verificar(
                usuario,
                documento,
                "MODIFICAR_DOCUMENTO",
                dispositivo
        );

        if (StringUtils.hasText(request.getTitulo())) {
            documento.setTitulo(request.getTitulo());
        }

        if (request.getDescripcion() != null) {
            documento.setDescripcion(request.getDescripcion());
        }

        if (request.getDepartamentoId() != null) {
            documento.setDepartamento(
                    buscarDepartamento(request.getDepartamentoId()));
        }

        if (StringUtils.hasText(request.getNivelConfidencialidad())) {
            documento.setNivelConfidencialidad(
                    request.getNivelConfidencialidad());
        }

        if (StringUtils.hasText(request.getEstado())) {
            documento.setEstado(request.getEstado());
        }

        if (StringUtils.hasText(request.getPais())) {
            documento.setPais(request.getPais());
        }

        Documento actualizado = documentoRepository.save(documento);

        auditoriaService.registrar(
                usuario,
                "DOCUMENTO",
                "MODIFICAR_DOCUMENTO",
                "PERMITIDO",
                "Documento actualizado"
        );

        return actualizado;
    }

    public Documento aprobarDocumento(
            Long id,
            Long usuarioId,
            String dispositivo) {

        Documento documento = buscarDocumento(id);

        Usuario usuario = buscarUsuario(usuarioId);

        abacService.verificar(
                usuario,
                documento,
                "APROBAR_DOCUMENTO",
                dispositivo
        );

        documento.setEstado("APROBADO");
        Documento aprobado = documentoRepository.save(documento);

        auditoriaService.registrar(
                usuario,
                "DOCUMENTO",
                "APROBAR_DOCUMENTO",
                "PERMITIDO",
                "Documento aprobado"
        );

        return aprobado;
    }

    public void eliminarDocumento(
            Long id,
            Long usuarioId,
            String dispositivo) {

        Documento documento = buscarDocumento(id);

        Usuario usuario = buscarUsuario(usuarioId);

        abacService.verificar(
                usuario,
                documento,
                "ELIMINAR_DOCUMENTO",
                dispositivo
        );

        documentoRepository.delete(documento);

        auditoriaService.registrar(
                usuario,
                "DOCUMENTO",
                "ELIMINAR_DOCUMENTO",
                "PERMITIDO",
                "Documento eliminado"
        );
    }

    private Documento buscarDocumento(Long id) {
        return documentoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Documento no encontrado"));
    }

    private Usuario buscarUsuario(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuario no encontrado"));
    }

    private Departamento buscarDepartamento(Long departamentoId) {
        return departamentoRepository.findById(departamentoId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Departamento no encontrado"));
    }
}

package com.securedocs.documento;

import com.securedocs.abac.AbacService;
import com.securedocs.auditoria.AuditoriaService;
import com.securedocs.departamento.Departamento;
import com.securedocs.departamento.DepartamentoRepository;
import com.securedocs.exception.ForbiddenOperationException;
import com.securedocs.usuario.Usuario;
import com.securedocs.usuario.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DocumentoServiceTest {

    @Mock
    private DocumentoRepository documentoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private DepartamentoRepository departamentoRepository;

    @Mock
    private AbacService abacService;

    @Mock
    private AuditoriaService auditoriaService;

    @InjectMocks
    private DocumentoService documentoService;

    @Test
    void crearDocumentoVerificaAbacGuardaYAudita() {
        Usuario usuario = usuario(1L);
        Departamento departamento = departamento(2L);
        CrearDocumentoRequest request = crearRequest();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(departamentoRepository.findById(2L))
                .thenReturn(Optional.of(departamento));
        when(documentoRepository.save(any(Documento.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Documento creado =
                documentoService.crearDocumento(1L, request, "WEB");

        assertThat(creado.getPropietario()).isEqualTo(usuario);
        assertThat(creado.getDepartamento()).isEqualTo(departamento);

        InOrder orden = inOrder(abacService, documentoRepository,
                auditoriaService);
        orden.verify(abacService).verificar(
                usuario,
                creado,
                "CREAR_DOCUMENTO",
                "WEB"
        );
        orden.verify(documentoRepository).save(creado);
        orden.verify(auditoriaService).registrar(
                usuario,
                "DOCUMENTO",
                "CREAR_DOCUMENTO",
                "PERMITIDO",
                "Documento creado"
        );
    }

    @Test
    void crearDocumentoNoGuardaNiAuditaPermitidoSiAbacDeniega() {
        Usuario usuario = usuario(1L);
        Departamento departamento = departamento(2L);
        CrearDocumentoRequest request = crearRequest();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(departamentoRepository.findById(2L))
                .thenReturn(Optional.of(departamento));
        doThrow(new ForbiddenOperationException("ABAC: denegado"))
                .when(abacService)
                .verificar(any(Usuario.class), any(Documento.class),
                        any(String.class), any(String.class));

        assertThatThrownBy(() ->
                documentoService.crearDocumento(1L, request, "WEB"))
                .isInstanceOf(ForbiddenOperationException.class);

        verify(documentoRepository, never()).save(any(Documento.class));
        verify(auditoriaService, never()).registrar(
                any(), any(), any(), any(), any());
    }

    private CrearDocumentoRequest crearRequest() {
        CrearDocumentoRequest request = new CrearDocumentoRequest();
        request.setTitulo("Plan de seguridad");
        request.setDescripcion("Documento interno");
        request.setDepartamentoId(2L);
        request.setNivelConfidencialidad("MEDIO");
        request.setEstado("BORRADOR");
        request.setPais("PE");
        return request;
    }

    private Usuario usuario(Long id) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNombre("Ana");
        usuario.setCorreo("ana@securedocs.com");
        usuario.setEstado("ACTIVO");
        return usuario;
    }

    private Departamento departamento(Long id) {
        Departamento departamento = new Departamento("TI");
        departamento.setId(id);
        return departamento;
    }
}

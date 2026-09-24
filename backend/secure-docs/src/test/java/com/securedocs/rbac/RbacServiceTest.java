package com.securedocs.rbac;

import com.securedocs.auditoria.AuditoriaService;
import com.securedocs.exception.BadRequestException;
import com.securedocs.usuario.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RbacServiceTest {

    @Mock
    private RolRepository rolRepository;

    @Mock
    private PermisoRepository permisoRepository;

    @Mock
    private RolPermisoRepository rolPermisoRepository;

    @Mock
    private AuditoriaService auditoriaService;

    @InjectMocks
    private RbacService rbacService;

    @Test
    void asignarPermisoARolRegistraAuditoria() {
        Usuario administrador = usuario(1L);
        Rol rol = new Rol("GERENTE");
        rol.setId(2L);
        Permiso permiso = new Permiso(
                "CONSULTAR_DOCUMENTO",
                "Consultar documentos"
        );
        permiso.setId(3L);

        when(rolRepository.findById(2L)).thenReturn(Optional.of(rol));
        when(permisoRepository.findById(3L))
                .thenReturn(Optional.of(permiso));
        when(rolPermisoRepository.existsByRolAndPermiso(rol, permiso))
                .thenReturn(false);
        when(rolPermisoRepository.save(org.mockito.ArgumentMatchers.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RolPermiso asignado =
                rbacService.asignarPermisoARol(2L, 3L, administrador);

        assertThat(asignado.getRol()).isEqualTo(rol);
        assertThat(asignado.getPermiso()).isEqualTo(permiso);
        verify(auditoriaService).registrar(
                administrador,
                "RBAC",
                "ASIGNAR_PERMISO_ROL",
                "PERMITIDO",
                "Permiso asignado a rol"
        );
    }

    @Test
    void asignarPermisoDuplicadoNoAuditaPermitido() {
        Usuario administrador = usuario(1L);
        Rol rol = new Rol("GERENTE");
        rol.setId(2L);
        Permiso permiso = new Permiso(
                "CONSULTAR_DOCUMENTO",
                "Consultar documentos"
        );
        permiso.setId(3L);

        when(rolRepository.findById(2L)).thenReturn(Optional.of(rol));
        when(permisoRepository.findById(3L))
                .thenReturn(Optional.of(permiso));
        when(rolPermisoRepository.existsByRolAndPermiso(rol, permiso))
                .thenReturn(true);

        assertThatThrownBy(() ->
                rbacService.asignarPermisoARol(2L, 3L, administrador))
                .isInstanceOf(BadRequestException.class);

        verify(rolPermisoRepository, never())
                .save(org.mockito.ArgumentMatchers.any());
        verify(auditoriaService, never())
                .registrar(org.mockito.ArgumentMatchers.any(),
                        org.mockito.ArgumentMatchers.any(),
                        org.mockito.ArgumentMatchers.any(),
                        org.mockito.ArgumentMatchers.any(),
                        org.mockito.ArgumentMatchers.any());
    }

    private Usuario usuario(Long id) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNombre("Admin");
        usuario.setCorreo("admin@securedocs.com");
        return usuario;
    }
}

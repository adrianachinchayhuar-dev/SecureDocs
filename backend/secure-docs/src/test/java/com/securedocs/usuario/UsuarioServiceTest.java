package com.securedocs.usuario;

import com.securedocs.auditoria.AuditoriaService;
import com.securedocs.departamento.Departamento;
import com.securedocs.departamento.DepartamentoRepository;
import com.securedocs.rbac.Rol;
import com.securedocs.rbac.RolRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private DepartamentoRepository departamentoRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuditoriaService auditoriaService;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void crearUsuarioCodificaPasswordYRegistraAuditoria() {
        Rol rol = new Rol("EMPLEADO");
        rol.setId(1L);
        Departamento departamento = new Departamento("TI");
        departamento.setId(2L);

        when(usuarioRepository.existsByCorreo("ana@securedocs.com"))
                .thenReturn(false);
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));
        when(departamentoRepository.findById(2L))
                .thenReturn(Optional.of(departamento));
        when(passwordEncoder.encode("secreto123"))
                .thenReturn("hash-bcrypt");
        when(usuarioRepository.save(any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Usuario creado = usuarioService.crearUsuario(
                "Ana",
                "ana@securedocs.com",
                "secreto123",
                1L,
                2L,
                "MEDIO",
                "PE",
                "FIJO",
                "ACTIVO"
        );

        assertThat(creado.getPassword()).isEqualTo("hash-bcrypt");
        verify(auditoriaService).registrar(
                creado,
                "USUARIO",
                "CREAR_USUARIO",
                "PERMITIDO",
                "Usuario creado"
        );
    }

    @Test
    void cambiarDepartamentoRegistraAccionCorrecta() {
        Usuario usuario = new Usuario();
        usuario.setId(10L);
        Departamento departamento = new Departamento("LEGAL");
        departamento.setId(4L);

        when(usuarioRepository.findById(10L)).thenReturn(Optional.of(usuario));
        when(departamentoRepository.findById(4L))
                .thenReturn(Optional.of(departamento));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario actualizado =
                usuarioService.cambiarDepartamento(10L, 4L);

        assertThat(actualizado.getDepartamento()).isEqualTo(departamento);
        verify(auditoriaService).registrar(
                actualizado,
                "USUARIO",
                "CAMBIAR_DEPARTAMENTO_USUARIO",
                "PERMITIDO",
                "Departamento de usuario actualizado"
        );
    }

    @Test
    void eliminarUsuarioEsLogicoYRegistraAuditoria() {
        Usuario usuario = new Usuario();
        usuario.setId(9L);
        usuario.setEstado("ACTIVO");

        when(usuarioRepository.findById(9L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        usuarioService.eliminarUsuario(9L);

        ArgumentCaptor<Usuario> captor =
                ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        assertThat(captor.getValue().getEstado()).isEqualTo("ELIMINADO");
        verify(auditoriaService).registrar(
                usuario,
                "USUARIO",
                "ELIMINAR_USUARIO",
                "PERMITIDO",
                "Usuario eliminado logicamente"
        );
    }
}

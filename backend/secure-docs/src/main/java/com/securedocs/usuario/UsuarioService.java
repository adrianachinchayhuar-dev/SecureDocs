package com.securedocs.usuario;

import com.securedocs.auditoria.AuditoriaService;
import com.securedocs.departamento.Departamento;
import com.securedocs.departamento.DepartamentoRepository;
import com.securedocs.exception.BadRequestException;
import com.securedocs.exception.ResourceNotFoundException;
import com.securedocs.rbac.Rol;
import com.securedocs.rbac.RolRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final DepartamentoRepository departamentoRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditoriaService auditoriaService;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            DepartamentoRepository departamentoRepository,
            PasswordEncoder passwordEncoder,
            AuditoriaService auditoriaService) {

        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.departamentoRepository = departamentoRepository;
        this.passwordEncoder = passwordEncoder;
        this.auditoriaService = auditoriaService;
    }

    // ==========================================
    // CREAR USUARIO
    // ==========================================

    public Usuario crearUsuario(
            String nombre,
            String correo,
            String password,
            Long rolId,
            Long departamentoId,
            String nivelSeguridad,
            String pais,
            String tipoContrato,
            String estado) {

        // Verificar si el correo ya existe
        if (usuarioRepository.existsByCorreo(correo)) {
            throw new BadRequestException(
                    "El correo ya está registrado");
        }

        // Buscar rol
        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Rol no encontrado"));

        // Buscar departamento
        Departamento departamento =
                departamentoRepository.findById(departamentoId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Departamento no encontrado"));

        // Crear usuario
        Usuario usuario = new Usuario();

        usuario.setNombre(nombre);
        usuario.setCorreo(correo);

        // IMPORTANTE:
        // Nunca guardar password en texto plano
        usuario.setPassword(
                passwordEncoder.encode(password));

        usuario.setRol(rol);
        usuario.setDepartamento(departamento);
        usuario.setNivelSeguridad(nivelSeguridad);
        usuario.setPais(pais);
        usuario.setTipoContrato(tipoContrato);
        usuario.setEstado(estado);

        Usuario creado = usuarioRepository.save(usuario);

        auditoriaService.registrar(
                creado,
                "USUARIO",
                "CREAR_USUARIO",
                "PERMITIDO",
                "Usuario creado"
        );

        return creado;
    }

    // ==========================================
    // BUSCAR USUARIO POR CORREO
    // ==========================================

    public Usuario buscarPorCorreo(String correo) {

        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuario no encontrado"));
    }

    // ==========================================
    // BUSCAR USUARIO POR ID
    // ==========================================

    public Usuario buscarPorId(Long id) {

        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuario no encontrado"));
    }

    // ==========================================
    // LISTAR USUARIOS
    // ==========================================

    public List<Usuario> listarUsuarios() {

        return usuarioRepository.findAll();
    }

    public Usuario actualizarUsuario(
            Long id,
            ActualizarUsuarioRequest request) {

        Usuario usuario = buscarPorId(id);

        if (StringUtils.hasText(request.getNombre())) {
            usuario.setNombre(request.getNombre());
        }

        if (StringUtils.hasText(request.getCorreo()) &&
                !request.getCorreo().equals(usuario.getCorreo())) {

            if (usuarioRepository.existsByCorreo(request.getCorreo())) {
                throw new BadRequestException(
                        "El correo ya está registrado");
            }

            usuario.setCorreo(request.getCorreo());
        }

        if (StringUtils.hasText(request.getPassword())) {
            usuario.setPassword(
                    passwordEncoder.encode(request.getPassword()));
        }

        if (request.getRolId() != null) {
            usuario.setRol(buscarRol(request.getRolId()));
        }

        if (request.getDepartamentoId() != null) {
            usuario.setDepartamento(
                    buscarDepartamento(request.getDepartamentoId()));
        }

        if (StringUtils.hasText(request.getNivelSeguridad())) {
            usuario.setNivelSeguridad(request.getNivelSeguridad());
        }

        if (StringUtils.hasText(request.getPais())) {
            usuario.setPais(request.getPais());
        }

        if (StringUtils.hasText(request.getTipoContrato())) {
            usuario.setTipoContrato(request.getTipoContrato());
        }

        if (StringUtils.hasText(request.getEstado())) {
            usuario.setEstado(request.getEstado());
        }

        Usuario actualizado = usuarioRepository.save(usuario);

        auditoriaService.registrar(
                actualizado,
                "USUARIO",
                "MODIFICAR_USUARIO",
                "PERMITIDO",
                "Usuario actualizado"
        );

        return actualizado;
    }

    public Usuario cambiarRol(Long id, Long rolId) {
        Usuario usuario = buscarPorId(id);
        usuario.setRol(buscarRol(rolId));
        Usuario actualizado = usuarioRepository.save(usuario);

        auditoriaService.registrar(
                actualizado,
                "USUARIO",
                "CAMBIAR_ROL_USUARIO",
                "PERMITIDO",
                "Rol de usuario actualizado"
        );

        return actualizado;
    }

    public Usuario cambiarDepartamento(
            Long id,
            Long departamentoId) {

        Usuario usuario = buscarPorId(id);
        usuario.setDepartamento(
                buscarDepartamento(departamentoId));
        Usuario actualizado = usuarioRepository.save(usuario);

        auditoriaService.registrar(
                actualizado,
                "USUARIO",
                "CAMBIAR_DEPARTAMENTO_USUARIO",
                "PERMITIDO",
                "Departamento de usuario actualizado"
        );

        return actualizado;
    }

    public Usuario cambiarEstado(Long id, String estado) {
        Usuario usuario = buscarPorId(id);
        usuario.setEstado(estado);
        Usuario actualizado = usuarioRepository.save(usuario);

        auditoriaService.registrar(
                actualizado,
                "USUARIO",
                "CAMBIAR_ESTADO_USUARIO",
                "PERMITIDO",
                "Estado de usuario actualizado"
        );

        return actualizado;
    }

    public void eliminarUsuario(Long id) {
        Usuario usuario = buscarPorId(id);
        usuario.setEstado("ELIMINADO");
        Usuario eliminado = usuarioRepository.save(usuario);

        auditoriaService.registrar(
                eliminado,
                "USUARIO",
                "ELIMINAR_USUARIO",
                "PERMITIDO",
                "Usuario eliminado logicamente"
        );
    }

    private Rol buscarRol(Long rolId) {
        return rolRepository.findById(rolId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Rol no encontrado"));
    }

    private Departamento buscarDepartamento(Long departamentoId) {
        return departamentoRepository.findById(departamentoId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Departamento no encontrado"));
    }
}

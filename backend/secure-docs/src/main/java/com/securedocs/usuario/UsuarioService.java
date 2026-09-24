package com.securedocs.usuario;

import com.securedocs.departamento.Departamento;
import com.securedocs.departamento.DepartamentoRepository;
import com.securedocs.rbac.Rol;
import com.securedocs.rbac.RolRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final DepartamentoRepository departamentoRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            DepartamentoRepository departamentoRepository,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.departamentoRepository = departamentoRepository;
        this.passwordEncoder = passwordEncoder;
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
            throw new RuntimeException(
                    "El correo ya está registrado");
        }

        // Buscar rol
        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Rol no encontrado"));

        // Buscar departamento
        Departamento departamento =
                departamentoRepository.findById(departamentoId)
                        .orElseThrow(() ->
                                new RuntimeException(
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

        return usuarioRepository.save(usuario);
    }

    // ==========================================
    // BUSCAR USUARIO POR CORREO
    // ==========================================

    public Usuario buscarPorCorreo(String correo) {

        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuario no encontrado"));
    }

    // ==========================================
    // BUSCAR USUARIO POR ID
    // ==========================================

    public Usuario buscarPorId(Long id) {

        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuario no encontrado"));
    }

    // ==========================================
    // LISTAR USUARIOS
    // ==========================================

    public List<Usuario> listarUsuarios() {

        return usuarioRepository.findAll();
    }
}
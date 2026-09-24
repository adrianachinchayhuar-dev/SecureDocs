package com.securedocs.auth;

import com.securedocs.auditoria.AuditoriaService;
import com.securedocs.usuario.Usuario;
import com.securedocs.usuario.UsuarioRepository;
import com.securedocs.exception.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuditoriaService auditoriaService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuditoriaService auditoriaService) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.auditoriaService = auditoriaService;
    }

    public LoginResponse login(LoginRequest request) {

        Usuario usuario = usuarioRepository
                .findByCorreo(request.getCorreo())
                .orElse(null);

        if (usuario == null) {
            auditoriaService.registrar(
                    null,
                    "AUTH",
                    "LOGIN",
                    "DENEGADO",
                    "Correo o contraseña incorrectos"
            );

            throw new BadRequestException(
                    "Correo o contraseña incorrectos");
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                usuario.getPassword())) {

            auditoriaService.registrar(
                    usuario,
                    "AUTH",
                    "LOGIN",
                    "DENEGADO",
                    "Correo o contraseña incorrectos"
            );

            throw new BadRequestException(
                    "Correo o contraseña incorrectos"
            );
        }

        String rol = usuario.getRol().getNombre();

        String token = jwtService.generarToken(
                usuario.getId(),
                usuario.getCorreo(),
                rol
        );

        auditoriaService.registrar(
                usuario,
                "AUTH",
                "LOGIN",
                "PERMITIDO",
                "Login exitoso"
        );

        return new LoginResponse(
                token,
                "Bearer",
                usuario.getId(),
                usuario.getNombre(),
                rol
        );
    }
}

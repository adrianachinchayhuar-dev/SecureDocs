package com.securedocs.config;

import com.securedocs.auth.JwtService;
import com.securedocs.usuario.Usuario;
import com.securedocs.usuario.UsuarioRepository;

import com.securedocs.rbac.RolPermiso;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UsuarioRepository usuarioRepository) {

        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader =
                request.getHeader("Authorization");

        // Si no hay token, dejamos que Spring Security
        // determine que el usuario no está autenticado.
        if (authorizationHeader == null ||
                !authorizationHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        String token =
                authorizationHeader.substring(7);

        try {

            // Validamos el JWT
            if (jwtService.esTokenValido(token)) {

                // Obtenemos el correo guardado dentro del JWT
                String correo =
                        jwtService.obtenerCorreo(token);

                // Buscamos al usuario en la base de datos
                Usuario usuario =
                        usuarioRepository
                                .findByCorreo(correo)
                                .orElse(null);

                if (usuario != null) {


                    List<GrantedAuthority> authorities =
                            usuario.getRol()
                                    .getPermisos()
                                    .stream()
                                    .map(RolPermiso::getPermiso)
                                    .map(permiso ->
                                            new SimpleGrantedAuthority(
                                                    permiso.getNombre()
                                            )
                                    )
                                    .collect(Collectors.toList());

                    /*
                     * Creamos la autenticación de Spring Security
                     * utilizando los permisos obtenidos desde la BD.
                     */
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    usuario,
                                    null,
                                    authorities
                            );

                    /*
                     * Guardamos la autenticación en el contexto
                     * de seguridad de Spring.
                     */
                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authentication);
                }
            }

        } catch (Exception e) {

            LOGGER.error(
                    "Error procesando el token JWT para la petición {}",
                    request.getRequestURI(),
                    e
            );

            // Ante cualquier error, eliminamos la autenticación existente.
            SecurityContextHolder
                    .clearContext();
        }

        // Continuamos con la petición.
        filterChain.doFilter(request, response);
    }
}

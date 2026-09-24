package com.securedocs.exception;

import com.securedocs.auditoria.AuditoriaService;
import com.securedocs.usuario.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final AuditoriaService auditoriaService;

    public GlobalExceptionHandler(AuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> manejarValidacion(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {

        List<String> detalles = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatearErrorCampo)
                .toList();

        return crearRespuesta(
                HttpStatus.BAD_REQUEST,
                "Solicitud inválida",
                "La solicitud contiene datos inválidos",
                request.getRequestURI(),
                detalles
        );
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiError> manejarValidacionMetodo(
            HandlerMethodValidationException exception,
            HttpServletRequest request) {

        List<String> detalles = exception.getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .toList();

        return crearRespuesta(
                HttpStatus.BAD_REQUEST,
                "Solicitud inválida",
                "La solicitud contiene datos inválidos",
                request.getRequestURI(),
                detalles
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> manejarBadRequest(
            BadRequestException exception,
            HttpServletRequest request) {

        return crearRespuesta(
                HttpStatus.BAD_REQUEST,
                "Solicitud inválida",
                exception.getMessage(),
                request.getRequestURI(),
                List.of()
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> manejarNoEncontrado(
            ResourceNotFoundException exception,
            HttpServletRequest request) {

        return crearRespuesta(
                HttpStatus.NOT_FOUND,
                "Recurso no encontrado",
                exception.getMessage(),
                request.getRequestURI(),
                List.of()
        );
    }

    @ExceptionHandler({
            ForbiddenOperationException.class,
            AccessDeniedException.class
    })
    public ResponseEntity<ApiError> manejarProhibido(
            RuntimeException exception,
            HttpServletRequest request) {

        if (exception instanceof AccessDeniedException) {
            registrarAccesoDenegado(request);
        }

        return crearRespuesta(
                HttpStatus.FORBIDDEN,
                "Acceso denegado",
                exception.getMessage(),
                request.getRequestURI(),
                List.of()
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> manejarNoAutenticado(
            AuthenticationException exception,
            HttpServletRequest request) {

        return crearRespuesta(
                HttpStatus.UNAUTHORIZED,
                "No autenticado",
                "La solicitud requiere autenticación",
                request.getRequestURI(),
                List.of()
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> manejarRuntime(
            RuntimeException exception,
            HttpServletRequest request) {

        return crearRespuesta(
                HttpStatus.BAD_REQUEST,
                "Solicitud inválida",
                exception.getMessage(),
                request.getRequestURI(),
                List.of()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> manejarGeneral(
            Exception exception,
            HttpServletRequest request) {

        return crearRespuesta(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno",
                "Ocurrió un error inesperado",
                request.getRequestURI(),
                List.of()
        );
    }

    private String formatearErrorCampo(FieldError error) {
        return error.getField() + ": " + error.getDefaultMessage();
    }

    private void registrarAccesoDenegado(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null) {
            return;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof Usuario usuario) {
            auditoriaService.registrar(
                    usuario,
                    "RBAC",
                    "ACCESO_DENEGADO",
                    "DENEGADO",
                    "Acceso denegado a " + request.getRequestURI()
            );
        }
    }

    private ResponseEntity<ApiError> crearRespuesta(
            HttpStatus status,
            String error,
            String mensaje,
            String ruta,
            List<String> detalles) {

        return ResponseEntity
                .status(status)
                .body(new ApiError(
                        status.value(),
                        error,
                        mensaje,
                        ruta,
                        detalles
                ));
    }
}

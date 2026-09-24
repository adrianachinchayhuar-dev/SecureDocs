package com.securedocs.rbac;

import com.securedocs.auditoria.AuditoriaService;
import com.securedocs.exception.BadRequestException;
import com.securedocs.exception.ResourceNotFoundException;
import com.securedocs.usuario.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RbacService {

    private final RolRepository rolRepository;
    private final PermisoRepository permisoRepository;
    private final RolPermisoRepository rolPermisoRepository;
    private final AuditoriaService auditoriaService;

    public RbacService(
            RolRepository rolRepository,
            PermisoRepository permisoRepository,
            RolPermisoRepository rolPermisoRepository,
            AuditoriaService auditoriaService) {

        this.rolRepository = rolRepository;
        this.permisoRepository = permisoRepository;
        this.rolPermisoRepository = rolPermisoRepository;
        this.auditoriaService = auditoriaService;
    }

    // Crear un nuevo rol
    public Rol crearRol(String nombre, Usuario usuarioAutenticado) {

        if (rolRepository.findByNombre(nombre).isPresent()) {
            throw new BadRequestException("El rol ya existe");
        }

        Rol rol = new Rol(nombre);

        Rol creado = rolRepository.save(rol);

        auditoriaService.registrar(
                usuarioAutenticado,
                "RBAC",
                "CREAR_ROL",
                "PERMITIDO",
                "Rol creado"
        );

        return creado;
    }

    public List<Rol> listarRoles() {
        return rolRepository.findAll();
    }

    public Rol buscarRolPorId(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Rol no encontrado"));
    }

    // Crear un nuevo permiso
    public Permiso crearPermiso(
            String nombre,
            String descripcion,
            Usuario usuarioAutenticado) {

        if (permisoRepository.findByNombre(nombre).isPresent()) {
            throw new BadRequestException("El permiso ya existe");
        }

        Permiso permiso = new Permiso(nombre, descripcion);

        Permiso creado = permisoRepository.save(permiso);

        auditoriaService.registrar(
                usuarioAutenticado,
                "RBAC",
                "CREAR_PERMISO",
                "PERMITIDO",
                "Permiso creado"
        );

        return creado;
    }

    public List<Permiso> listarPermisos() {
        return permisoRepository.findAll();
    }

    public Permiso buscarPermisoPorId(Long id) {
        return permisoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Permiso no encontrado"));
    }

    
    // ASIGNAR PERMISO A UN ROL


    public RolPermiso asignarPermisoARol(
            Long rolId,
            Long permisoId,
            Usuario usuarioAutenticado) {

        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Rol no encontrado"));

        Permiso permiso = permisoRepository.findById(permisoId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Permiso no encontrado"));

        // Verificar si el permiso ya está asignado
        if (rolPermisoRepository
                .existsByRolAndPermiso(rol, permiso)) {

            throw new BadRequestException(
                    "El permiso ya está asignado a este rol");
        }

        RolPermiso rolPermiso =
                new RolPermiso(rol, permiso);

        RolPermiso asignado = rolPermisoRepository.save(rolPermiso);

        auditoriaService.registrar(
                usuarioAutenticado,
                "RBAC",
                "ASIGNAR_PERMISO_ROL",
                "PERMITIDO",
                "Permiso asignado a rol"
        );

        return asignado;
    }

    // Obtener los permisos de un rol
    public List<RolPermiso> obtenerPermisosDelRol(Long rolId) {

        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Rol no encontrado"));

        return rolPermisoRepository.findByRol(rol);
    }

    public void quitarPermisoDeRol(
            Long rolId,
            Long permisoId,
            Usuario usuarioAutenticado) {

        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Rol no encontrado"));

        Permiso permiso = permisoRepository.findById(permisoId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Permiso no encontrado"));

        RolPermiso rolPermiso = rolPermisoRepository
                .findByRol(rol)
                .stream()
                .filter(rp -> rp.getPermiso()
                        .getId()
                        .equals(permiso.getId()))
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "El permiso no está asignado a este rol"));

        rolPermisoRepository.delete(rolPermiso);

        auditoriaService.registrar(
                usuarioAutenticado,
                "RBAC",
                "QUITAR_PERMISO_ROL",
                "PERMITIDO",
                "Permiso quitado de rol"
        );
    }

    // Verificar si un rol tiene determinado permiso
    public boolean tienePermiso(
            Long rolId,
            String nombrePermiso) {

        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Rol no encontrado"));

        List<RolPermiso> permisos =
                rolPermisoRepository.findByRol(rol);

        return permisos.stream()
                .anyMatch(rp ->
                        rp.getPermiso()
                                .getNombre()
                                .equals(nombrePermiso));
    }
}

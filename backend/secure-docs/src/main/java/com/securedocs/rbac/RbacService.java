package com.securedocs.rbac;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RbacService {

    private final RolRepository rolRepository;
    private final PermisoRepository permisoRepository;
    private final RolPermisoRepository rolPermisoRepository;

    public RbacService(
            RolRepository rolRepository,
            PermisoRepository permisoRepository,
            RolPermisoRepository rolPermisoRepository) {

        this.rolRepository = rolRepository;
        this.permisoRepository = permisoRepository;
        this.rolPermisoRepository = rolPermisoRepository;
    }

    // Crear un nuevo rol
    public Rol crearRol(String nombre) {

        if (rolRepository.findByNombre(nombre).isPresent()) {
            throw new RuntimeException("El rol ya existe");
        }

        Rol rol = new Rol(nombre);

        return rolRepository.save(rol);
    }

    // Crear un nuevo permiso
    public Permiso crearPermiso(String nombre, String descripcion) {

        if (permisoRepository.findByNombre(nombre).isPresent()) {
            throw new RuntimeException("El permiso ya existe");
        }

        Permiso permiso = new Permiso(nombre, descripcion);

        return permisoRepository.save(permiso);
    }

    // Asignar un permiso a un rol
    public RolPermiso asignarPermisoARol(
            Long rolId,
            Long permisoId) {

        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() ->
                        new RuntimeException("Rol no encontrado"));

        Permiso permiso = permisoRepository.findById(permisoId)
                .orElseThrow(() ->
                        new RuntimeException("Permiso no encontrado"));

        RolPermiso rolPermiso = new RolPermiso(rol, permiso);

        return rolPermisoRepository.save(rolPermiso);
    }

    // Obtener los permisos de un rol
    public List<RolPermiso> obtenerPermisosDelRol(Long rolId) {

        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() ->
                        new RuntimeException("Rol no encontrado"));

        return rolPermisoRepository.findByRol(rol);
    }

    // Verificar si un rol tiene determinado permiso
    public boolean tienePermiso(
            Long rolId,
            String nombrePermiso) {

        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() ->
                        new RuntimeException("Rol no encontrado"));

        List<RolPermiso> permisos =
                rolPermisoRepository.findByRol(rol);

        return permisos.stream()
                .anyMatch(rp ->
                        rp.getPermiso()
                                .getNombre()
                                .equals(nombrePermiso));
    }
}
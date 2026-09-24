package com.securedocs.config;

import com.securedocs.departamento.Departamento;
import com.securedocs.departamento.DepartamentoRepository;
import com.securedocs.rbac.Permiso;
import com.securedocs.rbac.PermisoRepository;
import com.securedocs.rbac.Rol;
import com.securedocs.rbac.RolPermiso;
import com.securedocs.rbac.RolPermisoRepository;
import com.securedocs.rbac.RolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner inicializarDatos(
            DepartamentoRepository departamentoRepository,
            RolRepository rolRepository,
            PermisoRepository permisoRepository,
            RolPermisoRepository rolPermisoRepository) {

        return args -> {

            // ==========================================
            // 1. DEPARTAMENTOS
            // ==========================================

            Departamento ti =
                    crearDepartamento(
                            "TI",
                            departamentoRepository);

            Departamento rrhh =
                    crearDepartamento(
                            "RRHH",
                            departamentoRepository);

            Departamento finanzas =
                    crearDepartamento(
                            "FINANZAS",
                            departamentoRepository);

            Departamento gerencia =
                    crearDepartamento(
                            "GERENCIA",
                            departamentoRepository);

            Departamento legal =
                    crearDepartamento(
                            "LEGAL",
                            departamentoRepository);


            // ==========================================
            // 2. PERMISOS
            // ==========================================

            Permiso crearDocumento =
                    crearPermiso(
                            "CREAR_DOCUMENTO",
                            "Permite crear documentos",
                            permisoRepository);

            Permiso consultarDocumento =
                    crearPermiso(
                            "CONSULTAR_DOCUMENTO",
                            "Permite consultar documentos",
                            permisoRepository);

            Permiso modificarDocumento =
                    crearPermiso(
                            "MODIFICAR_DOCUMENTO",
                            "Permite modificar documentos",
                            permisoRepository);

            Permiso eliminarDocumento =
                    crearPermiso(
                            "ELIMINAR_DOCUMENTO",
                            "Permite eliminar documentos",
                            permisoRepository);

            Permiso aprobarDocumento =
                    crearPermiso(
                            "APROBAR_DOCUMENTO",
                            "Permite aprobar documentos",
                            permisoRepository);

            Permiso verAuditoria =
                    crearPermiso(
                            "VER_AUDITORIA",
                            "Permite consultar la auditoría",
                            permisoRepository);

            Permiso gestionarUsuarios =
                    crearPermiso(
                            "GESTIONAR_USUARIOS",
                            "Permite gestionar usuarios",
                            permisoRepository);

            Permiso asignarRoles =
                    crearPermiso(
                            "ASIGNAR_ROLES",
                            "Permite asignar roles y permisos",
                            permisoRepository);


            // ==========================================
            // 3. ROLES
            // ==========================================

            Rol administrador =
                    crearRol(
                            "ADMINISTRADOR",
                            rolRepository);

            Rol gerente =
                    crearRol(
                            "GERENTE",
                            rolRepository);

            Rol supervisor =
                    crearRol(
                            "SUPERVISOR",
                            rolRepository);

            Rol empleado =
                    crearRol(
                            "EMPLEADO",
                            rolRepository);

            Rol auditor =
                    crearRol(
                            "AUDITOR",
                            rolRepository);

            Rol invitado =
                    crearRol(
                            "INVITADO",
                            rolRepository);


            // ==========================================
            // 4. PERMISOS DEL ADMINISTRADOR
            // ==========================================

            asignar(administrador, crearDocumento, rolPermisoRepository);
            asignar(administrador, consultarDocumento, rolPermisoRepository);
            asignar(administrador, modificarDocumento, rolPermisoRepository);
            asignar(administrador, eliminarDocumento, rolPermisoRepository);
            asignar(administrador, aprobarDocumento, rolPermisoRepository);
            asignar(administrador, verAuditoria, rolPermisoRepository);
            asignar(administrador, gestionarUsuarios, rolPermisoRepository);
            asignar(administrador, asignarRoles, rolPermisoRepository);


            // ==========================================
            // 5. PERMISOS DEL GERENTE
            // ==========================================

            asignar(gerente, consultarDocumento, rolPermisoRepository);
            asignar(gerente, modificarDocumento, rolPermisoRepository);
            asignar(gerente, aprobarDocumento, rolPermisoRepository);


            // ==========================================
            // 6. PERMISOS DEL SUPERVISOR
            // ==========================================

            asignar(supervisor, crearDocumento, rolPermisoRepository);
            asignar(supervisor, consultarDocumento, rolPermisoRepository);
            asignar(supervisor, modificarDocumento, rolPermisoRepository);


            // ==========================================
            // 7. PERMISOS DEL EMPLEADO
            // ==========================================

            asignar(empleado, crearDocumento, rolPermisoRepository);
            asignar(empleado, consultarDocumento, rolPermisoRepository);


            // ==========================================
            // 8. PERMISOS DEL AUDITOR
            // ==========================================

            asignar(auditor, consultarDocumento, rolPermisoRepository);
            asignar(auditor, verAuditoria, rolPermisoRepository);


            // ==========================================
            // 9. PERMISOS DEL INVITADO
            // ==========================================

            asignar(invitado, consultarDocumento, rolPermisoRepository);


            System.out.println("======================================");
            System.out.println("DATOS INICIALES CARGADOS");
            System.out.println("======================================");
        };
    }


    private Departamento crearDepartamento(
            String nombre,
            DepartamentoRepository repository) {

        return repository.findAll()
                .stream()
                .filter(d -> d.getNombre().equals(nombre))
                .findFirst()
                .orElseGet(() ->
                        repository.save(
                                new Departamento(nombre)));
    }


    private Permiso crearPermiso(
            String nombre,
            String descripcion,
            PermisoRepository repository) {

        return repository.findByNombre(nombre)
                .orElseGet(() ->
                        repository.save(
                                new Permiso(
                                        nombre,
                                        descripcion)));
    }


    private Rol crearRol(
            String nombre,
            RolRepository repository) {

        return repository.findByNombre(nombre)
                .orElseGet(() ->
                        repository.save(
                                new Rol(nombre)));
    }


    private void asignar(
            Rol rol,
            Permiso permiso,
            RolPermisoRepository repository) {

        boolean existe =
                repository.findByRol(rol)
                        .stream()
                        .anyMatch(rp ->
                                rp.getPermiso()
                                        .getId()
                                        .equals(permiso.getId()));

        if (!existe) {
            repository.save(
                    new RolPermiso(
                            rol,
                            permiso));
        }
    }
}
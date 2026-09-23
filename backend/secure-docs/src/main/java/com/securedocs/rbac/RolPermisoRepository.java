package com.securedocs.rbac;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolPermisoRepository extends JpaRepository<RolPermiso, Long> {

    List<RolPermiso> findByRol(Rol rol);

}
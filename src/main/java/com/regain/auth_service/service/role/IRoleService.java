package com.regain.auth_service.service.role;

import com.regain.auth_service.model.entity.Role;

import java.util.List;
import java.util.Optional;

public interface IRoleService {
    List<Role> getAllRoles();

    Optional<Role> findByRoleName(String roleName);

    Role save(Role role);
}

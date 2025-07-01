package com.regain.auth_service.service.role;

import com.regain.auth_service.model.entity.Role;
import com.regain.auth_service.repository.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    private IRoleRepository roleRepository;

    @Override
    public List<Role> getAllRoles() {
        return this.roleRepository.findAll();
    }

    @Override
    public Optional<Role> findByRoleName(String roleName) {
        return this.roleRepository.findByRoleName(roleName);
    }

    @Override
    public Role save(Role role) {
        return this.roleRepository.save(role);
    }
}

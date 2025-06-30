package com.regain.auth_service.repository;

import com.regain.auth_service.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository  extends JpaRepository<Role, Integer> {

    Optional<Role> findByRoleName(String roleName);

}

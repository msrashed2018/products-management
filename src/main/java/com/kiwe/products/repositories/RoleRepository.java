package com.kiwe.products.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kiwe.products.domain.Role;
import com.kiwe.products.domain.RoleName;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Role findByName(RoleName roleName);
}

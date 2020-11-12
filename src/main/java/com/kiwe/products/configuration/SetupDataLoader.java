package com.kiwe.products.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kiwe.products.domain.Role;
import com.kiwe.products.domain.RoleName;
import com.kiwe.products.repositories.RoleRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	private boolean alreadySetup = false;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// API

	@Override
	@Transactional
	public void onApplicationEvent(final ContextRefreshedEvent event) {
		if (alreadySetup) {
			return;
		}
		log.info("creating intial roles (If Not Created) =>  ROLE_USER");
		// == create initial roles
		createRoleIfNotFound(RoleName.USER);

		alreadySetup = true;
	}

	@Transactional
	private final Role createRoleIfNotFound(final RoleName name) {
		Role role = roleRepository.findByName(name);
		if (role == null) {
			role = new Role();
			role.setName(name);
		}
		role = roleRepository.save(role);
		return role;
	}
}
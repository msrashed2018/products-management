package com.kiwe.products.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kiwe.products.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmailIgnoreCase(String email);

	Optional<User> findByEmailIgnoreCase(String email);

}
